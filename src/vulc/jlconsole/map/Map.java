package vulc.jlconsole.map;

public class Map {

	public int width, height;
	public byte[] tiles;

	public byte getTile(int x, int y) {
		return tiles[x + y * width];
	}

	public void setTile(int x, int y, int id) {
		tiles[x + y * width] = (byte) id;
	}

}
