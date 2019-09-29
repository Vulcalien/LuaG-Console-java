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
		if(console.cartridge != null) {
			if(!game.initCartridgeResources()) return;
		} else {
			if(!game.initResources()) return;
		}
		game.initScript();
	}

	public void remove() {
		game.input.remove();
		game.sounds.remove();

		// Game's interface can set screen's transparent colors.
		// This will reset it.
		console.screen.setTransparent();
	}

	public void tick() {
		game.tick();
	}

}
