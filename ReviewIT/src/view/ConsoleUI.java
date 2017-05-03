package view;

import java.util.List;
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
	//A list of possible screen states:
	private final static int EXIT_PROGRAM = 0;
	private final static int PRELOGIN_SCREEN = 1;
	private final static int LOGIN_SCREEN = 2;
	private final static int NEW_PROFILE_SCREEN = 3;
	private final static int CHOOSE_CONFERENCE_SCREEN = 4;
	private final static int CONFERENCE_SCREEN = 5;
	private final static int SUBMIT_PAPER_SCREEN = 6;
	private final static int BAD_INPUT = -10000;
	
	/**
	 * This is the screen our ConsoleUI will start at.
	 */
	private final static int INITIAL_STATE = PRELOGIN_SCREEN;
	
	private UserProfile myCurrentUser;
	
	private Conference myCurrentConference;
	
	public ConsoleUI(){
		myCurrentUser = null;
		myCurrentConference = null;
		initUsersAndConferences();
	}
	
	public void startConsoleUI(){
		int programState = INITIAL_STATE;
		final Scanner sc = new Scanner(System.in);
		final PrintStream ps = System.out;
		
		//Main logic loop
		while(programState != EXIT_PROGRAM){
			switch(programState){
			case PRELOGIN_SCREEN:
				programState = prelogin(sc, ps);
				break;
			case LOGIN_SCREEN:
				programState = login(sc, ps);
			case NEW_PROFILE_SCREEN:
				programState = createProfile(sc, ps);
			case CHOOSE_CONFERENCE_SCREEN:
				programState = chooseConferenceScreen(sc, ps);
				break;
			case CONFERENCE_SCREEN:
				programState = conferenceScreen(sc, ps);
				break;
			default:
				ps.println("Unrecognized option. Please follow prompts.");
				programState = INITIAL_STATE;
				
			}
			
			//Makes sure we have everything saved before program closes
			RSystem.serializeModel();
			
			//End of program
		}
	} 
	
	final static int CONSOLE_WIDTH = 80;
	final static int SCREEN_HEIGHT = 160;
	private void printHeader(final PrintStream ps){
		ps.println(new String(new char[SCREEN_HEIGHT]).replace("\0", "\n"));
		
		ps.println(
				new String(new char[32]).replace("\0", " ") + 
				new String(new char[16]).replace("\0", "#") +
				new String(new char[32]).replace("\0", " ")
				);
		ps.println(
				new String(new char[32]).replace("\0", " ") + 
				new String(new char[4]).replace("\0", "#") +
				"ReviewIT" +
				new String(new char[4]).replace("\0", "#") +
				new String(new char[32]).replace("\0", " ")
				);
		ps.println(
				new String(new char[32]).replace("\0", " ") + 
				new String(new char[16]).replace("\0", "#") +
				new String(new char[32]).replace("\0", " ")
				);
		ps.print(new String(new char[3]).replace("\0", "\n"));
		
		
		if(myCurrentUser != null){
			final int gapWidth1 = CONSOLE_WIDTH - 
					myCurrentUser.getName().length() - "UserID:".length();
			ps.println(
					"UserID:" + 
					new String(new char[gapWidth1]).replace("\0", " ") + 
					"Name:"
			);
			
			final int gapWidth2 = CONSOLE_WIDTH - 
					(myCurrentUser.getUID().length() + myCurrentUser.getName().length());
			ps.println(
					myCurrentUser.getUID() +
					new String(new char[gapWidth2]).replace("\0", " ") +
					myCurrentUser.getName()
					);
			ps.println(new String(new char[CONSOLE_WIDTH]).replace("\0", "-"));
		}

	}
	
	private int login(final Scanner sc, final PrintStream ps) {
		final String loginPrompt = "Please entered your UserID:\nUserID:";
		
		String userInput = "";
		while(true){
			printHeader(ps);
			ps.println(loginPrompt);
			userInput = sc.next();
			
			if(RSystem.getInstance().getUserProfile(userInput) == null){
				
			}else{
				myCurrentUser = RSystem.getInstance().getUserProfile(userInput);
				return CHOOSE_CONFERENCE_SCREEN;
			}
		}
	}

	private int createProfile(final Scanner sc, final PrintStream ps) {
		final String chooserUIDPrompt = "Please enter the UserID you wish to use:\nUserID:";
		final String chooserNamePrompt = "Please enter your first and last name:\nName:";
		printHeader(ps);
		
		while(true){
			
			ps.print(chooserUIDPrompt);
			final String userID = sc.next();
			if(RSystem.getInstance().getUserProfile(userID) != null){
				
			}
			
		}
		
	
//		return LOGIN_SCREEN;
	}

	private int prelogin(
			final Scanner sc,
			final PrintStream ps
			){
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
			printHeader(ps);
			ps.println(preLoginPrompt);
			userInput = sc.next();
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
				ps.println("Unrecognized option. Please follow prompts.");
				break;
			}
		}
		while(true);
	}
	

	private int chooseConferenceScreen(
			final Scanner sc,
			final PrintStream ps
			){
		printHeader(ps);
		ps.println("Welcome " + myCurrentUser.getName() + "\n");
		ps.println("Please choose a Conference:");
		List<Conference> conferenceList = RSystem.getInstance().getConferences();
		
		for(int i = 0; i < conferenceList.size(); ++i){
			final Conference currentConference = conferenceList.get(i);
			ps.println(i + ") "+ currentConference.getInfo().getName());
		}
		//myCurrenteConference
		
		return 0;
	}
	
	private int conferenceScreen(
			final Scanner sc,
			final PrintStream ps
			){
		
		printHeader(ps);
		return 0;
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
