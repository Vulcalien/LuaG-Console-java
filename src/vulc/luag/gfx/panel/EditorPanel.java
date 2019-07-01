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
		if(!game.initResources()) return;

		mapEditor = new MapEditor(console, this, 0, 10, guiPanel.w, guiPanel.h - 20);

		currentEditor = mapEditor;
	}

	public void remove() {
		if(currentEditor != null) currentEditor.remove();
		guiPanel.removeInputListeners();
	}

	public void tick() {
		currentEditor.tick();
		guiPanel.tick();
		guiPanel.render(console.screen);
	}

}
