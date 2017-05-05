package business_rules;

import model.IllegalOperationException;
import model.Paper;
import model.UserProfile;
import model.conference.Conference;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertTrue;


/**
 * A Test unit for business Rule 1A:
 * An author cannot submit a paper after the submission deadline has passed.
 * @author Kevin Ravana
 * @version 04/30/2017
 */
public class Rule1a {

    /**
     * Conference Object containing method to be tested.
     */
    private Conference testCon;

    /*
     Paper Objects with varying submission dates to be tested.
     */
    private Paper hourEarlyPaper;
    private Paper secondEarlyPaper;
    private Paper hourLatePaper;
    private Paper secondLatePaper;

    /**
     * ID of the user submitting a paper.
     */
    private final UserProfile userProfile = new UserProfile("some id", "Some Person Name");

    @Before
    public void setUp() throws ParseException {

        /*
        Create date format and host of dates to be tested.
         */
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date hourEarly = format.parse("2017/04/30 20:00:00");
        Date secondEarly = format.parse("2017/04/30 23:59:58");
        Date hourLate = format.parse("2017/05/01 01:00:00");
        Date secondLate = format.parse("2017/05/01 00:00:00");
        Date deadline = format.parse("2017/04/30 23:59:59");

        String conName = "On the Testing of Dates and Times";
        String[] authorArray = {"John Doe", "Some Coauthor"};
        int subLimit = 5;
        int assLimit = 8;

        testCon = Conference.createConference(conName,
                deadline,
                subLimit,
                assLimit);

        hourEarlyPaper = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(authorArray)),
                "Title",
                userProfile);
        secondEarlyPaper = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(authorArray)),
                "Title",
                userProfile);
        hourLatePaper = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(authorArray)),
                "Title",
                userProfile);
        secondLatePaper = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(authorArray)),
                "Title",
                userProfile);

        /*
        Set submission dates of papers to be tested.
         */
        hourEarlyPaper.setSubmissionDate(hourEarly);
        secondEarlyPaper.setSubmissionDate(secondEarly);
        hourLatePaper.setSubmissionDate(hourLate);
        secondLatePaper.setSubmissionDate(secondLate);
    }

    @Test
    public void testHourEarlySubmission() throws IllegalOperationException {
        testCon.getUserRole().addPaper(userProfile, hourEarlyPaper);
        assertTrue(testCon.getInfo().getPapersSubmittedBy(userProfile).size() == 1);
    }

    @Test
    public void testSecondEarlySubmission() throws IllegalOperationException {
        testCon.getUserRole().addPaper(userProfile, secondEarlyPaper);
        assertTrue(testCon.getInfo().getPapersSubmittedBy(userProfile).size() == 1);
    }

    @Test (expected = IllegalOperationException.class)
    public void testHourLateSubmission() throws IllegalOperationException {
        testCon.getUserRole().addPaper(userProfile, hourLatePaper);
    }

    @Test (expected = IllegalOperationException.class)
    public void testSecondLateSubmission() throws IllegalOperationException {
        testCon.getUserRole().addPaper(userProfile, secondLatePaper);
    }
}