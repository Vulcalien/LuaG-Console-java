package vulc.jlconsole.editor.map;

import java.awt.event.MouseEvent;

import vulc.jlconsole.Console;
import vulc.jlconsole.editor.Editor;
import vulc.jlconsole.game.Game;
import vulc.jlconsole.gfx.panel.EditorPanel;
import vulc.jlconsole.input.InputHandler;
import vulc.jlconsole.input.InputHandler.Key;
import vulc.jlconsole.input.InputHandler.KeyType;

public class MapEditor extends Editor {

	private static final Key
	MOUSE_1 = new Key(KeyType.MOUSE, MouseEvent.BUTTON1);

	private int xOffset = 0, yOffset = 0;

	private int selectedTile = 1;

	public MapEditor(Console console, EditorPanel panel) {
		super(console, panel);
	}

	public void tick() {
		Game game = panel.game;

		mouse_if:
		if(MOUSE_1.isKeyDown()) {
			int xt = (InputHandler.xMouse - xOffset) / 8 / Console.SCALE;
			int yt = (InputHandler.yMouse - yOffset - 10 * Console.SCALE) / 8 / Console.SCALE;

			if(xt < 0 || yt < 0 || xt >= game.map.width || yt >= game.map.height) break mouse_if;

			game.map.setTile(xt, yt, selectedTile);
		}

		game.map.render(panel.screen, game, xOffset, yOffset, 1);
	}

}
