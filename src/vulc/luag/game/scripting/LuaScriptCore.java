package vulc.luag.game.scripting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.zip.ZipEntry;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import vulc.luag.Console;
import vulc.luag.game.Game;

public class LuaScriptCore {

	private final LuaInterface luaInterface = new LuaInterface();

	private Console console;

	private LuaFunction luaTick;

	// return true if init was successfully, else false
	public boolean init(Console console, Game game) {
		this.console = console;
		Globals globals = JsePlatform.standardGlobals();

		luaInterface.init(console, game, globals);

		// READ main.lua
		boolean error = false;
		InputStream input = null;
		try {
			Console.LOGGER.info("Load 'main.lua'");
			if(console.cartridge == null) {
				File mainFile = new File(Game.SCRIPT_DIR + "/main.lua");
				if(!mainFile.exists()) {
					console.die("Error:\n"
					            + "'" + Game.SCRIPT_DIR_NAME + "/main.lua'"
					            + "file not found");
					return false;
				}

				input = new FileInputStream(mainFile);
			} else {
				ZipEntry mainLuaEntry = game.cartridgeFile.getEntry("script/main.lua");
				if(mainLuaEntry == null || mainLuaEntry.isDirectory()) {
					console.die("Cartridge Error:\n"
					            + "'" + Game.SCRIPT_DIR_NAME + "/main.lua'"
					            + "file not found");
					return false;
				}

				input = game.cartridgeFile.getInputStream(mainLuaEntry);
			}

			globals.load(input, "@main.lua", "t", globals).call();
		} catch(LuaError e) {
			handleError(e);
			error = true;
		} catch(IOException e) {
			e.printStackTrace();
			error = true;
		}

		try {
			input.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		if(error) return false;

		// LOAD TICK FUNCTION
		try {
			LuaValue tick = globals.get("tick");
			if(tick == LuaValue.NIL || !tick.isfunction()) {
				console.die("Error:\n"
				            + "'main.lua' must contain\n"
				            + "a function 'tick()'");
				return false;
			} else {
				luaTick = tick.checkfunction();
			}

			Console.LOGGER.info("Calling 'init' function");
			LuaValue init = globals.get("init");
			if(init == LuaValue.NIL || !init.isfunction()) {
				console.die("Error:\n"
				            + "'main.lua' must contain\n"
				            + "a function 'init()'");
				return false;
			} else {
				init.checkfunction().call();
			}
		} catch(LuaError e) {
			handleError(e);
			return false;
		}
		return true;
	}

	public void tick() {
		try {
			luaTick.call();
		} catch(LuaError e) {
			handleError(e);
		}
	}

	private void handleError(LuaError e) {
		String sep = "[\\\\/]";

		String msg = "LuaError: "
		             + e.getLocalizedMessage()
		                .replace("@", "")
		                .replaceAll("\\.?" + sep + Game.USERDATA_DIR.replace("./", ""), "");

		Console.LOGGER.log(Level.SEVERE, msg);

		console.die("Script Error:\n"
		            + "see the log file");
	}

}
