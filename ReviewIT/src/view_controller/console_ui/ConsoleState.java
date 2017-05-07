package view_controller.console_ui;

import model.UserProfile;
import model.conference.Conference;


/**
 * A container class containing relevant state data for the ConsoleUI.
 * @author Dimitar Kumanov
 * @version 5/4/2017
 */
public class ConsoleState {
	//A list of possible screen states:
	public final static int EXIT_PROGRAM = 0;
	public final static int PRELOGIN_SCREEN = 1;
	public final static int LOGIN_SCREEN = 2;
	public final static int NEW_PROFILE_SCREEN = 3;
	public final static int USER_HOME_SCREEN = 4;
	public static final int VIEW_All_SUBMITTED_PAPERS_SCREEN = 5;
	public static final int VIEW_All_ASSIGNED_PAPERS_SCREEN = 6;
	public final static int CHOOSE_CONFERENCE_SCREEN = 7;
	public final static int CONFERENCE_SCREEN = 8;
	public final static int SUBMIT_PAPER_SCREEN = 9;
	public final static int ASSIGN_REVIEWER_SCREEN = 10;
	
	public final static int STARTING_SCREEN = PRELOGIN_SCREEN;
	
	
	private int myCurrentScreen;
	private UserProfile myCurrentUser;
	private Conference myCurrentConference;

	public ConsoleState(){
		this(STARTING_SCREEN);
	}
	
	public ConsoleState(final int theInitialScreen){
		myCurrentScreen = theInitialScreen;
	}
	
	public int getCurrentScreen() {
		return myCurrentScreen;
	}

	public void setCurrentScreen(final int myCurrentScreen) {
		this.myCurrentScreen = myCurrentScreen;
	}
	
	public UserProfile getCurrentUser() {
		return myCurrentUser;
	}

	public void setCurrentUser(final UserProfile myCurrentUser) {
		this.myCurrentUser = myCurrentUser;
	}

	public Conference getCurrentConference() {
		return myCurrentConference;
	}

	public void setCurrentConference(Conference myCurrentConference) {
		this.myCurrentConference = myCurrentConference;
	}
	
}
