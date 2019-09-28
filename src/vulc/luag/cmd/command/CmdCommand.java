package vulc.luag.cmd.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vulc.luag.cmd.Cmd;

public abstract class CmdCommand {

	public static final List<CmdCommand> COMMAND_LIST = new ArrayList<CmdCommand>();

	public static final CmdCommand RUN = new RunCommand();
	public static final CmdCommand EDIT = new EditCommand();
	public static final CmdCommand CLS = new ClsCommand();
	public static final CmdCommand VER = new VerCommand();
	public static final CmdCommand HELP = new HelpCommand();

	public final String[] names;

	public CmdCommand(String... names) {
		this.names = names;

		COMMAND_LIST.add(this);
	}

	public abstract void run(Cmd cmd, String[] args);

	// returns true if could find a command, else false
	public static boolean execute(Cmd cmd, String line) {
		String[] splittedLine = line.split(" ");

		String name = splittedLine[0];
		String[] args = Arrays.copyOfRange(splittedLine, 1, splittedLine.length);

		for(CmdCommand command : COMMAND_LIST) {
			for(int i = 0; i < command.names.length; i++) {
				if(name.equals(command.names[i])) {
					command.run(cmd, args);
					return true;
				}
			}
		}
		return false;
	}

}
