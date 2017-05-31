package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.ConferenceData;
import model.Conference;
import model.Paper;
import model.UserProfile;

/**
 * Testing the methods inside the ConferenceData class
 * @author Brian
 *
 */

public class ConferenceDataTest {
	
	// int constants for author paper submission limit and reviewer assignment
	private static final int PAPER_SUBMISSION_LIMIT = 5;
	private static final int PAPER_ASSIGNMENT_LIMIT = 8;
	
	// names, userid's, and titles for testing purposes
	private static final String TEST_AUTHOR = "John Doe";
	private static final String TEST_REVIEWER = "Rob Reviewer";
	private static final String TEST_SUBPROGRAMCHAIR = "Sally Subprogramchair";
	private static final String TEST_USER_ID1 = "UID1";
	private static final String TEST_USER_ID2 = "UID2";
	private static final String TEST_USER_ID3 = "UID3";
	private static final String TEST_CO_AUTHOR = "Sally Doe";
	private static final String TEST_CON_NAME = "A test con";
	private static final String TEST_TITLE = "Some Paper Title";
	private  final UserProfile TEST_USER_PROFILE_AUTHOR = new UserProfile(TEST_USER_ID1,
			TEST_AUTHOR);
	private  final UserProfile TEST_USER_PROFILE_REVIEWER = new UserProfile(TEST_USER_ID2, TEST_REVIEWER);
	private final UserProfile TEST_USER_PROFILE_SUBPROGRAMCHAIR = new UserProfile(TEST_USER_ID3, TEST_SUBPROGRAMCHAIR);
	private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	private static final String TEST_DATE = "4017/07/30 23:59:59";
	private static final String TEST_DATE_2 = "2016/06/30 23:59:59";
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat(DATE_FORMAT);
	
	private Date submissionDate;
	private Date paperDeadline;
	private Conference conference;
	private ConferenceData conferenceData;
//	private UserUtilities userUtilities;
	private List<Paper> authorPaperList;
//	private Map<String, List<Paper>> paperAuthorshipMap;
//	private Map<String, List<Paper>> reviewerAssignmentMap;
	
	
	// test papers for max reviewer assignment/author submission
	private Paper testPaper1;
	private Paper testPaper2;
	private Paper testPaper3;
	private Paper testPaper4;
	private Paper testPaper5;
	private Paper testPaper6;
	private Paper testPaper7;
	private Paper testPaper8;
	private Paper testPaper9;
	

