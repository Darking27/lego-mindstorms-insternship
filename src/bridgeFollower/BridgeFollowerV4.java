package bridgeFollower;

import bridgeFollower.states.State;
import exceptions.KeyPressedException;
import framework.ParcoursWalkable;
import framework.WalkableStatus;

public class BridgeFollowerV4 implements ParcoursWalkable {

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		State active = State.FIND_LEFT;
		while (active != State.TUNNEL_FINDER) {
			active = active.handleState();
		}
		
		return new TunnelFinder().start_walking();
	}

}
