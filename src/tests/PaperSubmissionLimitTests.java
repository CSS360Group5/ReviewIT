package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;

import model.Conference;
import model.Paper;
import model.UserProfile;
/**
 * Test unit testing business rule 1b of the ReviewIt application. 
 * Business Rule 1b:
 * An author is limited to 5 manuscript submissions as author or co-author per conference
 * @author Harlan Stewart
 * @version 1.5
 */
public class PaperSubmissionLimitTests {
	/* Conference object used for creating mock conference for tests.*/
	private Conference testCon;
	
	/*Paper objects used for creating mock papers for tests.*/
	private Paper testPaper1;
	private Paper testPaper2;
	private Paper testPaper3;
	private Paper testPaper4;
	private Paper testPaper5;
	private Paper testPaper6;
	
	/*String constants for author, co-author, conference name, and paper title.*/
	private static final String TEST_AUTHOR = "John Doe";
	private static final String TEST_CO_AUTHOR = "Sally Doe";
	private static final String TEST_CON_NAME = "A test con";
	private static final String TEST_TITLE = "Some Paper Title";
	
	/*Integer constants for submission limit, under submission limit, and assignment limit.*/
	private static final int SUBMISSION_LIMIT = 5;
	private static final int SUBMISSION_UNDER_LIMIT_VAL = 4;
	private static final int ASSIGNMENT_LIMIT = 8;
	
	/*String constants for date format and test date.*/
	private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	private static final String TEST_DATE = "4017/04/30 23:59:59";
	
	
	/*ArrayList used to store mock papers.*/
	private static final ArrayList<Paper> TEST_PAPER_LIST = new ArrayList<>();
	
	 /* Mock user profile used to represent an author submitting papers to a conference.*/
	private static final UserProfile testUserProfile = new UserProfile("UID1", "Some Name1");
	
	/*Date format used to create mock date strings for testing.*/
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(DATE_FORMAT);;
    /*Date object used to represent a mock deadline for testing.*/
    Date deadline;
	
	/**
	 * Setup method that helps setup the mock conference environment and test objects.
	 * @throws ParseException
	 */
	@Before
	public void setUp() throws ParseException {       
        deadline = FORMAT.parse(TEST_DATE);
        
		testCon = Conference.createConference(TEST_CON_NAME, deadline, SUBMISSION_LIMIT,ASSIGNMENT_LIMIT);
		
		testPaper1 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{TEST_AUTHOR, TEST_CO_AUTHOR})), TEST_TITLE, testUserProfile);
		testPaper2 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{TEST_AUTHOR, TEST_CO_AUTHOR})), TEST_TITLE, testUserProfile);
		testPaper3 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{TEST_AUTHOR, TEST_CO_AUTHOR})), TEST_TITLE, testUserProfile);
		testPaper4 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{TEST_AUTHOR, TEST_CO_AUTHOR})), TEST_TITLE, testUserProfile);
		testPaper5 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{TEST_AUTHOR, TEST_CO_AUTHOR})), TEST_TITLE, testUserProfile);
		testPaper6 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{TEST_AUTHOR, TEST_CO_AUTHOR})), TEST_TITLE, testUserProfile);
		TEST_PAPER_LIST.add(testPaper1);
		TEST_PAPER_LIST.add(testPaper2);
		TEST_PAPER_LIST.add(testPaper3);
		TEST_PAPER_LIST.add(testPaper4);
		TEST_PAPER_LIST.add(testPaper5);
		TEST_PAPER_LIST.add(testPaper6);
	}

	/**
	 * This test method will add 3 test papers to the test user's submission map
	 * to ensure that they were added without any issues.
	 * @throws IllegalArgumentException
	 */
	@Test
	public void paperSubmitUnderLimitCheck_IsSubmitted()throws IllegalArgumentException {
		for(int i = 0; i < SUBMISSION_UNDER_LIMIT_VAL; i++) {
			testCon.getUserRole().addPaper(testUserProfile, TEST_PAPER_LIST.get(i));
		}
		assertTrue(testCon.getInfo().getPapersSubmittedBy(testUserProfile).size() == SUBMISSION_UNDER_LIMIT_VAL);		
	}
	
	/**
	 * This test method will add 5 test papers to the test user's submission map
	 * to ensure that an author can submit the number of papers equal to the submission
	 * limit set by the conference.
	 * @throws IllegalArgumentException
	 */
	@Test
	public void paperSubmitExactLimitCheck_IsSubmitted()throws IllegalArgumentException {
		for(int i = 0; i < SUBMISSION_LIMIT; i++) {
			testCon.getUserRole().addPaper(testUserProfile, TEST_PAPER_LIST.get(i));
		}
		assertTrue(testCon.getInfo().getPapersSubmittedBy(testUserProfile).size() == SUBMISSION_LIMIT);
	}
	
	/**
	 * This test method attempts to add all 6 of the test papers to ensure
	 * that an author is not allowed to submit more than the allowed amount
	 * of papers and also to ensure that the correct ErrorException is thrown.
	 * @throws IllegalArgumentException
	 */
	@Test (expected = IllegalArgumentException.class)
	public void paperSubmitOverLimitCheck_IsNotSubmitted() throws IllegalArgumentException {
		for(int i = 0; i < TEST_PAPER_LIST.size(); i++) {
			testCon.getUserRole().addPaper(testUserProfile, TEST_PAPER_LIST.get(i));
		}
		assertTrue(testCon.getInfo().getPapersSubmittedBy(testUserProfile).size() == SUBMISSION_LIMIT);
	}
	@Test //(expected = IllegalArgumentException.class)
	public void paperSubmittedRemovedAndAnotherSubmitted_IsSubmitted() throws IllegalArgumentException {
		for(int i = 0; i < SUBMISSION_LIMIT; i++) {
			testCon.getUserRole().addPaper(testUserProfile, TEST_PAPER_LIST.get(i));
		}
		//check if valid size
		assertTrue(testCon.getInfo().getPapersSubmittedBy(testUserProfile).size() == SUBMISSION_LIMIT);
		
		//remove one of the submitted papers
		testCon.getUserRole().removePaper(testUserProfile,  TEST_PAPER_LIST.get(0));
		assertTrue(testCon.getInfo().getPapersSubmittedBy(testUserProfile).size() == SUBMISSION_LIMIT - 1);
		
		//add it back and check if size has increased
		testCon.getUserRole().addPaper(testUserProfile, TEST_PAPER_LIST.get(0));
		assertTrue(testCon.getInfo().getPapersSubmittedBy(testUserProfile).size() == SUBMISSION_LIMIT);
	}

}
