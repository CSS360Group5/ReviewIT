package tests;

import model.Conference;
import model.ErrorException;
import model.Paper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Kevin on 4/30/2017.
 */
class Rule1a {

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
    private final String userID = "someid";

    @BeforeEach
    void setUp() throws ParseException {

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
        int subLimit = 5;
        int assLimit = 8;

        testCon = Conference.createConference(conName,
                deadline,
                subLimit,
                assLimit);

        hourEarlyPaper = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})),
                "Title",
                "someid");
        secondEarlyPaper = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})),
                "Title",
                "someid");
        hourLatePaper = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})),
                "Title",
                "someid");
        secondLatePaper = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(new String[]{"John Doe", "Some Coauthor"})),
                "Title",
                "someid");

        /*
        Set submission dates of papers to be tested.
         */
        hourEarlyPaper.setSubmissionDate(hourEarly);
        secondEarlyPaper.setSubmissionDate(secondEarly);
        hourLatePaper.setSubmissionDate(hourLate);
        secondLatePaper.setSubmissionDate(secondLate);
    }

    @Test
    void testHourEarlySubmission() throws ErrorException {
        testCon.addPaper(userID, hourEarlyPaper);
        assertTrue(testCon.getPapersSubmittedBy(userID).size() == 1);
    }

    @Test
    void testSecondEarlySubmission() throws ErrorException {
        testCon.addPaper(userID, secondEarlyPaper);
        assertTrue(testCon.getPapersSubmittedBy(userID).size() == 1);
    }

    @Test
    void testHourLateSubmission() throws ErrorException {
        assertThrows(ErrorException.class, () -> {testCon.addPaper(userID, hourLatePaper);});
    }

    @Test
    void testSecondLateSubmission() throws ErrorException {
        assertThrows(ErrorException.class, () -> {testCon.addPaper(userID, secondLatePaper);});
    }

}