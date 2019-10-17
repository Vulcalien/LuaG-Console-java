package vulc.luag.shell.command;

import vulc.luag.shell.Shell;

public class HelpCommand extends ShellCommand {

	public HelpCommand() {
		super("help");
	}

	public void run(String[] args) {
		Shell.write("run: runs game\n");
		Shell.write("edit: opens editor\n");
		Shell.write("pack: creates cartridge\n");
		Shell.write("cls: clears shell\n");
		Shell.write("ver: prints version\n");
		Shell.write("help: prints this list\n");
		Shell.write("mode: changes console mode\n");
		Shell.write("files: opens game folder\n");
		Shell.write("setup: creates game files\n\n");
	}

}
