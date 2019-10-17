package vulc.luag.shell.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vulc.luag.Console;
import vulc.luag.Console.Mode;

public abstract class ShellCommand {

	public static final List<ShellCommand> COMMAND_LIST = new ArrayList<ShellCommand>();

	public static final ShellCommand RUN = new RunCommand();
	public static final ShellCommand EDIT = new EditCommand();
	public static final ShellCommand PACK = new PackCommand();
	public static final ShellCommand CLS = new ClsCommand();
	public static final ShellCommand VER = new VerCommand();
	public static final ShellCommand HELP = new HelpCommand();
	public static final ShellCommand MODE = new ModeCommand();
	public static final ShellCommand FILES = new FilesCommand();
	public static final ShellCommand SETUP = new SetupCommand();
	public static final ShellCommand EXIT = new ExitCommand();

	public final String[] names;
	protected boolean isDevelopersOnly = false;

	public ShellCommand(String... names) {
		this.names = names;

		COMMAND_LIST.add(this);
	}

	public abstract void run(String[] args);

	// returns true if could find a command, else false
	public static boolean execute(String line) {
		Console.LOGGER.info("Shell execute: '" + line + "'");

		String[] splittedLine = line.split(" ");

		String name = splittedLine[0].toLowerCase();
		String[] args = Arrays.copyOfRange(splittedLine, 1, splittedLine.length);

		for(ShellCommand command : COMMAND_LIST) {
			for(int i = 0; i < command.names.length; i++) {
				if(name.equals(command.names[i])) {
					if(command.isDevelopersOnly && Console.mode != Mode.DEVELOPER) {
						Console.shell.write("Error:\n"
						                    + "only developers can\n"
						                    + "use this command\n\n");
					} else {
						command.run(args);
					}
					return true;
				}
			}
		}
		return false;
	}

}
