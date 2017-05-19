package tests;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import model.Conference;
import model.Paper;
import model.UserProfile;

/**
 * A class to test the business rules around a Reviewer being assigned for given deadlines.
 * 
 * @author Zachary Chandler
 */
public class ReviewerDeadlineAssignTests {
    
    /**
     * conferences for testing.
     */
    private Conference beforeDeadlineConference;
    private Conference afterDeadlineConference;
    
    /**
     * sample papers to test the 8 paper assignment limit.
     */
    private Paper paper1;
    private Paper paper2;

    @Before
    public void setUp() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date hourEarly = format.parse("2017/04/30 20:00:00");
        Date hourLate = format.parse("2017/04/30 21:00:00");

        Date yearLaterHourEarly = format.parse("2018/04/30 20:00:00");
        Date yearLaterHourLate = format.parse("2018/04/30 21:00:00");
        
        int authorSubmitLimit = 5;
        int reviewwerAssignLimit = 8;
        String testID1 = "someid";
        UserProfile testUserProfile1 = new UserProfile(testID1, "Some Usersname");

        afterDeadlineConference = Conference.createConference("Before Deadline Conference",
                hourLate,
                authorSubmitLimit,
                reviewwerAssignLimit);
        
        beforeDeadlineConference = Conference.createConference("After Deadline Conference",
                yearLaterHourLate,
                authorSubmitLimit,
                reviewwerAssignLimit);

        paper1 = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})),
                "Title",
                testUserProfile1);
        
        paper2 = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})),
                "Title",
                testUserProfile1);

        paper1.setSubmissionDate(hourEarly);
        paper2.setSubmissionDate(yearLaterHourEarly);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testEarlyAssignement_ThrowsException() throws IllegalArgumentException {
        UserProfile aReviewerProfile = new UserProfile("Reviewer Joe", "Reviewer Guy");
        beforeDeadlineConference.getSubprogramRole().assignReviewer(
                aReviewerProfile, 
                paper2
                );
    }

    @Test
    public void testHourLateSubmission_IsAssigned() throws IllegalArgumentException {
        UserProfile aReviewerProfile = new UserProfile("Reviewer Joe", "Reviewer Guy");
        afterDeadlineConference.getSubprogramRole().assignReviewer(
                aReviewerProfile, 
                paper1
                );
    }

}
