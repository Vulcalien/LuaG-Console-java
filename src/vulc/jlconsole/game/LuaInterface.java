package vulc.jlconsole.game;

import vulc.jlconsole.Console;
import vulc.jlconsole.game.sfx.Sound;
import vulc.jlconsole.game.sfx.Sounds;

public class LuaInterface {

	private final Console console;
	private final Game game;

	public LuaInterface(Console console, Game game) {
		this.console = console;
		this.game = game;
	}

	public boolean key(int id) {
		return console.keys[id].isKeyDown();
	}

	public void settransparent(int color) {
		console.screen.setTransparent(color);
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

	public void write(String text, int color, int x, int y) {
		console.screen.write(text, color, x, y);
	}

	public void spr(int x, int y, int xs, int ys, int ws, int hs) {
		console.screen.draw(game.atlas.getSubimage(xs, ys, ws, hs), x, y);
	}

	public void sfx(String name) {
		Sound sound = Sounds.get(name);
		if(sound == null) {
			System.err.println("Error: sound '" + name + "' does not exist");
		} else {
			sound.play();
		}
	}

	public byte gettile(int x, int y) {
		return game.map.getTile(x, y);
	}

	public void settile(int x, int y, int id) {
		game.map.setTile(x, y, id);
	}

}
