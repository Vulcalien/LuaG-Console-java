package vulc.luag.gfx.panel;

import vulc.luag.Console;
import vulc.luag.gfx.Screen;

public class DeathPanel extends Panel {

	private final int background = 0x0000cc;
	private final int foreground = 0xffffff;

	private final String text;
	private boolean hasTicked = false;

	public DeathPanel(Console console, String text) {
		super(console);
		this.text = text;
	}

	public void tick() {
		if(hasTicked) return;
		hasTicked = true;

		Screen screen = console.screen;
		screen.clear(background);

		screen.write(text, foreground, 1, 1);
	}

}
