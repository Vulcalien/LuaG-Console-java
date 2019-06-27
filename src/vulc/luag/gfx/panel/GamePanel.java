package vulc.luag.gfx.panel;

import vulc.luag.Console;
import vulc.luag.game.Game;

public class GamePanel extends Panel {

	private final Game game;

	public GamePanel(Console console) {
		super(console);
		this.game = new Game(console);
	}

	public void init() {
		if(!game.initResources()) return;
		game.initScript();
	}

	public void remove() {
		game.input.remove();
	}

	public void tick() {
		game.tick();
	}

}
