package framework;

import exception.KeyPressedException;

/**
 * Interface for all Parcours Walkers (LineFollower, etc)
 * 
 * The start walking method starts the action
 * 
 * Strategy Design Pattern
 *
 * @author Samuel Born
 *
 */
public interface ParcoursWalkable {
	public WalkableStatus start_walking() throws KeyPressedException;
}
