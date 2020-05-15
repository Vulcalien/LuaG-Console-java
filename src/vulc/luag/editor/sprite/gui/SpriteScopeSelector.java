package vulc.luag.editor.sprite.gui;

import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.gfx.Colors;
import vulc.luag.gfx.gui.GUIPanel;

public class SpriteScopeSelector extends GUIPanel {

	public SpriteScopeSelector(int x, int y, int w, int h, SpriteEditor editor, int[] scopes) {
		super(x, y, w, h);
		background = Colors.BACKGROUND_0;

		for(int i = 0; i < scopes.length; i++) {
			add(new SpriteScopeButton(1, 1 + i * 9, 8, 8, editor, scopes[i]));
		}
	}

}
