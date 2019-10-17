package vulc.luag.shell.command;

import vulc.luag.Console;
import vulc.luag.shell.Shell;

public class HelpCommand extends ShellCommand {

	public HelpCommand() {
		super("help");
	}

	public void run(String[] args) {
		Shell shell = Console.shell;

		shell.write("run: runs game\n");
		shell.write("edit: opens editor\n");
		shell.write("pack: creates cartridge\n");
		shell.write("cls: clears shell\n");
		shell.write("ver: prints version\n");
		shell.write("help: prints this list\n");
		shell.write("mode: changes console mode\n");
		shell.write("files: opens game folder\n");
		shell.write("setup: creates game files\n\n");
	}

}
