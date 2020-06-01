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
			String[] requestedVersion;
			int majorVerReq, minorVerReq;
			try {
				requestedVersion = game.cartridgeInfo.get("interface-version")
				                                     .getAsString()
				                                     .split("\\.");

				majorVerReq = Integer.parseInt(requestedVersion[0]);
				minorVerReq = Integer.parseInt(requestedVersion[1]);
			} catch(Exception e) {
				Console.die("Error:\n"
				            + "'" + Game.CARTRIDGE_INFO_NAME + "'\n"
				            + "is invalid");
				return false;
			}

			luaInterface = LuaInterface.getInterface(majorVerReq, game, globals);
			if(luaInterface == null) {
				Console.die("Error:\n"
				            + "interface version " + majorVerReq + "\n"
				            + "not supported");
				return false;
			}
			int yVer = LuaInterface.minorVersion(majorVerReq);
			if(yVer < minorVerReq) {
				Console.die("Error:\n"
				            + "interface " + majorVerReq + "." + minorVerReq + "\n"
				            + "not supported\n"
				            + "Lastest: " + majorVerReq + "." + LuaInterface.minorVersion(majorVerReq));
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
					            + "'" + Game.SCRIPT_DIR_NAME + "/main.lua'\n"
					            + "file not found");
					return false;
				}

				input = new FileInputStream(mainFile);
			} else {
				ZipEntry mainLuaEntry = game.cartridgeFile.getEntry("script/main.lua");
				if(mainLuaEntry == null || mainLuaEntry.isDirectory()) {
					Console.die("Cartridge Error:\n"
					            + "'" + Game.SCRIPT_DIR_NAME + "/main.lua'\n"
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

		try {
			// load tick
			LuaValue tick = globals.get("tick");
			if(tick == LuaValue.NIL || !tick.isfunction()) {
				Console.die("Error:\n"
				            + "'main.lua' must contain\n"
				            + "a function 'tick()'");
				return false;
			} else {
				luaTick = tick.checkfunction();
			}

			// call init
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
