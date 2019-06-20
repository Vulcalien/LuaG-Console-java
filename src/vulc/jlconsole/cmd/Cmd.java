package vulc.jlconsole.cmd;

import java.util.ArrayList;
import java.util.List;

import vulc.jlconsole.Console;
import vulc.jlconsole.gfx.Screen;
import vulc.jlconsole.gfx.panel.CmdPanel;
import vulc.jlconsole.gfx.panel.EditorPanel;
import vulc.jlconsole.gfx.panel.GamePanel;
import vulc.jlconsole.gfx.panel.Panel;

public class Cmd {

	private final int background = 0x000000;
	private final int foreground = 0xffffff;

	private final List<String> closedLines = new ArrayList<String>();
	public final List<CmdChar> charBuffer = new ArrayList<CmdChar>();
	private String currentLine = "";

	private int animationTicks = 0;

	private Console console;
	public CmdPanel cmdPanel;

	public Cmd(Console console) {
		this.console = console;
	}

	public void tick() {
		boolean writing = false;
		if(charBuffer.size() != 0) {
			CmdChar character = charBuffer.remove(0);
			receiveInput(character.val, character.writtenByUser);
			writing = true;
		} else {
			animationTicks++;
		}

		console.screen.clear(background);
		for(int i = 0; i <= closedLines.size(); i++) {
			String text;
			if(i != closedLines.size()) {
				text = closedLines.get(i);
			} else {
				text = currentLine;
				if(!writing && animationTicks / 25 % 2 == 1) {
					text += "_";
				}
			}
			console.screen.write(text, foreground, 1, 1 + i * (Screen.FONT.getHeight() + 1));
		}
	}

	public void init() {
		write("Vulc's Java-Lua Console\n");
		write("Copyright 2019 Vulcalien\n");
		write("Version: " + Console.VERSION + "\n");
		write("\n");
	}

	public void receiveInput(char character, boolean shouldExecute) {
		switch(character) {
			case '\n':
				if(shouldExecute) execute(currentLine);
				closedLines.add(currentLine);
				currentLine = "";
				break;

			case '\b':
				if(currentLine.length() > 0) {
					currentLine = currentLine.substring(0, currentLine.length() - 1);
				}
				break;

			default:
				if(character >= 32 && character < 127) {
					currentLine += character;
				}
				break;
		}
	}

	public void write(String line) {
		for(int i = 0; i < line.length(); i++) {
			charBuffer.add(new CmdChar(line.charAt(i), false));
		}
	}

	public void execute(String command) {
		switch(command) {
			case "run":
				Panel gamePanel = new GamePanel(console);
				console.currentPanel = gamePanel;
				gamePanel.init();
				cmdPanel.remove();
				break;

			case "edit":
			case "editor":
				Panel editorPanel = new EditorPanel(console);
				console.currentPanel = editorPanel;
				editorPanel.init();
				cmdPanel.remove();
				break;

			case "cls":
				closedLines.clear();
				currentLine = "";
				break;

			default:
				write("unknown command\n");
				break;
		}
	}

}
