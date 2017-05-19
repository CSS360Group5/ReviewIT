package tests;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import model.Conference;
import model.Paper;
import model.UserProfile;

/**
 * A class to test the business rules around an Author removing a paper.
 * 
 * @author Zachary Chandler
 */
public class AuthorRemovePaperTests {

    Conference pastDeadlineConference;
    private Paper testPaper1;

    /* Mock user profile used to represent an author submitting papers to a conference.*/
    private static final UserProfile AUTHOR_PROFILE1 = new UserProfile("UID1", "Some Name1");
    private static final UserProfile REVIEWER_PROFILE1 = new UserProfile("UID3", "Some Name3");
   
    @Before
    public void setUp() throws Exception {
        Date now = new Date();
        Date before = new Date(now.getTime() - (1000L * 60 * 60));
        Date aBitBeforeBefore = new Date(before.getTime() - 1);
        
        pastDeadlineConference = Conference.createConference("Conference Name", before, 8, 5);
        
        testPaper1 = Paper.createPaper(new File(""), 
                new ArrayList<>(Arrays.asList(new String[]{"author1", "author2"})), "title", AUTHOR_PROFILE1);
        
        testPaper1.setSubmissionDate(aBitBeforeBefore);
        pastDeadlineConference.getUserRole().addPaper(AUTHOR_PROFILE1, testPaper1);
    }

    /**
     * Should not throw an exception, because no reviewer has been set.
     * @throws IllegalArgumentException
     */
    @Test 
    public void removePaper_WithoutReviewer_NoExceptions() throws IllegalArgumentException {
        pastDeadlineConference.getUserRole().removePaper(AUTHOR_PROFILE1, testPaper1);
    }
    
    /**
     * Should not throw an exception, because no reviewer has been set.
     * @throws IllegalArgumentException
     */
    @Test (expected = IllegalArgumentException.class)
    public void removePaper_WithReviewer_ThrowsExceptions() throws IllegalArgumentException {
        pastDeadlineConference.getSubprogramRole().assignReviewer(REVIEWER_PROFILE1, testPaper1);
        pastDeadlineConference.getUserRole().removePaper(AUTHOR_PROFILE1, testPaper1);
    }
}
