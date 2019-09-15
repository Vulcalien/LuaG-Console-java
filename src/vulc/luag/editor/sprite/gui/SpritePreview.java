package vulc.luag.editor.sprite.gui;

import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIComponent;

public class SpritePreview extends GUIComponent {

	public final SpriteEditor editor;

	public SpritePreview(int x, int y, int w, int h, SpriteEditor editor) {
		super(x, y, w, h);
		this.editor = editor;
	}

	public void render(Screen screen) {
		screen.draw(editor.preview.getScaled(editor.previewScale),
		            x, y);
	}

	public void onPress(int xMouse, int yMouse) {
		int xPix = xMouse / editor.previewScale;
		int yPix = yMouse / editor.previewScale;

		if(editor.toolkit.currentTool.onEdit(xPix, yPix, editor, editor.preview)) {
			editor.isEditing = true;
			editor.shouldSaveContent = true;
		} else if(editor.wasEditing) {
			editor.isEditing = true;
		}
	}

}
