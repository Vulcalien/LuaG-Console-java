package vulc.jlconsole.game.scripting;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import vulc.jlconsole.Console;
import vulc.jlconsole.game.Game;

public class LuaScriptCore {

	private final LuaInterface luaInterface = new LuaInterface();

	private LuaFunction luaTick;

	public void init(Console console, Game game) {
		Globals globals = JsePlatform.standardGlobals();

		luaInterface.init(console, game, globals);

		try {
			globals.get("dofile").call(Game.USER_DIR + "/script/main.lua");
		} catch(LuaError e) {
			System.err.println("Error: 'main.lua' not found");
			System.exit(1);
		}

		//LOAD TICK FUNCTION
		try {
			luaTick = globals.get("tick").checkfunction();
			globals.get("init").checkfunction().call();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void tick() {
		luaTick.call();
	}

}
