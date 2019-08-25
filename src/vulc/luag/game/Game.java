package vulc.luag.game;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.KeyStroke;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import vulc.bitmap.Bitmap;
import vulc.luag.Console;
import vulc.luag.Console.Mode;
import vulc.luag.editor.map.MapCompiler;
import vulc.luag.game.map.Map;
import vulc.luag.game.scripting.LuaScriptCore;
import vulc.luag.gfx.panel.CmdPanel;
import vulc.luag.input.InputHandler;
import vulc.luag.input.InputHandler.Key;
import vulc.luag.input.InputHandler.KeyType;

public class Game {

	public static final String USER_DIR = "./console-userdata";

	public final Console console;
	public final LuaScriptCore scriptCore = new LuaScriptCore();
	public final GameSounds sounds = new GameSounds();

	public JsonObject jsonConfig;
	public InputHandler input = new InputHandler();
	public Bitmap atlas;

	public Map map;

	public final List<Key> keys = new ArrayList<Key>();

	private Key debugGotoCMD = null;

	public Game(Console console) {
		this.console = console;
		if(console.mode == Mode.DEVELOPER) {
			debugGotoCMD = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_F8);
		}
	}

	public boolean initResources() {
		// root
		File rootFolder = new File(USER_DIR);
		if(!rootFolder.isDirectory()) {
			console.die("Error:\n"
			            + "'" + USER_DIR.replaceFirst("./", "") + "'\n"
			            + "folder not found");
			return false;
		}

		// sounds
		if(!sounds.init(console)) return false;

		// config.json
		File configFile = new File(USER_DIR + "/config.json");
		if(configFile.isFile()) {
			try {
				JsonElement element = new JsonParser().parse(new FileReader(configFile));
				if(element.isJsonObject()) {
					jsonConfig = element.getAsJsonObject();
				} else {
					console.die("Error:\n"
					            + "'config.json'\n"
					            + "must be a json object");
					return false;
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else {
			console.die("Error:\n"
			            + "'config.json'\n"
			            + "file not found");
			return false;
		}

		// atlas.png
		File atlasFile = new File(USER_DIR + "/atlas.png");
		if(atlasFile.isFile()) {
			try {
				BufferedImage img = ImageIO.read(atlasFile);
				if(img != null) {
					atlas = new Bitmap(img);
					if(atlas.width != 128 || atlas.height != 128) {
						console.die("Error:\n"
						            + "atlas must be\n"
						            + "128x128 pixels\n"
						            + "(256 sprites)");
						return false;
					}
				} else {
					console.die("Error:\n"
					            + "'atlas.png'\n"
					            + "is not an image");
					return false;
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else {
			console.die("Error:\n"
			            + "'atlas.png'\n"
			            + "file not found");
			return false;
		}

		// map
		File mapFile = new File(USER_DIR + "/map");
		try {
			map = Map.load(new FileInputStream(mapFile), console);
			if(map == null) return false;
		} catch(FileNotFoundException e) {
			map = new Map(10, 10);
			MapCompiler.compile(map);
		}

		return true;
	}

	public boolean initScript() {
		JsonElement keysElement = jsonConfig.get("keys");
		if(keysElement != null && keysElement.isJsonArray()) {
			JsonArray keyArray = keysElement.getAsJsonArray();
			for(int i = 0; i < keyArray.size(); i++) {
				String key = keyArray.get(i).getAsString().toUpperCase();
				keys.add(input.new Key(KeyType.KEYBOARD,
				                       KeyStroke.getKeyStroke(key).getKeyCode()));
			}
		} else {
			console.die("Error:\n"
			            + "'config.json'\n"
			            + "must contain\n"
			            + "a string array 'keys'");
			return false;
		}
		input.init(console);
		scriptCore.init(console, this);

		return true;
	}

	public void tick() {
		scriptCore.tick();
		if(console.mode == Mode.DEVELOPER) {
			if(debugGotoCMD.isPressed()) {
				console.switchToPanel(new CmdPanel(console));
			}
		}
		input.tick();
	}

	public Bitmap getSprite(int id, int sw, int sh) {
		return getSprite(id % 16, id / 16, sw, sh);
	}

	private Bitmap getSprite(int x, int y, int w, int h) {
		return atlas.getSubimage(x * 8, y * 8, w * 8, h * 8);
	}

}
