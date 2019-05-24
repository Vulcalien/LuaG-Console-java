package vulc.jlconsole.game;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import vulc.bitmap.Bitmap;
import vulc.jlconsole.Console;
import vulc.jlconsole.game.map.Map;
import vulc.jlconsole.game.sfx.Sounds;

public class Game {

	public static final String USER_DIR = "./console-userdata";

	public Console console;
	public JsonObject jsonConfig;
	public Bitmap atlas;

	//TODO add map file system
	public Map map = new Map(100, 100);

	public void init(Console console) {
		this.console = console;
		Sounds.init();
		try {
			atlas = new Bitmap(ImageIO.read(new File(USER_DIR + "/atlas.png")));
			jsonConfig = new JsonParser().parse(new FileReader(USER_DIR + "/config.json")).getAsJsonObject();
		} catch(IOException e) {
			e.printStackTrace();
		}
		LuaScriptCore.init(console, this);
	}

	public void tick() {
		LuaScriptCore.tick();
	}

}
