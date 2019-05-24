package vulc.jlconsole.gfx.panel;

import vulc.jlconsole.Console;

public abstract class Panel {

	protected final Console console;

	public Panel(Console console) {
		this.console = console;
	}

	public void preInit() {
	}

	public abstract void tick();

}
