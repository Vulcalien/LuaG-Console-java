package vulc.jlconsole.gfx.panel;

import vulc.jlconsole.Console;
import vulc.jlconsole.editor.Editor;
import vulc.jlconsole.editor.map.MapEditor;
import vulc.jlconsole.game.Game;
import vulc.jlconsole.gfx.Screen;
import vulc.jlconsole.gfx.gui.GUIButton;
import vulc.jlconsole.gfx.gui.GUIContainer;

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

		// DEBUG
		GUIButton cmdBtn = new GUIButton(0, 0, Screen.FONT.lengthOf(">_") + 2, 10);
		cmdBtn.opaque = true;
		cmdBtn.background = 0xAA4444;
		cmdBtn.text = ">_";
		cmdBtn.textColor = 0xffffff;
		cmdBtn.action = () -> {
			Panel cmdPanel = new CmdPanel(console);
			console.currentPanel = cmdPanel;
			cmdPanel.init();
			this.remove();
		};
		guiPanel.add(cmdBtn);
	}

	public void init() {
		game.initResources();

		mapEditor = new MapEditor(console, this, 0, 10, guiPanel.w, guiPanel.h - 20);

		currentEditor = mapEditor;
	}

	public void remove() {
		currentEditor.remove();
		guiPanel.removeInputListeners();
	}

	public void tick() {
		currentEditor.tick();
		guiPanel.tick();
		guiPanel.render(console.screen);
	}

}
