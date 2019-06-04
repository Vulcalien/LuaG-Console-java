package vulc.jlconsole.editor.map;

import java.awt.event.MouseEvent;

import com.sun.glass.events.KeyEvent;

import vulc.jlconsole.Console;
import vulc.jlconsole.editor.Editor;
import vulc.jlconsole.editor.map.gui.SaveMapButton;
import vulc.jlconsole.game.Game;
import vulc.jlconsole.game.map.Map;
import vulc.jlconsole.gfx.Screen;
import vulc.jlconsole.gfx.gui.GUIComponent;
import vulc.jlconsole.gfx.gui.GUIContainer;
import vulc.jlconsole.gfx.gui.GUILabel;
import vulc.jlconsole.gfx.gui.GUITextBox;
import vulc.jlconsole.gfx.panel.EditorPanel;
import vulc.jlconsole.input.InputHandler;
import vulc.jlconsole.input.InputHandler.Key;
import vulc.jlconsole.input.InputHandler.KeyType;

public class MapEditor extends Editor {

	private final Key
	MOUSE_1,
	MOVE_UP,
	MOVE_LEFT,
	MOVE_DOWN,
	MOVE_RIGHT;

	private final GUIContainer guiPanel;
	private final InputHandler input;
	private final int editWidth = 128, editHeight = 128;

	private final GUITextBox hTextBox, wTextBox;
	private final SaveMapButton saveButton;

	private int xOffset = 0, yOffset = 0;
	private int selectedTile = 1;

	public MapEditor(Console console, EditorPanel panel) {
		super(console, panel);

		this.guiPanel = new GUIContainer(console, 0, 0, panel.innerScreen.width, panel.innerScreen.height);
		this.input = guiPanel.input;

		guiPanel.xInputOff = panel.margins[1];
		guiPanel.yInputOff = panel.margins[0];

		MOUSE_1 = input.new Key(KeyType.MOUSE, MouseEvent.BUTTON1);
		MOVE_UP = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_W);
		MOVE_LEFT = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_A);
		MOVE_DOWN = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_S);
		MOVE_RIGHT = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_D);

		//INTERFACE
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

		wTextBox = new GUITextBox(editWidth + 1, 30, guiPanel.w - editWidth - 1, 10) {
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

		hTextBox = new GUITextBox(editWidth + 1, 60, guiPanel.w - editWidth - 1, 10) {
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
	}

	public void remove() {
		guiPanel.remove(null);
		input.remove();
	}

	public void tick() {
		Game game = panel.game;

		if(MOVE_UP.isKeyDown()) yOffset--;
		if(MOVE_LEFT.isKeyDown()) xOffset--;
		if(MOVE_DOWN.isKeyDown()) yOffset++;
		if(MOVE_RIGHT.isKeyDown()) xOffset++;

		mouse_if:
		if(MOUSE_1.isKeyDown()) {
			int xm = (input.xMouse / Console.SCALE) - panel.margins[1];
			int ym = (input.yMouse / Console.SCALE) - panel.margins[0];

			if(xm < 0 || ym < 0 || xm >= editWidth || ym >= editHeight) break mouse_if;

			int xt = (xm + xOffset) / 8;
			int yt = (ym + yOffset) / 8;

			if(xt < 0 || yt < 0 || xt >= game.map.width || yt >= game.map.height) break mouse_if;

			game.map.setTile(xt, yt, selectedTile);
			saveButton.setContentModified(true);
		}

		guiPanel.tick();
		guiPanel.render(panel.innerScreen);
	}

	public Map resizeMap(Map old, int w, int h) {
		Map newMap = new Map(w, h);

		x_loop:
		for(int x = 0; x < old.width; x++) {
			if(x >= w) break x_loop;

			y_loop:
			for(int y = 0; y < old.height; y++) {
				if(x >= h) break y_loop;

				newMap.setTile(x, y, old.getTile(x, y));
			}
		}

		return newMap;
	}

}
