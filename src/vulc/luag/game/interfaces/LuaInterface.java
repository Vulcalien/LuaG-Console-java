package vulc.luag.game.interfaces;

import java.util.HashMap;
import java.util.Map;

import org.luaj.vm2.Globals;

import vulc.luag.game.Game;

/** versioning: major.minor
 * major -> backward compatibility with other major versions is broken
 * minor -> backward compatibility with the same major version is not broken
 *
 * eg. 6.4 can run using an interface 6.7,
 * but 6.7 cannot run using an interface 6.4
 */
public abstract class LuaInterface {

	public static final Map<Integer, Class<? extends LuaInterface>> INTERFACES =
	        new HashMap<Integer, Class<? extends LuaInterface>>();
	public static final Map<Integer, Integer> MINOR_VERSIONS = new HashMap<Integer, Integer>();

	public static final int DEFAULT_MAJOR_VERSION = 1;

	static {
		INTERFACES.put(1, Interface001.class);
		MINOR_VERSIONS.put(1, 3);
	}

	protected final Game game;
	protected final Globals env;

	public LuaInterface(Game game, Globals env) {
		this.game = game;
		this.env = env;
	}

	public abstract void init();

	public static LuaInterface getInterface(int majorVersion, Game game, Globals env) {
		try {
			Class<? extends LuaInterface> interfaceClass = INTERFACES.get(majorVersion);
			if(interfaceClass != null) {
				return interfaceClass.getConstructor(Game.class, Globals.class).newInstance(game, env);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int minorVersion(int majorVersion) {
		return MINOR_VERSIONS.get(majorVersion);
	}

}
