package vulc.luag.shell.command;

import vulc.luag.Console;
import vulc.luag.gfx.panel.EditorPanel;
import vulc.luag.shell.Shell;

public class EditCommand extends ShellCommand {

	public EditCommand() {
		super("edit", "editor");

		isDevelopersOnly = true;
	}

	public void run(Shell shell, String[] args) {
		Console.switchToPanel(new EditorPanel());
	}

}
