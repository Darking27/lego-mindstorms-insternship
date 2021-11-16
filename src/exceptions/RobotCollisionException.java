package exceptions;

public class RobotCollisionException extends Exception {

    private static final long serialVersionUID = 1L;
    
    String message;
    
    public RobotCollisionException(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
}
