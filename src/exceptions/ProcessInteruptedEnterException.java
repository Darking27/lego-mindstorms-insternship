package exceptions;

public class ProcessInteruptedEnterException extends Exception {

    private static final long serialVersionUID = 1L;
    
    String message;
    
    public ProcessInteruptedEnterException(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }

}
