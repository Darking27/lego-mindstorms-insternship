package framework;

import java.util.Arrays;
import java.util.List;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.utility.TextMenu;

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
	private static List<ParcoursSection> parcours_section_order = Arrays.asList(ParcoursSection.LINE_FOLLOW,
			ParcoursSection.BOX_MOVE, ParcoursSection.BRIDGE, ParcoursSection.COLOR_SEARCH);

	public static void main(String[] args) {
		Brick brick = BrickFinder.getDefault();
		Key stop_programm_key = brick.getKey("Enter");

		// displays the menu with all entries from Parcours Section
		// when the given element is part of the parcours the correct start index is returned
		// else just the selected section will be walked
		String[] menuItems = new String[ParcoursSection.values().length];
		for (int i = 0; i < ParcoursSection.values().length; i++) {
			menuItems[i] = ParcoursSection.values()[i].name();
		}
		TextMenu textMenu = new TextMenu(menuItems);
		int pressed_index = textMenu.select();
		ParcoursSection start_section = ParcoursSection.valueOf(menuItems[pressed_index]);
		int parcours_section_index = parcours_section_order.indexOf(start_section);
		if(parcours_section_index == -1) { // section not part of the parcours
			start_section.start_walking();
		}

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
