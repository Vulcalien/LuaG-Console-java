package vulc.luag.editor;

import vulc.luag.Console;
import vulc.luag.gfx.gui.GUIPanel;
import vulc.luag.gfx.panel.EditorPanel;
import vulc.luag.input.InputHandler;

public abstract class Editor {

	protected final EditorPanel editorPanel;

	protected final GUIPanel guiPanel;
	protected final InputHandler input;

	public Editor(Console console, EditorPanel editorPanel, int x, int y, int w, int h) {
		this.editorPanel = editorPanel;
		this.guiPanel = new GUIPanel(x, y, w, h);
		this.input = editorPanel.mainPanel.input;
	}

	public void onShow() {
		editorPanel.mainPanel.add(this.guiPanel);
	}

	public void tick() {
	}

	public abstract String getTitle();

	public void remove() {
		editorPanel.mainPanel.remove(this.guiPanel);
	}

}
