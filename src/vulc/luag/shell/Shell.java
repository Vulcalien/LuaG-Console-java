package vulc.luag.shell;

import java.util.ArrayList;
import java.util.List;

import vulc.bitmap.font.Font;
import vulc.luag.Console;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.panel.ShellPanel;
import vulc.luag.shell.command.ShellCommand;

public abstract class Shell {

	private static final int BACKGROUND = 0x000000;
	private static final int FOREGROUND = 0xffffff;

	public static final int HORIZONTAL_CHARS = Console.WIDTH / (Screen.FONT.widthOf(' ') + 1);
	public static final int VERTICAL_LINES = Console.HEIGHT / (Screen.FONT.getHeight() + 1);

	private static final int ANIMATION_DELAY = 25;

	public static final List<ShellChar> CHAR_BUFFER = new ArrayList<ShellChar>();
	public static int scrollBuffer = 0;
	public static boolean pressedUP = false, pressedLEFT = false, pressedDOWN = false, pressedRIGHT = false;

	private static final List<String> CLOSED_TEXT = new ArrayList<String>();
	private static String currentLine = "";
	private static int currentChar = 0;

	private static final List<String> COMMAND_HISTORY = new ArrayList<String>();
	private static int historyPoint = 0;

	private static int renderOffset = 0;
	private static int animationTicks = 0; // cursor animation

	public static ShellPanel panel;

	private Shell() {
	}

	public static void init() {
		write(Console.NAME + "\n");
		write(Console.COPYRIGHT + "\n");
		write("Version: " + Console.VERSION + "\n");
		write("\n");
	}

	public static void tick() {
		if(CHAR_BUFFER.size() != 0) {
			ShellChar character = CHAR_BUFFER.remove(0);
			receiveInput(character.val, character.writtenByUser);

			if(character.writtenByUser) animationTicks = 0;
			else animationTicks = ANIMATION_DELAY;
		} else {
			animationTicks++;
		}

		// history
		byte historyShift = 0;
		if(pressedUP) {
			pressedUP = false;
			historyShift++;
		}
		if(pressedDOWN) {
			pressedDOWN = false;
			historyShift--;
		}

		if(historyShift != 0) {
			int newHistoryPoint = historyPoint + historyShift;

			if(newHistoryPoint >= 0 && newHistoryPoint < COMMAND_HISTORY.size()) {
				historyPoint = newHistoryPoint;

				// most recent command is 0
				currentLine = COMMAND_HISTORY.get(historyPoint);
				currentChar = currentLine.length();
			}
		}

		if(scrollBuffer != 0) {
			int newOffset = renderOffset + scrollBuffer;

			// if is under the bottom then move back to bottom
			int lowestOffset = lowestOffset();
			if(newOffset > lowestOffset) {
				newOffset = lowestOffset;
			}

			// this may be caused by the previous if block
			if(newOffset < 0) newOffset = 0;

			renderOffset = newOffset;
			scrollBuffer = 0;
		}

		if(pressedLEFT) {
			pressedLEFT = false;
			if(currentChar > 0) {
				if(panel.ctrl.isKeyDown()) {
					currentChar = leftWordPosition();
				} else {
					currentChar--;
				}
			}
		}
		if(pressedRIGHT) {
			pressedRIGHT = false;
			if(currentChar < currentLine.length()) {
				if(panel.ctrl.isKeyDown()) {
					currentChar = rightWordPosition();
				} else {
					currentChar++;
				}
			}
		}
		render();
	}

	private static void render() {
		Console.SCREEN.clear(BACKGROUND);

		String textToRender = "";
		for(int i = 0; i < VERTICAL_LINES; i++) {
			int line = i + renderOffset;

			if(line < CLOSED_TEXT.size()) {
				// closed lines always end with '\n'
				textToRender += CLOSED_TEXT.get(line);
			} else {
				String text = currentLine;

				String[] splitCurrent = splitCurrentLine(text);
				for(int a = 0; a < splitCurrent.length; a++) {
					textToRender += splitCurrent[a];

					if(a != splitCurrent.length - 1) {
						textToRender += "\n";
					}
				}
				break;
			}
		}
		Console.SCREEN.write(textToRender, FOREGROUND, 1, 1);

		// draw the cursor
		if(animationTicks / ANIMATION_DELAY % 2 == 0) {
			Font font = Screen.FONT;
			Console.SCREEN.write("_", FOREGROUND,
			                     1 + (font.widthOf(' ') + font.getLetterSpacing())
			                         * (currentChar % HORIZONTAL_CHARS),
			                     1 + (font.getHeight() + font.getLineSpacing())
			                         * (currentChar / HORIZONTAL_CHARS + CLOSED_TEXT.size() - renderOffset));
		}
	}

