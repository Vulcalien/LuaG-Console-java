package vulc.luag.game;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.KeyStroke;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import vulc.bitmap.Bitmap;
import vulc.bitmap.IntBitmap;
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
	public static final String CONFIG_FILE = USER_DIR + "/config.json";
	public static final String ATLAS_FILE = USER_DIR + "/atlas.png";
	public static final String MAP_FILE = USER_DIR + "/map";

	public static final int SPR_SIZE = 8;

	public final Console console;
	public final LuaScriptCore scriptCore = new LuaScriptCore();
	public final GameSounds sounds = new GameSounds();

	public JsonObject jsonConfig;
	public InputHandler input = new InputHandler();
	public Bitmap<Integer> atlas;
	public Map map;

	public final List<Key> keys = new ArrayList<Key>();

	private Key debugGotoCMD = null;

	public Game(Console console) {
		this.console = console;
		if(console.mode == Mode.DEVELOPER) {
			debugGotoCMD = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_F8);
		}
	}

	// init as console-userdata
	// resources and scripts are loaded on different moments because
	// the developer may want to restart the game changing the script
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
		try {
			InputStream in = new FileInputStream(CONFIG_FILE);
			boolean error = !loadJsonConfig(in);
			in.close();
			if(error) return false;
		} catch(FileNotFoundException e) {
			console.die("Error:\n"
			            + "'config.json'\n"
			            + "file not found");
			return false;
		} catch(IOException e) {
			e.printStackTrace();
		}

		// atlas.png
		try {
			InputStream in = new FileInputStream(ATLAS_FILE);
			boolean error = !loadAtlas(in);
			in.close();
			if(error) return false;
		} catch(FileNotFoundException e) {
			console.die("Error:\n"
			            + "'atlas.png'\n"
			            + "file not found");
			return false;
		} catch(IOException e) {
			e.printStackTrace();
		}

		// map
		File mapFile = new File(MAP_FILE);
		try {
			map = Map.load(new FileInputStream(mapFile), console);
			if(map == null) return false;
		} catch(FileNotFoundException e) {
			map = new Map(10, 10);
			MapCompiler.compile(map);
		}

		return true;
	}

	// init as console-userdata
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

	// init as cartridge assiming that there is no missing file
	public void initAsCartridge(String cartridge) {
		try {
			ZipFile zip = new ZipFile(cartridge);

		} catch(Exception e) {
			console.die("Error:\n"
			            + "cartridge is damaged");
		}
	}

	private boolean loadJsonConfig(InputStream in) {
		JsonElement element = new JsonParser().parse(new InputStreamReader(in));
		if(element.isJsonObject()) {
			jsonConfig = element.getAsJsonObject();
		} else {
			console.die("Error:\n"
			            + "'config.json'\n"
			            + "must be a json object");
			return false;
		}
		return true;
	}

	private boolean loadAtlas(InputStream in) {
		try {
			BufferedImage img = ImageIO.read(in);
			if(img != null) {
				atlas = new IntBitmap(img);
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

	public Bitmap<Integer> getSprite(int id, int sw, int sh) {
		return getSprite(id % 16, id / 16, sw, sh);
	}

	public Bitmap<Integer> getSprite(int x, int y, int w, int h) {
		return atlas.getSubimage(x * SPR_SIZE, y * SPR_SIZE, w * SPR_SIZE, h * SPR_SIZE);
	}

}
