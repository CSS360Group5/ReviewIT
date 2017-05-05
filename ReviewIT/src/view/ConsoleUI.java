package view;

import java.util.List;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import controller.RSystem;
import model.UserProfile;
import model.conference.Conference;

/**
 * A console based UI running the ReviewIt internal model.
 * @author Dimitar Kumanov
 * @version 4/27/2017
 *
 */
public class ConsoleUI {
	private final static String CONTINUE_PROMPT = "Press <Enter> to continue...";
	
	//A list of possible screen states:
	private final static int EXIT_PROGRAM = 0;
	private final static int PRELOGIN_SCREEN = 1;
	private final static int LOGIN_SCREEN = 2;
	private final static int NEW_PROFILE_SCREEN = 3;
	private final static int CHOOSE_CONFERENCE_SCREEN = 4;
	private final static int CONFERENCE_SCREEN = 5;
	private final static int SUBMIT_PAPER_SCREEN = 6;
	private final static int ASSIGN_REVIEWER_SCREEN = 7;
	private final static int BAD_INPUT = -10000;
	
	/**
	 * This is the screen our ConsoleUI will start at.
	 */
	private final static int INITIAL_STATE = PRELOGIN_SCREEN;
	
	private final Scanner myScanner;
	
	private UserProfile myCurrentUser;
	
	private Conference myCurrentConference;
	
	/**
	 * A constructor that initializes the program's state.
	 */
	public ConsoleUI(){
		myScanner = new Scanner(System.in);
		myCurrentUser = null;
		myCurrentConference = null;
		initUsersAndConferences();
	}
	
