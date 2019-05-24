package vulc.jlconsole.gfx.panel;

import vulc.jlconsole.Console;
import vulc.jlconsole.gfx.Screen;

public class BootPanel extends Panel {

	private int bootTime = 180;

	public BootPanel(Console console, Screen screen) {
		super(console, screen);
	}

	public void tick() {
		screen.write("Vulc's Java-Lua Console", 0xffffff, 1, 1);
		screen.write("Copyright 2019 Vulcalien", 0xffffff, 1, 11);
		screen.write("Version: " + Console.VERSION, 0xffffff, 1, 22);

		bootTime--;
		if(bootTime <= 0) {
			console.currentPanel = new GamePanel(console, screen);
			screen.clear(0);
		}
	}

}
