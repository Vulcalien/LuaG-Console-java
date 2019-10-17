package vulc.luag.shell.command;

import vulc.luag.Console;
import vulc.luag.shell.Shell;

public class ClsCommand extends ShellCommand {

	public ClsCommand() {
		super("cls");
	}

	public void run(String[] args) {
		Shell shell = Console.shell;

		shell.clear();
	}

}
