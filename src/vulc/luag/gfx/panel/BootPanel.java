package vulc.luag.gfx.panel;

import vulc.luag.Console;
import vulc.luag.gfx.Screen;

public class BootPanel extends Panel {

	public Panel nextPanel;
	private int bootTime = 135; // 2.25s
	private boolean hasInit = false;
	private int animationTicks = 0;

	public BootPanel(Console console) {
		super(console);
		console.screen.clear(0);
	}

	public void init() {
		new Thread() {
			public void run() {
				nextPanel.init();
				hasInit = true;
			}
		}.start();
	}

	public void tick() {
		Screen screen = console.screen;

		screen.write(Console.NAME + "\n"
		             + Console.COPYRIGHT + "\n"
		             + "Version: " + Console.VERSION,
		             0xffffff, 1, 1);

		bootTime--;
		if(bootTime <= 0) {
			if(hasInit) {
				console.currentPanel = nextPanel; // cannot call console.switchToPanel because it'd call panel.init() again
				nextPanel.onShow();
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