	@Before
	public void setUp() throws Exception {
		paperDeadline = FORMAT.parse(TEST_DATE);
		submissionDate = FORMAT.parse(TEST_DATE_2);
		
		conference = Conference.createConference(TEST_CON_NAME, paperDeadline, PAPER_SUBMISSION_LIMIT, PAPER_ASSIGNMENT_LIMIT);
		
		conferenceData = conference.getInfo();
		
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
		testPaper6 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{TEST_AUTHOR, TEST_CO_AUTHOR})), TEST_TITLE, TEST_USER_PROFILE_AUTHOR);
		testPaper7 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{TEST_AUTHOR, TEST_CO_AUTHOR})), TEST_TITLE, TEST_USER_PROFILE_AUTHOR);
		testPaper8 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{TEST_AUTHOR, TEST_CO_AUTHOR})), TEST_TITLE, TEST_USER_PROFILE_AUTHOR);
		testPaper9 = Paper.createPaper(new File(""), 
				new ArrayList<>(Arrays.asList(new String[]{TEST_REVIEWER, TEST_CO_AUTHOR})), TEST_TITLE, TEST_USER_PROFILE_AUTHOR);
	
		authorPaperList = new LinkedList<>();
		authorPaperList.add(testPaper1);
		authorPaperList.add(testPaper2);
		authorPaperList.add(testPaper3);
		authorPaperList.add(testPaper4);
		authorPaperList.add(testPaper5);
		authorPaperList.add(testPaper6);
		authorPaperList.add(testPaper7);
		authorPaperList.add(testPaper8);
		authorPaperList.add(testPaper9);
		
	}

	@Test
	public void isPaperInSubmissionLimit_OnePaper_True() {
		conference.getUserRole().addPaper(TEST_USER_PROFILE_AUTHOR, authorPaperList.get(0));
		conferenceData = conference.getInfo();
		assertTrue(conferenceData.isPaperInAuthorSubmissionLimit(testPaper2));
		
	}
	
	@Test
	public void isPaperInSubmissionLimit_PAPER_SUBMISSION_LIMIT_False() {
		for(int i = 0; i < PAPER_SUBMISSION_LIMIT; i++)
			conference.getUserRole().addPaper(TEST_USER_PROFILE_AUTHOR, authorPaperList.get(i));
		assertFalse(conferenceData.isPaperInAuthorSubmissionLimit(testPaper1));
	}
	
	@Test
	public void isSubmissionOpen_submissionDate_True() {
		assertTrue(conferenceData.isSubmissionOpen(submissionDate));
	}
	
	/*
	 * Indirectly tests isPaperInSubmissionDeadline()
	 */
	@Test
	public void isSubmissionOpen_paperDeadline_False() {
		assertFalse(conferenceData.isSubmissionOpen(paperDeadline));
	}
	
	@Test
	public void isReviewerInAssignmentLimit_OnePaper_True() {
		conference = Conference.createConference(TEST_CON_NAME, submissionDate, PAPER_SUBMISSION_LIMIT, PAPER_ASSIGNMENT_LIMIT);
		conference.getSubprogramRole().assignReviewer(TEST_USER_PROFILE_REVIEWER, testPaper1);
		assertTrue(conference.getInfo().getPapersAssignedToReviewer(TEST_USER_PROFILE_REVIEWER).size() == 1);
		assertTrue(conferenceData.isReviewerInAssignmentLimit(TEST_USER_PROFILE_REVIEWER));
	}
	
	@Test
	public void isReviewerInAssignmentLimit_PAPER_ASSIGNMENT_LIMIT_False() {
		conference = Conference.createConference(TEST_CON_NAME, submissionDate, PAPER_SUBMISSION_LIMIT, PAPER_ASSIGNMENT_LIMIT);
		conferenceData = conference.getInfo();
		for(int i = 0; i < PAPER_ASSIGNMENT_LIMIT; i++) 
			conference.getSubprogramRole().assignReviewer(TEST_USER_PROFILE_REVIEWER, authorPaperList.get(i));
		assertTrue(conference.getInfo().getPapersAssignedToReviewer(TEST_USER_PROFILE_REVIEWER).size() == 8);
		assertFalse(conferenceData.isReviewerInAssignmentLimit(TEST_USER_PROFILE_REVIEWER));
			
	}
	
	@Test
	public void isPaperAuthoredByReviewer_testPaper1_False() {
		assertFalse(conferenceData.isPaperAuthoredByReviewer(TEST_REVIEWER, testPaper1));
	}
	
	@Test
	public void isPaperAuthoredByReviewer_testPaper9_True() {
		assertTrue(conferenceData.isPaperAuthoredByReviewer(TEST_REVIEWER, testPaper9));
	}

	@Test
	public void isUserAuthor_TEST_USER_PROFILE_False() {
		assertFalse(conferenceData.isUserAuthor(TEST_USER_PROFILE_AUTHOR));
	}
	
	/* 
	 * Indirectly tests the protected addUserToRole method as well as the other isUser___
	 * methods that function the same way.
	 */
	@Test
	public void isUserAuthor_TEST_USER_PROFILE_True() {
		conference.getDirectorRole().addUserRole(TEST_USER_PROFILE_AUTHOR, "Author");
		conferenceData = conference.getInfo();
		assertTrue(conferenceData.isUserAuthor(TEST_USER_PROFILE_AUTHOR));
	}
	
	@Test
	public void assignSubprogramChairToPaper_testPaper1() {
		conferenceData.assignSubprogramchairToPaper(TEST_USER_PROFILE_SUBPROGRAMCHAIR, testPaper1);
		assertTrue(conferenceData.getPapersAssignedToSubProgramChair(TEST_USER_PROFILE_SUBPROGRAMCHAIR).size() == 1);
	}
}
