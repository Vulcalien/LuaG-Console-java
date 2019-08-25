package vulc.luag.editor.map.gui;

import vulc.luag.editor.map.MapEditor;
import vulc.luag.gfx.gui.GUITextBox;

public class MapSelectTileTextBox extends GUITextBox {

	private final MapEditor editor;

	public MapSelectTileTextBox(int x, int y, int w, int h, MapEditor editor) {
		super(x, y, w, h);
		this.editor = editor;

		acceptedText = GUITextBox.DEC_ONLY;
		nChars = 3;
		opaque = true;
		background = 0xffffff;
		textColor = 0x000000;

		text = editor.selectedTile + "";
	}

	public void onLostFocus() {
		text = editor.selectedTile + "";
	}

	public void onEnterPress() {
		super.onEnterPress();

		int id;
		if(text.equals("")) id = 0;
		else id = Integer.parseInt(text);
		if(id > 255) id = 255;

		editor.selectedTile = id;
		text = id + "";
	}

}
