package vulc.luag.game.interfaces;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import vulc.luag.Console;
import vulc.luag.game.Game;
import vulc.luag.gfx.Screen;
import vulc.luag.sfx.Sound;

public class Interface001 extends LuaInterface {

	public Interface001(Game game, Globals env) {
		super(game, env);
	}

	public void init() {
		// SCREEN
		env.set("scr_w", Console.SCREEN.width);
		env.set("scr_h", Console.SCREEN.height);

		// FONT
		env.set("font_w", Screen.FONT.widthOf(' '));
		env.set("font_h", Screen.FONT.getHeight());

		// MAP
		env.set("map_w", game.map.width);
		env.set("map_h", game.map.height);

		// FUNCTIONS
		// general
		env.set("loadscript", new loadscript());

		// keys
		env.set("key", new key());
		env.set("key_down", new key());
		env.set("key_pressed", new key_pressed());
		env.set("key_released", new key_released());

		// sound
		env.set("sfx", new sfx());
		env.set("sfx_play", new sfx());
		env.set("sfx_loop", new sfx_loop());
		env.set("sfx_stop", new sfx_stop());

		// screen
		env.set("settransparent", new settransparent());
		env.set("clear", new clear());
		env.set("pix", new pix());
		env.set("write", new write());
		env.set("spr", new spr());

		// map
		env.set("get_tile", new get_tile());
		env.set("set_tile", new set_tile());
		env.set("maprender", new maprender());
	}

	//---GENERAL---\\

	private class loadscript extends OneArgFunction {
		public LuaValue call(LuaValue script) {
			Console.LOGGER.info("Loading script '" + script + "'");

			InputStream input = null;
			try {
				if(Console.cartridge == null) {
					File file = new File(Game.SCRIPT_DIR + "/" + script);
					if(!file.exists()) throw new LuaError("bad argument: file '" + script + "' not found");

					input = new FileInputStream(file);
				} else {
					ZipFile cartridgeFile = game.cartridgeFile;
					ZipEntry entry = cartridgeFile.getEntry(Game.SCRIPT_DIR_NAME
					                                        + "/" + script);
					if(entry == null) throw new LuaError("bad argument: file '" + script + "' not found");

					input = cartridgeFile.getInputStream(entry);
				}
				env.load(input, "@" + script, "t", env).call();
			} catch(IOException e) {
				e.printStackTrace();
			}

			try {
				input.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
			return NIL;
		}
	}

	//---KEYS---\\

	private class key extends OneArgFunction {
		public LuaValue call(LuaValue id) {
			int key = id.checkint();
			if(key < 0 || key >= game.keys.size()) throw new LuaError("bad argument: key '" + key + "' does not exist");

			return valueOf(game.keys.get(key).isKeyDown());
		}
	}

	private class key_pressed extends OneArgFunction {
		public LuaValue call(LuaValue id) {
			int key = id.checkint();
			if(key < 0 || key >= game.keys.size()) throw new LuaError("bad argument: key '" + key + "' does not exist");

			return valueOf(game.keys.get(key).isPressed());
		}
	}

	private class key_released extends OneArgFunction {
		public LuaValue call(LuaValue id) {
			int key = id.checkint();
			if(key < 0 || key >= game.keys.size()) throw new LuaError("bad argument: key '" + key + "' does not exist");

			return valueOf(game.keys.get(key).isReleased());
		}
	}

	//---SOUND---\\

	private class sfx extends OneArgFunction {
		public LuaValue call(LuaValue name) {
			Sound sound = game.sounds.get(name.checkjstring());
			if(sound == null) throw new LuaError("bad argument: sound '" + name + "' does not exist");
			sound.play();

			return NIL;
		}
	}

	private class sfx_loop extends OneArgFunction {
		public LuaValue call(LuaValue name) {
			Sound sound = game.sounds.get(name.checkjstring());
			if(sound == null) throw new LuaError("bad argument: sound '" + name + "' does not exist");
			sound.loop();

			return NIL;
		}
	}

	private class sfx_stop extends OneArgFunction {
		public LuaValue call(LuaValue name) {
			Sound sound = game.sounds.get(name.checkjstring());
			if(sound == null) throw new LuaError("bad argument: sound '" + name + "' does not exist");
			sound.stop();

			return NIL;
		}
	}

	//---SCREEN---\\

	private class settransparent extends OneArgFunction {
		public LuaValue call(LuaValue color) {
			Console.SCREEN.setTransparent(color.checkint());
			return NIL;
		}
	}

	private class clear extends OneArgFunction {
		public LuaValue call(LuaValue color) {
			Console.SCREEN.clear(color.checkint());
			return NIL;
		}
	}

	private class pix extends VarArgFunction {
		public Varargs invoke(Varargs args) {
			int x = args.arg(1).checkint();
			int y = args.arg(2).checkint();
			int color = args.arg(3).checkint();
			int w = args.arg(4).isnil() ? 1 : args.arg(4).checkint();
			int h = args.arg(5).isnil() ? 1 : args.arg(5).checkint();

			Console.SCREEN.fill(x, y, x + w - 1, y + h - 1, color);
			return NIL;
		}
	}

	private class write extends VarArgFunction {
		public Varargs invoke(Varargs args) {
			String text = args.arg(1).checkjstring();
			int color = args.arg(2).checkint();
			int x = args.arg(3).checkint();
			int y = args.arg(4).checkint();

			Console.SCREEN.write(text, color, x, y);
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

			int xSprite = id % 16;
			int ySprite = id / 16;

			if(id < 0 || id >= 256) throw new LuaError("bad argument: id");
			if(sw <= 0 || xSprite + sw > 16) throw new LuaError("bad argument: sw");
			if(sh <= 0 || ySprite + sh > 16) throw new LuaError("bad argument: sh");

			Console.SCREEN.draw(game.getSprite(xSprite, ySprite, sw, sh).getScaled(scale), x, y);

			return NIL;
		}
	}

	//---MAP---\\

	private class get_tile extends TwoArgFunction {
		public LuaValue call(LuaValue x, LuaValue y) {
			int xt = x.checkint();
			int yt = y.checkint();

			if(xt < 0 || xt >= game.map.width) throw new LuaError("bad argument: x");
			if(yt < 0 || yt >= game.map.height) throw new LuaError("bad argument: y");

			return valueOf(game.map.getTile(xt, yt));
		}
	}

	// note for next interface version: change to set_tile(id, x, y)
	private class set_tile extends ThreeArgFunction {
		public LuaValue call(LuaValue x, LuaValue y, LuaValue idArg) {
			int xt = x.checkint();
			int yt = y.checkint();
			int id = idArg.checkint();

			if(xt < 0 || xt >= game.map.width) throw new LuaError("bad argument: x");
			if(yt < 0 || yt >= game.map.height) throw new LuaError("bad argument: y");
			if(id < 0 || id >= 256) throw new LuaError("bad argument: id");

			game.map.setTile(xt, yt, id);
			return NIL;
		}
	}

	private class maprender extends ThreeArgFunction {
		public LuaValue call(LuaValue scaleArg, LuaValue xOffArg, LuaValue yOffArg) {
			int scale = scaleArg.isnil() ? 1 : scaleArg.checkint();
			int xoff = xOffArg.isnil() ? 0 : xOffArg.checkint();
			int yoff = yOffArg.isnil() ? 0 : yOffArg.checkint();

			if(scale <= 0) throw new LuaError("bad argument: scale");

			game.map.render(Console.SCREEN, game, xoff, yoff, scale);
			return NIL;
		}
	}

}
