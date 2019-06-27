package vulc.luag.game;

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
import vulc.luag.game.map.Map;
import vulc.luag.game.scripting.LuaScriptCore;
import vulc.luag.gfx.panel.DeathPanel;
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

	public Game(Console console) {
		this.console = console;
	}

	public boolean initResources() {
		sounds.init(console);
		try {
			atlas = new Bitmap(ImageIO.read(new File(Game.USER_DIR + "/atlas.png")));
			if(atlas.width != 128 || atlas.height != 128) {
				console.switchToPanel(new DeathPanel(console, "Error:\n"
				                                              + "atlas must be\n"
				                                              + "128x128 (256 sprites)"));
				return false;
			}
		} catch(IOException e) {
			console.switchToPanel(new DeathPanel(console, "Error:\n"
			                                              + "'atlas.png' does\n"
			                                              + "not exist"));
			return false;
		}
		try {
			jsonConfig = new JsonParser().parse(new FileReader(Game.USER_DIR + "/config.json")).getAsJsonObject();
		} catch(IOException e) {
			console.switchToPanel(new DeathPanel(console, "Error:\n"
			                                              + "'config.json' does\n"
			                                              + "not exist"));
			return false;
		}

		try {
			map = Map.load(new FileInputStream(Game.USER_DIR + "/map"), console);
			if(map == null) return false;
		} catch(FileNotFoundException e) {
			console.switchToPanel(new DeathPanel(console, "Error:\n"
			                                              + "'map' does\n"
			                                              + "not exist"));
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
			console.switchToPanel(new DeathPanel(console, "Error:\n"
			                                              + "'config.json' must contain\n"
			                                              + "a string array 'keys'"));
			return false;
		}
		input.init(console);
		scriptCore.init(console, this);

		return true;
	}

	public void tick() {
		scriptCore.tick();
		input.tick();
	}

	public Bitmap getSprite(int id, int sw, int sh) {
		return getSprite(id % 16, id / 16, sw, sh);
	}

	public Bitmap getSprite(int x, int y, int w, int h) {
		return atlas.getSubimage(x * 8, y * 8, w * 8, h * 8);
	}

}
