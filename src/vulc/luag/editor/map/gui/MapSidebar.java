package vulc.luag.editor.map.gui;

import vulc.luag.editor.map.MapEditor;
import vulc.luag.gfx.Colors;
import vulc.luag.gfx.gui.GUIPanel;

public class MapSidebar extends GUIPanel {

	public MapSidebar(int x, int y, int w, int h, MapEditor editor) {
		super(x, y, w, h);

		int sidebarElementSpace = 5;

		this.opaque = true;
		this.background = Colors.BACKGROUND_1;

		GUIPanel widthPanel = new MapSizePanel(1, 1,
		                                       w - 2, 23,
		                                       editor, MapSizePanel.WIDTH);
		this.add(widthPanel);

		GUIPanel heightPanel = new MapSizePanel(1, widthPanel.y + widthPanel.h + sidebarElementSpace,
		                                        w - 2, 23,
		                                        editor, MapSizePanel.HEIGHT);
		this.add(heightPanel);

		GUIPanel selectTilePanel = new MapSelectTilePanel(1, heightPanel.y + heightPanel.h + sidebarElementSpace,
		                                                  w - 2, 39,
		                                                  editor);
		this.add(selectTilePanel);
	}

}
