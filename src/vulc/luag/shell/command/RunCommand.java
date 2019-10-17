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

	public void run(Shell shell, String[] args) {
		if(args.length >= 1) {
			Console.cartridge = args[0] + "." + Game.CARTRIDGE_EXTENSION;
		} else {
			Console.cartridge = null;

			if(Console.mode == Mode.USER_SHELL) {
				shell.write("Error:\n"
				          + "insert cartridge's name\n\n");
				return;
			}
		}

		Console.switchToPanel(new GamePanel());
	}

}
