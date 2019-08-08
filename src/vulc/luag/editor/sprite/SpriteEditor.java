package vulc.luag.editor.sprite;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import vulc.luag.Console;
import vulc.luag.editor.Editor;
import vulc.luag.gfx.panel.EditorPanel;
import vulc.luag.input.InputHandler.Key;
import vulc.luag.input.InputHandler.KeyType;

public class SpriteEditor extends Editor {

	private final Key mouse1;
	private final Key moveUp;
	private final Key moveLeft;
	private final Key moveDown;
	private final Key moveRight;

	public SpriteEditor(Console console, EditorPanel panel, int x, int y, int w, int h) {
		super(console, panel, x, y, w, h);

		mouse1 = input.new Key(KeyType.MOUSE, MouseEvent.BUTTON1);
		moveUp = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_W);
		moveLeft = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_A);
		moveDown = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_S);
		moveRight = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_D);

		// INTERFACE
	}

	public void tick() {
	}

}
