package vulc.luag.editor.sprite.gui;

import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.gfx.Colors;
import vulc.luag.gfx.gui.GUIButton;

public class SpriteScopeButton extends GUIButton {

	public SpriteScopeButton(int x, int y, int w, int h, SpriteEditor editor, int scope) {
		super(x, y, w, h);
		background = Colors.BACKGROUND_1;
		textColor = Colors.FOREGROUND_1;
		opaque = true;

		text = scope + "";

		this.onMouseDownAction = () -> {
			editor.scope = scope;
			editor.atlasPreview.scope = scope;
			editor.atlasPreview.resetPreview();
		};
	}

}
