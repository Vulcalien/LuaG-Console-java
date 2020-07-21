package vulc.luag.game.save;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import vulc.vdf.ObjectTag;

public class SaveSystem {

	public static final String SAVE_EXTENSION = "sav";

	private final String saveFile;

	public SaveSystem(String cartridgeFile) {
		this.saveFile = cartridgeFile + "." + SAVE_EXTENSION;
	}

	public void serialize(LuaTable saveTable) {
		ObjectTag obj = new ObjectTag();
		addTableToObjectTag(obj, saveTable);

		try(DataOutputStream out = new DataOutputStream(new FileOutputStream(saveFile))) {
			obj.serialize(out);
		} catch(IOException e) {
			e.printStackTrace();
		}
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

	public LuaTable deserialize() {
		LuaTable saveTable = new LuaTable();

		ObjectTag obj = new ObjectTag();
		try(DataInputStream in = new DataInputStream(new FileInputStream(saveFile))) {
			obj.deserialize(in);
		} catch(IOException e) {
			e.printStackTrace();
		}

		addObjectTagToTable(saveTable, obj);
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
