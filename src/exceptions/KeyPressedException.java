package exceptions;

import framework.WalkableStatus;

public abstract class KeyPressedException extends Exception {
	
	private static final long serialVersionUID = -3930597265069888656L;

	public abstract WalkableStatus getStatus();
}
