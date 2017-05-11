package tests;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import model.Conference;
import model.IllegalOperationException;
import model.Paper;
import model.UserProfile;

/**
 * A class to tests the business rules for submitting a recommendation.
 * 
 * @author Zachary Chandler
 */
public class ReccomendationTests {

    Conference pastDeadlineConference;
    private Paper testPaper1;

    /* Mock user profile used to represent an author submitting papers to a conference.*/
    private static final UserProfile AUTHOR_PROFILE1 = new UserProfile("UID1", "Some Name1");
    private static final UserProfile SUBPROGRAMCHAIR_PROFILE1 = new UserProfile("UID2", "Some Name2");
    private static final UserProfile REVIEWER_PROFILE1 = new UserProfile("UID3", "Some Name3");
    private static final UserProfile REVIEWER_PROFILE2 = new UserProfile("UID4", "Some Name4");
    private static final UserProfile REVIEWER_PROFILE3 = new UserProfile("UID5", "Some Name5");
   
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

        pastDeadlineConference.getSubprogramRole().assignReviewer(REVIEWER_PROFILE1, testPaper1);
        pastDeadlineConference.getSubprogramRole().assignReviewer(REVIEWER_PROFILE2, testPaper1);
        pastDeadlineConference.getSubprogramRole().assignReviewer(REVIEWER_PROFILE3, testPaper1);
    }

    /**
     * Should throw exception, because no recommendations have been given.
     * @throws IllegalOperationException
     */
    @Test (expected = IllegalOperationException.class)
    public void giveReview_WithoutReviews_ThrowsException() throws IllegalOperationException {
        pastDeadlineConference.getSubprogramRole().recommend(SUBPROGRAMCHAIR_PROFILE1, testPaper1);
    }
    
    /**
     * Should throw exception, because only two recommendations have been given.
     * @throws IllegalOperationException
     */
    @Test (expected = IllegalOperationException.class)
    public void giveReview_WithTwoReviews_ThrowsException() throws IllegalOperationException {
        pastDeadlineConference.getReviewerRole().sendReview(REVIEWER_PROFILE1, testPaper1, "review1");
        pastDeadlineConference.getReviewerRole().sendReview(REVIEWER_PROFILE2, testPaper1, "review2");
        pastDeadlineConference.getSubprogramRole().recommend(SUBPROGRAMCHAIR_PROFILE1, testPaper1);
    }

    /**
     * Should not throw an exception because we have enough recommendations.
     * @throws IllegalOperationException
     */
    @Test
    public void giveReview_WithThreeReviews_NoException() throws IllegalOperationException {
        pastDeadlineConference.getReviewerRole().sendReview(REVIEWER_PROFILE1, testPaper1, "review1");
        pastDeadlineConference.getReviewerRole().sendReview(REVIEWER_PROFILE2, testPaper1, "review2");
        pastDeadlineConference.getReviewerRole().sendReview(REVIEWER_PROFILE3, testPaper1, "review3");
        pastDeadlineConference.getSubprogramRole().recommend(SUBPROGRAMCHAIR_PROFILE1, testPaper1);
    }
}
