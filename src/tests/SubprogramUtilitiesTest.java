package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Conference;
import model.Paper;
import model.UserProfile;

/**
 * Unit testing directed towards the coverage of the SubprogramUtilities model class.
 * Tests various methods not tested by 'feature' unit tests.
 * @author Ian Jury
 * @version 5/29/2017
 *
 */
public class SubprogramUtilitiesTest {
	
	private int ASSIGNMENT_LIMIT = 8;
	
	private Conference testConference;
	
	private Paper testPaper;
	
	private ArrayList<String> testAuthorList = new ArrayList<>();
	
	private UserProfile testAuthorProfile = new UserProfile("TestUserID", "test user name");
	
	private UserProfile testReviewerProfile = new UserProfile("ReviewerTestUID", "Reviewer name");

	@Before
	public void setUp() throws Exception {
		testAuthorList.add(testAuthorProfile.getName());
		testConference = Conference.createConference("Test Conference", new Date(), 5, 8);
		testPaper = Paper.createPaper(new File("./path"), testAuthorList, "TestPaperTitle", testAuthorProfile);
	}

	/**
	 * Tests if a paper is properly assigned to a reviewer
	 */
	@Test
	public void assignReviewerToPaper_IsAssigned() {
		testConference.getSubprogramRole().assignReviewer(testReviewerProfile, testPaper);
		
		List<Paper> assignedPapersToReviewer =
				testConference.getInfo().getPapersAssignedToReviewer(testReviewerProfile);
		
		assertTrue(assignedPapersToReviewer.contains(testPaper));
	}
	/**
	 * Adds ASSIGNMENT_LIMIT + 1 papers to reviewer.
	 * @throws IllegalArgumentException
	 */
	@Test (expected = IllegalArgumentException.class)
	public void assignReviewerToPaperWhenReviewerIsAtLimit_IsntAssigned() throws IllegalArgumentException {
		for (int i = 0; i <= ASSIGNMENT_LIMIT; i++) {
			testConference.getSubprogramRole().assignReviewer(testReviewerProfile,
					Paper.createPaper(new File("./TestPath"), testAuthorList, "Test", testAuthorProfile));
		}
	}
	/**
	 * Attempts to assign a reviewer to a paper who is the author of that paper.
	 * @throws IllegalArgumentException
	 */
	@Test (expected = IllegalArgumentException.class)
	public void assignReviewerToPaperWhenReviewerIsAuthor_IsntAssigned() throws IllegalArgumentException {
		testConference.getSubprogramRole().assignReviewer(testAuthorProfile, testPaper);
	}

}
