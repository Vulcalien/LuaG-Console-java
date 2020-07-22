package vulc.luag.editor.map;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import vulc.luag.game.Game;
import vulc.luag.game.map.Map;

public abstract class MapCompiler {

	public static void compile(Map map) {
		try {
			File file = new File(Game.MAP_FILE);
			file.createNewFile();

			DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

			int w = map.width;
			int h = map.height;

			out.writeInt(w);
			out.writeInt(h);

			for(int i = 0; i < map.tiles.length; i++) {
				out.writeByte(map.tiles[i] + 128);
			}
			out.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
