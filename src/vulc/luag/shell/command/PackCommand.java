package vulc.luag.shell.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import vulc.luag.Console;
import vulc.luag.game.Game;
import vulc.luag.shell.Shell;

public class PackCommand extends ShellCommand {

	public PackCommand() {
		super("pack");

		isDevelopersOnly = true;
	}

	public void run(String[] args) {
		if(args.length < 1) {
			Shell.write("Error: missing arguments\n"
			            + "pack [cartridge-name]\n\n");
			return;
		}

		File consoleUserdata = new File(Game.USERDATA_DIR);
		if(!consoleUserdata.isDirectory()) {
			Shell.write("Error:\n"
			            + "'" + Game.USERDATA_DIR + "'\n"
			            + "folder not found\n\n");
			return;
		}

		File cartridge = new File(Console.rootDirectory + args[0] + "." + Game.CARTRIDGE_EXTENSION);
		if(cartridge.exists()) {
			Shell.write("Error:\n"
			            + "'" + cartridge + "'\n"
			            + "file already exists\n\n");
			return;
		}

		try {
			cartridge.createNewFile();
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(cartridge));

			// add files inside 'console-userdata'
			File[] files = new File(Game.USERDATA_DIR).listFiles();
			for(File f : files) {
				addToZip(out, "", f);
			}

			// add .cartridge-info file
			ZipEntry infoFile = new ZipEntry(".cartridge-info");
			out.putNextEntry(infoFile);
			out.write(("console version: " + Console.VERSION).getBytes());
			out.closeEntry();

			out.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void addToZip(ZipOutputStream zip, String folder, File file) throws IOException {
		if(file.isFile()) {
			ZipEntry entry = new ZipEntry(folder + file.getName());
			zip.putNextEntry(entry);

			InputStream in = new FileInputStream(file);
			byte[] dataBuffer = new byte[1024];
			int lengthRead;

			while((lengthRead = in.read(dataBuffer)) >= 0) {
				zip.write(dataBuffer, 0, lengthRead);
				zip.flush();
			}
			in.close();

			zip.closeEntry();
		} else if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(File f : files) {
				addToZip(zip, folder + file.getName() + "/", f);
			}
		}
	}

	protected String getHelpMessage() {
		return "`pack <cartridge name>`\n"
		       + "creates a cartridge";
	}

}
