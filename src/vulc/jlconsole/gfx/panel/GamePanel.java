package vulc.jlconsole.gfx.panel;

import vulc.jlconsole.Console;
import vulc.jlconsole.game.Game;

public class GamePanel extends Panel {

	private final Game game;

	public GamePanel(Console console, Game game) {
		super(console);
		this.game = game;
	}

	public void preInit() {
		game.init(console);
	}

	public void tick() {
		game.tick();
	}

}
