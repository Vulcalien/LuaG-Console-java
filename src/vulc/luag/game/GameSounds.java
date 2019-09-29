package vulc.luag.game;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Set;

import vulc.luag.Console;
import vulc.luag.sfx.Sound;

public class GameSounds {

	private final HashMap<String, Sound> list = new HashMap<String, Sound>();

	public Sound get(String name) {
		return list.get(name);
	}

	public void remove() {
		Set<String> keys = list.keySet();
		for(String key : keys) {
			list.get(key).stop();
		}
	}

	public boolean init(Console console) {
		File sfxDir = new File(Game.USER_DIR + "/sfx");
		if(!sfxDir.isDirectory()) {
			console.die("Error:\n"
			            + "'sfx'\n"
			            + "folder not found");
			return false;
		}

		readSoundsInFolder(sfxDir, "");

		return true;
	}

	private void readSoundsInFolder(File folder, String relativeToSfxRoot) {
		File[] files = folder.listFiles();
		for(File file : files) {
			String fileName = file.getName();

			if(file.isDirectory()) {
				readSoundsInFolder(file, relativeToSfxRoot + fileName + "/");
			} else {
				if(fileName.endsWith(".wav")) {
					String name = relativeToSfxRoot + fileName;
					name = name.substring(0, name.lastIndexOf('.'));
					try {
						list.put(name, new Sound(file.toURI().toURL()));
					} catch(MalformedURLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
