package bridgeFollower.states;

import exceptions.KeyPressedException;

public enum State {
	FIND_LEFT(new FindLeftState()),
	FOLLOW_LEFT(new FollowLeftState()),
	TUNNEL_FINDER(new TunnelFinderState()),
	DRIVE_STRAIT(new DriveStraitState()),
	TURN_LEFT_1(new TurnLeft1State()),
	TURN_LEFT_2(new TurnLeft2State()),
	FOLLOW_TOP(new FollowTopState()),
	PARALLEL_DRIVE_RIGHT(new ParallelDriveRightState());
	
	
	private BaseState state;
	State(BaseState state) {
		this.state = state;
	}
	
	public State handleState() throws KeyPressedException {
		return state.handleState();
	}

}
