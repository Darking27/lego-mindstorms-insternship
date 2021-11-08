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
		Key menu_key = brick.getKey("Escape");
		
		WalkerThread walkerThread = new WalkerThread(chooseParcoursSection());
		walkerThread.start();

		while (!stop_programm_key.isDown()) {
			if (menu_key.isDown()) {
				walkerThread.stop();
				walkerThread = new WalkerThread(chooseParcoursSection());
				walkerThread.start();
			}
		}
	}

	/**
	 * @return the index of the choosen Parcours Section
	 * 
	 * so if the parcours is LINE -> BOX -> BRIDGE
	 * and the user chooses BOX
	 * this method returns 1
	 */
	private static int chooseParcoursSection() {
		// displays the menu with all entries from parcours Section
		// when the given element is part of the parcours the correct start 
		// index is returned
		String[] menuItems = new String[ParcoursSection.values().length];
		for (int i = 0; i < ParcoursSection.values().length; i++) {
			menuItems[i] = ParcoursSection.values()[i].name();
		}
		TextMenu textMenu = new TextMenu(menuItems);
		int pressed_index = textMenu.select();
		ParcoursSection start_section = ParcoursSection.valueOf(menuItems[pressed_index]);
		return parcours_section_order.indexOf(start_section);
		
	}

	public static class WalkerThread extends Thread {
		int parcours_section_index;

		public WalkerThread(int parcours_section_index) {
			this.parcours_section_index = parcours_section_index;
		}

		@Override
		public void run() {
			for (int i = parcours_section_index; i < parcours_section_order.size(); i++) {
				parcours_section_order.get(i).start_walking();
				// once the start walking method returns (i.e. the robot has seen a blue stripe
				// go to the next parcours section)
			}
		}
	}
}
