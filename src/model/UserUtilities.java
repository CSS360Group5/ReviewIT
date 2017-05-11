package model;

import java.util.ArrayList;

/**
 * A class containing all the functionality any User has related to a Conference.
 * @author Dimitar Kumanov
 * @version 5/2/2017
 */
public class UserUtilities {
	private final ConferenceData myConferenceData;
    
	/**
	 * Creates a UserUtilities Object for a Conference. 
	 * @param theConferenceData The ConferenceData Object to manipulate.
	 */
    public UserUtilities(final ConferenceData theConferenceData){
    	myConferenceData = theConferenceData;
    }
    
	/**
	 * Adds a Paper to the associated Conference.
	 * Also adds theUserProfile as an Author for this Conference if not added already.
	 * PRECONDITION: thePaper is isPaperInAuthorSubmissionLimit()
	 * AND isPaperInSubmissionDeadline()
	 * @param theUserProfile The UserProfile of the user submitting the paper.
	 * @param thePaper The paper being submitted.
	 * @throws IllegalOperationException If the precondition is violated.
	 * @author Kevin Ravana
	 * @author Dimitar Kumanov
	 */
    public void addPaper(
    		final UserProfile theUserProfile,
    		final Paper thePaper
    		) throws IllegalOperationException {
    	if(!myConferenceData.isPaperInAuthorSubmissionLimit(thePaper)){
    		throw new IllegalOperationException("Paper exceeds paper submission limit.");
    	}
    	else if(!myConferenceData.isPaperInSubmissionDeadline(thePaper)){
    		throw new IllegalOperationException("Paper exceeds submission deadline.");
    	}
    	//Add paper to submission map:
        addPaperToSubmissionMap(theUserProfile, thePaper);

    	//Add paper to author map:
    	addPaperToAuthorshipMap(thePaper);
    	
    	myConferenceData.addUserToRole(theUserProfile, Conference.AUTHOR_ROLE);
    }
    
    /**
     * Adds paper to ConferenceData's paperSubmissionMap
     * If the Author has not yet submitted a paper
     * to this Conference, the Author's userID will be put into the map along with a
     * list for storing their papers. If a paper with the same name has already been
     * submitted, it will be replaced. (Add prompt to ask user if this should occur).
     * @param theUserID
     * @param thePaper
     * @author Kevin Ravana
     * @author Dimitar Kumanov
     */
    private void addPaperToSubmissionMap(final UserProfile theUserProfile, final Paper thePaper){
    	if(!myConferenceData.getPaperSubmissionMap().containsKey(theUserProfile)){
    		myConferenceData.getPaperSubmissionMap().put(theUserProfile, new ArrayList<>());
    	}
    	myConferenceData.getPaperSubmissionMap().get(theUserProfile).add(thePaper);
    }
    
    /**
     * Adds the paper to ConferenceData's paperAuthorshipMap.
     * This is a map that records all coauthors of
     * a particular paper.
     * @param thePaper
     * @author Kevin Ravana
     * @author Dimitar Kumanov
     */
    private void addPaperToAuthorshipMap(final Paper thePaper) {
        for(final String currentAuthor: thePaper.getAuthors()){
            if(!myConferenceData.getPaperAuthorshipMap().containsKey(currentAuthor)){
            	myConferenceData.getPaperAuthorshipMap().put(currentAuthor, new ArrayList<>());
            }
            myConferenceData.getPaperAuthorshipMap().get(currentAuthor).add(thePaper);
        }
    }

    public void removePaper(
            final UserProfile theUserProfile,
            final Paper thePaper
            ) throws IllegalOperationException {
        // TODO Auto-generated method stub
        
    }
}
