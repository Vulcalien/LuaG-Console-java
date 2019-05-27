package vulc.jlconsole.game;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import vulc.bitmap.Bitmap;
import vulc.jlconsole.Console;
import vulc.jlconsole.game.map.Map;
import vulc.jlconsole.game.scripting.LuaScriptCore;
import vulc.jlconsole.input.InputHandler.Key;
import vulc.jlconsole.input.InputHandler.KeyType;

public class Game {

	public final Console console;
	public final LuaScriptCore scriptCore = new LuaScriptCore();
	public final GameSounds sounds = new GameSounds();

	public JsonObject jsonConfig;
	public Bitmap atlas;

	public Map map;

	public static final Key[] KEYS = {
		new Key(KeyType.KEYBOARD, KeyEvent.VK_W),
		new Key(KeyType.KEYBOARD, KeyEvent.VK_A),
		new Key(KeyType.KEYBOARD, KeyEvent.VK_S),
		new Key(KeyType.KEYBOARD, KeyEvent.VK_D)
	};

	public Game(Console console) {
		this.console = console;
	}

	public void init() {
		sounds.init();
		try {
			atlas = new Bitmap(ImageIO.read(new File(Console.USER_DIR + "/atlas.png")));
		} catch(IOException e) {
			System.err.println("Error: 'atlas.png' does not exist");
			System.exit(1);
		}
		try {
			jsonConfig = new JsonParser().parse(new FileReader(Console.USER_DIR + "/config.json")).getAsJsonObject();
		} catch(IOException e) {
			System.err.println("Error: 'config.json' does not exist");
			System.exit(1);
		}

		//TODO change map system to a file
		JsonArray mapSize = jsonConfig.get("map-size").getAsJsonArray();
		map = new Map(mapSize.get(0).getAsInt(), mapSize.get(1).getAsInt());

		scriptCore.init(console, this);
	}

	public void tick() {
		scriptCore.tick();
	}

}
