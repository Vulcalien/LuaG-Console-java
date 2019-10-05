package vulc.luag.cmd.command;

import vulc.luag.Console;
import vulc.luag.Console.Mode;
import vulc.luag.cmd.Cmd;
import vulc.luag.game.Game;
import vulc.luag.gfx.panel.GamePanel;

public class RunCommand extends CmdCommand {

	public RunCommand() {
		super("run");
	}

	public void run(Cmd cmd, String[] args) {
		Console console = cmd.console;
		if(args.length >= 1) {
			console.cartridge = args[0] + "." + Game.CARTRIDGE_EXTENSION;
		} else if(console.mode == Mode.USER_CMD) {
			cmd.write("Error:\n"
			          + "insert cartridge's name\n\n");
			return;
		}

		console.switchToPanel(new GamePanel(console));
	}

}
