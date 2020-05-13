package vulc.luag.editor.map.gui;

import vulc.luag.editor.gui.AtlasPreview;
import vulc.luag.editor.map.MapEditor;

public class MapAtlasPreview extends AtlasPreview {

	private final MapEditor editor;

	public MapAtlasPreview(int x, int y, int w, int h, MapEditor editor, int verticalTiles) {
		super(x, y, w, h, editor.editorPanel.game, verticalTiles);
		this.editor = editor;
	}

	public void onMouseDown(int xMouse, int yMouse) {
		super.onMouseDown(xMouse, yMouse);
		editor.selectedTile = selected;
	}

}
