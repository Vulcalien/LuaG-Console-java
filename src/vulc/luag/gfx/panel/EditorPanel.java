package vulc.luag.gfx.panel;

import java.io.IOException;

import javax.imageio.ImageIO;

import vulc.bitmap.BoolBitmap;
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
	public final int foregroundColor = 0xeecccc;

	public final int btnDist = 3;

	public final GUILabel footerLabel;

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

		GUIButton cmdBtn = new GUIButton(1, 1, 8, 8);
		cmdBtn.opaque = true;
		cmdBtn.background = secondaryColor;
		try {
			cmdBtn.setImage(new BoolBitmap(ImageIO.read(EditorPanel.class.getResourceAsStream("/res/icons/cmd.png"))),
			                foregroundColor);
		} catch(IOException e) {
			e.printStackTrace();
		}
		cmdBtn.action = () -> {
			console.switchToPanel(new CmdPanel(console));
		};
		guiPanel.add(cmdBtn);

		GUIButton mapEditBtn = new GUIButton(cmdBtn.x + cmdBtn.w + btnDist, 1, 8, 8);
		mapEditBtn.opaque = true;
		mapEditBtn.background = secondaryColor;
		try {
			mapEditBtn.setImage(new BoolBitmap(ImageIO.read(EditorPanel.class.getResourceAsStream("/res/icons/map_editor.png"))),
			                    foregroundColor);
		} catch(IOException e) {
			e.printStackTrace();
		}
		mapEditBtn.action = () -> {
			switchToEditor(mapEditor);
		};
		guiPanel.add(mapEditBtn);

		GUIButton sprEditBtn = new GUIButton(mapEditBtn.x + mapEditBtn.w + btnDist, 1, 8, 8);
		sprEditBtn.opaque = true;
		sprEditBtn.background = secondaryColor;
		try {
			sprEditBtn.setImage(new BoolBitmap(ImageIO.read(EditorPanel.class.getResourceAsStream("/res/icons/sprite_editor.png"))),
			                    foregroundColor);
		} catch(IOException e) {
			e.printStackTrace();
		}
		sprEditBtn.action = () -> {
			switchToEditor(spriteEditor);
		};
		guiPanel.add(sprEditBtn);

		//
		//---FOOTER---\\
		//
		footerLabel = new GUILabel(1, guiPanel.h - 9, Screen.FONT.lengthOf("Game Editor"), 8);
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

		footerLabel.text = editor.getTitle();
	}

}
