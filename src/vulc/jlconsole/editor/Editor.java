package vulc.jlconsole.editor;

import vulc.jlconsole.Console;
import vulc.jlconsole.gfx.gui.GUIContainer;
import vulc.jlconsole.gfx.panel.EditorPanel;

public abstract class Editor {

	public final GUIContainer guiPanel;
	protected final EditorPanel panel;

	public Editor(Console console, EditorPanel panel, int x, int y, int w, int h) {
		this.panel = panel;
		this.guiPanel = new GUIContainer(console, x, y, w, h);
	}

	public abstract void tick();

	public void remove() {
		guiPanel.removeInputListeners();
	}

}
