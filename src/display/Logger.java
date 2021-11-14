package display;

public class Logger {
	public static final Logger INSTANCE = new Logger();
	
	private boolean showMessages = true;
	
	private Logger() {
		
	}
	
	public void log(String message) {
		System.out.println(message);
	}
	
	void disable() {
		showMessages = false;
	}
	
	void enable() {
		showMessages = true;
	}

}
