package vulc.jlconsole;

public abstract class Debug {

	@SuppressWarnings("unused")
	private static Console console;

	public static void onStartup() {
		boolean shouldExit = false;
		for(String arg : Console.args) {
			switch(arg) {
			}
		}
		if(shouldExit) System.exit(0);
	}

	public static void init(Console console) {
		Debug.console = console;
	}

	public static void tick() {
	}

}
