package vulc.luag.shell.command;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import vulc.luag.Console;
import vulc.luag.game.Game;

public class FilesCommand extends ShellCommand {

	public FilesCommand() {
		super("files");
		isDevelopersOnly = true;
	}

	public void run(String[] args) {
		if(Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			File folder = new File(Game.USERDATA_DIR);
			if(!folder.isDirectory()) {
				Console.die("Error:\n"
				            + "'" + Game.USERDATA_DIR_NAME + "'\n"
				            + "folder not found");
				return;
			}

			try {
				desktop.open(folder);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected String getHelpMessage() {
		return "opens the folder\n"
		       + "'" + Game.USERDATA_DIR_NAME + "'\n"
		       + "in the file explorer";
	}

}