	public static void receiveInput(char character, boolean shouldExecute) {
		int lowestOffset;
		switch(character) {
			case '\n':
				if(shouldExecute) execute(currentLine);

				String[] splitCurrent = splitCurrentLine(currentLine);
				for(int i = 0; i < splitCurrent.length; i++) {
					// here the text always ends with '\n'
					CLOSED_TEXT.add(splitCurrent[i] + "\n");
				}
				currentLine = "";
				currentChar = 0;

				// if is not at bottom then move to bottom
				lowestOffset = lowestOffset();
				if(renderOffset < lowestOffset) {
					renderOffset = lowestOffset;
				}
				break;

			case '\b':
				if(currentLine.length() > 0 && currentChar > 0) {
					if(panel.ctrl.isKeyDown()) {
						int oldLength = currentLine.length();
						currentLine = currentLine.substring(0, leftWordPosition())
						              + currentLine.substring(currentChar, currentLine.length());

						currentChar -= oldLength - currentLine.length();
					} else {
						currentLine = currentLine.substring(0, currentChar - 1)
						              + currentLine.substring(currentChar, currentLine.length());
						currentChar--;
					}
				}
				break;

			case 127:
				if(currentLine.length() > 0 && currentChar != currentLine.length()) {
					if(panel.ctrl.isKeyDown()) {
						currentLine = currentLine.substring(0, currentChar)
						              + currentLine.substring(rightWordPosition(), currentLine.length());
					} else {
						currentLine = currentLine.substring(0, currentChar)
						              + currentLine.substring(currentChar + 1, currentLine.length());
					}
				}
				break;

			default:
				if(character >= 32 && character < 127) {
					currentLine = currentLine.substring(0, currentChar)
					              + character
					              + currentLine.substring(currentChar, currentLine.length());
					currentChar++;

					// if is not at bottom then move to bottom
					lowestOffset = lowestOffset();
					if(renderOffset < lowestOffset) {
						renderOffset = lowestOffset;
					}
				}
				break;
		}
	}

	public static void write(String text) {
		for(int i = 0; i < text.length(); i++) {
			CHAR_BUFFER.add(new ShellChar(text.charAt(i), false));
		}
	}

	public static void clear() {
		CLOSED_TEXT.clear();
		currentLine = "";
		renderOffset = 0;
	}

	public static void execute(String line) {
		COMMAND_HISTORY.add(0, line);
		historyPoint = -1;

		line = line.trim().replaceAll(" +", " ");
		if(!ShellCommand.execute(line)) {
			write("unknown command\n\n");
		}
	}

	private static String[] splitCurrentLine(String line) {
		int height = (line.length() - 1) / (Shell.HORIZONTAL_CHARS) + 1;

		String[] splitLine = new String[height];
		for(int i = 0; i < height; i++) {
			int start = i * Shell.HORIZONTAL_CHARS;

			int end = start;
			if(i != height - 1) {
				end += Shell.HORIZONTAL_CHARS;
			} else {
				// if length == HORIZONTAL_CHARS the % will be 0
				if(line.length() != 0
				   && line.length() % Shell.HORIZONTAL_CHARS == 0) {
					end += Shell.HORIZONTAL_CHARS;
				} else {
					end += line.length() % (Shell.HORIZONTAL_CHARS);
				}
			}
			splitLine[i] = line.substring(start, end);
		}
		return splitLine;
	}

	private static int lowestOffset() {
		int currentLineHeight = currentLine.length() / HORIZONTAL_CHARS + 1;
		return CLOSED_TEXT.size() - VERTICAL_LINES + currentLineHeight;
	}

	// find left word's position, used for ctrl+left and ctrl+'\b'
	private static int leftWordPosition() {
		int spacePosition = -1;
		boolean foundNonSpace = false;
		for(int i = currentChar - 1; i >= 0; i--) {
			if(currentLine.charAt(i) == ' ') {
				if(foundNonSpace) {
					spacePosition = i;
					break;
				}
			} else {
				foundNonSpace = true;
			}
		}
		return spacePosition + 1;
	}

	// find right word's position, used for ctrl+right and ctrl+del
	private static int rightWordPosition() {
		int nonSpacePosition = currentLine.length();
		boolean foundSpace = false;
		for(int i = currentChar; i < currentLine.length(); i++) {
			if(currentLine.charAt(i) != ' ') {
				if(foundSpace) {
					nonSpacePosition = i;
					break;
				}
			} else {
				foundSpace = true;
			}
		}
		return nonSpacePosition;
	}

}
