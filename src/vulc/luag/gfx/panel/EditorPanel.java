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
import vulc.luag.gfx.gui.GUILabel;
import vulc.luag.gfx.gui.GUIMainPanel;
import vulc.luag.gfx.gui.GUIPanel;

public class EditorPanel extends Panel {

	public final int primaryColor = 0xdd4444;
	public final int secondaryColor = 0xaa4444;
	public final int foregroundColor = 0xeecccc;

	public final int btnDist = 3;

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
		try {
			cmdBtn.setImage(new BoolBitmap(ImageIO.read(EditorPanel.class.getResourceAsStream("/res/icons/cmd.png"))),
			                foregroundColor);
		} catch(IOException e) {
			e.printStackTrace();
		}
		cmdBtn.action = () -> {
			console.switchToPanel(new CmdPanel(console));
		};
		headerPanel.add(cmdBtn);

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
		headerPanel.add(mapEditBtn);

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
		headerPanel.add(sprEditBtn);

		//
		//---FOOTER---\\
		//

		GUIPanel footerPanel = new GUIPanel(0, mainPanel.h - 10, mainPanel.w, 10);
		footerPanel.background = primaryColor;
		mainPanel.add(footerPanel);

		footerLabel = new GUILabel(1, 1, Screen.FONT.lengthOf("Game Editor"), 8);
		footerLabel.textColor = 0xDDaaaa;
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
	}

	private void switchToEditor(Editor editor) {
		if(currentEditor != null) currentEditor.remove();
		editor.onShow();
		currentEditor = editor;

		footerLabel.text = editor.getTitle();
	}

}
