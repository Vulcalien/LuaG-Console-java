package vulc.luag.cmd.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vulc.luag.Console;
import vulc.luag.Console.Mode;
import vulc.luag.cmd.Cmd;

public abstract class CmdCommand {

	public static final List<CmdCommand> COMMAND_LIST = new ArrayList<CmdCommand>();

	public static final CmdCommand RUN = new RunCommand();
	public static final CmdCommand EDIT = new EditCommand();
	public static final CmdCommand PACK = new PackCommand();
	public static final CmdCommand CLS = new ClsCommand();
	public static final CmdCommand VER = new VerCommand();
	public static final CmdCommand HELP = new HelpCommand();
	public static final CmdCommand MODE = new ModeCommand();
	public static final CmdCommand FILES = new FilesCommand();
	public static final CmdCommand SETUP = new SetupCommand();
	public static final CmdCommand EXIT = new ExitCommand();

	public final String[] names;
	protected boolean isDevelopersOnly = false;

	public CmdCommand(String... names) {
		this.names = names;

		COMMAND_LIST.add(this);
	}

	public abstract void run(Cmd cmd, String[] args);

	// returns true if could find a command, else false
	public static boolean execute(Cmd cmd, String line) {
		Console.LOGGER.info("Cmd execute: '" + line + "'");

		String[] splittedLine = line.split(" ");

		String name = splittedLine[0].toLowerCase();
		String[] args = Arrays.copyOfRange(splittedLine, 1, splittedLine.length);

		for(CmdCommand command : COMMAND_LIST) {
			for(int i = 0; i < command.names.length; i++) {
				if(name.equals(command.names[i])) {
					if(command.isDevelopersOnly && cmd.console.mode != Mode.DEVELOPER) {
						cmd.write("Error:\n"
						          + "only developers can\n"
						          + "use this command\n\n");
					} else {
						command.run(cmd, args);
					}
					return true;
				}
			}
		}
		return false;
	}

}
