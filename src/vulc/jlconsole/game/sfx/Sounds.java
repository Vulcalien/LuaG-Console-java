package vulc.jlconsole.game.sfx;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.util.HashMap;

import vulc.jlconsole.game.Game;

public abstract class Sounds {

	private static final HashMap<String, Sound> LIST = new HashMap<String, Sound>();

	public static void init() {
		File sfxDir = new File(Game.USER_DIR + "/sfx");
		if(!sfxDir.isDirectory()) {
			System.err.println("Error: 'sfx' folder does not exist");
			return;
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
				LIST.put(name, new Sound(soundFile.toURI().toURL()));
			} catch(MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	public static Sound get(String name) {
		return LIST.get(name);
	}

}
