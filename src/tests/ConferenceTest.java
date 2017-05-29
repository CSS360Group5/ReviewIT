package tests;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import model.Conference;

public class ConferenceTest {

    
    Conference normalConference;
    Date now;
    
    @Before
    public void setUp() throws Exception {
        now = new Date();
        normalConference = Conference.createConference("Name", now, 5, 8);
    }

    @Test
    public void testCreateConference_normalConference_ValuesSet() {
        assertEquals(normalConference.getInfo().getName(), "Name");
        assertEquals(normalConference.getInfo().getSubmissionDate(), now);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateConference_NullName_NullPointerException() {
        Conference.createConference(null, now, 5, 8);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateConference_NullDate_NullPointerException() {
        Conference.createConference("Name", null, 5, 8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateConference_NegativeMaxSubmitions_IllegalArgumentException() {
        Conference.createConference("Name", now, -1, 8);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateConference_ZeroMaxSubmitions_IllegalArgumentException() {
        Conference.createConference("Name", now, 0, 8);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateConference_NegativeMaxAssignedPapers_IllegalArgumentException() {
        Conference.createConference("Name", now, 5, -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateConference_ZeroMaxAssignedPapers_IllegalArgumentException() {
        Conference.createConference("Name", now, 5, 0);
    }

}
