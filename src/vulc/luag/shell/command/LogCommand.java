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
//		if(args.length < 1) {
//			openFile();
//		} else {
//			String level = args[0];
//			if(level.equals("all")) {
//				Console.LOGGER.setLevel(Level.ALL);
//				Shell.write("switching to\n"
//				            + "'all' level\n\n");
//			} else if(level.equals("severe")) {
//				Console.LOGGER.setLevel(Level.SEVERE);
//				Shell.write("switching to\n"
//				            + "'severe' level\n\n");
//			} else {
//				Shell.write("Error:\n"
//				            + "unrecognized level\n"
//				            + "try 'all' or 'severe'\n\n");
//			}
//		}

		openFile();
	}

	private void openFile() {
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

//	protected String getHelpMessage() {
//		return "'log'\n"
//		       + "opens log file\n"
//		       + "'log <level>'\n"
//		       + "changes log level\n"
//		       + "'all': info and errors\n"
//		       + "'severe': errors";
//	}

	protected String getHelpMessage() {
		return "opens log file in\n"
		       + "the default editor";
	}

}
