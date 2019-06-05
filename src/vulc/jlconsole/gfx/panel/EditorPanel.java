package vulc.jlconsole.gfx.panel;

import vulc.jlconsole.Console;
import vulc.jlconsole.editor.Editor;
import vulc.jlconsole.editor.map.MapEditor;
import vulc.jlconsole.game.Game;
import vulc.jlconsole.gfx.gui.GUIContainer;

public class EditorPanel extends Panel {

	public final Game game;
	public final GUIContainer guiPanel;

	public Editor
	mapEditor;

	public Editor currentEditor;

	//TODO switch to a GUI based interface
	public EditorPanel(Console console) {
		super(console);
		this.game = new Game(console);
		guiPanel = new GUIContainer(console, 0, 0, console.screen.width, console.screen.height);
		guiPanel.background = 0xDD4444;
	}

	public void init() {
		game.initResources();

		mapEditor = new MapEditor(console, this, 0, 10, guiPanel.w, guiPanel.h - 20);

		currentEditor = mapEditor;
	}

	public void remove() {
		currentEditor.remove();
	}

	public void tick() {
		currentEditor.tick();
		guiPanel.tick();
		guiPanel.render(console.screen);
	}

}
