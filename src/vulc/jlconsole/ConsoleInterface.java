package vulc.jlconsole;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

public abstract class ConsoleInterface {

	private static LuaFunction luaTick;

	public static void init(Console console) {
		Globals globals = JsePlatform.standardGlobals();

		//set variables
		globals.set("_jinterface", CoerceJavaToLua.coerce(new JavaToLuaInterface(console)));

		globals.set("scr_w", LuaValue.valueOf(Console.WIDTH));
		globals.set("scr_h", LuaValue.valueOf(Console.HEIGHT));

		globals.set("font_w", console.screen.font.lengthOf(' '));
		globals.set("font_h", console.screen.font.getHeight());

		//LOAD INTERFACE FILE
		globals.get("dofile").call("/res/interface.lua");

		//LOAD USER FILES
		try {
			JsonArray list = console.jsonConfig.get("scripts").getAsJsonArray();
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
		} catch(Exception e) {
			System.err.println("Error: one of your scripts must contain a function 'tick()'");
			System.exit(1);
		}
	}

	public static void tick() {
		luaTick.call();
	}

}
