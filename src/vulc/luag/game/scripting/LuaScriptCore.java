package vulc.luag.game.scripting;

import java.io.File;

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

	public void init(Console console, Game game) {
		this.console = console;
		Globals globals = JsePlatform.standardGlobals();

		luaInterface.init(console, game, globals);

		try {
			File mainFile = new File(Game.USER_DIR + "/script/main.lua");
			if(!mainFile.exists()) {
				console.die("Error:\n"
				            + "'script/main.lua' does\n"
				            + "not exist");
				return;
			} else {
				globals.get("dofile").call(mainFile.getPath());
			}
		} catch(LuaError e) {
			handleError(e);
			return;
		}

		// LOAD TICK FUNCTION
		try {
			LuaValue tick = globals.get("tick");
			if(tick == LuaValue.NIL || !tick.isfunction()) {
				console.die("Error:\n"
				            + "'main.lua' must contain\n"
				            + "a function 'tick()'");
				return;
			} else {
				luaTick = tick.checkfunction();
			}

			LuaValue init = globals.get("init");
			if(init == LuaValue.NIL || !init.isfunction()) {
				console.die("Error:\n"
				            + "'main.lua' must contain\n"
				            + "a function 'init()'");
				return;
			} else {
				init.checkfunction().call();
			}
		} catch(LuaError e) {
			handleError(e);
			return;
		}
	}

	public void tick() {
		try {
			luaTick.call();
		} catch(LuaError e) {
			handleError(e);
		}
	}

	private void handleError(LuaError e) {
		console.die("Script Error:\n"
		            + "see the terminal");

		String sep = "[\\\\/]";

		String msg = "LuaError: "
		             + e.getLocalizedMessage()
		                .replace("@", "")
		                .replaceAll("\\.?" + sep + Game.USER_DIR.replace("./", ""), "");
		System.err.println(msg);
	}

}
