package vulc.luag.editor;

import vulc.luag.Console;
import vulc.luag.gfx.gui.GUIContainer;
import vulc.luag.gfx.panel.EditorPanel;

public abstract class Editor {

	public final GUIContainer guiPanel;
	protected final EditorPanel panel;

	public Editor(Console console, EditorPanel panel, int x, int y, int w, int h) {
		this.panel = panel;
		this.guiPanel = new GUIContainer(console, x, y, w, h);
	}

	public void init() {
		guiPanel.init();
		panel.guiPanel.add(this.guiPanel);
	}

	public abstract void tick();

	public void remove() {
		guiPanel.removeInputListeners();
	}

}
