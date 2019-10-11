package vulc.luag.cmd.command;

import vulc.luag.cmd.Cmd;

public class HelpCommand extends CmdCommand {

	public HelpCommand() {
		super("help");
	}

	public void run(Cmd cmd, String[] args) {
		cmd.write("run: runs game\n");
		cmd.write("edit: opens editor\n");
		cmd.write("pack: creates cartridge\n");
		cmd.write("cls: clears cmd\n");
		cmd.write("ver: prints version\n");
		cmd.write("help: prints this list\n");
		cmd.write("mode: changes console mode\n");
		cmd.write("files: opens game folder\n");
		cmd.write("setup: creates game files\n\n");
	}

}
