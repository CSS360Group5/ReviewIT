package model;

/**
 * A class containing all the functionality a Director has related to a Conference.
 * @author Dimitar Kumanov
 * @version 5/2/2017
 */
public class DirectorUtilities {
	private final ConferenceData myConferenceInfo;
    
	/**
	 * Creates a DirectorUtilities Object for a Conference. 
	 * @param theConferenceData The ConferenceData Object to manipulate.
	 */
    public DirectorUtilities(final ConferenceData theConferenceData){
    	myConferenceInfo = theConferenceData;
    }
    
	public void addUserRole(
			final UserProfile theUserProfile,
			final String theUserRole
			){
		myConferenceInfo.addUserToRole(theUserProfile, theUserRole);
	}
}
