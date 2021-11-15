package lineFollower.walker.stateMachine.exceptions;

public class RobotErrorException extends Exception {
	
	private static final long serialVersionUID = 1L;
    
    String message;
    
    public RobotErrorException(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }

}
