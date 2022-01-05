package vulc.luag.shell.command;

import vulc.luag.shell.Shell;

public class ClsCommand extends ShellCommand {

	public ClsCommand() {
		super("cls", "clear");
	}

	public void run(String[] args) {
		Shell.clear();
	}

	protected String getHelpMessage() {
		return "clears the shell";
	}

}
