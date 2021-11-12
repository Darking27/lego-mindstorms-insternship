package lineFollower.walker.stateMachine;

public class FinishLineException extends Exception {

	private static final long serialVersionUID = 1L;
    
    String message;
    
    public FinishLineException(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
}
