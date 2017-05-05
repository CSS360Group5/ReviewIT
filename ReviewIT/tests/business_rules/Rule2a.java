package business_rules;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import model.IllegalOperationException;
import model.Paper;
import model.UserProfile;
import model.conference.Conference;

/**
 * A test unit for business rule 2a):
 * A reviewer can't be assigned to review a paper he/she has authored or coauthored.
 * @author Dimitar Kumanov
 * @version 4/29/2017
 */
public class Rule2a {

	private Conference aConference;
	private UserProfile aUserProfile;
	private Paper anAuthoredPaper;
	private Paper aCoauthoredPaper;
	private Paper aNotAuthoredPaper;
	
	
	@Before
	public void setUp(){
		aUserProfile = new UserProfile("reviewer@uw.edu", "John Reviewer Doe");
		aConference = Conference.createConference("Sample Conference",
													new Date(),
													5,
													8);
		anAuthoredPaper = Paper.createPaper(new File(""),
											new ArrayList<>(Arrays.asList(new String[]{"John Reviewer Doe",
																						"Some Coauthor"})),
											"Sample Paper Title",
											new UserProfile("someid@uw.edu", "Users Name"));
		aCoauthoredPaper = Paper.createPaper(new File(""),
												new ArrayList<>(Arrays.asList(new String[]{"Some Author",
																							"John Reviewer Doe"})),
												"Sample Paper Title",
												new UserProfile("someid@uw.edu", "Users Name"));
		aNotAuthoredPaper = Paper.createPaper(new File(""),
												new ArrayList<>(Arrays.asList(new String[]{"Some Author",
																							"Some Coauthor"})),
												"Sample Paper Title",
												new UserProfile("someid@uw.edu", "Users Name"));
	}
	

	@Test(expected = IllegalOperationException.class)
	public void assignAuthoredPaperForReview() throws IllegalOperationException{
		aConference.getReviewRole().assignReviewer(aUserProfile, anAuthoredPaper);
	}
	
	@Test(expected = IllegalOperationException.class)
	public void assignCoauthoredPaperForReview() throws IllegalOperationException{
		aConference.getReviewRole().assignReviewer(aUserProfile, aCoauthoredPaper);
	}
	
	@Test
	public void assignNotAuthoredPaperForReview() throws IllegalOperationException{
		aConference.getReviewRole().assignReviewer(aUserProfile, aNotAuthoredPaper);
		assertEquals(aConference.getInfo().getPapersAssignedToReviewer(
				aUserProfile).get(0),
				aNotAuthoredPaper
				);
		
	}
}
