package vulc.jlconsole.game.map;

import java.io.IOException;
import java.io.InputStream;

import vulc.jlconsole.game.Game;
import vulc.jlconsole.gfx.Screen;

public class Map {

	public final int width, height;
	public final byte[] tiles;

	public Map(int w, int h) {
		this.width = w;
		this.height = h;
		this.tiles = new byte[w * h];
	}

	public void render(Screen screen, Game game, int xOffset, int yOffset, int scale) {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int id = getTile(x, y);

				screen.draw(game.getSprite(id % 16, id / 16, 1, 1).getScaled(scale), xOffset + x * 8 * scale, yOffset + y * 8 * scale);
			}
		}
	}

	public int getTile(int x, int y) {
		return tiles[x + y * width] + 128;
	}

	public void setTile(int x, int y, int id) {
		tiles[x + y * width] = (byte) (id - 128);
	}

	public static Map load(InputStream input) {
		try {
			int w = (input.read() << 24) | (input.read() << 16) | (input.read() << 8) | input.read();
			int h = (input.read() << 24) | (input.read() << 16) | (input.read() << 8) | input.read();
			Map map = new Map(w, h);

			for(int i = 0; i < map.tiles.length; i++) {
				int data = input.read();
				if(data == -1) {
					System.err.println("Error: map file is malformed (not enought tile data)");
					System.exit(1);
				}
				map.tiles[i] = (byte) (data - 128);
			}

			return map;
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

}
