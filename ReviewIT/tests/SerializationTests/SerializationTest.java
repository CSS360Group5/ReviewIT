package SerializationTests;

import TestResources.TestRSystem;
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
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * A test of the system's data persistence.
 *
 * @author Kevin Ravana
 * @version 5/7/2017.
 */
public class SerializationTest {

    private final int PAPER_SUBMISSION_LIMIT = 5;
    private final int PAPER_ASSIGNMENT_LIMIT = 8;

    private final String CONFERENCE_NAME = "THHDB - Food Preparation Under High-Risk and Adverse Conditions 2017";
    private final String USER_ID = "author@uw.edu";
    private final String AUTHOR_NAME = "Author John Doe";
    private final String PAPER_NAME =
            "On the Variety of Methodologies " +
            "Pertaining to the Production " +
            "and Consumption of the California " +
            "Roll Sushi Appetizer or Entre";

    private Conference myTestConference;
    private UserProfile myTestCurrentUser;
    private Paper myTestPaper;



    /**
     * Prior to each test:
     * A test Conference, UserProfile, and Paper are created and used
     * to invoke the functionality that allows a User to submit a
     * Paper to a Conference for Review. In the process, the
     * UserProfile, the Conference, and the Paper will be serialized.
     */
    @Before
    public void setup() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        try {
            myTestConference = Conference.createConference(CONFERENCE_NAME,
                    format.parse("2017/05/29 23:59:59"),
                    PAPER_SUBMISSION_LIMIT,
                    PAPER_ASSIGNMENT_LIMIT);
        } catch (IllegalArgumentException e) {
				e.printStackTrace();
        } catch (ParseException e) {
				e.printStackTrace();
        }

        myTestCurrentUser = new UserProfile(USER_ID, AUTHOR_NAME);

        myTestPaper = Paper.createPaper(new File(""),
                new ArrayList<>(Arrays.asList(AUTHOR_NAME)),
                PAPER_NAME,
                myTestCurrentUser);

        try {
            myTestConference.getUserRole().addPaper(myTestCurrentUser, myTestPaper);
        } catch (IllegalOperationException e) {
            System.err.println(e.getMessage());
        }

        TestRSystem.getInstance().addConference(myTestConference);
        TestRSystem.getInstance().addUserProfile(myTestCurrentUser);
        TestRSystem.getInstance().serializeModel();
    }

    /**
     * Tests constant fields against deserialized data.
     * One Serializable detail is tested per deserialized Object.
     */
    @Test
    public void deserializeModelTest() {
        TestRSystem.refreshInstance(); // Resets TestRSystem
        TestRSystem.getInstance().deserializeData();

        /*
        Below, the TestRSystem attempts to find deserialized Objects
        and their Serializable fields based on the given constant value.
         */

        final Conference deserializedConference =
                TestRSystem.getInstance().getConference(CONFERENCE_NAME);

        final String deserializedConferenceName =
                deserializedConference.getInfo().getName();

        final String deserializedUserID =
                TestRSystem.getInstance().getUserProfile(USER_ID).getUID();

        final List<Paper> deserializedPapers =
                TestRSystem.getInstance().getConference(CONFERENCE_NAME).getInfo().getPapersAuthoredBy(AUTHOR_NAME);

        final List<String> deserializedPaperTitles = new ArrayList<>();
        for (Paper p : deserializedPapers) {
            deserializedPaperTitles.add(p.getTitle());
        }

        /*
        Assertion that the deserialized Conference holds the same Name
        value that it had at serialization.
         */
        assertTrue(deserializedConferenceName.equals(CONFERENCE_NAME));

        /*
        Assertion that the deserialized UserProfile holds the same
        UserID value it had at serialization.
         */
        assertTrue(deserializedUserID.equals(USER_ID));

        /*
        Assertion that the deserialized Paper collection holds the same
        amount of papers as during its serialization.
         */
        assertTrue(deserializedPapers.size() == 1);

        /*
        Assertion that the deserialized Paper holds the same Title
        value that it had at serialization.
         */
        assertTrue(deserializedPaperTitles.get(0).equals(PAPER_NAME));
    }
}
