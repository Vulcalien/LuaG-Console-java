package vulc.jlconsole;

import vulc.jlconsole.sfx.Sound;
import vulc.jlconsole.sfx.Sounds;

public class JavaToLuaInterface {

	private final Console console;

	public JavaToLuaInterface(Console console) {
		this.console = console;
	}

	public boolean key(int id) {
		return console.keys[id].isKeyDown();
	}

	public void clear(int color) {
		console.screen.clear(color);
	}

	public void pix(int x, int y, int color) {
		console.screen.setPixel(x, y, color);
	}

	public void fill(int x0, int y0, int x1, int y1, int color) {
		console.screen.fill(x0, y0, x1, y1, color);
	}

	public void spr(int x, int y, int xs, int ys, int ws, int hs) {
		console.screen.draw(console.atlas.getSubimage(xs, ys, ws, hs), x, y);
	}

	public void write(String text, int color, int x, int y) {
		console.screen.write(text, color, x, y);
	}

	public void settransparent(int color) {
		console.screen.setTransparent(color);
	}

	public void sfx(String name) {
		Sound sound = Sounds.get(name);
		if(sound == null) {
			System.err.println("Error: sound '" + name + "' does not exist");
		} else {
			sound.play();
		}
	}

}
