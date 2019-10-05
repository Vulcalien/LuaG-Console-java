package vulc.luag.cmd.command;

import vulc.luag.Console;
import vulc.luag.Console.Mode;
import vulc.luag.cmd.Cmd;
import vulc.luag.gfx.panel.EditorPanel;

public class EditCommand extends CmdCommand {

	public EditCommand() {
		super("edit", "editor");
	}

	public void run(Cmd cmd, String[] args) {
		Console console = cmd.console;

		if(console.mode != Mode.DEVELOPER) {
			cmd.write("Error:\n"
			          + "only developers can\n"
			          + "use this command\n\n");
			return;
		}

		console.switchToPanel(new EditorPanel(console));
	}

}
