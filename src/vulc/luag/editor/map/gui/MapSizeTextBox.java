package vulc.luag.editor.map.gui;

import vulc.luag.editor.map.MapEditor;
import vulc.luag.game.map.Map;
import vulc.luag.gfx.gui.GUITextBox;

public class MapSizeTextBox extends GUITextBox {

	public static final boolean WIDTH = false, HEIGHT = true;

	private final MapEditor editor;
	private final boolean side;

	public MapSizeTextBox(int x, int y, int w, int h, MapEditor editor, boolean side) {
		super(x, y, w, h);
		this.editor = editor;
		this.side = side;

		acceptedText = GUITextBox.DEC_ONLY;
		nChars = 4;
		opaque = true;
		background = 0xffffff;
		textColor = 0x000000;

		text = getSizeString();
	}

	public void onEnterPress() {
		super.onEnterPress();
		editor.resizeMap();
	}

	public void onLostFocus() {
		text = getSizeString();
	}

	private String getSizeString() {
		Map map = editor.editorPanel.game.map;
		if(side == WIDTH) return map.width + "";
		else return map.height + "";
	}

}
