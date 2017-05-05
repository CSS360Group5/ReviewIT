package view_controller.console_ui;

import java.util.List;
import java.util.Scanner;

import model.UserProfile;
import model.conference.Conference;
import persistance.RSystem;

/**
 * A console based UI running the ReviewIt internal model.
 * @author Dimitar Kumanov
 * @version 4/27/2017
 *
 */
public class ConsoleUI {
	private final static String CONTINUE_PROMPT = "Press <Enter> to continue...";

	private final static int BAD_INPUT = -10000;
	
	private final Scanner myScanner;
	
	private final ConsoleState myState;
	
	/**
	 * A constructor that initializes the program's state.
	 */
	public ConsoleUI(){
		myScanner = new Scanner(System.in);
		myState = new ConsoleState();
		ConsoleUtility.initUsersAndConferences();
	}
	
	/**
	 * Main method for controlling the flow of operation of the user.
	 */
	public void run(){
		//Main logic loop
		while(myState.getCurrentScreen() != ConsoleState.EXIT_PROGRAM){
			final int newScreen;
			
			switch(myState.getCurrentScreen()){
			case ConsoleState.PRELOGIN_SCREEN:
				newScreen = prelogin();
				break;
			case ConsoleState.LOGIN_SCREEN:
				newScreen = login();
				break;
			case ConsoleState.NEW_PROFILE_SCREEN:
				newScreen = createProfile();
				break;
			case ConsoleState.CHOOSE_CONFERENCE_SCREEN:
				newScreen = chooseConferenceScreen();
				break;
			case ConsoleState.CONFERENCE_SCREEN:
				newScreen = conferenceScreen();
				break;
			case ConsoleState.SUBMIT_PAPER_SCREEN:
				newScreen = ConsoleState.STARTING_SCREEN; //TEMP,should = method call
				break;
			case ConsoleState.ASSIGN_REVIEWER_SCREEN:
				newScreen = ConsoleState.STARTING_SCREEN; //TEMP,should = method call
				break;
			default:
				ConsoleUtility.printHeader(myState);
				System.out.println("Unrecognized option. Please follow prompts.");
				newScreen = ConsoleState.STARTING_SCREEN;
				break;
			}
			myState.setCurrentScreen(newScreen);
			
			//End of program here:
			
			//Makes sure we have everything "saved" (as in serialized) before program closes
			RSystem.serializeModel();
		}
	}
	
	/**
	 * Handles the login screen.
	 * @return the new screen to go to.
	 */
	private int login() {
		final String loginHeader = new String(new char[30]).replace("\0", " ") +
				"Sign In to ReviewIT\n\n\n\n";
		final String inputPrompt =  "Please entered your UserID:\nUserID:";
		final String noSuchUserPrompt = "Sorry no UserID \"%s\" found in the System.\nPlease try again.\n";
		final UserProfile selectedProfile = ConsoleUtility.inputExistingUserID(myScanner, myState, loginHeader, inputPrompt, noSuchUserPrompt);
		if(selectedProfile == null){
			return ConsoleState.PRELOGIN_SCREEN;
		}
		myState.setCurrentUser(selectedProfile);
		return ConsoleState.CHOOSE_CONFERENCE_SCREEN;
	}

	/**
	 * Handles the creating a new UserProfile screen.
	 * @return the new screen to go to.
	 */	private int createProfile() {
		final int EXIT_OPTION = 1;
		final String chooseIDPrompt = "Please enter the UserID you wish to use or enter 1 to exit:\nUserID:";
		final String chooseNamePrompt = "Please enter your first and last name or enter 1 to exit:\nName:";
		final String userIDTakenPrompt = "UserID is already taken. Please try again.";
		final String successRegisterPrompt1 = "Successfuly registered as: \n\nUserID: ";
		final String successRegisterPrompt2 = "\nName:";
		ConsoleUtility.printHeader(myState);
		
		while(true){
			System.out.print(chooseIDPrompt);
			ConsoleUtility.flush();
			String userInput = myScanner.nextLine();
			try{
				final int chosenOption = Integer.parseInt(userInput);
				if(chosenOption == EXIT_OPTION){
					return ConsoleState.PRELOGIN_SCREEN;
				}
			}
			catch (NumberFormatException nfe) {
				//Didn't choose to exit, don't need to do anything.
			}
			if(RSystem.getInstance().getUserProfile(userInput) != null){
				System.out.println(userIDTakenPrompt);
				continue;
			}
			
			//If we get here we can use the input as ID
			final String userID = userInput;
			
			System.out.print(chooseNamePrompt);
			ConsoleUtility.flush();
			userInput = myScanner.nextLine();
			try{
				final int chosenOption = Integer.parseInt(userInput);
				if(chosenOption == EXIT_OPTION){
					return ConsoleState.PRELOGIN_SCREEN;
				}
			}
			catch (NumberFormatException nfe) {
				//Didn't choose to exit, don't need to do anything.
			}
			
			final String userName = userInput;
			
			RSystem.getInstance().addUserProfile(new UserProfile(userID, userName));
			
			System.out.println(successRegisterPrompt1 + userID + successRegisterPrompt2 + userName);
			System.out.println(CONTINUE_PROMPT);
			ConsoleUtility.flush();
			myScanner.nextLine();
			
			return ConsoleState.LOGIN_SCREEN;
		}
	}

	/**
	 * Handles the prelogin screen
	 * This includes choosing between:
	 * 1) Logging in with an existing UserProfile
	 * 2) Creating a new UserProfile
	 * 3) Exiting out of the program.
	 * @return the new screen to go to.
	 */
	private int prelogin(){
		final int LOGIN_OPTION = 1;
		final int NEW_PROFILE_OPTION = 2;
		final int EXIT_PROGRAM_OPTION = 3;
		final String inputPrompt = "Please use one of the following options:\n" +
											"1) Login with an existing UserID\n" +
											"2) Create a new Profile\n" + 
											"3) Exit program\n";
		final String invalidOptionPrompt = "Unrecognized option. Please follow prompts.";
		
		final int chosenOption = ConsoleUtility.inputNumberedOptions(
				myScanner, myState, LOGIN_OPTION, EXIT_PROGRAM_OPTION, inputPrompt, invalidOptionPrompt);
		switch(chosenOption){
		case LOGIN_OPTION:
			return ConsoleState.LOGIN_SCREEN;
		case NEW_PROFILE_OPTION:
			return ConsoleState.NEW_PROFILE_SCREEN;
		case EXIT_PROGRAM_OPTION:
			return ConsoleState.EXIT_PROGRAM;
		default:
			return BAD_INPUT;
		}
	}
	
	/**
	 * Handles the screen for choosing(selecting) a Conference. 
	 * @return the new screen to go to.
	 */
	private int chooseConferenceScreen(){
		ConsoleUtility.printHeader(myState);
		System.out.println("Welcome " + myState.getCurrentUser().getName() + "\n");
		System.out.println("Please choose a Conference:");
		List<Conference> conferenceList = RSystem.getInstance().getConferences();
		
		for(int i = 0; i < conferenceList.size(); ++i){
			final Conference currentConference = conferenceList.get(i);
			System.out.println(i + ") "+ currentConference.getInfo().getName());
		}
		
		
		//myCurrenteConference
		
		return 0;
	}
	
	/**
	 * A screen that displays all the functionality
	 * the UserProfile has related to the currentConference.
	 * @return the new screen to go to.
	 */
	private int conferenceScreen(){
		ConsoleUtility.printHeader(myState);
		//Finish me...
		return 0;
	}
	
}
