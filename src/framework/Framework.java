package framework;

import java.util.Arrays;
import java.util.List;

import exceptions.KeyPressedException;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.utility.Delay;
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
	private static List<ParcoursSection> parcours_section_order = Arrays.asList(ParcoursSection.LINE_FOLLOW, ParcoursSection.LINE_BOX_TRANSITIONER,
			ParcoursSection.BOX_MOVE, ParcoursSection.BRIDGE, ParcoursSection.MARKER_SEARCHER);
	private static Brick brick = BrickFinder.getDefault();

	public static void main(String[] args) {
		WalkableStatus returnStatus = WalkableStatus.STARTING;

		while (returnStatus != WalkableStatus.STOP) {
			// int start_index = chooseParcoursSection();
			ParcoursSection start_section = chooseParcoursSection();

			if (start_section == null)
				return;

			int start_index = parcours_section_order.indexOf(start_section);
			if (start_index == -1) {
				try {
					returnStatus = start_section.start_walking();
				} catch (KeyPressedException e) {
					if (e.getStatus() == WalkableStatus.STOP)
						return;
					RobotUtils.stopMotors();
					continue;
				}
				return;
			}

			sectionsIterate: for (int i = start_index; i < parcours_section_order.size(); i++) {
				while (Ports.ENTER.isDown()) {
					if (Ports.ESCAPE.isDown()) {
						return;
					}
				}
				try {
					returnStatus = parcours_section_order.get(i).start_walking();
				} catch (KeyPressedException e) {
					returnStatus = e.getStatus();
					System.out.println(returnStatus);
				}

				RobotUtils.stopMotors();

				switch (returnStatus) {
				case MENU:
					break sectionsIterate;
				case STOP:
					return; // stop entire programm
				default:
					break; // do nothing for finished and jump to next for loop iteration
				}
			}
		}
	}

	/**
	 * @return the index of the choosen Parcours Section
	 * 
	 *         so if the parcours is LINE -> BOX -> BRIDGE and the user chooses BOX
	 *         this method returns 1
	 */
	private static ParcoursSection chooseParcoursSection() {
		// displays the menu with all entries from parcours Section
		// when the given element is part of the parcours the correct start
		// index is returned

		GraphicsLCD display = brick.getGraphicsLCD();
		display.clear();
		for (int i = 0; i < 20; i++) {
			System.out.println(); // clear screen
		}

		String[] menuItems = new String[ParcoursSection.values().length];
		for (int i = 0; i < ParcoursSection.values().length; i++) {
			menuItems[i] = ParcoursSection.values()[i].name();
		}

		TextMenu textMenu = new TextMenu(menuItems);
		int pressed_index = textMenu.select();

		if (pressed_index == -1)
			return null;

		ParcoursSection start_section = ParcoursSection.valueOf(menuItems[pressed_index]);

		display.clear();

		return start_section;
		// return parcours_section_order.indexOf(start_section);

	}
}
