/**
 * 
 */
package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.UserProfile;

/**
 * @author Kevin Nguyen
 *
 */
public class UserProfileTest {
	private UserProfile Kevin;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		Kevin = new UserProfile("kvn96", "Kevin Nguyen");
	}


	@Test
	public final void UserProfile_getsUserIDshouldWorkIDiskvn96() throws IllegalArgumentException{
		assertEquals(Kevin.getUID(), "kvn96");
		assertNotEquals(Kevin.getUID(), "brianG");
	}

	@Test
	public final void UserProfile_getsUserNamehouldWorkNameisKevinNguyen() throws IllegalArgumentException{
		assertEquals(Kevin.getName(), "Kevin Nguyen");
		assertNotEquals("Zach", Kevin.getName());
	}
	@Test
	public final void UserProfile_testEqualsAndHashCodeSymmetricShouldWork() throws IllegalArgumentException{
	    UserProfile x = new UserProfile("kvn96", "Kevin Nguyen");  // equals and hashCode check name field value
	    UserProfile y = new UserProfile("kvn96", "Kevin Nguyen");
	    assertTrue(x.equals(y) && y.equals(x));
	    assertTrue(x.hashCode() == y.hashCode());
	}
}
