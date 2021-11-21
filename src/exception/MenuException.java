package exception;

import framework.WalkableStatus;

public class MenuException extends KeyPressedException {

	private static final long serialVersionUID = -3017626297771393539L;

	@Override
	public WalkableStatus getStatus() {
		return WalkableStatus.MENU;
	}
	
}
