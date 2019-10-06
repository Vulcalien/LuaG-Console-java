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
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

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

	public static final String SCRIPT_DIR_NAME = "script";
	public static final String SFX_DIR_NAME = "sfx";
	public static final String CONFIG_FILE_NAME = "config.json";
	public static final String ATLAS_FILE_NAME = "atlas.png";
	public static final String MAP_FILE_NAME = "map";

	public static final String USERDATA_DIR = "./console-userdata";
	public static final String SCRIPT_DIR = USERDATA_DIR + "/" + SCRIPT_DIR_NAME;
	public static final String SFX_DIR = USERDATA_DIR + "/" + SFX_DIR_NAME;
	public static final String CONFIG_FILE = USERDATA_DIR + "/" + CONFIG_FILE_NAME;
	public static final String ATLAS_FILE = USERDATA_DIR + "/" + ATLAS_FILE_NAME;
	public static final String MAP_FILE = USERDATA_DIR + "/" + MAP_FILE_NAME;

	public static final String CARTRIDGE_EXTENSION = "luag";

	public static final int SPR_SIZE = 8;

	public final Console console;
	public final LuaScriptCore scriptCore = new LuaScriptCore();
	public final GameSounds sounds = new GameSounds();

	public JsonObject jsonConfig;
	public InputHandler input = new InputHandler();
	public Bitmap<Integer> atlas;
	public Map map;

	public ZipFile cartridgeFile;

	public final List<Key> keys = new ArrayList<Key>();

	private Key debugGotoCMD = null;

	public Game(Console console) {
		this.console = console;
		if(console.mode == Mode.DEVELOPER) {
			debugGotoCMD = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_F8);
		}
	}

	// init resources as console-userdata
	public boolean initResources() {
		// root
		File rootFolder = new File(USERDATA_DIR);
		if(!rootFolder.isDirectory()) {
			console.die("Error:\n"
			            + "'" + USERDATA_DIR.replaceFirst("./", "") + "'\n"
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
			            + "'" + Game.CONFIG_FILE_NAME + "'\n"
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
			            + "'" + Game.ATLAS_FILE_NAME + "'\n"
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

	public boolean initCartridgeResources() {
		try {
			String cartridge = console.cartridge;

			try {
				cartridgeFile = new ZipFile(cartridge);
			} catch(FileNotFoundException e) {
				console.die("Error:\n"
				            + "'" + cartridge + "'\n"
				            + "cartridge not found");
				return false;
			}
			List<ZipEntry> entries = new ArrayList<ZipEntry>();
			{
				ZipInputStream zipIn = new ZipInputStream(new FileInputStream(cartridge));
				while(true) {
					ZipEntry entry = zipIn.getNextEntry();
					if(entry != null) {
						entries.add(entry);
					} else {
						break;
					}
				}
				zipIn.close();
			}

			// sound
			if(!sounds.initAsCartridge(cartridgeFile, entries)) return false;

			// config.json
			ZipEntry configEntry = cartridgeFile.getEntry(CONFIG_FILE_NAME);
			if(configEntry != null) {
				if(!loadJsonConfig(cartridgeFile.getInputStream(configEntry))) {
					throw new RuntimeException();
				}
			} else {
				console.die("Cartridge Error:"
				            + "'" + CONFIG_FILE_NAME + "'\n"
				            + "file not found");
				return false;
			}

			// atlas.png
			ZipEntry atlasEntry = cartridgeFile.getEntry(ATLAS_FILE_NAME);
			if(atlasEntry != null) {
				if(!loadAtlas(cartridgeFile.getInputStream(atlasEntry))) {
					throw new Exception();
				}
			} else {
				console.die("Cartirdge Error:\n"
				            + "'" + ATLAS_FILE_NAME + "'\n"
				            + "file not found");
				return false;
			}

			// map
			ZipEntry mapEntry = cartridgeFile.getEntry(MAP_FILE_NAME);
			if(mapEntry != null) {
				this.map = Map.load(cartridgeFile.getInputStream(mapEntry), console);
				if(map == null) return false;
			} else {
				console.die("Cartirdge Error:\n"
				            + "'" + MAP_FILE_NAME + "'\n"
				            + "file not found");
				return false;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
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
			            + "'" + CONFIG_FILE_NAME + "'\n"
			            + "must contain\n"
			            + "a string array 'keys'");
			return false;
		}
		input.init(console);
		scriptCore.init(console, this);

		return true;
	}

	private boolean loadJsonConfig(InputStream in) {
		JsonElement element = new JsonParser().parse(new InputStreamReader(in));
		if(element.isJsonObject()) {
			jsonConfig = element.getAsJsonObject();
		} else {
			console.die("Error:\n"
			            + "'" + Game.CONFIG_FILE_NAME + "'\n"
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
				            + "'" + Game.ATLAS_FILE_NAME + "'\n"
				            + "is not an image");
				return false;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void tick() {
		input.tick();

		scriptCore.tick();
		if(console.mode == Mode.DEVELOPER) {
			if(debugGotoCMD.isPressed()) {
				console.switchToPanel(new CmdPanel(console));
			}
		}
	}

	public void remove() {
		input.remove();
		sounds.remove();

		if(cartridgeFile != null) {
			try {
				cartridgeFile.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
			cartridgeFile = null;
		}
	}

	public Bitmap<Integer> getSprite(int id, int sw, int sh) {
		return getSprite(id % 16, id / 16, sw, sh);
	}

	public Bitmap<Integer> getSprite(int x, int y, int w, int h) {
		return atlas.getSubimage(x * SPR_SIZE, y * SPR_SIZE, w * SPR_SIZE, h * SPR_SIZE);
	}

}
