package view_controller.console_ui;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.IllegalOperationException;
import model.Paper;
import model.UserProfile;
import model.conference.Conference;
import persistance.RSystem;

/**
 * A utility class containing static helper methods for implementing a Console UI.
 * @author Dimitar Kumanov
 * @version 5/4/2017
 */
public class ConsoleUtility {
	
	public static final int EXIT_OPTION = 1;
	
	private static final String INVALID_OPTION_PROMPT = "Unrecognized option. Please follow prompts.";
	
	private final static int CONSOLE_WIDTH = 80;
	private final static int SCREEN_HEIGHT = 160;

	private final static String CONTINUE_PROMPT = "Press <Enter> to continue...";
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
			final List<Integer> validOptions,
			final String theInputPrompt
			){
		
		boolean isInvalidInput = false;
		while(true){
			ConsoleUtility.printHeader(theState);
			if(isInvalidInput){
				System.out.println(INVALID_OPTION_PROMPT);
				isInvalidInput = false;
			}
			System.out.println(theInputPrompt);
			flush();
			final String userInput = theScanner.nextLine();
			int chosenOption;
			try{
				chosenOption = Integer.parseInt(userInput);
				if(!validOptions.contains(chosenOption)) {
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
	
	public static UserProfile inputExistingUserProfile(
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
			userInput = theScanner.nextLine();
			try{
				final int chosenOption = Integer.parseInt(userInput);
				if(chosenOption == EXIT_OPTION){
					return null;
				}
			}
			catch (NumberFormatException nfe) {
				//Didn't choose to exit, don't need to do anything.
			}
			
			
			
			if(RSystem.getInstance().getUserProfile(userInput) == null){
				isNoSuchUser = true;
			}else{
				return RSystem.getInstance().getUserProfile(userInput);
			}
		}
	}
	
	/**
	 * Returns null if user wanted to exit out.
	 */
	public static UserProfile inputNewUserPofile(
			final Scanner theScanner,
			final ConsoleState theState,
			final String theChooseIDPrompt,
			final String theIDTakenPrompt,
			final String theChooseNamePrompt
			){
		
		
		while(true){
			System.out.print(theChooseIDPrompt);
			ConsoleUtility.flush();
			String userInput = theScanner.nextLine();
			try{
				final int chosenOption = Integer.parseInt(userInput);
				if(chosenOption == EXIT_OPTION){
					return null;
				}
			}
			catch (NumberFormatException nfe) {
				//Didn't choose to exit, don't need to do anything.
			}
			if(RSystem.getInstance().getUserProfile(userInput) != null){
				System.out.println(theIDTakenPrompt);
				continue;
			}
			
			//If we get here we can use the input as ID
			final String userID = userInput;
			
			System.out.print(theChooseNamePrompt);
			ConsoleUtility.flush();
			userInput = theScanner.nextLine();
			
			try{
				final int chosenOption = Integer.parseInt(userInput);
				if(chosenOption == EXIT_OPTION){
					return null;
				}
			}
			catch (NumberFormatException nfe) {
				//Didn't choose to exit, don't need to do anything.
			}
			
			final String userName = userInput;
			
			final UserProfile createdProfile = new UserProfile(userID, userName);
			RSystem.getInstance().addUserProfile(createdProfile);
			return createdProfile;

		}
	}
	
	/**
	 * Returns null if user wanted to exit out.
	 */
	public File inputFile(
			final Scanner theScanner,
			final ConsoleState theState,
			final String theInputPrompt,
			final String theFileNotExistPrompt,
			final String theWrongExtensionPrompt){
		
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
		
		
		if(theState.getCurrentConference() != null){
			System.out.println(
					"Conference:\n" + 
					theState.getCurrentConference().getInfo().getName() + "\n"
					);
			System.out.println(
					"Role(s):\n" + 
					theState.getCurrentConference().getInfo().getUserRoles(theState.getCurrentUser()) + "\n"
					);
		}			
		
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
	
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		for(final String currentSampleConferenceName: sampleConferenceNames){
			try {
				if(currentSampleConferenceName.contains("Annual Conference of the European")){
					RSystem.getInstance().addConference(
							Conference.createConference(
									currentSampleConferenceName,
									format.parse("2017/04/19 23:59:59"),
									paperSubmitLimit,
									paperAssignLimit
									)
							);
				}else{
					RSystem.getInstance().addConference(
							Conference.createConference(
									currentSampleConferenceName,
									format.parse("2017/05/20 23:59:59"),
									paperSubmitLimit,
									paperAssignLimit
									)
							);
				}
				
				
			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
			} catch (ParseException e) {
//				e.printStackTrace();
			}
		}
		
		final UserProfile sampleAuthorUser = new UserProfile("author@uw.edu", "Author John Doe"); 
		
		final List<UserProfile> sampleUserProfiles =
				new ArrayList<>(Arrays.asList(
						new UserProfile("reviewer@uw.edu", "Reviewer John Doe"),
						sampleAuthorUser));
		
		for(final UserProfile currentSampleUserProfile: sampleUserProfiles){
			RSystem.getInstance().addUserProfile(currentSampleUserProfile);
		}
		
		final Paper samplePaper = Paper.createPaper(
				new File(""),
				new ArrayList<>(Arrays.asList("Xu Zhuang", "Yan Zhu", "Chin-Chen Chang", "Qiang Peng")),
				"Feature bundling in decision tree algorithm.",
				sampleAuthorUser
				);
		try {
			RSystem.getInstance().getConference("Western Canadian Conference on Computing Education").getUserRole().addPaper(sampleAuthorUser, samplePaper);
		} catch (IllegalOperationException e) {
			System.err.println(e.getMessage());
//			e.printStackTrace();
		}
	}
	
	public static List<Integer> createConsecutiveList(final int from, final int to){
		final List<Integer> listResult = new ArrayList<>();
		for(int i = from; i <= to; ++i){
			listResult.add(i);
		}
		return listResult;
	}
	
	
	public static void showMessageToUser(
			final Scanner theScanner,
			final String theMessage
			){
		System.out.println(theMessage);
		System.out.println(CONTINUE_PROMPT);
		ConsoleUtility.flush();
		theScanner.nextLine();
	}
	
	public static void signOut(
			final Scanner theScanner,
			final ConsoleState theState
			){
		theState.setCurrentUser(null);
		theState.setCurrentConference(null);
		ConsoleUtility.showMessageToUser(theScanner, "You have signed out successfuly!");
	}
}