	/**
	 * Main method for controlling the flow of operation of the user.
	 */
	public void startConsoleUI(){
		int programState = INITIAL_STATE;
		final Scanner sc = new Scanner(System.in);
		final PrintStream ps = System.out;
		
		//Main logic loop
		while(programState != EXIT_PROGRAM){
			switch(programState){
			case PRELOGIN_SCREEN:
				programState = prelogin();
				break;
			case LOGIN_SCREEN:
				programState = login();
				break;
			case NEW_PROFILE_SCREEN:
				programState = createProfile();
				break;
			case CHOOSE_CONFERENCE_SCREEN:
				programState = chooseConferenceScreen();
				break;
			case CONFERENCE_SCREEN:
				programState = conferenceScreen();
				break;
			case SUBMIT_PAPER_SCREEN:
				break;
			case ASSIGN_REVIEWER_SCREEN:
				break;
			default:
				printHeader();
				ps.println("Unrecognized option. Please follow prompts.");
				programState = INITIAL_STATE;
				
			}
			
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
		final String loginPrompt = "Please entered your UserID:\nUserID:";
		
		String userInput = "";
		while(true){
			printHeader();
			System.out.println(loginPrompt);
			userInput = myScanner.next();
			
			if(RSystem.getInstance().getUserProfile(userInput) == null){
				
			}else{
				myCurrentUser = RSystem.getInstance().getUserProfile(userInput);
				return CHOOSE_CONFERENCE_SCREEN;
			}
		}
	}

	/**
	 * Handles the creating a new UserProfile screen.
	 * @return the new screen to go to.
	 */
	private int createProfile() {
		final int EXIT_OPTION = 1;
		final String chooseIDPrompt = "Please enter the UserID you wish to use or enter 1 to exit:\nUserID:";
		final String chooseNamePrompt = "Please enter your first and last name or enter 1 to exit:\nName:";
		final String userIDTakenPrompt = "UserID is already taken. Please try again.";
		final String successRegisterPrompt1 = "Successfuly registered as: \n\nUserID: ";
		final String successRegisterPrompt2 = "\nName:";
		printHeader();
		
		flush();
		while(true){
			System.out.print(chooseIDPrompt);
			
			String userInput = myScanner.nextLine();
			try{
				final int chosenOption = Integer.parseInt(userInput);
				if(chosenOption == EXIT_OPTION){
					return PRELOGIN_SCREEN;
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
			userInput = myScanner.nextLine();
			try{
				final int chosenOption = Integer.parseInt(userInput);
				if(chosenOption == EXIT_OPTION){
					return PRELOGIN_SCREEN;
				}
			}
			catch (NumberFormatException nfe) {
				//Didn't choose to exit, don't need to do anything.
			}
			
			final String userName = userInput;
			
			RSystem.getInstance().addUserProfile(new UserProfile(userID, userName));
			
			System.out.println(successRegisterPrompt1 + userID + successRegisterPrompt2 + userName);
			System.out.println(CONTINUE_PROMPT);
			myScanner.nextLine();
			
			return LOGIN_SCREEN;
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
		final String preLoginPrompt = "Please use one of the following options:\n" +
											"1) Login with an existing UserID\n" +
											"2) Create a new Profile\n" + 
											"3) Exit program\n";
		
		String userInput = "";
		int chosenOption = 0;
		
		do{
			printHeader();
			System.out.println(preLoginPrompt);
			userInput = myScanner.next();
			try{
				chosenOption = Integer.parseInt(userInput);
			}
			catch (NumberFormatException nfe) {
				return BAD_INPUT;
			}
			
			switch(chosenOption){
			case LOGIN_OPTION:
				return LOGIN_SCREEN;
			case NEW_PROFILE_OPTION:
				return NEW_PROFILE_SCREEN;
			case EXIT_PROGRAM_OPTION:
				return EXIT_PROGRAM;
			default:
				System.out.println("Unrecognized option. Please follow prompts.");
				break;
			}
		}
		while(true);
	}
	
	/**
	 * Handles the screen for choosing(selecting) a Conference. 
	 * @return the new screen to go to.
	 */
	private int chooseConferenceScreen(){
		printHeader();
		System.out.println("Welcome " + myCurrentUser.getName() + "\n");
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
		printHeader();
		//Finish me...
		return 0;
	}
	
	/**
	 * Flushes extraneous user input. Use generously to ensure System.in reads properly. 
	 */
	private static void flush(){
		try {
			while(System.in.available() != 0)
				System.in.read();
		} catch (IOException e) {
			System.err.println("Something went terribly wrong.\nPlease contact administrator:\"flush() broke\".");
		}
	}
	
	final static int CONSOLE_WIDTH = 80;
	final static int SCREEN_HEIGHT = 160;
	/**
	 * To be used as the beginning each screen.
	 * Should display general information such as:
	 * If user is logged in(myCurrentUser != null) display UserID and name
	 * If Conference selected(myCurrentConference != null) display name of Conference
	 */
	private void printHeader(){
		System.out.println(new String(new char[SCREEN_HEIGHT]).replace("\0", "\n"));
		
		System.out.println(
				new String(new char[32]).replace("\0", " ") + 
				new String(new char[16]).replace("\0", "#") +
				new String(new char[32]).replace("\0", " ")
				);
		System.out.println(
				new String(new char[32]).replace("\0", " ") + 
				new String(new char[4]).replace("\0", "#") +
				"ReviewIT" +
				new String(new char[4]).replace("\0", "#") +
				new String(new char[32]).replace("\0", " ")
				);
		System.out.println(
				new String(new char[32]).replace("\0", " ") + 
				new String(new char[16]).replace("\0", "#") +
				new String(new char[32]).replace("\0", " ")
				);
		System.out.print(new String(new char[3]).replace("\0", "\n"));
		
		
		if(myCurrentUser != null){
			final int gapWidth1 = CONSOLE_WIDTH - 
					myCurrentUser.getName().length() - "UserID:".length();
			System.out.println(
					"UserID:" + 
					new String(new char[gapWidth1]).replace("\0", " ") + 
					"Name:"
			);
			
			final int gapWidth2 = CONSOLE_WIDTH - 
					(myCurrentUser.getUID().length() + myCurrentUser.getName().length());
			System.out.println(
					myCurrentUser.getUID() +
					new String(new char[gapWidth2]).replace("\0", " ") +
					myCurrentUser.getName()
					);
			System.out.println(new String(new char[CONSOLE_WIDTH]).replace("\0", "-"));
		}
		flush();
	}
	
	/**
	 * A temporary method for creating some initial Conferences/UserProfiles we can play with.
	 * Should be replaced by a serialization mechanism which
	 * brings up all the Conference/UserProfiles from the last system execution.
	 */
	private static void initUsersAndConferences(){
		
		final List<String> sampleConferenceNames =
				new ArrayList<>(Arrays.asList(
						"Annual Conference of the European Association for Computer Graphics",
						 "Conference on Animation, Effects, VR, Games and Transmedia",
						 "19th International Conference on Enterprise Information Systems",
						 "3rd International Conference on Information and Communication Technologies for Ageing Well and e-Health",
						 "CHI Conference on Human Factors in Computing Systems",
						 "Western Canadian Conference on Computing Education"));
		final int paperSubmitLimit = 5;
		final int paperAssignLimit = 8;
		
		for(final String currentSampleConferenceName: sampleConferenceNames){
			RSystem.getInstance().addConference(Conference.createConference(currentSampleConferenceName,
														   	   				new Date(),
														   	   				paperSubmitLimit,
														   	   				paperAssignLimit));
		}
		
		final List<UserProfile> sampleUserProfiles =
				new ArrayList<>(Arrays.asList(
						new UserProfile("reviewer@uw.edu", "Reviewer John Doe"),
						new UserProfile("author@uw.edu", "Author John Doe")));
		
		for(final UserProfile currentSampleUserProfile: sampleUserProfiles){
			RSystem.getInstance().addUserProfile(currentSampleUserProfile);
		}
	}
	
}
