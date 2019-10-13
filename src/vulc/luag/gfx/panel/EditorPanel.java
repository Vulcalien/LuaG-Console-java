package vulc.luag.gfx.panel;

import vulc.luag.Console;
import vulc.luag.editor.Editor;
import vulc.luag.editor.map.MapEditor;
import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.game.Game;
import vulc.luag.gfx.Colors;
import vulc.luag.gfx.Icons;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIButton;
import vulc.luag.gfx.gui.GUILabel;
import vulc.luag.gfx.gui.GUIMainPanel;
import vulc.luag.gfx.gui.GUIPanel;

public class EditorPanel extends Panel {

	public final int btnDist = 3;

	public final GUIButton saveBtn;
	public final GUILabel footerLabel;

	public final Game game;
	public final GUIMainPanel mainPanel;
	public Editor currentEditor;

	public Editor mapEditor;
	public Editor spriteEditor;

	public EditorPanel(Console console) {
		super(console);
		this.game = new Game(console);
		mainPanel = new GUIMainPanel(console, 0, 0, console.screen.width, console.screen.height);
		mainPanel.background = 0x000000;
		mainPanel.init();

		//
		//---HEADER---\\
		//

		GUIPanel headerPanel = new GUIPanel(0, 0, mainPanel.w, 10);
		headerPanel.background = Colors.BACKGROUND_0;
		mainPanel.add(headerPanel);

		GUIButton cmdBtn = new GUIButton(1, 1, 8, 8);
		cmdBtn.opaque = true;
		cmdBtn.background = Colors.BACKGROUND_1;
		cmdBtn.setImage(Icons.CMD, Colors.FOREGROUND_0);
		cmdBtn.action = () -> {
			console.switchToPanel(new CmdPanel(console));
		};
		headerPanel.add(cmdBtn);

		GUIButton mapEditBtn = new GUIButton(cmdBtn.x + cmdBtn.w + btnDist, 1, 8, 8);
		mapEditBtn.opaque = true;
		mapEditBtn.background = Colors.BACKGROUND_1;
		mapEditBtn.setImage(Icons.MAP_EDITOR, Colors.FOREGROUND_0);
		mapEditBtn.action = () -> {
			switchToEditor(mapEditor);
		};
		headerPanel.add(mapEditBtn);

		GUIButton sprEditBtn = new GUIButton(mapEditBtn.x + mapEditBtn.w + btnDist, 1, 8, 8);
		sprEditBtn.opaque = true;
		sprEditBtn.background = Colors.BACKGROUND_1;
		sprEditBtn.setImage(Icons.SPRITE_EDITOR, Colors.FOREGROUND_0);
		sprEditBtn.action = () -> {
			switchToEditor(spriteEditor);
		};
		headerPanel.add(sprEditBtn);

		saveBtn = new GUIButton(headerPanel.w - 9, 1, 8, 8);
		saveBtn.opaque = true;
		saveBtn.background = Colors.BACKGROUND_1;
		saveBtn.setImage(Icons.SAVE, Colors.FOREGROUND_0);
		saveBtn.action = () -> { // BUG saving if holding the mouse. Should be only once per click
			Console.LOGGER.info("saving");

			mapEditor.onSave();
			spriteEditor.onSave();
		};
		headerPanel.add(saveBtn);

		//
		//---FOOTER---\\
		//

		GUIPanel footerPanel = new GUIPanel(0, mainPanel.h - 10, mainPanel.w, 10);
		footerPanel.background = Colors.BACKGROUND_0;
		mainPanel.add(footerPanel);

		footerLabel = new GUILabel(1, 1, Screen.FONT.widthOf("Game Editor"), 8);
		footerLabel.textColor = Colors.FOREGROUND_0;
		footerPanel.add(footerLabel);
	}

	public void init() {
		if(!game.initDevResources()) return;

		mapEditor = new MapEditor(console, this, 0, 10, mainPanel.w, mainPanel.h - 20);
		spriteEditor = new SpriteEditor(console, this, 0, 10, mainPanel.w, mainPanel.h - 20);

		switchToEditor(mapEditor);
	}

	public void remove() {
		game.remove();

		if(mapEditor != null) mapEditor.remove();
		if(spriteEditor != null) spriteEditor.remove();

		mainPanel.removeInputListeners();
	}

	public void tick() {
		currentEditor.tick();
		mainPanel.tick();
		mainPanel.render(console.screen);

		if(mapEditor.shouldSave()
		   || spriteEditor.shouldSave()) {
			saveBtn.colorAsBool = Colors.FOREGROUND_HIGHLIGHT;
		} else {
			saveBtn.colorAsBool = Colors.FOREGROUND_0;
		}
	}

	private void switchToEditor(Editor editor) {
		if(currentEditor != null) currentEditor.remove();
		editor.onShow();
		currentEditor = editor;

		footerLabel.text = editor.getTitle();
	}

}
