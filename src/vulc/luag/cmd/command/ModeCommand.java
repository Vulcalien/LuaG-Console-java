package vulc.luag.cmd.command;

import vulc.luag.Console;
import vulc.luag.Console.Mode;
import vulc.luag.cmd.Cmd;

public class ModeCommand extends CmdCommand {

	public ModeCommand() {
		super("mode");
	}

	public void run(Cmd cmd, String[] args) {
		if(args.length < 1) {
			cmd.write("current mode:\n");
			if(Console.mode == Mode.DEVELOPER) {
				cmd.write("developer");
			} else {
				cmd.write("user");
			}
			cmd.write("\n\n");
			return;
		}

		String mode = args[0];
		if(mode.equals("d") || mode.equals("developer")) {
			Console.mode = Mode.DEVELOPER;
			cmd.write("switching to\n"
			          + "developer mode\n\n");
		} else if(mode.equals("u") || mode.equals("user")) {
			cmd.write("switching to\n"
			          + "user mode\n\n");
			Console.mode = Mode.USER_CMD;
		} else {
			cmd.write("Error:\n"
			          + "unrecognized mode\n"
			          + "try 'd' or 'u'\n\n");
		}
	}

}
