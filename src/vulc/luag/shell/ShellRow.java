package vulc.luag.shell;

import java.util.ArrayList;

import vulc.bitmap.Bitmap;
import vulc.luag.gfx.Screen;

public class ShellRow extends ArrayList<ShellChar> {

	private static final long serialVersionUID = 1L;

	public void render(Bitmap<Integer> bitmap, int x, int y) {
		for(ShellChar c : this) {
			bitmap.write(c.value + "", c.color, x, y);
			x += Screen.FONT.widthOf(c.value) + Screen.FONT.getLetterSpacing();
		}
	}

	public char charAt(int index) {
		return get(index).value;
	}

	public ShellRow join(ShellChar c) {
		add(c);
		return this;
	}

	public ShellRow join(ShellRow row) {
		for(ShellChar c : row) {
			add(c);
		}
		return this;
	}

	public ShellRow subrow(int begin, int end) {
		ShellRow result = new ShellRow();
		for(int i = begin; i < end; i++) {
			result.add(get(i));
		}
		return result;
	}

	public String toString() {
		String string = "";
		for(ShellChar c : this) {
			string += c.value;
		}
		return string;
	}

	public ShellRow copy() {
		ShellRow newRow = new ShellRow();
		for(ShellChar c : this) {
			newRow.add(c);
		}
		return newRow;
	}

}
