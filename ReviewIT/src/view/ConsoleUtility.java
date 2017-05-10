package view;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import model.Conference;
import model.IllegalOperationException;
import model.Paper;
import model.ConferenceSystem;
import model.UserProfile;

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
			
			
			
			if(ConferenceSystem.getInstance().getUserProfile(userInput) == null){
				isNoSuchUser = true;
			}else{
				return ConferenceSystem.getInstance().getUserProfile(userInput);
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
			if(ConferenceSystem.getInstance().getUserProfile(userInput) != null){
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
			ConferenceSystem.getInstance().addUserProfile(createdProfile);
			return createdProfile;

		}
	}
	
	/**
	 * Returns null if user wanted to exit out.
	 */
	public static File inputFile(
			final Scanner theScanner,
			final ConsoleState theState,
			final String theInputPrompt,
			final String theFileNotExistPrompt,
			final String theWrongExtensionPrompt){
		
		boolean fileDoesntExist = false;
		boolean fileWrongExtension = false;
		
		
		while(true){
			if(fileDoesntExist){
				fileDoesntExist = false;
				System.out.print(theFileNotExistPrompt);
			}else if(fileWrongExtension){
				fileWrongExtension = false;
				System.out.print(theWrongExtensionPrompt);
			}
			
			System.out.print(theInputPrompt);
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
			
			final File inputtedFile = new File(userInput);
			
			if(!inputtedFile.exists()){
				fileDoesntExist = true;
				continue;
			}else if(!inputtedFile.getName().endsWith(".pdf")){
				fileWrongExtension = true;
				continue;
			}
			return inputtedFile;
		}
	}
	
	public static String inputNonEmptyString(
			final Scanner theScanner,
			final ConsoleState theState,
			final String thePrompt,
			final String theEmptyStringPrompt){
		boolean emptyStringInputted = false;
		
		while(true){
			if(emptyStringInputted){
				emptyStringInputted = false;
				System.out.print(theEmptyStringPrompt);
			}
			System.out.print(thePrompt);
			ConsoleUtility.flush();
			String userInput = theScanner.nextLine();
			if(userInput.trim().isEmpty()){
				emptyStringInputted = true;
				continue;
			}
			return userInput;
		}
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
					"Submission Deadline: " + 
					theState.getCurrentConference().getInfo().getSubmissionDate() + "\n"
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

		final int paperSubmitLimit = 5;
		final int paperAssignLimit = 8;
	
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		try {
			Conference conf1 = Conference.createConference(
					"19th International Conference on Enterprise Information Systems *Af.",
					format.parse("2017/05/06 23:59:59"),
					paperSubmitLimit,
					paperAssignLimit
					);
			Conference conf2 = Conference.createConference(
					"Conference on Animation, Effects, VR, Games and Transmedia *Bef.",
					format.parse("2017/05/08 23:59:59"),
					paperSubmitLimit,
					paperAssignLimit
					);
			Conference conf3 = Conference.createConference(
					"Western Canadian Conference on Computing Education *Af",
					format.parse("2017/05/20 23:59:59"),
					paperSubmitLimit,
					paperAssignLimit
					);
			ConferenceSystem.getInstance().addConference(conf1);
			ConferenceSystem.getInstance().addConference(conf2);
			ConferenceSystem.getInstance().addConference(conf3);

			final UserProfile sampleAuthorUser = new UserProfile("submitalot@uw.edu", "Garrett Wolfe");
			final UserProfile sampleAuthorUser2 = new UserProfile("submitertoo@uw.edu", "Irma Turner");
			final UserProfile sampleReviewerUser = new UserProfile("reviewer@uw.edu", "Geoffre Tucker");
			final UserProfile sampleSubprogramUser = new UserProfile("subprogram@uw.edu", "Laverne Rogers ");

			for(int i = 0; i < 4; ++i){
				final Paper currentPaper = Paper.createPaper(
						new File("C;/Paper" + i + ".pdf"),
						Arrays.asList("Garrett Wolfe"),
						"Sample Paper" + i,
						sampleAuthorUser);
				conf2.getUserRole().addPaper(
						sampleAuthorUser,
						currentPaper
						);
				conf2.getSubprogramRole().assignReviewer(
						sampleReviewerUser,
						currentPaper
						);
			}
			for(int i = 0; i < 3; ++i){
				final Paper currentPaper = Paper.createPaper(
						new File("C;/Paper" + i + ".pdf"),
						Arrays.asList("Irma Turner"),
						"Sample Paper" + i,
						sampleAuthorUser2);
				conf2.getUserRole().addPaper(
						sampleAuthorUser2,
						currentPaper
						);
				conf2.getSubprogramRole().assignReviewer(
						sampleReviewerUser,
						currentPaper
						);
			}
			
			
			conf2.getDirectorRole().addUserRole(sampleSubprogramUser, Conference.SUBPROGRAM_ROLE);
			
			
			conf3.getUserRole().addPaper(sampleAuthorUser, 
					Paper.createPaper(
							new File("C:/ROC_Curve.pdf"),
							Arrays.asList("Garrett Wolfe"),
							"The use of the area under the ROC curve in the evaluation of machine learning algorithms",
							sampleAuthorUser));
			conf3.getUserRole().addPaper(sampleAuthorUser, 
					Paper.createPaper(
							new File("C:/KernelClassifiers.pdf"),
							Arrays.asList("Garrett Wolfe"),
							"Learning kernel classifiers: theory and algorithms",
							sampleAuthorUser));
//			conf3.getUserRole().addPaper(sampleAuthorUser, 
//					Paper.createPaper(
//							new File("C:/InstanceBased.pdf"),
//							Arrays.asList("Garrett Wolfe"),
//							"Instance-based learning algorithms",
//							sampleAuthorUser));
			final List<UserProfile> sampleUserProfiles =
					new ArrayList<>(Arrays.asList(
							sampleReviewerUser,
							sampleAuthorUser,
							sampleSubprogramUser,
							sampleAuthorUser2
							)
							);
			
			for(final UserProfile currentSampleUserProfile: sampleUserProfiles){
				ConferenceSystem.getInstance().addUserProfile(currentSampleUserProfile);
			}

			
			final Paper samplePaper1 = Paper.createPaper(
					new File(""),
					new ArrayList<>(Arrays.asList("Xu Zhuang", "Yan Zhu", "Chin-Chen Chang", "Qiang Peng", "Garrett Wolfe")),
					"Feature bundling in decision tree algorithm.",
					sampleAuthorUser
					);
			conf3.getUserRole().addPaper(sampleAuthorUser, samplePaper1);
			
			
		} catch (IllegalArgumentException e) {
//				e.printStackTrace();
		} catch (ParseException e) {
//				e.printStackTrace();
		} catch (IllegalOperationException e) {
//				e.printStackTrace();
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
