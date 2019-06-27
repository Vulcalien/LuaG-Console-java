package vulc.luag.game.scripting;

import java.io.File;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import vulc.luag.Console;
import vulc.luag.game.Game;
import vulc.luag.gfx.panel.DeathPanel;

public class LuaScriptCore {

	private final LuaInterface luaInterface = new LuaInterface();

	private LuaFunction luaTick;

	public void init(Console console, Game game) {
		Globals globals = JsePlatform.standardGlobals();

		luaInterface.init(console, game, globals);

		try {
			File mainFile = new File(Game.USER_DIR + "/script/main.lua");
			if(!mainFile.exists()) {
				console.switchToPanel(new DeathPanel(console, "Error:\n"
				                                              + "'script/main.lua' does\n"
				                                              + "not exist"));
				return;
			} else {
				globals.get("dofile").call(mainFile.getPath());
			}
		} catch(LuaError e) {
			console.switchToPanel(new DeathPanel(console, "Script Error:\n"
			                                              + "see the terminal"));
			e.printStackTrace();
			return;
		}

		// LOAD TICK FUNCTION
		try {
			LuaValue tick = globals.get("tick");
			if(tick == LuaValue.NIL || !tick.isfunction()) {
				console.switchToPanel(new DeathPanel(console, "Error:\n"
				                                              + "'main.lua' must contain\n"
				                                              + "a function 'tick()'"));
				return;
			} else {
				luaTick = tick.checkfunction();
			}

			LuaValue init = globals.get("init");
			if(init == LuaValue.NIL || !init.isfunction()) {
				console.switchToPanel(new DeathPanel(console, "Error:\n"
				                                              + "'main.lua' must contain\n"
				                                              + "a function 'init()'"));
				return;
			} else {
				init.checkfunction().call();
			}
		} catch(Exception e) {
			console.switchToPanel(new DeathPanel(console, "Script Error:\n"
			                                              + "see the terminal"));
			e.printStackTrace();
			return;
		}
	}

	public void tick() {
		luaTick.call();
	}

}
