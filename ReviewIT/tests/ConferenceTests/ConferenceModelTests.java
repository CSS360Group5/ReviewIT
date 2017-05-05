package ConferenceTests;

import model.Paper;
import model.UserProfile;
import model.conference.Conference;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.fail;

public class ConferenceModelTests {
	/* Conference object used for creating mock conference for tests.*/
	private Conference testCon;
	
	/*Paper objects used for creating mock papers for tests.*/
	private Paper testPaper1;
	private Paper testPaper2;
	private Paper testPaper3;
	private Paper testPaper4;
	private Paper testPaper5;
	private Paper testPaper6;
	
	/*ArrayList used to store mock papers.*/
	private final ArrayList<Paper> testPaperList = new ArrayList<>();
	
	 /* Mock user profile used to represent an author submitting papers to a conference.*/
	private final UserProfile testUserProfile = new UserProfile("UID1", "Some Name1");
	
	/*Date format used to create mock date strings for testing.*/
    SimpleDateFormat format;
    /*Date object used to represent a mock deadline for testing.*/
    Date deadline;
	
	/**
	 * Setup method that helps setup the mock conference environment and test objects.
	 * @throws ParseException
	 */
	@Before
	public void setUp() throws ParseException {
		String conName = "A Test Con";
		int subLimit = 5;
		int assLimit = 8;		
        format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");        
        deadline = format.parse("4017/04/30 23:59:59");
        
		testCon = Conference.createConference(conName, deadline, subLimit,assLimit);
		
		testPaper1 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})), "Test Title", testUserProfile);
		testPaper2 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})), "Test Title", testUserProfile);
		testPaper3 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})), "Test Title", testUserProfile);
		testPaper4 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})), "Test Title", testUserProfile);
		testPaper5 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})), "Test Title", testUserProfile);
		testPaper6 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})), "Test Title", testUserProfile);
		testPaperList.add(testPaper1);
		testPaperList.add(testPaper2);
		testPaperList.add(testPaper3);
		testPaperList.add(testPaper4);
		testPaperList.add(testPaper5);
		testPaperList.add(testPaper6);
	}
	@Test
	public void test() {
		fail("Not yet implemented");
	}
}
