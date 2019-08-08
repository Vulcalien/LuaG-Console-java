package vulc.luag.editor;

import vulc.luag.Console;
import vulc.luag.gfx.gui.GUIMainPanel;
import vulc.luag.gfx.panel.EditorPanel;
import vulc.luag.input.InputHandler;

public abstract class Editor {

	protected final EditorPanel panel;

	// guiPanel is an indipendent gui panel
	protected final GUIMainPanel guiPanel;
	protected final InputHandler input;

	public Editor(Console console, EditorPanel panel, int x, int y, int w, int h) {
		this.panel = panel;
		this.guiPanel = new GUIMainPanel(console, x, y, w, h);
		this.input = guiPanel.input;
	}

	public void onShow() {
		guiPanel.init();
		panel.mainPanel.add(this.guiPanel);
	}

	public abstract void tick();

	public abstract String getTitle();

	public void remove() {
		guiPanel.removeInputListeners();
	}

}
