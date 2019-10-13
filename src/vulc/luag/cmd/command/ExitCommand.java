package vulc.luag.cmd.command;

import vulc.luag.cmd.Cmd;

public class ExitCommand extends CmdCommand {

	public ExitCommand() {
		super("exit");
	}

	public void run(Cmd cmd, String[] args) {
		System.exit(0);
	}

}
