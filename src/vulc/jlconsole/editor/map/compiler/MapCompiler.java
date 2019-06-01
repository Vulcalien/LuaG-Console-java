package vulc.jlconsole.editor.map.compiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import vulc.jlconsole.game.Game;
import vulc.jlconsole.game.map.Map;

public abstract class MapCompiler {

	public static void compile(Map map) {
		try {
			File file = new File(Game.USER_DIR + "/map");
			file.createNewFile();

			OutputStream out = new FileOutputStream(file);

			int w = map.width;
			int h = map.height;

			write(out, w >> 24, w >> 16, w >> 8, w);
			write(out, h >> 24, h >> 16, h >> 8, h);

			out.write(map.tiles);

			out.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private static void write(OutputStream out, int... bytes) throws IOException {
		for(int i = 0; i < bytes.length; i++) {
			out.write(bytes[i]);
		}
	}

}
