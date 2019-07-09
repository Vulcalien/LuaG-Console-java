package vulc.luag.editor.map;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import vulc.luag.Console;
import vulc.luag.editor.Editor;
import vulc.luag.editor.map.gui.SaveMapButton;
import vulc.luag.game.Game;
import vulc.luag.game.map.Map;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIComponent;
import vulc.luag.gfx.gui.GUILabel;
import vulc.luag.gfx.gui.GUITextBox;
import vulc.luag.gfx.panel.EditorPanel;
import vulc.luag.input.InputHandler;
import vulc.luag.input.InputHandler.Key;
import vulc.luag.input.InputHandler.KeyType;

public class MapEditor extends Editor {

	private final Key mouse1;
	private final Key moveUp;
	private final Key moveLeft;
	private final Key moveDown;
	private final Key moveRight;

	private final InputHandler input;
	private final int editWidth = 128, editHeight = 128;

	private final GUITextBox hTextBox, wTextBox, selectTileTxt;
	private final SaveMapButton saveButton;

	private int xOffset = 0, yOffset = 0;
	private int selectedTile = 0;

	public MapEditor(Console console, EditorPanel panel, int x, int y, int w, int h) {
		super(console, panel, x, y, w, h);

		this.input = guiPanel.input;
		panel.guiPanel.add(this.guiPanel);

		mouse1 = input.new Key(KeyType.MOUSE, MouseEvent.BUTTON1);
		moveUp = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_W);
		moveLeft = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_A);
		moveDown = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_S);
		moveRight = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_D);

		// INTERFACE
		guiPanel.opaque = true;
		guiPanel.background = 0x000000;

		GUIComponent levelComponent = new GUIComponent(0, 0, editWidth, editHeight) {
			public void render(Screen screen) {
				panel.game.map.render(screen, panel.game, xOffset, yOffset, 1);
			}
		};
		guiPanel.add(levelComponent);

		GUIComponent p1 = new GUIComponent(editWidth, 0, guiPanel.w - editWidth, guiPanel.h);
		p1.opaque = true;
		p1.background = 0x999999;
		guiPanel.add(p1);

		GUIComponent p2 = new GUIComponent(0, editHeight, editWidth, guiPanel.h - editHeight);
		p2.opaque = true;
		p2.background = 0x999999;
		guiPanel.add(p2);

		GUILabel header = new GUILabel(editWidth + 1, 1, guiPanel.w - editWidth - 2, 10);
		header.text = "LEVEL";
		guiPanel.add(header);

		GUILabel wLabel = new GUILabel(editWidth + 1, 22, guiPanel.w - editWidth - 1, 10);
		wLabel.text = "Map W";
		guiPanel.add(wLabel);

		wTextBox = new GUITextBox(editWidth + 1, 32, guiPanel.w - editWidth - 1, 10) {
			public void onLostFocus() {
				text = panel.game.map.width + "";
			}

			public void onEnterPress() {
				super.onEnterPress();
				panel.game.map = resizeMap(panel.game.map,
				                           Integer.parseInt(wTextBox.text),
				                           Integer.parseInt(hTextBox.text));
				saveButton.setContentModified(true);
			}
		};
		wTextBox.nChars = 4;
		wTextBox.numbersOnly = true;
		wTextBox.opaque = true;
		wTextBox.text = panel.game.map.width + "";
		guiPanel.add(wTextBox);

		GUILabel hLabel = new GUILabel(editWidth + 1, 52, guiPanel.w - editWidth - 1, 10);
		hLabel.text = "Map H";
		guiPanel.add(hLabel);

		hTextBox = new GUITextBox(editWidth + 1, 62, guiPanel.w - editWidth - 1, 10) {
			public void onLostFocus() {
				text = panel.game.map.height + "";
			}

			public void onEnterPress() {
				super.onEnterPress();
				panel.game.map = resizeMap(panel.game.map,
				                           Integer.parseInt(wTextBox.text),
				                           Integer.parseInt(hTextBox.text));
				saveButton.setContentModified(true);
			}
		};
		hTextBox.nChars = 4;
		hTextBox.numbersOnly = true;
		hTextBox.opaque = true;
		hTextBox.text = panel.game.map.height + "";
		guiPanel.add(hTextBox);

		saveButton = new SaveMapButton(editWidth + 1, 100, guiPanel.w - editWidth - 2, 10, panel);
		guiPanel.add(saveButton);

		selectTileTxt = new GUITextBox(1, editHeight + 1, 19, 10) {
			public void onLostFocus() {
				text = selectedTile + "";
			}

			public void onEnterPress() {
				super.onEnterPress();
				int n = Integer.parseInt(selectTileTxt.text);
				if(n > 255) n = 255;

				selectedTile = n;
				selectTileTxt.text = n + "";
			}
		};
		selectTileTxt.text = selectedTile + "";
		selectTileTxt.nChars = 3;
		selectTileTxt.numbersOnly = true;
		selectTileTxt.opaque = true;
		guiPanel.add(selectTileTxt);

		GUIComponent spriteComp = new GUIComponent(21, editHeight + 1, 8, 8) {
			public void render(Screen screen) {
				super.render(screen);
				screen.draw(panel.game.getSprite(selectedTile, 1, 1), x, y);
			}
		};
		guiPanel.add(spriteComp);
	}

	public void tick() {
		Game game = panel.game;

		if(moveUp.isKeyDown()) yOffset--;
		if(moveLeft.isKeyDown()) xOffset--;
		if(moveDown.isKeyDown()) yOffset++;
		if(moveRight.isKeyDown()) xOffset++;

		mouse_if:
		if(mouse1.isKeyDown()) {
			int xm = (input.xMouse / Console.SCALE) - guiPanel.xParentAbs - guiPanel.x;
			int ym = (input.yMouse / Console.SCALE) - guiPanel.yParentAbs - guiPanel.y;

			if(xm < 0 || ym < 0 || xm >= editWidth || ym >= editHeight) break mouse_if;

			int xt = (xm + xOffset) / 8;
			int yt = (ym + yOffset) / 8;

			if(xt < 0 || yt < 0 || xt >= game.map.width || yt >= game.map.height) break mouse_if;

			game.map.setTile(xt, yt, selectedTile);
			saveButton.setContentModified(true);
		}
	}

	public Map resizeMap(Map old, int w, int h) {
		Map newMap = new Map(w, h);

		x_loop:
		for(int x = 0; x < old.width; x++) {
			if(x >= w) break x_loop;

			y_loop:
			for(int y = 0; y < old.height; y++) {
				if(y >= h) break y_loop;

				newMap.setTile(x, y, old.getTile(x, y));
			}
		}

		return newMap;
	}

}
