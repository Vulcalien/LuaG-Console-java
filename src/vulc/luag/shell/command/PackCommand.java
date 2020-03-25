package vulc.luag.shell.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.gson.stream.JsonWriter;

import vulc.luag.Console;
import vulc.luag.game.Game;
import vulc.luag.game.interfaces.LuaInterface;
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
			            + "'" + Game.USERDATA_DIR_NAME + "'\n"
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
			ZipOutputStream out = null;
			boolean error = false;
			try {
				out = new ZipOutputStream(new FileOutputStream(cartridge));
				cartridge.createNewFile();

				if(!cartridge.exists()) error = true;
			} catch(FileNotFoundException e) {
				error = true;
			}
			if(error) {
				Shell.write("Error:\n"
				            + "could not\n"
				            + "create cartridge\n\n");
				return;
			}

			// add files inside 'console-userdata'
			File[] files = new File(Game.USERDATA_DIR).listFiles();
			for(File f : files) {
				addToZip(out, "", f);
			}

			// add .cartridge-info file
			ZipEntry infoFile = new ZipEntry(Game.CARTRIDGE_INFO_NAME);
			out.putNextEntry(infoFile);
			{
				@SuppressWarnings("resource")
				JsonWriter writer = new JsonWriter(new OutputStreamWriter(out));
				writer.beginObject();
				writer.name("console-version").value(Console.VERSION);
				writer.name("interface-version").value(LuaInterface.DEFAULT_X_VERSION
				                                       + "."
				                                       + LuaInterface.yVersion(LuaInterface.DEFAULT_X_VERSION));
				writer.endObject();

				writer.flush();
			}
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
