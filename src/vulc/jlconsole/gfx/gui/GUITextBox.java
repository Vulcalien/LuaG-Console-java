package vulc.jlconsole.gfx.gui;

import vulc.jlconsole.gfx.Screen;

public class GUITextBox extends GUILabel {

	public boolean numbersOnly = false;
	public int nChars = -1;

	public GUITextBox(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	public void press() {
	}

	public void render(Screen screen, int xOff, int yOff) {
		super.render(screen, xOff, yOff);
	}

	public void onKeyPress(char character) {
		if(text.length() == nChars) return;
		if(numbersOnly && (character < 48 || character > 57)) return;
		text += character;
	}

}
