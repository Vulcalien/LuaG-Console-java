package vulc.luag.game.save;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import vulc.luag.Console;
import vulc.luag.gfx.Icons;
import vulc.vdf.ObjectTag;

public class SaveSystem {

	public static final String SAVE_EXTENSION = "sav";
	public static final int MAX_SIZE = 256 * 1024; // 256 KB
	public static final int SAVE_DELAY = 60; // 1 sec

	private final String saveFile;

	private int lastSave = 0;
	private boolean hasLoaded = false;

	public SaveSystem(String cartridgeFile) {
		this.saveFile = cartridgeFile + "." + SAVE_EXTENSION;
	}

	public void tick() {
		// render the "save" icon only if the action was performed recently
		if(Console.ticks - lastSave > SAVE_DELAY) return;

		Console.SCREEN.drawBool(Icons.SAVE, 0xffffff,
		                        ((SAVE_DELAY - (Console.ticks - 1 - lastSave)) * 0xff / SAVE_DELAY),
		                        151, 3);
	}

	public int serialize(LuaTable saveTable) {
		Console.LOGGER.info("Saving game data...");

		// if the save action was performed recently, don't save again
		if(Console.ticks - lastSave < SAVE_DELAY) {
			Console.LOGGER.severe("Save Error 1: trying to save too fast "
			                      + "(delay: " + SAVE_DELAY + " ticks)");
			return 1; // too fast
		}

		lastSave = Console.ticks;

		ObjectTag obj = new ObjectTag();
		addTableToObjectTag(obj, saveTable);

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		try(DataOutputStream bufferOut = new DataOutputStream(buffer)) {
			obj.serialize(bufferOut);
		} catch(IOException e) {
			e.printStackTrace();
		}

		if(buffer.size() > MAX_SIZE) {
			Console.LOGGER.severe("Save Error 2: too much data "
			                      + "(max: " + MAX_SIZE + " bytes, sent: " + buffer.size() + ")");
			return 2; // too much data
		}

		try(FileOutputStream out = new FileOutputStream(saveFile)) {
			synchronized(Console.DONT_STOP_LOCK) {
				out.write(buffer.toByteArray());
			}
		} catch(IOException e) {
			e.printStackTrace();
		}

		Console.LOGGER.info("Saving complete: " + buffer.size() + " bytes written");
		return 0; // ok
	}

	private static void addTableToObjectTag(ObjectTag obj, LuaTable table) {
		for(LuaValue keyLua : table.keys()) {
			LuaValue value = table.get(keyLua);
			String key = keyLua.toString();

			// add all valid values to the ObjectTag
			if(value.isboolean()) obj.setBoolean(key, value.checkboolean());
			else if(value.isint()) {
				int num = value.checkint();

				if((byte) num == num) obj.setByte(key, (byte) num);
				else if((short) num == num) obj.setShort(key, (short) num);
				else obj.setInt(key, num);
				// long is considered Double by luaj
			} else if(value.isnumber()) obj.setDouble(key, value.checkdouble());
			else if(value.isstring()) obj.setString(key, value.tojstring());
			else if(value.istable()) {
				ObjectTag subObj = new ObjectTag();
				addTableToObjectTag(subObj, value.checktable());
				obj.setObject(key, subObj);
			} else throw new LuaError("bad argument: type is " + value.typename());
		}
	}

	public LuaValue deserialize() {
		Console.LOGGER.info("Loading game data...");

		if(hasLoaded) {
			Console.LOGGER.severe("Load Error: data can be loaded only once");
			return LuaValue.NIL;
		}
		hasLoaded = true;

		ObjectTag obj = new ObjectTag();
		try(DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(saveFile)))) {
			obj.deserialize(in);
		} catch(IOException e) {
			e.printStackTrace();
		}

		LuaTable saveTable = new LuaTable();
		addObjectTagToTable(saveTable, obj);

		Console.LOGGER.info("Loading complete");
		return saveTable;
	}

	private static void addObjectTagToTable(LuaTable table, ObjectTag obj) {
		for(String key : obj.keySet()) {
			Object value = obj.getValue(key);

			LuaValue toAdd = null;
			if(value instanceof Boolean) toAdd = LuaValue.valueOf((boolean) value);
			else if(value instanceof Byte) toAdd = LuaValue.valueOf((byte) value);
			else if(value instanceof Short) toAdd = LuaValue.valueOf((short) value);
			else if(value instanceof Integer) toAdd = LuaValue.valueOf((int) value);
			else if(value instanceof Double) toAdd = LuaValue.valueOf((double) value);
			else if(value instanceof String) toAdd = LuaValue.valueOf((String) value);
			else if(value instanceof ObjectTag) {
				toAdd = new LuaTable();
				addObjectTagToTable((LuaTable) toAdd, (ObjectTag) value);
			} else {
				throw new LuaError("save file is malformed (contains invalid data type: "
				                   + value.getClass().getCanonicalName() + ")");
			}
			if(toAdd != null) table.set(key, toAdd);
		}
	}

}
