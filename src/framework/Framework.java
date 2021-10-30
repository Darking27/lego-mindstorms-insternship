package framework;

import java.util.Arrays;
import java.util.List;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;

/**
 * Framework class -- Main Class Start Lejos here to load the Program on the EV3
 *
 * @author Samuel Born
 *
 */
public class Framework {

	/**
	 * sets the order of the obstacles in the parcours
	 */
	static List<ParcoursSection> parcours_section_order = Arrays.asList(ParcoursSection.LINE_FOLLOW,
																		ParcoursSection.BOX_MOVE, 
																		ParcoursSection.BRIDGE, 
																		ParcoursSection.COLOR_SEARCH);

	public static void main(String[] args) {
		Brick brick = BrickFinder.getDefault();
		Key stop_programm_key = brick.getKey("Enter");

		// sets the starting obstacle of the parcours
		int parcours_section_index = 0;

		// TODO: STOP KEY DETECTION DOESNT WORK YET BECAUSE WHILE DOESNT LOOP
		while (!stop_programm_key.isDown()) {
			// starts with the starting index and then follow the order defined in parcours
			// order
			for (int i = parcours_section_index; i < parcours_section_order.size(); i++) {
				parcours_section_order.get(i).start_walking();
				// once the start walking method returns (i.e. the robot has seen a blue stripe
				// go to the next parcours section)
			}
		}
	}

}
