package vulc.luag.shell.command;

import java.util.Arrays;

import vulc.luag.Console;
import vulc.luag.Console.Mode;
import vulc.luag.shell.Shell;

public abstract class ShellCommand {

	private static final ShellCommand[] COMMAND_LIST = {
	    new RunCommand(),
	    new EditCommand(),
	    new PackCommand(),
	    new SetupCommand(),
	    new ClsCommand(),
	    new VerCommand(),
	    new HelpCommand(),
	    new ModeCommand(),
	    new FilesCommand(),
	    new LogCommand(),
	    new ExitCommand()
	};

	private final String[] names;
	protected boolean isDevelopersOnly = false;

	public ShellCommand(String... names) {
		this.names = names;
	}

	protected abstract void run(String[] args);

	protected String getHelpMessage() {
		return "this command has no\n"
		       + "'help' message";
	}

	// returns true if could find a command, else false
	public static boolean execute(String line) {
		Console.LOGGER.info("Shell execute: '" + line + "'");

		String[] splittedLine = line.split(" ");

		String name = splittedLine[0];
		String[] args = Arrays.copyOfRange(splittedLine, 1, splittedLine.length);

		ShellCommand command = findCommand(name);
		if(command != null) {
			if(command.isDevelopersOnly && Console.mode != Mode.DEVELOPER) {
				Shell.write("Error:\n"
				            + "only developers can\n"
				            + "use this command\n\n");
			} else {
				command.run(args);
			}
			return true;
		}
		return false;
	}

	protected static ShellCommand findCommand(String name) {
		name = name.toLowerCase();

		for(ShellCommand command : COMMAND_LIST) {
			for(int i = 0; i < command.names.length; i++) {
				if(name.equals(command.names[i])) return command;
			}
		}
		return null;
	}

}
