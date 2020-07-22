package vulc.luag.editor.sprite.gui;

import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.editor.sprite.tool.SpriteTool;
import vulc.luag.gfx.Colors;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIComponent;

public class SpritePreview extends GUIComponent {

	private enum ToolAction {
		DOWN, PRESS, RELEASE
	}

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
		onAction(xMouse, yMouse, ToolAction.DOWN);
	}

	public void onMousePress(int xMouse, int yMouse) {
		onAction(xMouse, yMouse, ToolAction.PRESS);
	}

	public void onMouseRelease(int xMouse, int yMouse) {
		onAction(xMouse, yMouse, ToolAction.RELEASE);
	}

	private void onAction(int xMouse, int yMouse, ToolAction action) {
		int scale = SpriteEditor.DEFAULT_SCALE / editor.scope;

		int xPix = Math.floorDiv(xMouse - BORDER, scale);
		int yPix = Math.floorDiv(yMouse - BORDER, scale);

		if(xPix < 0 || xPix >= editor.preview.width
		   || yPix < 0 || yPix >= editor.preview.height) return;

		SpriteTool tool = editor.toolkit.currentTool;

		if((action == ToolAction.DOWN && tool.onMouseDown(xPix, yPix, editor, editor.preview))
		   || (action == ToolAction.PRESS && tool.onMousePress(xPix, yPix, editor, editor.preview))
		   || (action == ToolAction.RELEASE && tool.onMouseRelease(xPix, yPix, editor, editor.preview))) {
			editor.isEditing = true;
			editor.shouldSaveContent = true;
		} else if(editor.wasEditing) {
			editor.isEditing = true;
		}
	}

}
