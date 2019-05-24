package vulc.jlconsole.gfx.panel;

import vulc.jlconsole.Console;
import vulc.jlconsole.gfx.Screen;

public abstract class Panel {

	protected final Console console;
	protected final Screen screen;

	public Panel(Console console, Screen screen) {
		this.console = console;
		this.screen = screen;
	}

	public abstract void tick();

}
