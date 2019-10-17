package vulc.luag.shell.command;

import vulc.luag.Console;
import vulc.luag.Console.Mode;
import vulc.luag.shell.Shell;

public class ModeCommand extends ShellCommand {

	public ModeCommand() {
		super("mode");
	}

	public void run(String[] args) {
		Shell shell = Console.shell;

		if(args.length < 1) {
			shell.write("current mode:\n");
			if(Console.mode == Mode.DEVELOPER) {
				shell.write("developer");
			} else {
				shell.write("user");
			}
			shell.write("\n\n");
			return;
		}

		String mode = args[0];
		if(mode.equals("d") || mode.equals("developer")) {
			Console.mode = Mode.DEVELOPER;
			shell.write("switching to\n"
			            + "developer mode\n\n");
		} else if(mode.equals("u") || mode.equals("user")) {
			shell.write("switching to\n"
			            + "user mode\n\n");
			Console.mode = Mode.USER_SHELL;
		} else {
			shell.write("Error:\n"
			            + "unrecognized mode\n"
			            + "try 'd' or 'u'\n\n");
		}
	}

}
