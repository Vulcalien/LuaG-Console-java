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
		screen.draw(editor.preview.getScaled(editor.previewScale),
		            x + BORDER, y + BORDER);
	}

	public void onPress(int xMouse, int yMouse) {
		int xPix = (xMouse - BORDER) / editor.previewScale;
		int yPix = (yMouse - BORDER) / editor.previewScale;

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
