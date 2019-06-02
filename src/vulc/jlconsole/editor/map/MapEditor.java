package vulc.jlconsole.editor.map;

import java.awt.event.MouseEvent;

import com.sun.glass.events.KeyEvent;

import vulc.jlconsole.Console;
import vulc.jlconsole.editor.Editor;
import vulc.jlconsole.game.Game;
import vulc.jlconsole.gfx.gui.GUIContainer;
import vulc.jlconsole.gfx.panel.EditorPanel;
import vulc.jlconsole.input.InputHandler;
import vulc.jlconsole.input.InputHandler.Key;
import vulc.jlconsole.input.InputHandler.KeyType;

public class MapEditor extends Editor {

	private final InputHandler input = new InputHandler();
	private final Key
	MOUSE_1 = input.new Key(KeyType.MOUSE, MouseEvent.BUTTON1),
	MOVE_UP = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_W),
	MOVE_LEFT = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_A),
	MOVE_DOWN = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_S),
	MOVE_RIGHT = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_D);

	private final GUIContainer guiPanel;
	private final int editWidth = 128, editHeight = 128;

	private int xOffset = 0, yOffset = 0;
	private int selectedTile = 1;

	public MapEditor(Console console, EditorPanel panel) {
		super(console, panel);
		input.init(console);

		this.guiPanel = new GUIContainer(console, 0, 0, panel.innerScreen.width, panel.innerScreen.height);

//		Screen screen = panel.innerScreen;
	}

//	private void tickGui() {
//	Screen screen = panel.innerScreen;
//
//	screen.fill(editWidth, 0, screen.width, screen.height, 0x999999);
//	screen.fill(0, editHeight, screen.width, screen.height, 0x999999);
//
//	screen.write("LEVEL", 0, editWidth + 1, 1);
//
//	screen.write("Map W", 0, editWidth + 1, 22);
//	int btnW_x = editWidth + 1;
//	int btnW_y = 30;
//	int btnW_w = screen.width - 2;
//	int btnW_h = 40;
//	screen.fill(btnW_x, btnW_y, btnW_w, btnW_h, 0xeeeeee);
//	screen.write(panel.game.map.width + "", 0, editWidth + 3, 32);
//
//	screen.write("Map H", 0, editWidth + 1, 52);
//	screen.fill(editWidth + 1, 60, screen.width - 2, 70, 0xeeeeee);
//	screen.write(panel.game.map.height + "", 0, editWidth + 3, 62);
//}

	public void tick() {
		Game game = panel.game;

		if(MOVE_UP.isKeyDown()) yOffset++;
		if(MOVE_LEFT.isKeyDown()) xOffset++;
		if(MOVE_DOWN.isKeyDown()) yOffset--;
		if(MOVE_RIGHT.isKeyDown()) xOffset--;

		mouse_if:
		if(MOUSE_1.isKeyDown()) {
			int xm = (input.xMouse / Console.SCALE) - panel.margins[1];
			int ym = (input.yMouse / Console.SCALE) - panel.margins[0];

			if(xm < 0 || ym < 0 || xm >= editWidth || ym >= editHeight) break mouse_if;

			int xt = (xm - xOffset) / 8;
			int yt = (ym - yOffset) / 8;

			if(xt < 0 || yt < 0 || xt >= game.map.width || yt >= game.map.height) break mouse_if;

			game.map.setTile(xt, yt, selectedTile);
		}

		panel.innerScreen.clear(0);
		game.map.render(panel.innerScreen, game, xOffset, yOffset, 1);
		guiPanel.tick();
		guiPanel.render(panel.innerScreen, 0, 0);
		input.tick();
	}

}
