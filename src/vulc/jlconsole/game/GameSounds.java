package vulc.jlconsole.game;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.util.HashMap;

import vulc.jlconsole.sfx.Sound;

public class GameSounds {

	private final HashMap<String, Sound> list = new HashMap<String, Sound>();

	public void init() {
		File sfxDir = new File(Game.USER_DIR + "/sfx");
		if(!sfxDir.isDirectory()) {
			System.err.println("Error: 'sfx' folder does not exist");
			System.exit(1);
		}

		File[] files = sfxDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".wav");
			}
		});

		for(File soundFile : files) {
			String name = soundFile.getName();
			name = name.substring(0, name.lastIndexOf('.'));
			try {
				list.put(name, new Sound(soundFile.toURI().toURL()));
			} catch(MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	public Sound get(String name) {
		return list.get(name);
	}

}
