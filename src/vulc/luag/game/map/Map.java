package vulc.luag.game.map;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import vulc.luag.Console;
import vulc.luag.game.Game;
import vulc.luag.gfx.Screen;

public class Map {

	public final int width, height;
	public final byte[] tiles;

	public Map(int w, int h) {
		this.width = w;
		this.height = h;
		this.tiles = new byte[w * h];
		for(int i = 0; i < tiles.length; i++) {
			tiles[i] = -128;
		}
	}

	public void render(Screen screen, Game game, int xOffset, int yOffset, int scale) {
		int tSize = Game.SPR_SIZE * scale;

		int xt0 = Math.floorDiv(xOffset, tSize);
		int yt0 = Math.floorDiv(yOffset, tSize);
		int xt1 = xt0 + (Console.WIDTH / tSize) + 1;
		int yt1 = yt0 + (Console.HEIGHT / tSize) + 1;

		for(int yt = yt0; yt < yt1; yt++) {
			if(yt < 0 || yt >= height) continue;

			for(int xt = xt0; xt < xt1; xt++) {
				if(xt < 0 || xt >= width) continue;

				int id = getTile(xt, yt);
				screen.draw(game.getSprite(id, 1, 1).getScaled(scale),
				            xt * Game.SPR_SIZE * scale - xOffset,
				            yt * Game.SPR_SIZE * scale - yOffset);
			}
		}
	}

	public int getTile(int x, int y) {
		return tiles[x + y * width] + 128;
	}

	public void setTile(int x, int y, int id) {
		tiles[x + y * width] = (byte) (id - 128);
	}

	public static Map load(InputStream inputStream, Console console) {
		try {
			DataInputStream in = new DataInputStream(inputStream);

			int w = in.readInt();
			int h = in.readInt();
			Map map = new Map(w, h);

			for(int i = 0; i < map.tiles.length; i++) {
				int data = in.read();
				if(data == -1) {
					console.die("Error:\n"
					            + "map file is malformed\n"
					            + "(not enought tile data)");
					return null;
				}
				map.tiles[i] = (byte) (data - 128);
			}

			in.close();
			return map;
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

}
