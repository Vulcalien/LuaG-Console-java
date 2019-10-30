package vulc.luag.shell.command;

public class ExitCommand extends ShellCommand {

	public ExitCommand() {
		super("exit");
	}

	public void run(String[] args) {
		System.exit(0);
	}

	protected String getHelpMessage() {
		return "shuts down the console";
	}

}
