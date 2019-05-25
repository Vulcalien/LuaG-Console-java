package vulc.jlconsole.game.scripting;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import vulc.jlconsole.Console;
import vulc.jlconsole.game.Game;

public abstract class LuaScriptCore {

	private static LuaFunction luaTick;

	public static void init(Console console, Game game) {
		Globals globals = JsePlatform.standardGlobals();

		LuaInterface.init(console, game, globals);

		//LOAD USER FILES
		try {
			JsonArray list = game.jsonConfig.get("scripts").getAsJsonArray();
			for(int i = 0; i < list.size(); i++) {
				try {
					String file = (String) list.get(i).getAsString();
					try {
						globals.get("dofile").call(Console.USER_DIR + "/script/" + file);
					} catch(LuaError e) {
						System.err.println("Error: script file not found: " + file);
					}
				} catch(ClassCastException e) {
					System.err.println("Error: json's 'scripts' array contains a non-string value");
				}
			}
		} catch(JsonSyntaxException e) {
			System.err.println("Error: config.json is malformed");
			System.exit(1);
		} catch(Exception e) {
			System.err.println("Error: config.json must contain an array called 'scripts'");
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

	public static void tick() {
		luaTick.call();
	}

}
