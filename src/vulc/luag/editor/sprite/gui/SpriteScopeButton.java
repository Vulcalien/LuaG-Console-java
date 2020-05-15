package vulc.luag.editor.sprite.gui;

import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.gfx.Colors;
import vulc.luag.gfx.Icons;
import vulc.luag.gfx.gui.GUIButton;

public class SpriteScopeButton extends GUIButton {

	public SpriteScopeButton(int x, int y, int w, int h, SpriteEditor editor, int scope) {
		super(x, y, w, h);
		background = Colors.BACKGROUND_1;
		colorAsBool = Colors.FOREGROUND_1;
		opaque = true;

		if(scope == 1) {
			boolImage = Icons.SPRITE_SCOPE_1;
		} else if(scope == 2) {
			boolImage = Icons.SPRITE_SCOPE_2;
		}

		this.onMousePressAction = () -> {
			editor.setScope(scope);
		};
	}

}
