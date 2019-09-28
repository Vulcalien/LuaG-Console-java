package vulc.luag.cmd.command;

import vulc.luag.cmd.Cmd;

public class HelpCommand extends CmdCommand {

	public HelpCommand() {
		super("help");
	}

	public void run(Cmd cmd, String[] args) {
		cmd.write("run: runs the game\n");
		cmd.write("edit: opens the editor\n");
		cmd.write("cls: clears the cmd\n");
		cmd.write("ver: prints version\n");
		cmd.write("help: prints this list\n\n");
	}

}
