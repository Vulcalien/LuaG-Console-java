package vulc.jlconsole.gfx.panel;

import vulc.jlconsole.Console;
import vulc.jlconsole.editor.Editor;
import vulc.jlconsole.editor.map.MapEditor;
import vulc.jlconsole.game.Game;
import vulc.jlconsole.gfx.Screen;

public class EditorPanel extends Panel {

	public final Game game;
	public final Screen innerScreen;

	public final int[] margins = {10, 0, 10, 0};

	public final Editor
	mapEditor;

	public Editor currentEditor;

	//TODO switch to a GUI based interface
	public EditorPanel(Console console) {
		super(console);
		this.game = new Game(console);

		innerScreen = new Screen(console.screen.width - margins[1] - margins[3], console.screen.height - margins[0] - margins[2]);

		mapEditor = new MapEditor(console, this);

		currentEditor = mapEditor;
	}

	public void init() {
		game.initResources();
	}

	public void tick() {
		currentEditor.tick();
		console.screen.clear(0xDD4444);
		console.screen.draw(innerScreen, margins[1], margins[0]);
	}

}
