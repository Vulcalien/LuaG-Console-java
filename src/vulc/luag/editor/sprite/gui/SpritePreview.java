package vulc.luag.editor.sprite.gui;

import vulc.bitmap.Bitmap;
import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.editor.sprite.tool.SpriteTool;
import vulc.luag.editor.sprite.tool.SpriteToolkit;
import vulc.luag.gfx.Colors;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIComponent;

public class SpritePreview extends GUIComponent {

	private enum ToolAction {
		DOWN, PRESS, RELEASE
	}

	public static final int BORDER = 2;

	public final SpriteEditor editor;
	private int animationTicks = 0;

	public SpritePreview(int x, int y, int w, int h, SpriteEditor editor) {
		super(x, y, w, h);
		this.editor = editor;

		this.opaque = true;
		this.background = Colors.BACKGROUND_0;
	}

	public void tick() {
		animationTicks++;
	}

	public void render(Screen screen) {
		super.render(screen);
		Bitmap<Integer> preview = editor.preview.getCopy();

		if(editor.pasteMode) {
			preview.draw(editor.copied, editor.xPasted, editor.yPasted);
		}

		// selection highlight
		SpriteToolkit toolkit = editor.toolkit;
		if(toolkit.currentTool == toolkit.select) {
			int x0 = Math.min(editor.selx0, editor.selx1);
			int y0 = Math.min(editor.sely0, editor.sely1);
			int x1 = Math.max(editor.selx0, editor.selx1);
			int y1 = Math.max(editor.sely0, editor.sely1);

			int transparency = animationTicks / 50 % 2 == 0 ? 0x55 : 0x77;
			preview.fill(x0, y0, x1, y1, 0xffffff, transparency);
		}

		preview = preview.getScaled(SpriteEditor.DEFAULT_SCALE / editor.scope);
		screen.draw(preview, x + BORDER, y + BORDER);

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
