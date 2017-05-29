package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import model.Conference;
import model.Paper;
import model.UserProfile;

/**
 * Unit testing for coverage of ReviewerUtitlites model class.
 * Tests various methods not tested by 'feature' unit tests.
 * @author Ian Jury
 * @version 5/29/2017
 *
 */
public class ReviewerUtilitiesTest {
	
	Conference testConference;
	
	Paper testPaper;
	
	ArrayList<String> testAuthorList = new ArrayList<>();
	
	UserProfile testAuthorProfile = new UserProfile("TestUserID", "test user name");
	
	UserProfile testReviewerProfile = new UserProfile("ReviewerTestUID", "Reviewer name");
	
	@Before
	public void setUp() throws Exception {
		testAuthorList.add("test user name");
		testConference = Conference.createConference("Test Conference", new Date(), 5, 8);
		testPaper = Paper.createPaper(new File("./path"), testAuthorList, "TestPaperTitle", testAuthorProfile);
	}

	@Test
	public void reviewerSendsReviewToPaper_SendsReview() {
		testConference.getReviewerRole().sendReview(testReviewerProfile, testPaper, new File("./review"));
		int numberOfReviewsSentToPaper = testPaper.getReviews().size();
		assertTrue(numberOfReviewsSentToPaper == 1);
	}
	/**
	 * 
	 * @throws IllegalArgumentException
	 */
	@Test (expected = IllegalArgumentException.class)
	public void reviewerSendsReviewToPaperButItAuthor_ReviewIsntSent() throws IllegalArgumentException {
		testConference.getReviewerRole().sendReview(testAuthorProfile, testPaper, new File("./review"));
	}

}
