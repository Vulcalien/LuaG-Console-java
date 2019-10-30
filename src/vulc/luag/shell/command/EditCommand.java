package vulc.luag.shell.command;

import vulc.luag.Console;
import vulc.luag.gfx.panel.EditorPanel;

public class EditCommand extends ShellCommand {

	public EditCommand() {
		super("edit", "editor");

		isDevelopersOnly = true;
	}

	public void run(String[] args) {
		Console.switchToPanel(new EditorPanel());
	}

	protected String getHelpMessage() {
		return "opens the game editor";
	}

}
