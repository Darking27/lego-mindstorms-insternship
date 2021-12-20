package bridgeFollower;

import bridgeFollower.states.State;
import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.WalkableStatus;

public class BridgeFollowerV4 implements ParcoursWalkable {

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		Ports.ULTRASONIC_MOTOR.setSpeed(80);
		Ports.ULTRASONIC_MOTOR.rotateTo(-85);
		
		State active = State.START;
		while (active != State.TUNNEL_FINDER) {
			if (active == State.FINISH) {
				return WalkableStatus.FINISHED;
			}
			active = active.handleState();
		}
		
		return new TunnelFinder().start_walking();
	}

}
