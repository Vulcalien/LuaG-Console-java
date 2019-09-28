package vulc.luag.cmd.command;

import vulc.luag.Console;
import vulc.luag.cmd.Cmd;
import vulc.luag.gfx.panel.EditorPanel;

public class EditCommand extends CmdCommand {

	public EditCommand() {
		super("edit", "editor");
	}

	public void run(Cmd cmd, String[] args) {
		Console console = cmd.console;
		console.switchToPanel(new EditorPanel(console));
	}

}
