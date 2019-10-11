package vulc.luag.cmd.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import vulc.luag.cmd.Cmd;
import vulc.luag.game.Game;

public class SetupCommand extends CmdCommand {

	private static final String TEMPLATE_FILE = "/res/templates/template.zip";

	public SetupCommand() {
		super("setup");

		isDevelopersOnly = true;
	}

	public void run(Cmd cmd, String[] args) {
		File folder = new File(Game.USERDATA_DIR);
		if(folder.exists()) {
			cmd.write("Error:\n"
			          + "'" + Game.USERDATA_DIR_NAME + "'\n"
			          + "already exists\n\n");
			return;
		}

		try {
			new File(Game.USERDATA_DIR).mkdir();

			ZipInputStream template = new ZipInputStream(SetupCommand.class.getResourceAsStream(TEMPLATE_FILE));

			ZipEntry entry;
			byte[] buffer = new byte[1024];

			while((entry = template.getNextEntry()) != null) {
				File file = new File(Game.USERDATA_DIR + "/" + entry.getName());
				if(entry.isDirectory()) {
					file.mkdirs();
				} else {
					FileOutputStream out = new FileOutputStream(file);

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

}
