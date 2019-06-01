package vulc.jlconsole.gfx.panel;

import vulc.jlconsole.Console;
import vulc.jlconsole.editor.Editor;
import vulc.jlconsole.editor.map.MapEditor;
import vulc.jlconsole.game.Game;
import vulc.jlconsole.gfx.Screen;

public class EditorPanel extends Panel {

	public final Game game;
	public final Screen screen;

	public final Editor
	mapEditor;

	public Editor currentEditor;

	public EditorPanel(Console console) {
		super(console);
		this.game = new Game(console);

		mapEditor = new MapEditor(console, this);

		screen = new Screen(console.screen.width, console.screen.height - 20);
		currentEditor = mapEditor;
	}

	public void init() {
		game.initResources();
	}

	public void tick() {
		currentEditor.tick();
		console.screen.clear(0xDD4444);
		console.screen.draw(screen, 0, 10);
	}

}
