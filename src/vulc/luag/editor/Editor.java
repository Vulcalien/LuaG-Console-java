package vulc.luag.editor;

import vulc.luag.Console;
import vulc.luag.gfx.gui.GUIMainContainer;
import vulc.luag.gfx.panel.EditorPanel;
import vulc.luag.input.InputHandler;

public abstract class Editor {

	public final GUIMainContainer guiMainPanel;
	protected final EditorPanel panel;
	protected final InputHandler input;

	public Editor(Console console, EditorPanel panel, int x, int y, int w, int h) {
		this.panel = panel;
		this.guiMainPanel = new GUIMainContainer(console, x, y, w, h);
		this.input = guiMainPanel.input;
	}

	public void onShow() {
		guiMainPanel.init();
		panel.mainPanel.add(this.guiMainPanel);
	}

	public abstract void tick();

	public abstract String getTitle();

	public void remove() {
		guiMainPanel.removeInputListeners();
	}

}
