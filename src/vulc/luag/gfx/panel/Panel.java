package vulc.luag.gfx.panel;

import vulc.luag.Console;

public abstract class Panel {

	protected final Console console;

	public Panel(Console console) {
		this.console = console;
	}

	public void init() {
	}

	public void remove() {
	}

	public abstract void tick();

}
