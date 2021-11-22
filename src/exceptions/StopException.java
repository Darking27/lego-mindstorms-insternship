package exceptions;

import framework.WalkableStatus;

public class StopException extends KeyPressedException{

	private static final long serialVersionUID = 3116860073616079122L;
	
	@Override
	public WalkableStatus getStatus() {
		return WalkableStatus.STOP;
	}
	
}
