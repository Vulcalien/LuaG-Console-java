package vulc.jlconsole.game.map;

import java.io.IOException;
import java.io.InputStream;

public class Map {

	public final int width, height;
	public final byte[] tiles;

	public Map(int w, int h) {
		this.width = w;
		this.height = h;
		this.tiles = new byte[w * h];
	}

	public byte getTile(int x, int y) {
		return tiles[x + y * width];
	}

	public void setTile(int x, int y, int id) {
		tiles[x + y * width] = (byte) id;
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
