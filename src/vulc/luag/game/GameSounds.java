package vulc.luag.game;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.util.HashMap;

import vulc.luag.Console;
import vulc.luag.gfx.panel.DeathPanel;
import vulc.luag.sfx.Sound;

public class GameSounds {

	private final HashMap<String, Sound> list = new HashMap<String, Sound>();

	public boolean init(Console console) {
		File sfxDir = new File(Game.USER_DIR + "/sfx");
		if(!sfxDir.isDirectory()) {
			console.switchToPanel(new DeathPanel(console, "Error:\n"
			                                              + "'sfx' folder does\n"
			                                              + "not exist"));
			return false;
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
		return true;
	}

	public Sound get(String name) {
		return list.get(name);
	}

}
