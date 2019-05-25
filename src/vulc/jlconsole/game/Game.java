package vulc.jlconsole.game;

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
import vulc.jlconsole.game.sfx.Sounds;

public class Game {

	public final Console console;
	public JsonObject jsonConfig;
	public Bitmap atlas;

	public Map map;

	public Game(Console console) {
		this.console = console;
	}

	public void init() {
		Sounds.init();
		try {
			atlas = new Bitmap(ImageIO.read(new File(Console.USER_DIR + "/atlas.png")));
			jsonConfig = new JsonParser().parse(new FileReader(Console.USER_DIR + "/config.json")).getAsJsonObject();
		} catch(IOException e) {
			e.printStackTrace();
		}

		//TODO change map system to a file
		JsonArray mapSize = jsonConfig.get("map-size").getAsJsonArray();
		map = new Map(mapSize.get(0).getAsInt(), mapSize.get(1).getAsInt());

		LuaScriptCore.init(console, this);
	}

	public void tick() {
		LuaScriptCore.tick();
	}

}
