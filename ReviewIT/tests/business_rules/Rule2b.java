/**
 * TCSS 360
 * Group 5
 * 
 */
package business_rules;

import model.IllegalOperationException;
import model.Paper;
import model.UserProfile;
import model.conference.Conference;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests business rule 2b:
 * A reviewer can be assigned to review a maximum of 8
 * manuscripts for any conference.
 * 
 * @author Danielle Lambion
 * 
 */
public class Rule2b {
	
	/**
	 * conference for testing.
	 */
    private Conference testCon;
    
    /**
     * sample papers to test the 8 paper assignment limit.
     */
    private Paper paper1;
    private Paper paper2;
    private Paper paper3;
    private Paper paper4;
    private Paper paper5;
    private Paper paper6;
    private Paper paper7;
    private Paper paper8;
    private Paper paper9;
    private final String reviewerID = "Reviewer Joe";
    private final ArrayList<Paper> papers = new ArrayList<>();

    @Before
	public void setUp() {
		String conName = "Testing max amount of assignment to a reviewer";

        int subLimit = 5;
        int assLimit = 8;
        String testID1 = "someid";
        String testID2 = "someid2";
        UserProfile testUserProfile1 = new UserProfile(testID1, "Some Usersname");
        UserProfile testUserProfile2 = new UserProfile(testID2, "Some Usersname");
        

        testCon = Conference.createConference(conName,
                new Date(),
                subLimit,
                assLimit);
        
        paper1 = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})),
                "Title",
                testUserProfile1);
        
        paper2 = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})),
                "Title",
                testUserProfile1);
        
        paper3 = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})),
                "Title",
                testUserProfile1);
        
        paper4 = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})),
                "Title",
                testUserProfile1);
        
        paper5 = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})),
                "Title",
                testUserProfile1);
        
        paper6 = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})),
                "Title",
                testUserProfile2);
        
        paper7 = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})),
                "Title",
                testUserProfile2);
        
        paper8 = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})),
                "Title",
                testUserProfile2);
        
        paper9 = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})),
                "Title",
                testUserProfile2);
        
        papers.add(paper1);
        papers.add(paper2);
        papers.add(paper3);
        papers.add(paper4);
        papers.add(paper5);
        papers.add(paper6);
        papers.add(paper7);
        papers.add(paper8);
        papers.add(paper9);

	}  
    @Test
    public void test3PapersAssigned() throws IllegalOperationException {
    	UserProfile aReviewerProfile = new UserProfile(reviewerID, "Reviewer Guy");
    	for(int i = 0; i < 3; i++) {
    		testCon.getSubprogramRole().assignReviewer(
    				aReviewerProfile, 
    				papers.get(i)
    				);
    	}
        assertTrue(testCon.getInfo().getPapersAssignedToReviewer(aReviewerProfile).size() == 3);
    }

    @Test
    public void testExactLimit() throws IllegalOperationException {
    	UserProfile aReviewerProfile = new UserProfile(reviewerID, "Reviewer Jane");
    	for(int i = 0; i < 8; i++) {
    		testCon.getSubprogramRole().assignReviewer(
    				aReviewerProfile,
    				papers.get(i)
    				);
    	}
        assertTrue(testCon.getInfo().getPapersAssignedToReviewer(aReviewerProfile).size() == 8);
    }

    @Test(expected = IllegalOperationException.class)
    public void testOverLimit() throws IllegalOperationException {
    	UserProfile aUserProfile = new UserProfile(reviewerID, "Reviewer Dylan");
    	for(int i = 0; i < 9; i++) {
    		testCon.getSubprogramRole().assignReviewer(
    				aUserProfile,
    				papers.get(i)
    				);
    	}
    }
}
