package model;

import java.util.Objects;

/**
 * A container class for holding basic information related to a UserProfile.
 * 
 * 
 * INVARIANT: getters return non-null objects.
 * Strings are non-empty.
 * 
 * Use note: theUID should be unique!
 * @author Dimitar Kumanov
 * @version 5/4/2017
 */
public class UserProfile {
	private String myUID;
	private String myName;
	

	/**
	 * Creates a UserProfile given theUID and theName.
	 * PRECONDITION: theUID or theName must be non-null and non-empty.
	 * @param theUID The UserID for this UserProfile. Should be non-null, non-empty and unique.
	 * @param theName The Name of the person of this UserProfile. Should be non-null, non-empty.
	 * @throws IllegalArgumentException precondition violated.
	 */
	public UserProfile(
			final String theUID,
			final String theName
			) throws IllegalArgumentException{
		myUID = Objects.requireNonNull(theUID);
		myName = Objects.requireNonNull(theName);
		if(myUID.isEmpty() || myName.isEmpty())
			throw new IllegalArgumentException();
	}
	
	/**
	 * Returns the (non-null, non-empty) user ID for this profile.
	 * ID should be unique
	 * @return the (non-null, non-empty)user ID for this profile. Should be unique.
	 */
	public String getUID(){
		return myUID;
	}
	
	
	/**
	 * Returns the (non-null, non-empty) name associated with this profile
	 * @return the (non-null, non-empty) name associated with this profile
	 */
	public String getName(){
		return myName;
	}
}
