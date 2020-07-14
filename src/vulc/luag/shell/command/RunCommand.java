package vulc.luag.shell.command;

import vulc.luag.Console;
import vulc.luag.Console.Mode;
import vulc.luag.game.Game;
import vulc.luag.gfx.panel.GamePanel;

public class RunCommand extends ShellCommand {

	public RunCommand() {
		super("run");
	}

	public void run(String[] args) {
		if(args.length >= 1) {
			Console.cartridge = Console.rootDirectory + args[0] + "." + Game.CARTRIDGE_EXTENSION;
		} else {
			Console.cartridge = null;

			if(Console.mode == Mode.USER_SHELL) {
				Console.die("Error:\n"
				            + "insert cartridge name");
				return;
			}
		}
		Console.switchToPanel(new GamePanel());
	}

	protected String getHelpMessage() {
		if(Console.mode == Mode.DEVELOPER) {
			return "'run'\n"
			       + "runs the developed game\n"
			       + "'run <cartridge name>'\n"
			       + "runs the cartridge";
		} else {
			return "'run <cartridge name>'\n"
			       + "runs the cartridge";
		}
	}

}
