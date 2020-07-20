/*******************************************************************************
 * Copyright (C) 2019 Vulcalien
 * This code or part of it is licensed under MIT License by Vulcalien
 ******************************************************************************/
package vulc.luag.input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import vulc.luag.Console;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener, FocusListener {

	public static enum KeyType {
		KEYBOARD, MOUSE
	}

	public static enum KeyAction {
		PRESS, RELEASE
	}

	private final List<Key> keys = new ArrayList<Key>();
	private final List<Key> keyboardKeys = new ArrayList<Key>();
	private final List<Key> mouseKeys = new ArrayList<Key>();

	public int xMouse = -1, yMouse = -1;

	public void init() {
		Console console = Console.instance;

		console.setFocusTraversalKeysEnabled(false);

		console.addKeyListener(this);
		console.addMouseListener(this);
		console.addMouseMotionListener(this);
		console.addFocusListener(this);
	}

	public void remove() {
		Console console = Console.instance;

		releaseAll();

		console.removeKeyListener(this);
		console.removeMouseListener(this);
		console.removeMouseMotionListener(this);
		console.removeFocusListener(this);
	}

	public void tick() {
		for(int i = 0; i < keys.size(); i++) {
			keys.get(i).tick();
		}
	}

	private void releaseAll() {
		for(Key key : keys) {
			key.isKeyDown = false;
			key.wasKeyDown = false;
		}
	}

	private void receiveInput(KeyAction action, KeyType type, int code) {
		List<Key> keys = getList(type);
		for(Key key : keys) {
			if(key.code == code) {
				if(action == KeyAction.PRESS) {
					key.shouldStayDown = true;
				} else if(action == KeyAction.RELEASE) {
					key.shouldRelease = true;
				}
			}
		}
	}

	private List<Key> getList(KeyType type) {
		switch(type) {
			case KEYBOARD:
				return keyboardKeys;

			case MOUSE:
				return mouseKeys;

			default:
				return null;
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		receiveInput(KeyAction.PRESS, KeyType.KEYBOARD, e.getKeyCode());
	}

	public void keyReleased(KeyEvent e) {
		receiveInput(KeyAction.RELEASE, KeyType.KEYBOARD, e.getKeyCode());
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		receiveInput(KeyAction.PRESS, KeyType.MOUSE, e.getButton());
	}

	public void mouseReleased(MouseEvent e) {
		receiveInput(KeyAction.RELEASE, KeyType.MOUSE, e.getButton());
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		xMouse = e.getX();
		yMouse = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		xMouse = e.getX();
		yMouse = e.getY();
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		for(Key key : keys) {
			key.shouldRelease = true;
		}
	}

	public class Key {

		private KeyType type;
		private int code;

		private boolean shouldStayDown = false;
		private boolean shouldRelease = false;

		private boolean isKeyDown = false;
		private boolean wasKeyDown = false;

		public Key() {
		}

		public Key(KeyType type, int code) {
			init(type, code);
		}

		private void init(KeyType type, int code) {
			this.type = type;
			this.code = code;

			keys.add(this);
			getList(type).add(this);
		}

		private void tick() {
			wasKeyDown = isKeyDown;
			isKeyDown = shouldStayDown;

			if(shouldRelease) {
				shouldRelease = false;
				shouldStayDown = false;
			}
		}

		public void setKeyBinding(KeyType newType, int newCode) {
			if(this.type != null) getList(this.type).remove(this);
			init(newType, newCode);
		}

		public boolean isKeyDown() {
			return isKeyDown;
		}

		public boolean isPressed() {
			return !wasKeyDown && isKeyDown;
		}

		public boolean isReleased() {
			return wasKeyDown && !isKeyDown;
		}

	}

}
