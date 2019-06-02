package vulc.jlconsole.cmd;

import java.util.ArrayList;
import java.util.List;

import vulc.jlconsole.Console;
import vulc.jlconsole.gfx.Screen;
import vulc.jlconsole.gfx.panel.GamePanel;
import vulc.jlconsole.gfx.panel.Panel;

public class Cmd {

	public final List<String> lines = new ArrayList<String>();
	public final List<Character> charBuffer = new ArrayList<Character>();

	private Console console;
	private int ticks = 0;

	public Cmd(Console console) {
		lines.add("");
		this.console = console;
	}

	public void tick() {
		ticks++;

		if(charBuffer.size() != 0) {
			receiveInput(charBuffer.get(0));
			charBuffer.remove(0);
		}

		for(int i = 0; i < lines.size(); i++) {
			console.screen.write(lines.get(i), 0xffffff, 1, i * (Screen.FONT.getHeight() + 1));
		}
	}

	public void receiveInput(char character) {
		switch(character) {
			case '\n':
				execute(lines.get(lines.size() - 1));
				lines.add("");
				break;

			default:
				if(character >= 32 && character <= 127) {
					String value = lines.get(lines.size() - 1);
					value += character;
					lines.set(lines.size() - 1, value);
				}
				break;
		}
		lines.get(lines.size() - 1);
	}

	public void execute(String command) {
		switch(command) {
			case "run":
				Panel gamePanel = new GamePanel(console);
				console.currentPanel = gamePanel;
				gamePanel.init();
				break;

			default:
				break;
		}
	}

}
