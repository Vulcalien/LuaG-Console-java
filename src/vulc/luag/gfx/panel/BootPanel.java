package vulc.luag.gfx.panel;

import vulc.luag.Console;
import vulc.luag.gfx.Screen;

public class BootPanel extends Panel {

	public Panel nextPanel;
	private int bootTime = 135; // 2.25s
	private boolean hasInit = false;
	private int animationTicks = 0;

	public BootPanel() {
		Console.SCREEN.clear(0);
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
		Screen screen = Console.SCREEN;

		screen.write(Console.NAME + "\n"
		             + Console.COPYRIGHT + "\n"
		             + "Version: " + Console.VERSION,
		             0xffffff, 1, 1);

		bootTime--;
		if(bootTime <= 0) {
			if(hasInit) {
				Console.currentPanel = nextPanel; // cannot call console.switchToPanel because it'd call panel.init() again
				nextPanel.onShow();
				screen.clear(0);
			} else {
				int animatPhase = animationTicks / 30 % 4;
				String text = null;
				if(animatPhase == 0) text = "Loading";
				else if(animatPhase == 1) text = "Loading.";
				else if(animatPhase == 2) text = "Loading..";
				else if(animatPhase == 3) text = "Loading...";

				Console.SCREEN.clear(0);
				Console.SCREEN.write(text, 0xffffff, 1, 1);

				animationTicks++;
			}
		}
	}

}
