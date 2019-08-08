package vulc.luag.gfx.panel;

import vulc.luag.Console;
import vulc.luag.editor.Editor;
import vulc.luag.editor.map.MapEditor;
import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.game.Game;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIButton;
import vulc.luag.gfx.gui.GUIContainer;
import vulc.luag.gfx.gui.GUILabel;

public class EditorPanel extends Panel {

	public final int primaryColor = 0xdd4444;
	public final int secondaryColor = 0xaa4444;
	public final int textColor = 0xffffff;

	public final Game game;
	public final GUIContainer guiPanel;
	public Editor currentEditor;

	public Editor mapEditor;
	public Editor spriteEditor;

	public EditorPanel(Console console) {
		super(console);
		this.game = new Game(console);
		guiPanel = new GUIContainer(console, 0, 0, console.screen.width, console.screen.height);
		guiPanel.background = primaryColor;
		guiPanel.init();

		//
		//---HEADER---\\
		//

		GUIButton cmdBtn = new GUIButton(1, 1, Screen.FONT.lengthOf(">_") + 2, 8);
		cmdBtn.opaque = true;
		cmdBtn.background = secondaryColor;
		cmdBtn.text = ">_";
		cmdBtn.textColor = textColor;
		cmdBtn.action = () -> {
			console.switchToPanel(new CmdPanel(console));
		};
		guiPanel.add(cmdBtn);

		GUIButton mapEditBtn = new GUIButton(cmdBtn.x + cmdBtn.w + 1, 1, Screen.FONT.lengthOf("Map") + 2, 8);
		mapEditBtn.opaque = true;
		mapEditBtn.background = secondaryColor;
		mapEditBtn.text = "Map";
		mapEditBtn.textColor = textColor;
		mapEditBtn.action = () -> {
			switchToEditor(mapEditor);
		};
		guiPanel.add(mapEditBtn);

		GUIButton sprEditBtn = new GUIButton(mapEditBtn.x + mapEditBtn.w + 1, 1, Screen.FONT.lengthOf("Spr"), 8);
		sprEditBtn.opaque = true;
		sprEditBtn.background = secondaryColor;
		sprEditBtn.text = "Spr";
		sprEditBtn.textColor = textColor;
		sprEditBtn.action = () -> {
			switchToEditor(spriteEditor);
		};
		guiPanel.add(sprEditBtn);

		//
		//---FOOTER---\\
		//
		GUILabel footerLabel = new GUILabel(1, guiPanel.h - 9, Screen.FONT.lengthOf("Game Editor"), 8);
		footerLabel.text = "Game Editor";
		footerLabel.textColor = 0xDDaaaa;
		guiPanel.add(footerLabel);
	}

	public void init() {
		if(!game.initResources()) return;

		mapEditor = new MapEditor(console, this, 0, 10, guiPanel.w, guiPanel.h - 20);
		spriteEditor = new SpriteEditor(console, this, 0, 10, guiPanel.w, guiPanel.h - 20);

		switchToEditor(mapEditor);
	}

	public void remove() {
		mapEditor.remove();
		spriteEditor.remove();

		guiPanel.removeInputListeners();
	}

	public void tick() {
		currentEditor.tick();
		guiPanel.tick();
		guiPanel.render(console.screen);
	}

	private void switchToEditor(Editor editor) {
		if(currentEditor != null) currentEditor.remove();
		editor.onShow();
		currentEditor = editor;
	}

}
