package tests;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import model.Conference;

public class ConferenceTest {

    
    Conference normalConference;
    
    @Before
    public void setUp() throws Exception {
        normalConference = Conference.createConference("Name", new Date(), 5, 8);
    }

    @Test
    public void testCreateConference_normalConference_ValuesSet() {
        assertEquals(normalConference.getInfo().getName(), "Name");
        assertEquals(normalConference.getInfo().getSubmissionDate(), new Date());
    }
    
    @Test
    public void testCreateConference() {
        Conference.createConference("Name", new Date(), 5, 8);
    }

    @Test
    public void testGetInfo() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetUserRole() {
        fail("Not yet implemented");
    }

}
