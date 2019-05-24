package vulc.jlconsole.gfx.panel;

import vulc.jlconsole.Console;
import vulc.jlconsole.ConsoleInterface;
import vulc.jlconsole.gfx.Screen;

public class GamePanel extends Panel {

	public GamePanel(Console console, Screen screen) {
		super(console, screen);
	}

	public void tick() {
		ConsoleInterface.tick();
	}

}
