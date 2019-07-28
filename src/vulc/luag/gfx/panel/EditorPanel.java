package vulc.luag.gfx.panel;

import vulc.luag.Console;
import vulc.luag.editor.Editor;
import vulc.luag.editor.map.MapEditor;
import vulc.luag.game.Game;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIButton;
import vulc.luag.gfx.gui.GUIContainer;

public class EditorPanel extends Panel {

	public final Game game;
	public final GUIContainer guiPanel;
	public Editor currentEditor;

	public Editor mapEditor;

	public EditorPanel(Console console) {
		super(console);
		this.game = new Game(console);
		guiPanel = new GUIContainer(console, 0, 0, console.screen.width, console.screen.height);
		guiPanel.background = 0xDD4444;
		guiPanel.init();

		GUIButton cmdBtn = new GUIButton(1, 1, Screen.FONT.lengthOf(">_") + 2, 8);
		cmdBtn.opaque = true;
		cmdBtn.background = 0xAA4444;
		cmdBtn.text = ">_";
		cmdBtn.textColor = 0xffffff;
		cmdBtn.action = () -> {
			console.switchToPanel(new CmdPanel(console));
		};
		guiPanel.add(cmdBtn);

		GUIButton mapEditBtn = new GUIButton(cmdBtn.w + 2, 1, Screen.FONT.lengthOf("Map") + 2, 8);
		mapEditBtn.opaque = true;
		mapEditBtn.background = 0xAA4444;
		mapEditBtn.text = "Map";
		mapEditBtn.textColor = 0xffffff;
		mapEditBtn.action = () -> {
			switchToEditor(mapEditor);
		};
		guiPanel.add(mapEditBtn);
	}

	public void init() {
		if(!game.initResources()) return;

		mapEditor = new MapEditor(console, this, 0, 10, guiPanel.w, guiPanel.h - 20);

		switchToEditor(mapEditor);
	}

	public void remove() {
		mapEditor.remove();

		guiPanel.removeInputListeners();
	}

	public void tick() {
		currentEditor.tick();
		guiPanel.tick();
		guiPanel.render(console.screen);
	}

	private void switchToEditor(Editor editor) {
		if(currentEditor != null) currentEditor.remove();
		editor.init();
		currentEditor = editor;
	}

}
