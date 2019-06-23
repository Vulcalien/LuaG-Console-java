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

import javax.swing.JFrame;

import vulc.luag.cmd.Cmd;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.panel.BootPanel;
import vulc.luag.gfx.panel.CmdPanel;
import vulc.luag.gfx.panel.EditorPanel;
import vulc.luag.gfx.panel.GamePanel;
import vulc.luag.gfx.panel.Panel;

/**
 * Open Source since: 20.06.2019<br>
 * GitHub: https://github.com/Vulcalien/LuaG-Console<br>
 * Author: Vulcalien<br>
 *
 * <h2>Used Libraries</h2>
 * <ul>
 * <li>'Bitmap Utility'     by Vulcalien</li>
 * <li>'Gson'               by Google</li>
 * <li>'LuaJ'               by LuaJ</li>
 * </ul>
 *
 */
public class Console extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final String NAME = "Vulc's LuaG Console";
	public static final String VERSION = "0.4 (WIP)";
	public static final String COPYRIGHT = "Copyright 2019 Vulcalien";

	public static final int WIDTH = 160, HEIGHT = 160, SCALE = 3;
	private final BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private final int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

	public final Screen screen = new Screen(WIDTH, HEIGHT);
	public Cmd cmd;
	public Panel currentPanel;

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

				tick();
			}

			try {
				Thread.sleep(4);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void init(String[] args) {
		cmd = new Cmd(this);
		cmd.init();
		Panel nextPanel = null;

		if(args.length > 0) {
			switch(args[0]) {
				case "-run":
					nextPanel = new GamePanel(this);
					break;

				case "-editor":
				case "-edit":
					nextPanel = new EditorPanel(this);
					break;

				default:
					System.err.println("Error: invalid boot argument");
					System.exit(1);
			}
		} else {
			nextPanel = new CmdPanel(this);
		}

		requestFocus();

		BootPanel bootPanel = new BootPanel(this);
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
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame(NAME + VERSION);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		Console instance = new Console();
		instance.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		instance.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		instance.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		frame.add(instance);
		frame.pack();
		frame.setLocationRelativeTo(null);

		instance.init(args);

		frame.setVisible(true);
		new Thread(instance).start();
	}

}
