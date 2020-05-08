package vulc.luag.shell.command;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import vulc.luag.Console;
import vulc.luag.shell.Shell;

public class LogCommand extends ShellCommand {

	public LogCommand() {
		super("log");
		isDevelopersOnly = true;
	}

	protected void run(String[] args) {
		if(Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			File file = new File(Console.logFile);
			if(!file.isFile()) {
				Shell.write("Error:\n"
				            + "log file not found\n\n");
				return;
			}

			try {
				desktop.open(file);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected String getHelpMessage() {
		return super.getHelpMessage();
	}

}
