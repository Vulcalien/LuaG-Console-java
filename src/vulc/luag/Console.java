/*******************************************************************************
 * Copyright 2019 Vulcalien
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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import vulc.luag.gfx.Screen;
import vulc.luag.gfx.panel.BootPanel;
import vulc.luag.gfx.panel.DeathPanel;
import vulc.luag.gfx.panel.GamePanel;
import vulc.luag.gfx.panel.Panel;
import vulc.luag.gfx.panel.ShellPanel;
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
	public static final String VERSION = "0.6.1";
	public static final String COPYRIGHT = "Copyright 2019 Vulcalien";

	public static final int WIDTH = 160, HEIGHT = 160;
	public static int scale;

	public static final Logger LOGGER = Logger.getLogger(Console.class.getName());
	public static String rootDirectory;

	public static final Screen SCREEN = new Screen(WIDTH, HEIGHT);
	public static Panel currentPanel;

	public static String cartridge;
	public static Mode mode;

	public static Console instance;

	private static final JFrame FRAME = new JFrame();

	private final BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private final int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

	public void run() {
		int ticksPerSecond = 60;

		long nanosPerTick = 1_000_000_000 / ticksPerSecond;
		long unprocessedNanos = 0;
		long lastTime = System.nanoTime();

		while(true) {
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
		g.drawImage(img, 0, 0, WIDTH * scale, HEIGHT * scale, null);
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

		FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FRAME.setTitle(NAME + " " + VERSION);
		FRAME.setResizable(false);

		instance = new Console();
		FRAME.add(instance);

		scaleFrame(3);
		FRAME.setLocationRelativeTo(null);

		try {
			FRAME.setIconImage(ImageIO.read(Console.class.getResourceAsStream("/res/icon.png")));
		} catch(IOException e) {
			e.printStackTrace();
		}

		instance.init(args);

		FRAME.setVisible(true);
		new Thread(instance).start();
	}

	private static void startupOperations() {
		if(Console.class.getResource("Console.class").toString().startsWith("jar")) {
			File jarFile = new File(Console.class.getProtectionDomain()
			                                     .getCodeSource()
			                                     .getLocation()
			                                     .getPath());
			rootDirectory = jarFile.getParent() + "/";
		} else {
			rootDirectory = "./";
		}

		// setup LOGGER
		LOGGER.setLevel(Level.ALL);
		Locale.setDefault(Locale.ENGLISH);
		try {
			FileHandler fh = new FileHandler(rootDirectory + "luag.log");
			fh.setFormatter(new SimpleFormatter());
			LOGGER.addHandler(fh);
		} catch(Exception e) {
			e.printStackTrace();
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				LOGGER.info("Shutting down Console");
			}
		});
	}

	private static void scaleFrame(int newScale) {
		LOGGER.info("Console Scale: " + newScale);

		scale = newScale;

		instance.setPreferredSize(new Dimension(WIDTH * scale, HEIGHT * scale));
		instance.setMaximumSize(new Dimension(WIDTH * scale, HEIGHT * scale));
		instance.setMinimumSize(new Dimension(WIDTH * scale, HEIGHT * scale));

		FRAME.pack();
	}

}
