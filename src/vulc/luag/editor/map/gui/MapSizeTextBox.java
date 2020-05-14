package vulc.luag.editor.map.gui;

import vulc.luag.editor.map.MapEditor;
import vulc.luag.gfx.gui.GUITextBox;

public class MapSizeTextBox extends GUITextBox {

	public static final boolean WIDTH = false, HEIGHT = true;
	public static final int N_CHARS = 4;

	private final MapEditor editor;
	private final boolean side;

	public MapSizeTextBox(int x, int y, int w, int h, MapEditor editor, boolean side) {
		super(x, y, w, h);
		this.editor = editor;
		this.side = side;

		acceptedText = GUITextBox.DEC_ONLY;
		nChars = N_CHARS;
		opaque = true;
		background = 0xffffff;
		textColor = 0x000000;
	}

	public void onEnterPress() {
		super.onEnterPress();

		int value;
		if(text.equals("")) {
			value = 0;
		} else {
			value = Integer.parseInt(text);
			if(value > 256) {
				value = 256;
			}
		}
		text = "" + value;

		editor.resizeMap(value, side);
	}

}
