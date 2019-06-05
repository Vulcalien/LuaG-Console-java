package vulc.jlconsole.game.scripting;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import vulc.jlconsole.Console;
import vulc.jlconsole.game.Game;
import vulc.jlconsole.gfx.Screen;
import vulc.jlconsole.sfx.Sound;

public class LuaInterface {

	private Console console;
	private Game game;
	private Globals env;

	public void init(Console console, Game game, Globals env) {
		this.console = console;
		this.game = game;
		this.env = env;

		//SCREEN
		env.set("scr_w", console.screen.width);
		env.set("scr_h", console.screen.height);

		//FONT
		env.set("font_w", Screen.FONT.lengthOf(' '));
		env.set("font_h", Screen.FONT.getHeight());

		//MAP
		env.set("map_w", game.map.width);
		env.set("map_h", game.map.height);

		//FUNCTIONS
		//general
		env.set("loadscript", new loadscript());
		env.set("key", new key());
		env.set("sfx", new sfx());

		//screen
		env.set("settransparent", new settransparent());
		env.set("clear", new clear());
		env.set("pix", new pix());
		env.set("write", new write());
		env.set("spr", new spr());

		//map
		env.set("gettile", new gettile());
		env.set("settile", new settile());
		env.set("maprender", new maprender());

	}

	private class key extends OneArgFunction {
		public LuaValue call(LuaValue id) {
			return valueOf(game.KEYS[id.checkint()].isKeyDown());
		}
	}

	private class settransparent extends OneArgFunction {
		public LuaValue call(LuaValue color) {
			console.screen.setTransparent(color.checkint());
			return NIL;
		}
	}

	private class clear extends OneArgFunction {
		public LuaValue call(LuaValue color) {
			console.screen.clear(color.checkint());
			return NIL;
		}
	}

	private class pix extends VarArgFunction {
		public Varargs invoke(Varargs args) {
			int x = args.arg(1).checkint();
			int y = args.arg(2).checkint();
			int color = args.arg(3).checkint();
			LuaValue w = args.arg(4);
			LuaValue h = args.arg(5);

			if(!w.isnil() && !h.isnil()) {
				console.screen.fill(x, y, x + w.checkint() - 1, y + h.checkint() - 1, color);
			} else {
				console.screen.setPixel(x, y, color);
			}
			return NIL;
		}
	}

	private class write extends VarArgFunction {
		public Varargs invoke(Varargs args) {
			String text = args.arg(1).checkjstring();
			int color = args.arg(2).checkint();
			int x = args.arg(3).checkint();
			int y = args.arg(4).checkint();

			console.screen.write(text, color, x, y);
			return NIL;
		}
	}

	private class spr extends VarArgFunction {
		public Varargs invoke(Varargs args) {
			int id = args.arg(1).checkint();
			int x = args.arg(2).checkint();
			int y = args.arg(3).checkint();

			int scale = args.arg(4).isnil() ? 1 : args.arg(4).checkint();

			int sw = args.arg(5).isnil() ? 1 : args.arg(5).checkint();
			int sh = args.arg(6).isnil() ? 1 : args.arg(6).checkint();

			console.screen.draw(game.getSprite(id, sw, sh).getScaled(scale), x, y);
			return NIL;
		}
	}

	private class sfx extends OneArgFunction {
		public LuaValue call(LuaValue name) {
			Sound sound = game.sounds.get(name.checkjstring());
			if(sound == null) {
				System.err.println("Error: sound '" + name + "' does not exist");
			} else {
				sound.play();
			}
			return NIL;
		}
	}

	private class gettile extends TwoArgFunction {
		public LuaValue call(LuaValue x, LuaValue y) {
			return valueOf(game.map.getTile(x.checkint(), y.checkint()));
		}
	}

	private class settile extends ThreeArgFunction {
		public LuaValue call(LuaValue x, LuaValue y, LuaValue id) {
			game.map.setTile(x.checkint(), y.checkint(), id.checkint());
			return NIL;
		}
	}

	private class maprender extends ThreeArgFunction {
		public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
			int scale = arg1.isnil() ? 1 : arg1.checkint();
			int x = arg2.isnil() ? 0 : arg2.checkint();
			int y = arg3.isnil() ? 0 : arg3.checkint();

			game.map.render(console.screen, game, x, y, scale);
			return NIL;
		}
	}

	private class loadscript extends OneArgFunction {
		public LuaValue call(LuaValue script) {
			env.get("dofile").call(Game.USER_DIR + "/script/" + script);
			return NIL;
		}
	}

}
