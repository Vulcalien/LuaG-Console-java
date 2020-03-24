package vulc.luag.game.interfaces;

import java.util.HashMap;
import java.util.Map;

import org.luaj.vm2.Globals;

import vulc.luag.game.Game;

/** versioning: x.y
 * x -> retrocompatibility with other x versions
 * y -> updates of an x versions (that do not break retrocompatibility)
 * an y greater than the interface's version means it is not up to date
 * eg. 6.4 can run using an interface 6.7,
 * but 6.7 cannot run using an interface 6.4
 */
public abstract class LuaInterface {

	public static final Map<Integer, Class<? extends LuaInterface>> INTERFACES =
	        new HashMap<Integer, Class<? extends LuaInterface>>();
	public static final Map<Integer, Integer> Y_VERSIONS = new HashMap<Integer, Integer>();

	static {
		INTERFACES.put(1, Interface001.class);
		Y_VERSIONS.put(1, 0);
	}

	protected final Game game;
	protected final Globals env;

	public LuaInterface(Game game, Globals env) {
		this.game = game;
		this.env = env;
	}

	public abstract void init();

	public static LuaInterface getInterface(int xVersion, Game game, Globals env) {
		try {
			Class<? extends LuaInterface> interfaceClass = INTERFACES.get(xVersion);
			if(interfaceClass != null) {
				return interfaceClass.getConstructor(Game.class, Globals.class).newInstance(game, env);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int yVersion(int xVersion) {
		return Y_VERSIONS.get(xVersion);
	}

	public static int getLastestXVersion() {
		return 1;
	}

}
