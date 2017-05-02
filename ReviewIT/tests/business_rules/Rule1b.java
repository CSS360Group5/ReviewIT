package business_rules;

import static org.junit.Assert.*;

import java.util.List;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import model.ErrorException;
import model.Paper;
import model.UserProfile;
import model.conference.Conference;
/**
 * Test unit testing business rule 1b of the ReviewIt application. 
 * Business Rule 1b:
 * An author is limited to 5 manuscript submissions as author or co-author per conference
 * @author Harlan Stewart
 * @version 1.4
 */
public class Rule1b {
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
	
	 /* Mock user id used to represent an author submitting papers to a conference.*/
	private final String testUID = "UID1";
	
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
				new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})), "Test Title", testUID);
		testPaper2 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})), "Test Title", testUID);
		testPaper3 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})), "Test Title", testUID);
		testPaper4 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})), "Test Title", testUID);
		testPaper5 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})), "Test Title", testUID);
		testPaper6 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})), "Test Title", testUID);
		testPaperList.add(testPaper1);
		testPaperList.add(testPaper2);
		testPaperList.add(testPaper3);
		testPaperList.add(testPaper4);
		testPaperList.add(testPaper5);
		testPaperList.add(testPaper6);
	}

	/**
	 * This test method will add 3 test papers to the test user's submission map
	 * to ensure that they were added without any issues.
	 * @throws ErrorException
	 */
	@Test
	public void paperSubmitUnderLimitCheck()throws ErrorException {
		for(int i = 0; i < 4; i++) {
			testCon.getUserRole().addPaper(testUID, testPaperList.get(i));
		}
		assertTrue(testCon.getInfo().getPapersSubmittedBy(testUID).size() == 4);		
	}
	
	/**
	 * This test method will add 5 test papers to the test user's submission map
	 * to ensure that an author can submit the number of papers equal to the submission
	 * limit set by the conference.
	 * @throws ErrorException
	 */
	@Test
	public void paperSubmitExactLimitCheck()throws ErrorException {
		for(int i = 0; i < 5; i++) {
			testCon.getUserRole().addPaper(testUID, testPaperList.get(i));
		}
		assertTrue(testCon.getInfo().getPapersSubmittedBy(testUID).size() == 5);
	}
	
	/**
	 * This test method attempts to add all 6 of the test papers to ensure
	 * that an author is not allowed to submit more than the allowed amount
	 * of papers and also to ensure that the correct ErrorException is thrown.
	 * @throws ErrorException
	 */
	@Test (expected = ErrorException.class)
	public void paperSubmitOverLimitCheck() throws ErrorException {
		for(int i = 0; i < testPaperList.size(); i++) {
			testCon.getUserRole().addPaper(testUID, testPaperList.get(i));
		}
		assertTrue(testCon.getInfo().getPapersSubmittedBy(testUID).size() == 5);
	}

}
