package vulc.luag.game;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.NoSuchFileException;
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
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import vulc.bitmap.Bitmap;
import vulc.bitmap.IntBitmap;
import vulc.luag.Console;
import vulc.luag.Console.Mode;
import vulc.luag.editor.map.MapCompiler;
import vulc.luag.game.map.Map;
import vulc.luag.game.save.SaveSystem;
import vulc.luag.gfx.panel.GamePanel;
import vulc.luag.gfx.panel.ShellPanel;
import vulc.luag.input.InputHandler;
import vulc.luag.input.InputHandler.Key;
import vulc.luag.input.InputHandler.KeyType;

public class Game {

	public static final String USERDATA_DIR_NAME = "console-userdata";
	public static final String SCRIPT_DIR_NAME = "script";
	public static final String SFX_DIR_NAME = "sfx";
	public static final String CONFIG_FILE_NAME = "config.json";
	public static final String ATLAS_FILE_NAME = "atlas.png";
	public static final String MAP_FILE_NAME = "map";
	public static final String CARTRIDGE_INFO_NAME = ".cartridge-info";

	public static final String USERDATA_DIR = Console.rootDirectory + USERDATA_DIR_NAME;
	public static final String SCRIPT_DIR = USERDATA_DIR + "/" + SCRIPT_DIR_NAME;
	public static final String SFX_DIR = USERDATA_DIR + "/" + SFX_DIR_NAME;
	public static final String CONFIG_FILE = USERDATA_DIR + "/" + CONFIG_FILE_NAME;
	public static final String ATLAS_FILE = USERDATA_DIR + "/" + ATLAS_FILE_NAME;
	public static final String MAP_FILE = USERDATA_DIR + "/" + MAP_FILE_NAME;

	public static final int SPR_SIZE = 8;

	private final LuaScriptCore scriptCore = new LuaScriptCore();
	private final InputHandler input = new InputHandler();
	public final GameSounds sounds = new GameSounds();

	public JsonObject jsonConfig, cartridgeInfo;
	public SaveSystem saveSystem;
	public Bitmap<Integer> atlas;
	public Map map;

	public ZipFile cartridgeFile;

	public final List<Key> keys = new ArrayList<Key>();

	private Key debugRestartGame = null;
	private Key debugGotoShell = null;

