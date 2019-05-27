package vulc.jlconsole.gfx.panel;

import vulc.jlconsole.Console;

public abstract class BootablePanel extends Panel {

	public BootablePanel(Console console) {
		super(console);
	}

	public abstract void preInit();

	public abstract void tick();

}
