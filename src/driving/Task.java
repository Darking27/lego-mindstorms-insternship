package driving;

public interface Task {
	
	/**
	 * This method should be called in the loop. It returns immediately.
	 * 
	 * @return whether the Task finished
	 */
	boolean run();
	
	/**
	 * This method should be called when the Task did not finish, but should not be run again.
	 * If this method is not called necessary actions such as stopping motors cannot be performed
	 */
	void cancel();

}
