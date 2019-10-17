package vulc.luag.shell.command;

import vulc.luag.shell.Shell;

public class ClsCommand extends ShellCommand {

	public ClsCommand() {
		super("cls");
	}

	public void run(Shell shell, String[] args) {
		shell.clear();
	}

}
