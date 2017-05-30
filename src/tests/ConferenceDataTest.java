package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import model.ConferenceData;
import model.Conference;
import model.Paper;
import model.UserProfile;
import model.UserUtilities;

public class ConferenceDataTest {
	
	// int constants for author paper submission limit and reviewer assignment
	private static final int PAPER_SUBMISSION_LIMIT = 5;
	private static final int PAPER_ASSIGNMENT_LIMIT = 8;
	
	// names, userid's, and titles for testing purposes
	private static final String TEST_AUTHOR = "John Doe";
	private static final String TEST_REVIEWER = "Rob Reviewer";
	private static final String TEST_USER_ID1 = "UID1";
	private static final String TEST_USER_ID2 = "UID2";
	private static final String TEST_CO_AUTHOR = "Sally Doe";
	private static final String TEST_CON_NAME = "A test con";
	private static final String TEST_TITLE = "Some Paper Title";
	private  final UserProfile TEST_USER_PROFILE_AUTHOR = new UserProfile(TEST_USER_ID1,
			TEST_AUTHOR);
	private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	private static final String TEST_DATE = "4017/07/30 23:59:59";
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat(DATE_FORMAT);
	
	private Date paperDeadline;
	private ConferenceData conferenceData;
	private List<Paper> authorPaperList;
	private Map<String, List<Paper>> paperAuthorshipMap;
	private Map<String, List<Paper>> reviewerAssignmentMap;
	
	
	// test papers for max reviewer assignment/author submission
	private Paper testPaper1;
	private Paper testPaper2;
	private Paper testPaper3;
	private Paper testPaper4;
	private Paper testPaper5;
	private Paper testPaper6;
	private Paper testPaper7;
	private Paper testPaper8;
	

	@Before
	public void setUp() throws Exception {
		paperDeadline = FORMAT.parse(TEST_DATE);
		
		conferenceData = new ConferenceData(TEST_CON_NAME, paperDeadline, PAPER_SUBMISSION_LIMIT, PAPER_ASSIGNMENT_LIMIT);
		
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
		
		authorPaperList.add(testPaper1);
		authorPaperList.add(testPaper2);
		authorPaperList.add(testPaper3);
		authorPaperList.add(testPaper4);
		
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
