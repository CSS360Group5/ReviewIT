package view_controller.console_ui;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

import model.Paper;
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
	private final static int BAD_INPUT = -10000;
	
	
	
	private final Scanner myScanner;
	
	private final ConsoleState myState;
	
	/**
	 * A constructor that initializes the program's state.
	 */
	public ConsoleUI(){
		myScanner = new Scanner(System.in);
		myScanner.useDelimiter("\\n");
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
			case ConsoleState.USER_HOME_SCREEN:
				newScreen = userHomeScreen();
				break;
			case ConsoleState.VIEW_All_SUBMITTED_PAPERS_SCREEN:
				newScreen = viewAllSubmittedPapersScreen();
				break;
			case ConsoleState.VIEW_All_ASSIGNED_PAPERS_SCREEN:
				newScreen = viewAllAssignedPapersScreen();
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
		}
		//End of program here:
		
		//Makes sure we have everything "saved" (as in serialized) before program closes
		RSystem.serializeModel();
		
		ConsoleUtility.showMessageToUser(myScanner, "Exiting Program!");
	}
	
	private int viewAllAssignedPapersScreen() {
		// TODO Auto-generated method stub
		return 0;
	}

	private int viewAllSubmittedPapersScreen() {
		ConsoleUtility.printHeader(myState);
		System.out.println("All submitted papers:\n");
		int paperNum = 0;
		for(final Paper currentPaper: RSystem.getInstance().getAllPapersSubmitted(myState.getCurrentUser())){
			++paperNum;
			System.out.println(
				String.valueOf(paperNum) + ") " +
				"Title: \"" + currentPaper.getTitle() + "\"\n" +
				"Authors: " + currentPaper.getAuthors() + "\n" +
				"Paper's File: " + currentPaper.getPaperFile() + "\n" +
				"Submitted on: " + currentPaper.getSubmitDate() + "\n" +
				"Submitted by: \"" + currentPaper.getSubmitterUserProfile().getUID() + "\"\n"
			);
		}
		
		ConsoleUtility.showMessageToUser(myScanner, "");
		
		return ConsoleState.USER_HOME_SCREEN;
	}

	private int userHomeScreen() {
		final int VIEW_ALL_MY_SUBMITTED_PAPERS_OPTION = 1;
		final int VIEW_ALL_MY_ASSIGNED_PAPERS_OPTION = 2;
		final int SELECT_CONFERENCE_OPTION = 3;
		final int SIGN_OUT_OPTION = 4;
		final int EXIT_PROGRAM_OPTION = 5;
		final String inputPrompt = "Please use one of the following options:\n" +
				"1) View all my submitted papers.\n" +
				"2) View all my assigned papers(for review).\n" + 
				"3) Select a conference.\n" +
				"4) Sign out.\n" + 
				"5) Exit.\n";
		final int chosenOption = ConsoleUtility.inputNumberedOptions(
				myScanner, myState, ConsoleUtility.createConsecutiveList(1, 5), inputPrompt);
	 	
		switch(chosenOption){
		case VIEW_ALL_MY_SUBMITTED_PAPERS_OPTION:
			return ConsoleState.VIEW_All_SUBMITTED_PAPERS_SCREEN;
		case VIEW_ALL_MY_ASSIGNED_PAPERS_OPTION:
			return ConsoleState.VIEW_All_ASSIGNED_PAPERS_SCREEN;
		case SELECT_CONFERENCE_OPTION:
			return ConsoleState.CHOOSE_CONFERENCE_SCREEN;
		case SIGN_OUT_OPTION:
			myState.setCurrentUser(null);
			ConsoleUtility.showMessageToUser(myScanner, "You have signed out successfuly!");
			return ConsoleState.PRELOGIN_SCREEN;
		case EXIT_PROGRAM_OPTION:
			return ConsoleState.EXIT_PROGRAM;
		}
		
		
		return 0;
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
		
		final UserProfile selectedProfile = ConsoleUtility.inputExistingUserProfile(myScanner, myState, loginHeader, inputPrompt, noSuchUserPrompt);
		if(selectedProfile == null){
			return ConsoleState.PRELOGIN_SCREEN;
		}
		myState.setCurrentUser(selectedProfile);
		return ConsoleState.USER_HOME_SCREEN;
	}

	/**
	 * Handles the creating a new UserProfile screen.
	 * @return the new screen to go to.
	 */	private int createProfile() {
		final String chooseIDPrompt = "Please enter the UserID you wish to use or enter 1 to exit:\nUserID:";
		final String chooseNamePrompt = "Please enter your first and last name or enter 1 to exit:\nName:";
		final String userIDTakenPrompt = "UserID is already taken. Please try again.";
		final String successRegisterPrompt1 = "Successfuly registered as: \n\nUserID: \"";
		final String successRegisterPrompt2 = "\"\nName: \"";
		ConsoleUtility.printHeader(myState);
		
		final UserProfile inputtedUserProfile = ConsoleUtility.inputNewUserPofile(myScanner, myState, chooseIDPrompt, userIDTakenPrompt, chooseNamePrompt);
		if(inputtedUserProfile != null){
			ConsoleUtility.showMessageToUser(myScanner, successRegisterPrompt1 + inputtedUserProfile.getUID() +
					successRegisterPrompt2 + inputtedUserProfile.getName() + "\"\n");
			return ConsoleState.LOGIN_SCREEN;
		}else{
			//They wanted to exit of of inputting userProfile
			return ConsoleState.PRELOGIN_SCREEN;
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
		
		final int chosenOption = ConsoleUtility.inputNumberedOptions(
				myScanner, myState, ConsoleUtility.createConsecutiveList(1, 3), inputPrompt);
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
		final StringBuilder promptBuilder = new StringBuilder();
		promptBuilder.append("Please choose a Conference:\n");
		List<Conference> conferenceList = RSystem.getInstance().getConferences();
		
		int i = 0;
		for(; i < conferenceList.size(); ++i){
			final Conference currentConference = conferenceList.get(i);
			promptBuilder.append(i + ") "+ currentConference.getInfo().getName() + "\n");
		}
		
		final int GO_BACK_OPTION = i;
		promptBuilder.append(GO_BACK_OPTION + ") Back to profile screen.\n");
		final int EXIT_PROGRAM_OPTION = i + 1;
		promptBuilder.append(EXIT_PROGRAM_OPTION + ") Exit Program.\n");
		
		final int chosenOption = ConsoleUtility.inputNumberedOptions(
				myScanner, myState, ConsoleUtility.createConsecutiveList(1, EXIT_PROGRAM_OPTION), promptBuilder.toString());
		
		if(chosenOption == GO_BACK_OPTION){
			return ConsoleState.USER_HOME_SCREEN;
		}
		else if(chosenOption == EXIT_PROGRAM_OPTION){
			return ConsoleState.EXIT_PROGRAM;
		}else{ //They chose a conference:
			myState.setCurrentConference(conferenceList.get(chosenOption));
			return ConsoleState.CONFERENCE_SCREEN;
		}
	}
	
	/**
	 * A screen that displays all the functionality
	 * the UserProfile has related to the currentConference.
	 * @return the new screen to go to.
	 */
	private int conferenceScreen(){
		ConsoleUtility.printHeader(myState);
		final int SUBMIT_PAPER_OPTION = 1;
		final int VIEW_SUBMITTED_PAPERS = 2;
		final int ASSIGN_REVIEWER = 4;
		
//		final int SUBMIT_PAPER_OPTION = 1;
//		final int VIEW_SUBMITTED_PAPERS = 2;
//		final int EXIT_PROGRAM_OPTION = 3;
//		final String inputPrompt = "Please use one of the following options:\n" +
//											"1) Login with an existing UserID\n" +
//											"2) Create a new Profile\n" + 
//											"3) Exit program\n";
//		
//		final int chosenOption = ConsoleUtility.inputNumberedOptions(
//				myScanner, myState, 1, 3, inputPrompt);
//		switch(chosenOption){
//		case SUBMIT_PAPER_OPTION:
//			return ConsoleState.LOGIN_SCREEN;
//		case NEW_PROFILE_OPTION:
//			return ConsoleState.NEW_PROFILE_SCREEN;
//		case EXIT_PROGRAM_OPTION:
//			return ConsoleState.EXIT_PROGRAM;
//		default:
//			return BAD_INPUT;
//		}
		return 0;
	}
	
}
