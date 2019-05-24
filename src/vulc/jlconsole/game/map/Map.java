package vulc.jlconsole.game.map;

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

}
