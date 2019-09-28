package vulc.luag.cmd.command;

import vulc.luag.cmd.Cmd;

public class ClsCommand extends CmdCommand {

	public ClsCommand() {
		super("cls");
	}

	public void run(Cmd cmd, String[] args) {
		cmd.clear();
	}

}
