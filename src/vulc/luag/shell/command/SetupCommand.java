package vulc.luag.shell.command;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import vulc.luag.Console;
import vulc.luag.game.Game;

public class SetupCommand extends ShellCommand {

	private static final String TEMPLATE_FILE = "/res/templates/template.zip";

	public SetupCommand() {
		super("setup");
		isDevelopersOnly = true;
	}

	public void run(String[] args) {
		File folder = new File(Game.USERDATA_DIR);
		if(folder.exists()) {
			Console.die("Error:\n"
			            + "'" + Game.USERDATA_DIR_NAME + "'\n"
			            + "already exists");
			return;
		}

		try {
			new File(Game.USERDATA_DIR).mkdir();

			ZipInputStream template =
			        new ZipInputStream(new BufferedInputStream(SetupCommand.class.getResourceAsStream(TEMPLATE_FILE)));

			ZipEntry entry;
			byte[] buffer = new byte[1024];

			while((entry = template.getNextEntry()) != null) {
				File file = new File(Game.USERDATA_DIR + "/" + entry.getName());
				if(entry.isDirectory()) {
					file.mkdirs();
				} else {
					OutputStream out = new FileOutputStream(file);

					int readLength;
					while((readLength = template.read(buffer)) > 0) {
						out.write(buffer, 0, readLength);
					}
					out.close();
				}
			}
			template.closeEntry();
			template.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	protected String getHelpMessage() {
		return "creates blank game files";
	}

}
