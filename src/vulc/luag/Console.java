/*******************************************************************************
 * Copyright 2019-2020 Vulcalien
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package vulc.luag;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import vulc.luag.gfx.ConsoleFrame;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.panel.BootPanel;
import vulc.luag.gfx.panel.DeathPanel;
import vulc.luag.gfx.panel.GamePanel;
import vulc.luag.gfx.panel.Panel;
import vulc.luag.gfx.panel.ShellPanel;
import vulc.luag.input.FullScreenListener;
import vulc.luag.shell.Shell;

/**
 * Open Source since: 20.06.2019<br>
 * GitHub: https://github.com/Vulcalien/LuaG-Console<br>
 * Author: Vulcalien<br>
 *
 * <h2>Used Libraries</h2>
 * <ul>
 * <li>'Bitmap Utility'    by Vulcalien (Copyright 2019 Vulcalien - https://github.com/Vulcalien/Bitmap-Utility/blob/master/LICENSE)</li>
 * <li>'Gson'              by Google (Copyright 2008-2011 Google Inc. - https://github.com/google/gson/blob/master/LICENSE)</li>
 * <li>'LuaJ'              by LuaJ (Copyright (c) 2007-2013 LuaJ - http://luaj.sourceforge.net/license.txt)</li>
 * </ul>
 */
public class Console extends Canvas implements Runnable {

	public static enum Mode {
		USER_GAME, USER_SHELL, DEVELOPER
	}

	private static final long serialVersionUID = 1L;

	public static final String NAME = "LuaG Console";
	public static final String VERSION = "post-0.6.3";
	public static final String COPYRIGHT = "Copyright 2020 Vulcalien";

	public static final int WIDTH = 160, HEIGHT = 160;
	public static int scaledWidth, scaledHeight;
	public static int xOffset, yOffset;
	public static boolean isFullScreen;

	public static final Logger LOGGER = Logger.getLogger(Console.class.getName());
	public static String rootDirectory;
	public static String logFile;

	public static final Screen SCREEN = new Screen(WIDTH, HEIGHT);
	public static Panel currentPanel;

	public static String cartridge;
	public static Mode mode;

	public static Console instance;

	private static boolean running = false;
	private static Thread thread;

	public static ConsoleFrame frame;

	private final BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private final int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

	public void run() {
		int ticksPerSecond = 60;

		long nanosPerTick = 1_000_000_000 / ticksPerSecond;
		long unprocessedNanos = 0;
		long lastTime = System.nanoTime();

		while(running) {
			long now = System.nanoTime();
			long passedTime = now - lastTime;
			lastTime = now;

			if(passedTime < 0) passedTime = 0;
			if(passedTime > 1_000_000_000) passedTime = 1_000_000_000;

			unprocessedNanos += passedTime;

			while(unprocessedNanos >= nanosPerTick) {
				unprocessedNanos -= nanosPerTick;

				try {
					tick();
				} catch(Throwable e) {
					LOGGER.log(Level.SEVERE, "Console Error", e);
					System.exit(1);
				}
			}

			try {
				Thread.sleep(4);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void start() {
		if(running) return;
		running = true;

		LOGGER.info("Console: start");

		thread = new Thread(instance);
		thread.start();
	}

	public static void stop() {
		if(!running) return;
		running = false;

		LOGGER.info("Console: stop");

		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void init(String[] args) {
		requestFocus();

		if(args.length > 0) {
			if(args[0].equals("-dev")) {
				mode = Mode.DEVELOPER;
			} else {
				// this is not relative to root: it should be an argument passed automatically
				cartridge = args[0];
				mode = Mode.USER_GAME;
			}
		} else {
			mode = Mode.USER_SHELL;
		}

		LOGGER.info("Startup mode: " + mode);

		Panel nextPanel = null;

		if(mode == Mode.DEVELOPER || mode == Mode.USER_SHELL) {
			Shell.init();
			nextPanel = new ShellPanel();
		} else if(mode == Mode.USER_GAME) {
			nextPanel = new GamePanel();
		}

		BootPanel bootPanel = new BootPanel();
		bootPanel.nextPanel = nextPanel;
		currentPanel = bootPanel;
		currentPanel.init();
	}

	private void tick() {
		currentPanel.tick();

		frame.checkSize();
		render();
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}

		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = SCREEN.raster.getPixel(i);
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, xOffset, yOffset, scaledWidth, scaledHeight, null);
		g.dispose();
		bs.show();
	}

	public static void switchToPanel(Panel panel) {
		LOGGER.info("Switching to panel: " + panel.getClass().getSimpleName());

		if(currentPanel != null) currentPanel.remove();
		currentPanel = panel;
		panel.init();
		panel.onShow();
	}

	public static void die(String text) {
		LOGGER.severe("Console die:\n" + text);

		if(mode == Mode.DEVELOPER || mode == Mode.USER_SHELL) {
			switchToPanel(new ShellPanel());
			Shell.write(text + "\n\n");
		} else {
			switchToPanel(new DeathPanel(text));
		}
	}

	public static void main(String[] args) {
		startupOperations();
		LOGGER.info("Starting Console...");

		Toolkit.getDefaultToolkit().setDynamicLayout(false);

		instance = new Console();
		instance.setBackground(Color.DARK_GRAY);

		initFrame(false);

		instance.init(args);

		frame.setVisible(true);
		start();

		instance.addKeyListener(new FullScreenListener());
	}

	private static void startupOperations() {
		if(Console.class.getResource("Console.class").toString().startsWith("jar")) {
			try {
				File jarFile = new File(Console.class.getProtectionDomain()
				                                     .getCodeSource()
				                                     .getLocation()
				                                     .toURI());
				rootDirectory = jarFile.getParent() + "/";
			} catch(URISyntaxException e) {
				e.printStackTrace();
			}
		} else {
			rootDirectory = "./";
		}

		LoggerSetup.setup(LOGGER, rootDirectory);
	}

	private static void setInitialScale() {
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

		// by default, the console's height is half of the screen
		double newScale = (double) screenHeight / HEIGHT / 2;

		updateScaledSize((int) (WIDTH * newScale), (int) (HEIGHT * newScale));
		instance.setSize(scaledWidth, scaledHeight);
		frame.pack();
	}

	public static void updateScaledSize(int width, int height) {
		// find lowest scale and use it on both sides
		int minScaled, minScreen;
		if(width * Console.HEIGHT < height * Console.WIDTH) {
			minScaled = width;
			minScreen = Console.WIDTH;
		} else {
			minScaled = height;
			minScreen = Console.HEIGHT;
		}

		scaledWidth = Console.WIDTH * minScaled / minScreen;
		scaledHeight = Console.HEIGHT * minScaled / minScreen;

		xOffset = (width - scaledWidth) / 2;
		yOffset = (height - scaledHeight) / 2;
	}

	private static void initFrame(boolean fullScreen) {
		frame = new ConsoleFrame();
		frame.init();
		frame.add(instance);

		isFullScreen = fullScreen;

		if(fullScreen) {
			frame.setUndecorated(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			// set minimum size (it is console's size not scaled + insets)
			frame.pack(); // this sets the insets of the frame

			Insets insets = frame.getInsets();
			frame.setMinimumSize(new Dimension(WIDTH + insets.left + insets.right,
			                                   HEIGHT + insets.top + insets.bottom));

			setInitialScale();
			frame.setLocationRelativeTo(null);
		}
	}

	public static void switchFullScreen() {
		stop();

		frame.setVisible(false);

		// this changes the value of isFullScreen
		initFrame(!isFullScreen);

		frame.setVisible(true);
		instance.requestFocus();

		start();
	}

}
