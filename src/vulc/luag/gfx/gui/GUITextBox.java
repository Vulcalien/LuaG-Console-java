package vulc.luag.gfx.gui;

public class GUITextBox extends GUILabel {

	public boolean numbersOnly = false;
	public int nChars = -1;

	public GUITextBox(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	public void press() {
	}

	public void onKeyPress(char character) {
		if(!focused) return;
		if(character == '\b') {
			if(text.length() > 0) text = text.substring(0, text.length() - 1);
		} else if(character == '\n') {
			onEnterPress();
		} else {
			if(text.length() == nChars) return;
			if(character < 32 || character > 127) return;
			if(numbersOnly && (character < 48 || character > 57)) return;
			text += character;
		}
	}

	public void onEnterPress() {
		focused = false;
	}

}
