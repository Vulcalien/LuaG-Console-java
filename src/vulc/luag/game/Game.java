package vulc.luag.game;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import vulc.bitmap.Bitmap;
import vulc.luag.Console;
import vulc.luag.game.map.Map;
import vulc.luag.game.scripting.LuaScriptCore;
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

	public final Key[] KEYS = {
		input.new Key(KeyType.KEYBOARD, KeyEvent.VK_W),
		input.new Key(KeyType.KEYBOARD, KeyEvent.VK_A),
		input.new Key(KeyType.KEYBOARD, KeyEvent.VK_S),
		input.new Key(KeyType.KEYBOARD, KeyEvent.VK_D)
	};

	public Game(Console console) {
		this.console = console;
	}

	public void initResources() {
		sounds.init();
		try {
			atlas = new Bitmap(ImageIO.read(new File(Game.USER_DIR + "/atlas.png")));
			if(atlas.width != 128 || atlas.height != 128) {
				System.err.println("Error: atlas must be 128x128 (256 sprites)");
				System.exit(1);
			}
		} catch(IOException e) {
			System.err.println("Error: 'atlas.png' does not exist");
			System.exit(1);
		}
		try {
			jsonConfig = new JsonParser().parse(new FileReader(Game.USER_DIR + "/config.json")).getAsJsonObject();
		} catch(IOException e) {
			System.err.println("Error: 'config.json' does not exist");
			System.exit(1);
		}

		try {
			map = Map.load(new FileInputStream(Game.USER_DIR + "/map"));
		} catch(FileNotFoundException e) {
			System.err.println("Error: file 'map' not found");
			System.exit(1);
		}
	}

	public void initScript() {
		input.init(console);
		scriptCore.init(console, this);
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
