package vulc.jlconsole.editor;

import vulc.jlconsole.Console;
import vulc.jlconsole.gfx.panel.EditorPanel;

public abstract class Editor {

	protected final EditorPanel panel;

	public Editor(Console console, EditorPanel panel) {
		this.panel = panel;
	}

	public abstract void tick();

	public void remove() {
	}

}
