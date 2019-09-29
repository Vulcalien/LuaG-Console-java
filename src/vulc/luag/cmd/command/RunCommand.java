package vulc.luag.cmd.command;

import vulc.luag.Console;
import vulc.luag.cmd.Cmd;
import vulc.luag.gfx.panel.GamePanel;

public class RunCommand extends CmdCommand {

	public RunCommand() {
		super("run");
	}

	public void run(Cmd cmd, String[] args) {
		Console console = cmd.console;
		if(args.length >= 1) {
			console.cartridge = args[0];
		}

		console.switchToPanel(new GamePanel(console));
	}

}
