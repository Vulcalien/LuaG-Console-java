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
import vulc.jlconsole.game.sfx.Sound;
import vulc.jlconsole.game.sfx.Sounds;

public abstract class LuaInterface {

	private static Console console;
	private static Game game;

	public static void init(Console console, Game game, Globals env) {
		LuaInterface.console = console;
		LuaInterface.game = game;

		//SCREEN
		env.set("scr_w", Console.WIDTH);
		env.set("scr_h", Console.HEIGHT);

		//FONT
		env.set("font_w", console.screen.font.lengthOf(' '));
		env.set("font_h", console.screen.font.getHeight());

		//FUNCTIONS
		env.set("key", new key());
		env.set("settransparent", new settransparent());
		env.set("clear", new clear());
		env.set("pix", new pix());
		env.set("write", new write());
		env.set("spr", new spr());
		env.set("sfx", new sfx());
		env.set("gettile", new gettile());
		env.set("settile", new settile());
	}

	private static class key extends OneArgFunction {
		public LuaValue call(LuaValue id) {
			return valueOf(console.keys[id.checkint()].isKeyDown());
		}
	}

	private static class settransparent extends OneArgFunction {
		public LuaValue call(LuaValue color) {
			console.screen.setTransparent(color.checkint());
			return NIL;
		}
	}

	private static class clear extends OneArgFunction {
		public LuaValue call(LuaValue color) {
			console.screen.clear(color.checkint());
			return NIL;
		}
	}

	private static class pix extends VarArgFunction {
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

	private static class write extends VarArgFunction {
		public Varargs invoke(Varargs args) {
			String text = args.arg(1).checkjstring();
			int color = args.arg(2).checkint();
			int x = args.arg(3).checkint();
			int y = args.arg(4).checkint();

			console.screen.write(text, color, x, y);
			return NIL;
		}
	}

	private static class spr extends VarArgFunction {
		public Varargs invoke(Varargs args) {
			int x = args.arg(1).checkint();
			int y = args.arg(2).checkint();
			int xs = args.arg(3).checkint();
			int ys = args.arg(4).checkint();
			int ws = args.arg(5).checkint();
			int hs = args.arg(6).checkint();

			console.screen.draw(game.atlas.getSubimage(xs, ys, ws, hs), x, y);
			return NIL;
		}
	}

	private static class sfx extends OneArgFunction {
		public LuaValue call(LuaValue name) {
			Sound sound = Sounds.get(name.checkjstring());
			if(sound == null) {
				System.err.println("Error: sound '" + name + "' does not exist");
			} else {
				sound.play();
			}
			return NIL;
		}
	}

	private static class gettile extends TwoArgFunction {
		public LuaValue call(LuaValue x, LuaValue y) {
			return valueOf(game.map.getTile(x.checkint(), y.checkint()));
		}
	}

	private static class settile extends ThreeArgFunction {
		public LuaValue call(LuaValue x, LuaValue y, LuaValue id) {
			game.map.setTile(x.checkint(), y.checkint(), id.checkint());
			return NIL;
		}
	}

}
