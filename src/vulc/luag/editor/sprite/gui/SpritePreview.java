package vulc.luag.editor.sprite.gui;

import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.gfx.Colors;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIComponent;

public class SpritePreview extends GUIComponent {

	public final SpriteEditor editor;

	public static final int BORDER = 2;

	public SpritePreview(int x, int y, int w, int h, SpriteEditor editor) {
		super(x, y, w, h);
		this.editor = editor;

		this.opaque = true;
		this.background = Colors.BACKGROUND_0;
	}

	public void render(Screen screen) {
		super.render(screen);
		screen.draw(editor.preview.getScaled(SpriteEditor.DEFAULT_SCALE / editor.scope),
		            x + BORDER, y + BORDER);
	}

	public void onMouseDown(int xMouse, int yMouse) {
		int scale = SpriteEditor.DEFAULT_SCALE / editor.scope;

		int xPix = (xMouse - BORDER) / scale;
		int yPix = (yMouse - BORDER) / scale;

		if(xPix < 0 || xPix >= editor.preview.width
		   || yPix < 0 || yPix >= editor.preview.height) return;

		if(editor.toolkit.currentTool.onEdit(xPix, yPix, editor, editor.preview)) {
			editor.isEditing = true;
			editor.shouldSaveContent = true;
		} else if(editor.wasEditing) {
			editor.isEditing = true;
		}
	}

}
