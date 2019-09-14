package vulc.luag.gfx.panel;

import vulc.luag.Console;
import vulc.luag.editor.Editor;
import vulc.luag.editor.map.MapEditor;
import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.game.Game;
import vulc.luag.gfx.Icons;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIButton;
import vulc.luag.gfx.gui.GUILabel;
import vulc.luag.gfx.gui.GUIMainPanel;
import vulc.luag.gfx.gui.GUIPanel;

public class EditorPanel extends Panel {

	public final int primaryColor = 0xdd4444;
	public final int secondaryColor = 0xaa4444;

	public final int primaryTextColor = 0xeecccc;
	public final int secondaryTextColor = 0x000000;

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
		headerPanel.background = primaryColor;
		mainPanel.add(headerPanel);

		GUIButton cmdBtn = new GUIButton(1, 1, 8, 8);
		cmdBtn.opaque = true;
		cmdBtn.background = secondaryColor;
		cmdBtn.setImage(Icons.CMD, primaryTextColor);
		cmdBtn.action = () -> {
			console.switchToPanel(new CmdPanel(console));
		};
		headerPanel.add(cmdBtn);

		GUIButton mapEditBtn = new GUIButton(cmdBtn.x + cmdBtn.w + btnDist, 1, 8, 8);
		mapEditBtn.opaque = true;
		mapEditBtn.background = secondaryColor;
		mapEditBtn.setImage(Icons.MAP_EDITOR, primaryTextColor);
		mapEditBtn.action = () -> {
			switchToEditor(mapEditor);
		};
		headerPanel.add(mapEditBtn);

		GUIButton sprEditBtn = new GUIButton(mapEditBtn.x + mapEditBtn.w + btnDist, 1, 8, 8);
		sprEditBtn.opaque = true;
		sprEditBtn.background = secondaryColor;
		sprEditBtn.setImage(Icons.SPRITE_EDITOR, primaryTextColor);
		sprEditBtn.action = () -> {
			switchToEditor(spriteEditor);
		};
		headerPanel.add(sprEditBtn);

		saveBtn = new GUIButton(headerPanel.w - 9, 1, 8, 8);
		saveBtn.opaque = true;
		saveBtn.background = secondaryColor;
		saveBtn.setImage(Icons.SAVE, primaryTextColor);
		saveBtn.action = () -> {
			mapEditor.onSave();
			spriteEditor.onSave();
		};
		headerPanel.add(saveBtn);

		//
		//---FOOTER---\\
		//

		GUIPanel footerPanel = new GUIPanel(0, mainPanel.h - 10, mainPanel.w, 10);
		footerPanel.background = primaryColor;
		mainPanel.add(footerPanel);

		footerLabel = new GUILabel(1, 1, Screen.FONT.lengthOf("Game Editor"), 8);
		footerLabel.textColor = primaryTextColor;
		footerPanel.add(footerLabel);
	}

	public void init() {
		if(!game.initResources()) return;

		mapEditor = new MapEditor(console, this, 0, 10, mainPanel.w, mainPanel.h - 20);
		spriteEditor = new SpriteEditor(console, this, 0, 10, mainPanel.w, mainPanel.h - 20);

		switchToEditor(mapEditor);
	}

	public void remove() {
		mapEditor.remove();
		spriteEditor.remove();

		mainPanel.removeInputListeners();
	}

	public void tick() {
		currentEditor.tick();
		mainPanel.tick();
		mainPanel.render(console.screen);

		if(mapEditor.shouldSave()
		   || spriteEditor.shouldSave()) {
			saveBtn.colorAsBool = 0xffff55;
		} else {
			saveBtn.colorAsBool = primaryTextColor;
		}
	}

	private void switchToEditor(Editor editor) {
		if(currentEditor != null) currentEditor.remove();
		editor.onShow();
		currentEditor = editor;

		footerLabel.text = editor.getTitle();
	}

}
