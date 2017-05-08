package ConferenceTests;

import model.IllegalOperationException;
import model.Paper;
import model.UserProfile;
import model.conference.Conference;
import model.conference.UserUtilities;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The purpose of this unit of tests is to ensure that the model code of the various conference classes
 * that make up the model code for the ReviewIt application are performing their operations
 * correctly. Included are all of the currently created tests that are applicable to the application in
 * its current state of development and do not represent a final build. More tests will be added as more
 * functionality is added to the model code.
 * @author Harlan Stewart
 * @version 1.2
 */
public class ConferenceModelTests {
	/* Conference object used for creating mock conference for tests.*/
	private  Conference TEST_CON;
	
	/*Paper objects used for creating mock papers for tests.*/
	private Paper testPaper1;
	private Paper testPaper2;
	private Paper testPaper3;
	private Paper testPaper4;
	private Paper testPaper5;
	
	/*String constants for author, co-author, conference name, and paper title.*/
	private static final String TEST_AUTHOR = "John Doe";
	private static final String TEST_REVIEWER = "Rob Reviewer";
	private static final String TEST_USER_ID1 = "UID1";
	private static final String TEST_USER_ID2 = "UID2";
	private static final String TEST_CO_AUTHOR = "Sally Doe";
	private static final String TEST_CON_NAME = "A test con";
	private static final String TEST_TITLE = "Some Paper Title";
	
	/*Integer constants for submission limit, under submission limit, and assignment limit.*/
	private static final int SUBMISSION_LIMIT = 5;
	private static final int ASSIGNMENT_LIMIT = 8;
	
	/*String constants for date format and test date.*/
	private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	private static final String TEST_DATE = "4017/04/30 23:59:59";
	
	
	/*ArrayList used to store mock papers.*/
	private  final ArrayList<Paper> TEST_PAPER_LIST = new ArrayList<>();
	
	 /* Mock user profile used to represent an author submitting papers to a conference.*/
	private  final UserProfile TEST_USER_PROFILE_AUTHOR = new UserProfile(TEST_USER_ID1, TEST_AUTHOR);
	
	/* Mock user profile used to represent a user who has the reviewer role. */
	private  final UserProfile TEST_USER_PROFILE_REVIEWER = new UserProfile(TEST_USER_ID2, TEST_REVIEWER);
	
	/*Date format used to create mock date strings for testing.*/
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(DATE_FORMAT);;
    /*Date object used to represent a mock deadline for testing.*/
    Date deadline;
	
	/**
	 * Setup method that helps setup the mock conference environment and test objects.
	 * @throws ParseException
	 * @throws IllegalOperationException 
	 */
	@Before
	public void setUp() throws ParseException, IllegalOperationException {       
        deadline = FORMAT.parse(TEST_DATE);
        
		TEST_CON = Conference.createConference(TEST_CON_NAME, deadline, SUBMISSION_LIMIT,ASSIGNMENT_LIMIT);
		testPaper1 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{TEST_AUTHOR, TEST_CO_AUTHOR})), TEST_TITLE, TEST_USER_PROFILE_AUTHOR);
		testPaper2 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{TEST_AUTHOR, TEST_CO_AUTHOR})), TEST_TITLE, TEST_USER_PROFILE_AUTHOR);
		testPaper3 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{TEST_AUTHOR, TEST_CO_AUTHOR})), TEST_TITLE, TEST_USER_PROFILE_AUTHOR);
		testPaper4 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{TEST_AUTHOR, TEST_CO_AUTHOR})), TEST_TITLE, TEST_USER_PROFILE_AUTHOR);
		testPaper5 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{TEST_AUTHOR, TEST_CO_AUTHOR})), TEST_TITLE, TEST_USER_PROFILE_AUTHOR);
		TEST_PAPER_LIST.add(testPaper1);
		TEST_PAPER_LIST.add(testPaper2);
		TEST_PAPER_LIST.add(testPaper3);
		TEST_PAPER_LIST.add(testPaper4);
		TEST_PAPER_LIST.add(testPaper5);
		
	}
	
	

	/**
	 * This method is used to test the getPapersAuthoredByUser() method which returns
	 * a list of the papers that a user has submitted to a conference.
	 * @throws IllegalOperationException 
	 */
	@Test
	public void getPapersAuthoredByUserTest() throws IllegalOperationException {		
		UserUtilities testUserUtil = TEST_CON.getUserRole();
		for(int i = 0; i < TEST_PAPER_LIST.size();i++) {
			testUserUtil.addPaper(TEST_USER_PROFILE_AUTHOR, TEST_PAPER_LIST.get(i));
		}
		List<Paper> testConAuthorPapers = TEST_CON.getInfo().getPapersAuthoredBy(TEST_AUTHOR);
		for(int i = 0; i < testConAuthorPapers.size();i++) {
			assertEquals(testConAuthorPapers.get(i).getSubmitterUserProfile().getUID(),TEST_USER_PROFILE_AUTHOR.getUID());
		}	
	}
	
	/**
	 * This method will test whether the system correctly assigns a user to be the
	 * reviewer for a paper.
	 * @throws IllegalOperationException
	 */
	@Test
	public void setUserRoleToReviewerTest() throws IllegalOperationException {
		TEST_CON.getSubprogramRole().assignReviewer(TEST_USER_PROFILE_REVIEWER, testPaper1);
		assertTrue(TEST_CON.getInfo().isUserReviewer(TEST_USER_PROFILE_REVIEWER));
	}
	
	/**
	 * This method will test to make sure the system allows a reviewer to be assigned multiple
	 * papers and they are contained in list specific to the reviewer.
	 * @throws IllegalOperationException 
	 */
	@Test
	public void getPapersAssignedToReviewerTest() throws IllegalOperationException {
		for(int i = 0; i < TEST_PAPER_LIST.size();i++) {
			TEST_CON.getSubprogramRole().assignReviewer(TEST_USER_PROFILE_REVIEWER, TEST_PAPER_LIST.get(i));
		}
		assertEquals(TEST_CON.getInfo().getPapersAssignedToReviewer(TEST_USER_PROFILE_REVIEWER).size(),TEST_PAPER_LIST.size());
	}
	
	/**
	 * This method is used to test the case where the system attempts to assign a user
	 * as a reviewer to a paper in which the user is also the author.
	 * @throws IllegalOperationException
	 */
	@Test(expected = IllegalOperationException.class)
	public void assignAuthorAsReviewerForTheirPaperTest() throws IllegalOperationException {
		TEST_CON.getSubprogramRole().assignReviewer(TEST_USER_PROFILE_AUTHOR, testPaper1);
	}
	
	/**
	 * This method is used to test whether the system correctly assigns the author role to
	 * a user after they have submitted a paper to the conference.
	 * @throws IllegalOperationException
	 */
	@Test
	public void setUserRoleToAuthorTest()throws IllegalOperationException {
		TEST_CON.getUserRole().addPaper(TEST_USER_PROFILE_AUTHOR, testPaper1);
		assertTrue(TEST_CON.getInfo().isUserAuthor(TEST_USER_PROFILE_AUTHOR));
	}

}
