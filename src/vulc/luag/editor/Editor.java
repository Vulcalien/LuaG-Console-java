package vulc.luag.editor;

import vulc.luag.Console;
import vulc.luag.gfx.gui.GUIContainer;
import vulc.luag.gfx.panel.EditorPanel;
import vulc.luag.input.InputHandler;

public abstract class Editor {

	public final GUIContainer guiPanel;
	protected final EditorPanel panel;
	protected final InputHandler input;

	public Editor(Console console, EditorPanel panel, int x, int y, int w, int h) {
		this.panel = panel;
		this.guiPanel = new GUIContainer(console, x, y, w, h);
		this.input = guiPanel.input;
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
