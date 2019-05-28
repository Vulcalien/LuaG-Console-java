package vulc.jlconsole.map_editor.compiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import vulc.jlconsole.Console;

public abstract class MapCompiler {

	public static void compile(int w, int h, byte[] tiles) {
		try {
			File file = new File(Console.USER_DIR + "/map");
			file.createNewFile();

			OutputStream out = new FileOutputStream(file);

			write(out, w >> 24, w >> 16, w >> 8, w);
			write(out, h >> 24, h >> 16, h >> 8, h);

			out.write(tiles);

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
