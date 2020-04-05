package vulc.luag.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.zip.ZipEntry;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseMathLib;

import vulc.luag.Console;
import vulc.luag.game.interfaces.LuaInterface;

public class LuaScriptCore {

	private LuaFunction luaTick;

	// return true if init was successfully, else false
	public boolean init(Game game) {
		Globals globals = new Globals();

		// must load
		globals.load(new JseBaseLib());
		globals.load(new PackageLib());

		// math
		globals.load(new Bit32Lib());
		globals.load(new JseMathLib());

		// utils
		globals.load(new TableLib());
		globals.load(new StringLib());

		// disable require
		globals.set("require", LuaValue.NIL);

		/* libraries that are not loaded
		 *
		 * CoroutineLib
		 * JseIoLib
		 * JseOsLib
		 * LuajavaLib
		 */

		LoadState.install(globals);
		LuaC.install(globals);

		// load luag interface
		LuaInterface luaInterface;
		if(Console.cartridge != null) {
			String[] requestedVersion = game.cartridgeInfo.get("interface-version").getAsString().split("\\.");
			int xVerReq = Integer.parseInt(requestedVersion[0]);
			int yVerReq = Integer.parseInt(requestedVersion[1]);

			luaInterface = LuaInterface.getInterface(xVerReq, game, globals);
			if(luaInterface == null) {
				Console.die("Error:\n"
				            + "interface version " + xVerReq + "\n"
				            + "not supported");
				return false;
			}
			int yVer = LuaInterface.minorVersion(xVerReq);
			if(yVer < yVerReq) {
				Console.die("Error:\n"
				            + "interface " + xVerReq + "." + yVerReq + "\n"
				            + "not supported\n"
				            + "Lastest: " + xVerReq + "." + LuaInterface.minorVersion(xVerReq));
				return false;
			}
		} else {
			luaInterface = LuaInterface.getInterface(LuaInterface.DEFAULT_MAJOR_VERSION, game, globals);
		}
		luaInterface.init();

		// READ main.lua
		boolean error = false;
		InputStream input = null;
		try {
			Console.LOGGER.info("Load 'main.lua'");
			if(Console.cartridge == null) {
				File mainFile = new File(Game.SCRIPT_DIR + "/main.lua");
				if(!mainFile.exists()) {
					Console.die("Error:\n"
					            + "'" + Game.SCRIPT_DIR_NAME + "/main.lua'"
					            + "file not found");
					return false;
				}

				input = new FileInputStream(mainFile);
			} else {
				ZipEntry mainLuaEntry = game.cartridgeFile.getEntry("script/main.lua");
				if(mainLuaEntry == null || mainLuaEntry.isDirectory()) {
					Console.die("Cartridge Error:\n"
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
				Console.die("Error:\n"
				            + "'main.lua' must contain\n"
				            + "a function 'tick()'");
				return false;
			} else {
				luaTick = tick.checkfunction();
			}

			Console.LOGGER.info("Calling 'init' function");
			LuaValue init = globals.get("init");
			if(init == LuaValue.NIL || !init.isfunction()) {
				Console.die("Error:\n"
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
		                .replaceAll("\\.?" + sep + Game.USERDATA_DIR_NAME, "");

		Console.LOGGER.log(Level.SEVERE, msg);

		Console.die("Script Error:\n"
		            + "see the log file");
	}

}
