package vulc.luag.gfx.panel;

import vulc.luag.Console;
import vulc.luag.editor.Editor;
import vulc.luag.editor.map.MapEditor;
import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.game.Game;
import vulc.luag.gfx.Colors;
import vulc.luag.gfx.Icons;
import vulc.luag.gfx.gui.GUIButton;
import vulc.luag.gfx.gui.GUILabel;
import vulc.luag.gfx.gui.GUIMainPanel;
import vulc.luag.gfx.gui.GUIPanel;

public class EditorPanel extends Panel {

	private final int btnDist = 3;

	private final GUIButton saveBtn;
	private final GUILabel footerLabel;

	public final Game game;
	public final GUIMainPanel mainPanel;
	public Editor currentEditor;

	public Editor mapEditor;
	public Editor spriteEditor;

	public EditorPanel() {
		this.game = new Game();
		mainPanel = new GUIMainPanel(0, 0, Console.SCREEN.width, Console.SCREEN.height);
		mainPanel.background = 0x000000;
		mainPanel.init();

		//
		//---HEADER---\\
		//

		GUIPanel headerPanel = new GUIPanel(0, 0, mainPanel.w, 10);
		headerPanel.background = Colors.BACKGROUND_0;
		mainPanel.add(headerPanel);

		GUIButton shellBtn = new GUIButton(1, 1, 8, 8);
		shellBtn.opaque = true;
		shellBtn.background = Colors.BACKGROUND_1;
		shellBtn.setImage(Icons.SHELL, Colors.FOREGROUND_1);
		shellBtn.onMousePressAction = () -> {
			Console.switchToPanel(new ShellPanel());
		};
		headerPanel.add(shellBtn);

		GUIButton mapEditBtn = new GUIButton(shellBtn.x + shellBtn.w + btnDist, 1, 8, 8);
		mapEditBtn.opaque = true;
		mapEditBtn.background = Colors.BACKGROUND_1;
		mapEditBtn.setImage(Icons.MAP_EDITOR, Colors.FOREGROUND_1);
		mapEditBtn.onMousePressAction = () -> {
			switchToEditor(mapEditor);
		};
		headerPanel.add(mapEditBtn);

		GUIButton sprEditBtn = new GUIButton(mapEditBtn.x + mapEditBtn.w + btnDist, 1, 8, 8);
		sprEditBtn.opaque = true;
		sprEditBtn.background = Colors.BACKGROUND_1;
		sprEditBtn.setImage(Icons.SPRITE_EDITOR, Colors.FOREGROUND_1);
		sprEditBtn.onMousePressAction = () -> {
			switchToEditor(spriteEditor);
		};
		headerPanel.add(sprEditBtn);

		saveBtn = new GUIButton(headerPanel.w - 9, 1, 8, 8);
		saveBtn.opaque = true;
		saveBtn.background = Colors.BACKGROUND_1;
		saveBtn.setImage(Icons.SAVE, Colors.FOREGROUND_1);
		saveBtn.onMousePressAction = () -> {
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

		footerLabel = new GUILabel(1, 1, footerPanel.w - 2, 8);
		footerLabel.textColor = Colors.FOREGROUND_1;
		footerPanel.add(footerLabel);
	}

	public void init() {
		if(!game.initDevResources()) return;

		mapEditor = new MapEditor(this, 0, 10, mainPanel.w, mainPanel.h - 20);
		spriteEditor = new SpriteEditor(this, 0, 10, mainPanel.w, mainPanel.h - 20);

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
		mainPanel.render(Console.SCREEN);

		if(mapEditor.shouldSave()
		   || spriteEditor.shouldSave()) {
			saveBtn.colorAsBool = Colors.FOREGROUND_HIGHLIGHT;
		} else {
			saveBtn.colorAsBool = Colors.FOREGROUND_1;
		}
	}

	private void switchToEditor(Editor editor) {
		if(currentEditor != null) currentEditor.remove();
		editor.onShow();
		currentEditor = editor;

		footerLabel.text = editor.getTitle();
	}

}
