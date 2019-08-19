package vulc.luag.editor.sprite;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import vulc.bitmap.Bitmap;
import vulc.luag.Console;
import vulc.luag.editor.Editor;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIComponent;
import vulc.luag.gfx.panel.EditorPanel;
import vulc.luag.input.InputHandler.Key;
import vulc.luag.input.InputHandler.KeyType;

public class SpriteEditor extends Editor {

	private final Key mouse1;
	private final Key moveUp;
	private final Key moveLeft;
	private final Key moveDown;
	private final Key moveRight;

	private final Bitmap atlas;
	private final int previewScale = 6;
	private int spriteID = 0;
	private int scope = 1;

	public SpriteEditor(Console console, EditorPanel panel, int x, int y, int w, int h) {
		super(console, panel, x, y, w, h);
		this.atlas = panel.game.atlas;

		mouse1 = input.new Key(KeyType.MOUSE, MouseEvent.BUTTON1);
		moveUp = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_W);
		moveLeft = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_A);
		moveDown = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_S);
		moveRight = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_D);

		// INTERFACE
		guiPanel.background = 0x000000;

		// TODO actionbar

		int previewSize = 8 * previewScale;
		GUIComponent sprPreview = new GUIComponent((guiPanel.w - previewSize) / 2, 5,
		                                           previewSize, previewSize) {
			public void render(Screen screen) {
				screen.draw(panel.game.getSprite(spriteID, scope, scope)
				                      .getScaled(previewScale),
				            x, y);
			}
		};
		guiPanel.add(sprPreview);

	}

	public void tick() {
	}

	public String getTitle() {
		return "Sprite Editor";
	}

}
