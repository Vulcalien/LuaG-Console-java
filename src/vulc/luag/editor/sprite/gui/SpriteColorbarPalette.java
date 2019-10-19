package vulc.luag.editor.sprite.gui;

import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.gfx.Colors;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIComponent;
import vulc.luag.gfx.gui.GUIPanel;

public class SpriteColorbarPalette extends GUIPanel {

	public SpriteColorbarPalette(int x, int y, int w, int h, SpriteEditor editor, int columns) {
		super(x, y, w, h);

		this.background = Colors.BACKGROUND_1;

		for(int i = 0; i < SpriteEditor.PALETTE_SIZE; i++) {
			int id = i;
			int xt = (id % columns);
			int yt = (id / columns);

			GUIComponent comp = new GUIComponent(1 + xt * 9, 1 + yt * 9, 8, 8) {
				public void render(Screen screen) {
					screen.fill(x, y, x + w - 1, y + h - 1, editor.lastColors.get(id));
				}

				public void onMousePress(int xMouse, int yMouse) {
					editor.selectColor(editor.lastColors.get(id));
				}
			};
			this.add(comp);
		}
	}

}
