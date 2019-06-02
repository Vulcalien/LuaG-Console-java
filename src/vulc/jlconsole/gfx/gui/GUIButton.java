package vulc.jlconsole.gfx.gui;

public class GUIButton extends GUILabel {

	public Runnable action = null;

	public GUIButton(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	public void press() {
		if(action != null) action.run();
	}

}