	public Game() {
		if(Console.mode == Mode.DEVELOPER) {
			debugRestartGame = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_F7);
			debugGotoShell = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_F8);
		}
	}

	// init resources in console-userdata
	public boolean initDevResources() {
		Console.LOGGER.info("Loading '" + USERDATA_DIR_NAME + "' resources...");
		saveSystem = new SaveSystem(USERDATA_DIR);

		// root
		File rootFolder = new File(USERDATA_DIR);
		if(!rootFolder.isDirectory()) {
			Console.die("Error:\n"
			            + "'" + USERDATA_DIR_NAME + "'\n"
			            + "folder not found");
			return false;
		}

		// sounds
		Console.LOGGER.info("Load '" + SFX_DIR_NAME + "'");
		if(!sounds.init()) return false;

		// config.json
		Console.LOGGER.info("Load '" + CONFIG_FILE_NAME + "'");
		try(InputStream in = new FileInputStream(CONFIG_FILE)) {
			if(!loadConfig(in)) return false;
		} catch(FileNotFoundException e) {
			Console.die("Error:\n"
			            + "'" + Game.CONFIG_FILE_NAME + "'\n"
			            + "file not found");
			return false;
		} catch(IOException e) {
			e.printStackTrace();
		}

		// atlas.png
		Console.LOGGER.info("Load '" + ATLAS_FILE_NAME + "'");
		try(InputStream in = new FileInputStream(ATLAS_FILE)) {
			if(!loadAtlas(in)) return false;
		} catch(FileNotFoundException e) {
			Console.die("Error:\n"
			            + "'" + Game.ATLAS_FILE_NAME + "'\n"
			            + "file not found");
			return false;
		} catch(IOException e) {
			e.printStackTrace();
		}

		// map
		Console.LOGGER.info("Load '" + MAP_FILE_NAME + "'");
		File mapFile = new File(MAP_FILE);
		try {
			map = Map.load(new FileInputStream(mapFile));
			if(map == null) return false;
		} catch(FileNotFoundException e) {
			Console.LOGGER.info("Missing map file: generating new file");

			map = new Map(10, 10);
			MapCompiler.compile(map);
		}
		return true;
	}

	public boolean initCartridgeResources() {
		try {
			String cartridge = Console.cartridge;
			Console.LOGGER.info("Loading cartridge '" + cartridge + "' resources...");
			saveSystem = new SaveSystem(cartridge);

			try {
				cartridgeFile = new ZipFile(cartridge);
			} catch(FileNotFoundException | NoSuchFileException e) {
				Console.die("Error:\n"
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
			Console.LOGGER.info("Load '" + SFX_DIR_NAME + "'");
			if(!sounds.initAsCartridge(cartridgeFile, entries)) return false;

			// config.json
			Console.LOGGER.info("Load '" + CONFIG_FILE_NAME + "'");
			ZipEntry configEntry = cartridgeFile.getEntry(CONFIG_FILE_NAME);
			if(configEntry == null) {
				Console.die("Cartridge Error:\n"
				            + "'" + CONFIG_FILE_NAME + "'\n"
				            + "file not found");
				return false;
			} else {
				if(!loadConfig(cartridgeFile.getInputStream(configEntry))) {
					return false;
				}
			}

			// atlas.png
			Console.LOGGER.info("Load '" + ATLAS_FILE_NAME + "'");
			ZipEntry atlasEntry = cartridgeFile.getEntry(ATLAS_FILE_NAME);
			if(atlasEntry == null) {
				Console.die("Cartridge Error:\n"
				            + "'" + ATLAS_FILE_NAME + "'\n"
				            + "file not found");
				return false;
			} else {
				if(!loadAtlas(cartridgeFile.getInputStream(atlasEntry))) {
					return false;
				}
			}

			// map
			Console.LOGGER.info("Load '" + MAP_FILE_NAME + "'");
			ZipEntry mapEntry = cartridgeFile.getEntry(MAP_FILE_NAME);
			if(mapEntry == null) {
				Console.die("Cartridge Error:\n"
				            + "'" + MAP_FILE_NAME + "'\n"
				            + "file not found");
				return false;
			} else {
				this.map = Map.load(cartridgeFile.getInputStream(mapEntry));
				if(map == null) return false;
			}

			// .cartridge-info
			Console.LOGGER.info("Load '" + CARTRIDGE_INFO_NAME + "'");
			ZipEntry cartridgeInfoEntry = cartridgeFile.getEntry(CARTRIDGE_INFO_NAME);
			if(cartridgeInfoEntry == null) {
				Console.die("Cartridge Error:\n"
				            + "'" + CARTRIDGE_INFO_NAME + "'\n"
				            + "file not found");
				return false;
			} else {
				if(!loadCartridgeInfo(cartridgeFile.getInputStream(cartridgeInfoEntry))) {
					return false;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean initScript() {
		Console.LOGGER.info("Loading scripts");

		JsonElement keysElement = jsonConfig.get("keys");
		if(keysElement == null || !keysElement.isJsonArray()) {
			Console.die("Error:\n"
			            + "'" + CONFIG_FILE_NAME + "'\n"
			            + "must contain\n"
			            + "a string array 'keys'");
			return false;
		}

		JsonArray keyArray = keysElement.getAsJsonArray();
		for(int i = 0; i < keyArray.size(); i++) {
			try {
				String key = keyArray.get(i).getAsString().toUpperCase();
				keys.add(input.new Key(KeyType.KEYBOARD,
				                       KeyStroke.getKeyStroke(key).getKeyCode()));
			} catch(Exception e) {
				Console.die("Error:\n"
				            + "'" + CONFIG_FILE_NAME + "'\n"
				            + "contains invalid keys");
				return false;
			}
		}

		input.init();
		if(scriptCore.init(this)) {
			Console.LOGGER.info("Game Started");
		}
		return true;
	}

	private boolean loadConfig(InputStream in) {
		jsonConfig = loadJson(in);
		if(jsonConfig == null) {
			Console.die("Error:\n"
			            + "'" + Game.CONFIG_FILE_NAME + "'\n"
			            + "must be a json object");
			return false;
		}
		return true;
	}

	private boolean loadCartridgeInfo(InputStream in) {
		cartridgeInfo = loadJson(in);
		if(cartridgeInfo == null) {
			Console.die("Error:\n"
			            + "'" + Game.CARTRIDGE_INFO_NAME + "'\n"
			            + "is invalid");
			return false;
		}
		return true;
	}

	private boolean loadAtlas(InputStream in) {
		try {
			BufferedImage img = ImageIO.read(in);
			if(img == null) {
				Console.die("Error:\n"
				            + "'" + Game.ATLAS_FILE_NAME + "'\n"
				            + "is not an image");
				return false;
			}
			atlas = new IntBitmap(img);
			if(atlas.width != 128 || atlas.height != 128) {
				Console.die("Error:\n"
				            + "atlas must be\n"
				            + "128x128 pixels\n"
				            + "(256 sprites)");
				return false;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	private JsonObject loadJson(InputStream in) {
		try {
			JsonElement element = new JsonParser().parse(new InputStreamReader(in));
			if(!element.isJsonObject()) return null;
			return element.getAsJsonObject();
		} catch(JsonParseException e) {
			return null;
		}
	}

	public void tick() {
		input.tick();

		scriptCore.tick();
		saveSystem.tick();

		if(Console.mode == Mode.DEVELOPER) {
			if(debugRestartGame.isPressed()) {
				Console.LOGGER.info("Restarting Game");
				Console.switchToPanel(new GamePanel());
			} else if(debugGotoShell.isPressed()) {
				Console.LOGGER.info("Stopping Game");
				Console.switchToPanel(new ShellPanel());
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

	public Bitmap<Integer> getSprite(int x, int y, int sw, int sh) {
		return atlas.getSubimage(x * SPR_SIZE, y * SPR_SIZE, sw * SPR_SIZE, sh * SPR_SIZE);
	}

}
