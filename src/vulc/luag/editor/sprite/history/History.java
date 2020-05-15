package vulc.luag.editor.sprite.history;

import java.util.ArrayList;
import java.util.List;

import vulc.bitmap.Bitmap;
import vulc.luag.editor.sprite.SpriteEditor;

public class History {

	private final SpriteEditor editor;
	private final int historySize;

	private List<Bitmap<Integer>> records = new ArrayList<Bitmap<Integer>>();
	private int nextHistoryIndex = 0;

	public History(SpriteEditor editor, int size) {
		this.editor = editor;
		this.historySize = size;
	}

	public void save() {
		// if UNDOs where done, clear the "future" records
		records = records.subList(0, nextHistoryIndex);

		records.add(editor.atlas.getCopy());
		if(records.size() > historySize) {
			records.remove(0);
		}
		nextHistoryIndex = records.size();
	}

	public boolean undo() {
		if(nextHistoryIndex == 1) return false;

		editor.atlas.draw(records.get(nextHistoryIndex - 2), 0, 0);
		nextHistoryIndex--;

		return true;
	}

	public boolean redo() {
		if(nextHistoryIndex == records.size()) return false;

		editor.atlas.draw(records.get(nextHistoryIndex), 0, 0);
		nextHistoryIndex++;

		return true;
	}

}
