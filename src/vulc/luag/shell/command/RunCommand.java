package vulc.luag.shell.command;

import vulc.luag.Console;
import vulc.luag.Console.Mode;
import vulc.luag.game.Game;
import vulc.luag.gfx.panel.GamePanel;
import vulc.luag.shell.Shell;

public class RunCommand extends ShellCommand {

	public RunCommand() {
		super("run");
	}

	public void run(String[] args) {
		if(args.length >= 1) {
			Console.cartridge = args[0] + "." + Game.CARTRIDGE_EXTENSION;
		} else {
			Console.cartridge = null;

			if(Console.mode == Mode.USER_SHELL) {
				Shell.write("Error:\n"
				            + "insert cartridge's name\n\n");
				return;
			}
		}

		Console.switchToPanel(new GamePanel());
	}

	protected String getHelpMessage() {
		if(Console.mode == Mode.DEVELOPER) {
			return "`run <nothing or\n"
			       + "     cartridge's name>`\n"
			       + "runs the game/cartridge";
		} else {
			return "`run <cartridge's name>`\n"
			       + "runs the cartridge";
		}
	}

}
