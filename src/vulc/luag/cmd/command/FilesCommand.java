package vulc.luag.cmd.command;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import vulc.luag.cmd.Cmd;
import vulc.luag.game.Game;

public class FilesCommand extends CmdCommand {

	public FilesCommand() {
		super("files");

		isDevelopersOnly = true;
	}

	public void run(Cmd cmd, String[] args) {
		if(Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			File folder = new File(Game.USERDATA_DIR);
			if(!folder.isDirectory()) {
				cmd.write("Error:\n"
				          + "'" + Game.USERDATA_DIR_NAME + "'\n"
				          + "folder not found\n\n");
				return;
			}

			try {
				desktop.open(folder);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

}
