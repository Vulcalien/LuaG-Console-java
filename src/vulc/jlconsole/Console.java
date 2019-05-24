package vulc.jlconsole;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileReader;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import vulc.bitmap.Bitmap;
import vulc.jlconsole.gfx.Screen;
import vulc.jlconsole.gfx.panel.BootPanel;
import vulc.jlconsole.gfx.panel.Panel;
import vulc.jlconsole.input.InputHandler;
import vulc.jlconsole.input.InputHandler.Key;
import vulc.jlconsole.input.InputHandler.KeyType;
import vulc.jlconsole.sfx.Sounds;

public class Console extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final String USER_DIR = "./console-userdata";
	public static final String VERSION = "0.1";

	public static final int WIDTH = 150, HEIGHT = 150, SCALE = 3;
	public final BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	public final int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

	public final Screen screen = new Screen(WIDTH, HEIGHT);
	public JsonObject jsonConfig;
	public Panel currentPanel = new BootPanel(this, screen);
	public Bitmap atlas;

	public Key[] keys = {
		new Key(KeyType.KEYBOARD, KeyEvent.VK_W),
		new Key(KeyType.KEYBOARD, KeyEvent.VK_A),
		new Key(KeyType.KEYBOARD, KeyEvent.VK_S),
		new Key(KeyType.KEYBOARD, KeyEvent.VK_D)
	};

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

	private void init() {
		try {
			InputHandler.init(this);
			atlas = new Bitmap(ImageIO.read(new File(USER_DIR + "/atlas.png")));
			jsonConfig = new JsonParser().parse(new FileReader(USER_DIR + "/config.json")).getAsJsonObject();
			Sounds.init();
			ConsoleInterface.init(this);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void tick() {
		currentPanel.tick();
		InputHandler.tick();

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
		JFrame frame = new JFrame("LuaJ Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		Console instance = new Console();
		instance.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		instance.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		instance.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		frame.add(instance);
		frame.pack();
		frame.setLocationRelativeTo(null);

		new Thread() {
			public void run() {
				instance.init();
			}
		}.start();


		frame.setVisible(true);
		new Thread(instance).start();
	}

}
