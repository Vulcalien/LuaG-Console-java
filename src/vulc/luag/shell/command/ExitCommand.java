package vulc.luag.shell.command;

import vulc.luag.shell.Shell;

public class ExitCommand extends ShellCommand {

	public ExitCommand() {
		super("exit");
	}

	public void run(Shell shell, String[] args) {
		System.exit(0);
	}

}
