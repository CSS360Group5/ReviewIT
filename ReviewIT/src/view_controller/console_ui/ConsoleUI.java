package view_controller.console_ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

import model.IllegalOperationException;
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
//		RSystem.getInstance().serializeModel();
		
//		RSystem.getInstance().deserializeData();
		
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
			case ConsoleState.VIEW_SUBMITTED_PAPERS_FOR_CONFERENCE_SCREEN:
				newScreen = viewConferenceSubmittedPaperScreen();
				break;
			case ConsoleState.VIEW_ASSIGNED_PAPERS_FOR_CONFERENCE_SCREEN:
				newScreen = viewConferenceAssignedPapersScreen();
				break;
			case ConsoleState.SUBMIT_PAPER_SCREEN:
				newScreen = submitPaper();
				break;
			case ConsoleState.ASSIGN_REVIEWER_START_SCREEN:
				newScreen = assignReviewer();
				break;
			case ConsoleState.ASSIGN_REVIEWER_ID_SCREEN:
				newScreen = assignReviewerByID();
				break;
			case ConsoleState.ASSIGN_REVIEWER_LIST_SCREEN:
				newScreen = assignReviewerFromList();
				break;
			case ConsoleState.ASSIGN_REVIEWER_TO_PAPER_SCREEN:
				newScreen = assignReviewerToPaper();
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
		RSystem.getInstance().serializeModel();
		
		ConsoleUtility.showMessageToUser(myScanner, "Exiting Program!");
	}

	private int assignReviewerFromList() {
		ConsoleUtility.printHeader(myState);
		final StringBuilder promptBuilder = new StringBuilder();
		promptBuilder.append("Please choose a Reviewer:\n");
		List<UserProfile> reviewerList = myState.getCurrentConference().getInfo().getReviewers();
		
		int i = 0;
		for(; i < reviewerList.size(); ++i){
			final UserProfile currentReviewer = reviewerList.get(i);
			promptBuilder.append(i + ") ID: "+ currentReviewer.getUID() + ", Name: " + currentReviewer.getName() + "\n");
		}
		
		final int GO_BACK_OPTION = i;
		promptBuilder.append(GO_BACK_OPTION + ") Back to Conference screen.\n");
		final int EXIT_PROGRAM_OPTION = i + 1;
		promptBuilder.append(EXIT_PROGRAM_OPTION + ") Exit Program.\n");
		
		final int chosenOption = ConsoleUtility.inputNumberedOptions(
				myScanner, myState, ConsoleUtility.createConsecutiveList(0, EXIT_PROGRAM_OPTION), promptBuilder.toString());
		
		if(chosenOption == GO_BACK_OPTION){
			return ConsoleState.CONFERENCE_SCREEN;
		}
		else if(chosenOption == EXIT_PROGRAM_OPTION){
			return ConsoleState.EXIT_PROGRAM;
		}else{ //They chose a reviewer:
			myState.setSelectedUserForAssignment(reviewerList.get(chosenOption));
			if(!myState.getCurrentConference().getInfo().isReviewerInAssignmentLimit(reviewerList.get(chosenOption))){
				ConsoleUtility.showMessageToUser(myScanner, "Reviewer " + reviewerList.get(chosenOption).getUID() +  " is already at the assignment limit.");
				return ConsoleState.CONFERENCE_SCREEN;
			}
			return ConsoleState.ASSIGN_REVIEWER_TO_PAPER_SCREEN;
		}
	}

	private int assignReviewerToPaper(){
		ConsoleUtility.printHeader(myState);
		final StringBuilder promptBuilder = new StringBuilder();
		promptBuilder.append("Choose a Paper to assign to User ID: " + myState.getSelectedUserForAssignment().getUID() + ", Name: " + myState.getSelectedUserForAssignment().getName() + "\n");
		List<Paper> paperList = myState.getCurrentConference().getInfo().getAllPapers();
		
		int i = 0;
		for(; i < paperList.size(); ++i){
			final Paper currentPaper = paperList.get(i);
			promptBuilder.append(
					i + ") Title: \"" + currentPaper.getTitle() + "\"\n" +
					"Authors: " + currentPaper.getAuthors() + "\n" +
					"Paper's File: " + currentPaper.getPaperFile() + "\n" +
					"Submitted on: " + currentPaper.getSubmitDate() + "\n" +
					"Submitted by: \"" + currentPaper.getSubmitterUserProfile().getUID() + "\"\n"
					);
		}
		
		final int GO_BACK_OPTION = i;
		promptBuilder.append(GO_BACK_OPTION + ") Back to Conference screen.\n");
		final int EXIT_PROGRAM_OPTION = i + 1;
		promptBuilder.append(EXIT_PROGRAM_OPTION + ") Exit Program.\n");
		
		final int chosenOption = ConsoleUtility.inputNumberedOptions(
				myScanner, myState, ConsoleUtility.createConsecutiveList(0, EXIT_PROGRAM_OPTION), promptBuilder.toString());
		
		if(chosenOption == GO_BACK_OPTION){
			return ConsoleState.CONFERENCE_SCREEN;
		}
		else if(chosenOption == EXIT_PROGRAM_OPTION){
			return ConsoleState.EXIT_PROGRAM;
		}else{ //They chose a paper:
			final Paper chosenPaper = paperList.get(chosenOption);
			ConsoleUtility.showMessageToUser(
					myScanner,
					"You've successfully assigned Reviewer with UID: " +
					myState.getSelectedUserForAssignment().getUID() +
					", Name: " + myState.getSelectedUserForAssignment().getName() +
					" to review " + chosenPaper.getTitle()
					);
			return ConsoleState.CONFERENCE_SCREEN;
		}
	}
	
	private int assignReviewerByID() {
		final String loginHeader = new String(new char[30]).replace("\0", " ") +
				"Assign Paper to a Reviewer\n\n\n\n";
		final String inputPrompt =  "Please type in the Reviewer UserID or enter " + ConsoleUtility.EXIT_OPTION + " to exit:\nUserID:";
		final String noSuchUserPrompt = "Sorry no UserID \"%s\" found in the System.\nPlease try again.\n";
		
		final UserProfile selectedProfile = ConsoleUtility.inputExistingUserProfile(myScanner, myState, loginHeader, inputPrompt, noSuchUserPrompt);
		if(selectedProfile == null){
			return ConsoleState.CONFERENCE_SCREEN;
		}
		if(!myState.getCurrentConference().getInfo().isReviewerInAssignmentLimit(selectedProfile)){
			ConsoleUtility.showMessageToUser(myScanner, "Reviewer " + selectedProfile.getUID() +  " is already at the assignment limit.");
			return ConsoleState.CONFERENCE_SCREEN;
		}
		myState.setSelectedUserForAssignment(selectedProfile);
		return ConsoleState.ASSIGN_REVIEWER_TO_PAPER_SCREEN;
		
	}
	private int viewConferenceAssignedPapersScreen() {
		ConsoleUtility.printHeader(myState);
		System.out.println("All assigned papers:\n");
		int paperNum = 0;
		for(final Paper currentPaper: myState.getCurrentConference().getInfo().getPapersAssignedToReviewer(myState.getCurrentUser())){
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
		
		return ConsoleState.CONFERENCE_SCREEN;
	}

	private int viewAllAssignedPapersScreen() {
		ConsoleUtility.printHeader(myState);
		System.out.println("All papers assigned to me:\n");
		int paperNum = 0;
		for(final Paper currentPaper: RSystem.getInstance().getAllPapersAssignedTo(myState.getCurrentUser())){
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
	
	private int viewConferenceSubmittedPaperScreen() {
		ConsoleUtility.printHeader(myState);
		System.out.println("Papers submitted to this Conference:\n");
		int paperNum = 0;
		for(final Paper currentPaper: myState.getCurrentConference().getInfo().getPapersSubmittedBy(myState.getCurrentUser())){
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
		
		return ConsoleState.CONFERENCE_SCREEN;
	}

	private int submitPaper() {

		ConsoleUtility.printHeader(myState);
		if(!myState.getCurrentConference().getInfo().isSubmissionOpen(new Date())){
			ConsoleUtility.showMessageToUser(
					myScanner,
					"Sorry, Conference is closed for paper submissions.\n" +
					"Conference Submission Deadline was: " + myState.getCurrentConference().getInfo().getSubmissionDate() + "\n" +
					"Current System Date is: " + new Date());
			return ConsoleState.CONFERENCE_SCREEN;
		}else{
			final File paperFile = ConsoleUtility.inputFile(
					myScanner,
					myState,
					"SUBMITTING PAPER:\nPlease type in the path to a .pdf file.\nPATH:",
					"File does not exist. Please type in a valid path!\n",
					"File must be .pdf\n");
			
			if(paperFile == null){
				return ConsoleState.CONFERENCE_SCREEN;
			}
			
			final List<String> authors =
					Arrays.asList(
							ConsoleUtility.inputNonEmptyString(
									myScanner,
									myState,
									"Please enter all Paper Authors separated by comma\nAuthors: ", 
									"Paper must have at least 1 author!"
									).split(",")
							);
			final String paperTitle =
					ConsoleUtility.inputNonEmptyString(
							myScanner,
							myState,
							"Please enter the Paper Title\nTitle: ",
							"Cannot have an empty Title!"
							);
			
			final Paper enteredPaper = Paper.createPaper(
					paperFile, authors, paperTitle, myState.getCurrentUser());
			
			try {
				myState.getCurrentConference().getUserRole().addPaper(myState.getCurrentUser(), enteredPaper);
			} catch (final IllegalOperationException theException) {
				ConsoleUtility.showMessageToUser(
						myScanner,
						"Could not submit Paper:\n\"" + theException.getMessage() + "\"\n"
						);
				return ConsoleState.CONFERENCE_SCREEN;
			}
			ConsoleUtility.showMessageToUser(
					myScanner,
					"Paper submitted successfully!"
					);
			return ConsoleState.CONFERENCE_SCREEN;
		}
	}
	
	private int assignReviewer() {
		final int ASSIGN_BY_ID_OPTION = 1;
		final int ASSIGN_FROM_LIST_OPTION = 2;
		final int GO_BACK_OPTION = 3;
		final int EXIT_PROGRAM = 4;
		
		final String inputPrompt = "Please use one of the following options:\n" +
				"1) Assign Reviewer by UserID(and promote User to Reviewer)\n" +
				"2) Assign Reviewer from Conference's Reviewer list\n" + 
				"3) Go back.\n" +
				"4) Exit program.\n";
		
		final int chosenOption = ConsoleUtility.inputNumberedOptions(
				myScanner, myState, ConsoleUtility.createConsecutiveList(1, 4), inputPrompt);
	 	
		switch(chosenOption){
		case ASSIGN_BY_ID_OPTION:
			return ConsoleState.ASSIGN_REVIEWER_ID_SCREEN;
		case ASSIGN_FROM_LIST_OPTION:
			return ConsoleState.ASSIGN_REVIEWER_LIST_SCREEN;
		case GO_BACK_OPTION:
			return ConsoleState.CONFERENCE_SCREEN;
		case EXIT_PROGRAM:
			return ConsoleState.EXIT_PROGRAM;
		}
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
			ConsoleUtility.signOut(myScanner, myState);
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
		final String inputPrompt =  "Please entered your UserID or enter " + ConsoleUtility.EXIT_OPTION + " to exit:\nUserID:";
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
		final String chooseIDPrompt = "Please enter the UserID you wish to use or enter " + ConsoleUtility.EXIT_OPTION + " to exit:\nUserID:";
		final String chooseNamePrompt = "Please enter your first and last name or enter " + ConsoleUtility.EXIT_OPTION + " to exit:\nName:";
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
				myScanner, myState, ConsoleUtility.createConsecutiveList(0, EXIT_PROGRAM_OPTION), promptBuilder.toString());
		
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
		final int VIEW_ASSIGNED_PAPERS = 3;
		final int ASSIGN_REVIEWER_OPTION = 5;
		
		final int CHOOSE_DIFFERENT_CONFERENCE = 18;
		final int SIGN_OUT_OPTION = 19;
		final int EXIT_PROGRAM = 20;
		
		final StringBuilder promptBuilder = new StringBuilder();
		promptBuilder.append("Please use one of the following options:\n");
		
		final List<Integer> availableOptions = new ArrayList<>();
		
		promptBuilder.append(SUBMIT_PAPER_OPTION + ") Submit a Paper.\n");
		availableOptions.add(SUBMIT_PAPER_OPTION);
		
		if(myState.getCurrentConference().getInfo().isUserAuthor(myState.getCurrentUser())){
			promptBuilder.append("As an Author you can:\n");
			promptBuilder.append("\t" + VIEW_SUBMITTED_PAPERS + ") View submitted Papers.\n");
			availableOptions.add(VIEW_SUBMITTED_PAPERS);
		}
		
		if(myState.getCurrentConference().getInfo().isUserReviewer(myState.getCurrentUser())){
			promptBuilder.append("As a Reviewer you can:\n");
			promptBuilder.append("\t" + VIEW_ASSIGNED_PAPERS + ") View assigned Papers.\n");
			availableOptions.add(VIEW_ASSIGNED_PAPERS);
		}
		
		if(myState.getCurrentConference().getInfo().isUserSubprogramChair(myState.getCurrentUser())){
			promptBuilder.append("As a Subprogram Chair you can:\n");
			promptBuilder.append("\t" + ASSIGN_REVIEWER_OPTION + ") Assign a Reviewer.\n");
			availableOptions.add(ASSIGN_REVIEWER_OPTION);
		}
		
		promptBuilder.append(
				CHOOSE_DIFFERENT_CONFERENCE + ") Choose another Conference.\n" + 
				SIGN_OUT_OPTION + ") Sign out.\n" + 
				EXIT_PROGRAM + ") Exit program."
				);
		availableOptions.addAll(Arrays.asList(CHOOSE_DIFFERENT_CONFERENCE, SIGN_OUT_OPTION, EXIT_PROGRAM));
		
		final int chosenOption = ConsoleUtility.inputNumberedOptions(
				myScanner, myState, availableOptions, promptBuilder.toString());
		
		switch(chosenOption){
		case SUBMIT_PAPER_OPTION:
			return ConsoleState.SUBMIT_PAPER_SCREEN;
		case ASSIGN_REVIEWER_OPTION:
			return ConsoleState.ASSIGN_REVIEWER_START_SCREEN;
		case VIEW_SUBMITTED_PAPERS:
			return ConsoleState.VIEW_SUBMITTED_PAPERS_FOR_CONFERENCE_SCREEN;
		case VIEW_ASSIGNED_PAPERS:
			return ConsoleState.VIEW_ASSIGNED_PAPERS_FOR_CONFERENCE_SCREEN;
		case CHOOSE_DIFFERENT_CONFERENCE:
			myState.setCurrentConference(null);
			return ConsoleState.CHOOSE_CONFERENCE_SCREEN;
		case SIGN_OUT_OPTION:
			ConsoleUtility.signOut(myScanner, myState);
			return ConsoleState.PRELOGIN_SCREEN;
		case EXIT_PROGRAM:
			return ConsoleState.EXIT_PROGRAM;
		}

		return 0;
	}
	
}
