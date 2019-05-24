package vulc.jlconsole.game;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import vulc.jlconsole.Console;

public abstract class LuaScriptCore {

	private static Console console;

	private static LuaFunction luaTick;
	private static boolean hasInit = false;
	private static int loadingTicks = 0;

	public static void init(Console console, Game game) {
		LuaScriptCore.console = console;

		Globals globals = JsePlatform.standardGlobals();

		//set variables
		globals.set("_jinterface", CoerceJavaToLua.coerce(new LuaInterface(console, game)));

		globals.set("scr_w", LuaValue.valueOf(Console.WIDTH));
		globals.set("scr_h", LuaValue.valueOf(Console.HEIGHT));

		globals.set("font_w", console.screen.font.lengthOf(' '));
		globals.set("font_h", console.screen.font.getHeight());

		//LOAD INTERFACE FILE
		globals.get("dofile").call("/res/interface.lua");

		//LOAD USER FILES
		try {
			JsonArray list = game.jsonConfig.get("scripts").getAsJsonArray();
			for(int i = 0; i < list.size(); i++) {
				try {
					String file = (String) list.get(i).getAsString();
					try {
						globals.get("dofile").call(Game.USER_DIR + "/script/" + file);
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
			hasInit = true;
		} catch(Exception e) {
			System.err.println("Error: one of your scripts must contain functions 'tick()' and 'init()'");
			System.exit(1);
		}
	}

	public static void tick() {
		if(hasInit) {
			luaTick.call();
		} else {
			int phase = loadingTicks / 30 % 4;
			String text = null;
			if(phase == 0) text = "Loading";
			else if(phase == 1) text = "Loading.";
			else if(phase == 2) text = "Loading..";
			else if(phase == 3) text = "Loading...";

			console.screen.clear(0);
			console.screen.write(text, 0xffffff, 1, 1);

			loadingTicks++;
		}
	}

}
