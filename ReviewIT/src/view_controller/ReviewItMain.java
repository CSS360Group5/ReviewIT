package view_controller;

import persistance.RSystem;
import view_controller.console_ui.ConsoleUI;

/**
 * Entry point(main) for our system.
 * @author Dimitar Kumanov
 * @version 4/27/2017
 */
public class ReviewItMain {
	
	
	public static void main(final String[] theArgs){
		(new ConsoleUI()).startConsoleUI();
	}
}
