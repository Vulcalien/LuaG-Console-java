package vulc.luag.gfx.panel;

import vulc.luag.Console;
import vulc.luag.game.Game;

public class GamePanel extends Panel {

	private final Game game;

	public GamePanel() {
		this.game = new Game();
	}

	public void init() {
		try {
			if(Console.cartridge != null) {
				if(!game.initCartridgeResources()) return;
			} else {
				if(!game.initDevResources()) return;
			}
			game.initScript();
		} catch(Exception e) {
			// no error should be uncaught
			// this prevents the console to crash
			Console.die("Error:\n"
			            + "uncaught error");
			e.printStackTrace();
		}
	}

	public void remove() {
		game.remove();

		// Game's interface can set screen's transparent colors.
		// This will reset it.
		Console.SCREEN.setTransparent();
	}

	public void tick() {
		game.tick();
	}

}
