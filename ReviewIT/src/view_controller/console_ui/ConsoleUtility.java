package view_controller.console_ui;

import model.UserProfile;
import model.conference.Conference;
import persistance.RSystem;

import java.io.IOException;
import java.util.*;

/**
 * A utility class containing static helper methods for implementing a Console UI.
 * @author Dimitar Kumanov
 * @version 5/4/2017
 */
public class ConsoleUtility {
	
	public final static String EXIT_INPUTTING = "\b\b\b\b";
	
	private final static int CONSOLE_WIDTH = 80;
	private final static int SCREEN_HEIGHT = 160;

	/**
	 * 
	 * @param theScanner
	 * @param theState
	 * @param theFirstOption
	 * @param theLastOption
	 * @param theInputPrompt
	 * @return
	 */
	public static int inputNumberedOptions(
			final Scanner theScanner,
			final ConsoleState theState,
			final int theFirstOption,
			final int theLastOption,
			final String theInputPrompt,
			final String theInvalidOptionPrompt
			){
		
		boolean isInvalidInput = false;
		while(true){
			ConsoleUtility.printHeader(theState);
			if(isInvalidInput){
				System.out.println(theInvalidOptionPrompt);
				isInvalidInput = false;
			}
			System.out.println(theInputPrompt);
			flush();
			final String userInput = theScanner.next();
			int chosenOption;
			try{
				chosenOption = Integer.parseInt(userInput);
				if(chosenOption < theFirstOption || chosenOption > theLastOption){
					isInvalidInput = true;
				}else{
					return chosenOption;
				}
			}
			catch (final NumberFormatException nfe) {
				//HANDLE UNPARSABLE STRING
			}
		}
	}
	
	public static UserProfile inputExistingUserID(
			final Scanner theScanner,
			final ConsoleState theState,
			final String theAdditionalHeader,
			final String theInputPrompt,
			final String theNoSuchUserPrompt
			){
//		final String loginPrompt = "Please entered your UserID:\nUserID:";
		
		String userInput = "";
		boolean isNoSuchUser = false;
		
		while(true){
			ConsoleUtility.printHeader(theState);
			System.out.println(theAdditionalHeader);
			if(isNoSuchUser){
				System.out.println(String.format(theNoSuchUserPrompt, userInput));
				isNoSuchUser = false;
			}
			System.out.print(theInputPrompt);
			flush();
			userInput = theScanner.next();
			
			if(RSystem.getInstance().getUserProfile(userInput) == null){
				isNoSuchUser = true;
			}else{
				return RSystem.getInstance().getUserProfile(userInput);
			}
		}
	}
	
	public static UserProfile inputNewUserID(
			final Scanner theScanner,
			final ConsoleState theState,
			final String inputPrompt,
			final String userTakenPrompt
			){
		return null;
	}
	
	
	public static String inputNonEmptyString(final String thePrompt){
		return "";
	}
	
	/**
	 * Flushes extraneous user input.
	 * Use generously to ensure System.in reads properly. 
	 */
	public static void flush(){
		try {
			while(System.in.available() != 0)
				System.in.read();
		} catch (IOException e) {
			System.out.println("Something went t`ibly wrong.\nPlease contact administrator:\"flush() broke\".");
		}
	}
	
	/**
	 * To be used as the beginning each screen.
	 * Should display general information such as:
	 * If user is logged in(myCurrentUser != null) display UserID and name
	 * If Conference selected(myCurrentConference != null) display name of Conference
	 */
	public static void printHeader(final ConsoleState theState){
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
		
		
		if(theState.getCurrentUser() != null){
			final int gapWidth1 = CONSOLE_WIDTH - 
					theState.getCurrentUser().getName().length() - "UserID:".length();
			System.out.println(
					"UserID:" + 
					new String(new char[gapWidth1]).replace("\0", " ") + 
					"Name:"
			);
			
			final int gapWidth2 = CONSOLE_WIDTH - 
					(theState.getCurrentUser().getUID().length() + theState.getCurrentUser().getName().length());
			System.out.println(
					theState.getCurrentUser().getUID() +
					new String(new char[gapWidth2]).replace("\0", " ") +
					theState.getCurrentUser().getName()
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
	public static void initUsersAndConferences(){

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
