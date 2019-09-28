package vulc.luag.cmd.command;

import vulc.luag.Console;
import vulc.luag.cmd.Cmd;

public class VerCommand extends CmdCommand {

	public VerCommand() {
		super("ver", "version");
	}

	public void run(Cmd cmd, String[] args) {
		cmd.write(Console.VERSION + " - By Vulcalien\n\n");
	}

}
