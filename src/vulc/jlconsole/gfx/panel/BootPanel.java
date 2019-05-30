package vulc.jlconsole.gfx.panel;

import vulc.jlconsole.Console;
import vulc.jlconsole.gfx.Screen;

public class BootPanel extends Panel {

	private int bootTime = 150;
	private Panel nextPanel;
	private boolean hasInit = false;
	private int animationTicks = 0;

	public BootPanel(Console console, Panel nextPanel) {
		super(console);
		this.nextPanel = nextPanel;
		console.screen.clear(0);

		new Thread() {
			public void run() {
				nextPanel.init();
				hasInit = true;
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
			if(hasInit) {
				console.currentPanel = nextPanel;
				screen.clear(0);
			} else {
				int animatPhase = animationTicks / 30 % 4;
				String text = null;
				if(animatPhase == 0) text = "Loading";
				else if(animatPhase == 1) text = "Loading.";
				else if(animatPhase == 2) text = "Loading..";
				else if(animatPhase == 3) text = "Loading...";

				console.screen.clear(0);
				console.screen.write(text, 0xffffff, 1, 1);

				animationTicks++;
			}
		}
	}

}
