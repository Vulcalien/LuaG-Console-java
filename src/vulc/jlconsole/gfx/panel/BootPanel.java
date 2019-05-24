package vulc.jlconsole.gfx.panel;

import vulc.jlconsole.Console;
import vulc.jlconsole.gfx.Screen;

public class BootPanel extends Panel {

	private int bootTime = 180;
	private Panel nextPanel;

	public BootPanel(Console console, Panel nextPanel) {
		super(console);
		this.nextPanel = nextPanel;

		new Thread() {
			public void run() {
				nextPanel.preInit();
			}
		}.start();
	}

	public void tick() {
		Screen screen = console.screen;

		screen.write("Vulc's Java-Lua Console", 0xffffff, 1, 1);
		screen.write("Copyright 2019 Vulcalien", 0xffffff, 1, 11);
		screen.write("Version: " + Console.VERSION, 0xffffff, 1, 22);

		bootTime--;
		if(bootTime <= 0) {
			console.currentPanel = nextPanel;
			screen.clear(0);
		}
	}

}
