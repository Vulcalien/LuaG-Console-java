package vulc.luag.gfx.gui;

public class GUIButton extends GUILabel {

	public Runnable onMouseDownAction = null;
	public Runnable onMousePressAction = null;
	public Runnable onMouseReleaseAction = null;

	public GUIButton(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	public void onMouseDown(int xMouse, int yMouse) {
		if(onMouseDownAction != null) onMouseDownAction.run();
	}

	public void onMousePress(int xMouse, int yMouse) {
		if(onMousePressAction != null) onMousePressAction.run();
	}

	public void onMouseRelease(int xMouse, int yMouse) {
		if(onMouseReleaseAction != null) onMouseReleaseAction.run();
	}

}
