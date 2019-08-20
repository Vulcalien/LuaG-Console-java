package vulc.luag.gfx.gui;

public class GUITextBox extends GUILabel {

	public static final int ALL_TEXT = 0;
	public static final int DEC_ONLY = 1;
	public static final int HEX_ONLY = 2;

	public int acceptedText = ALL_TEXT;
	public int nChars = -1;

	public GUITextBox(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	public void onKeyPress(char character) {
		if(!focused) return;
		if(character == '\b') {
			if(text.length() > 0) text = text.substring(0, text.length() - 1);
		} else if(character == '\n') {
			onEnterPress();
		} else {
			if(text.length() == nChars) return;
			boolean canWrite = false;
			switch(acceptedText) {
				case ALL_TEXT:
					if(character >= ' ' && character <= '~')
					    canWrite = true;
					break;

				case DEC_ONLY:
					if(character >= '0' && character <= '9')
					    canWrite = true;
					break;
				case HEX_ONLY:
					if((character >= '0' && character <= '9')
					   || (character >= 'A' && character <= 'F')
					   || (character >= 'a' && character <= 'f')) {
						canWrite = true;
					}
					break;
			}
			if(canWrite) text += character;
		}
	}

	public void onEnterPress() {
		focused = false;
	}

}
