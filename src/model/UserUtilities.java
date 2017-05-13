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

    /**
     * Removes a paper from the conference that it was submitted to.
     * Will not remove a paper from a conference if that paper has any reviews.
     * @param theUserProfile the user whose paper is to be removed from the conference
     * @param thePaper the paper to be removed from the conference
     * @throws IllegalOperationException
     * @author Ian Jury
     */
    public void removePaper(final UserProfile theUserProfile, final Paper thePaper) throws IllegalOperationException {
    	//if a reviewer has been assigned, then we can't do anything
        if (myConferenceData.getReviewerAssignmentMap().containsValue(thePaper)) {
        	throw new IllegalOperationException("Paper cannot be removed because "
        										+ "at least one reviewer has been assigned to it");
        	
        } else { //otherwise, remove the paper
        	//Remove paper from submission map:
            removePaperFromSubmissionMap(theUserProfile, thePaper);
        	//Remove paper to author map:
        	removePaperFromAuthorshipMap(thePaper);    	
        }	
    }
    
    /**
     * Removes a paper from ConferenceData's paperSubmissionMap
     * @param theUserProfile the profile information of the user
     * @param thePaper the paper to be removed from submission map
     * @author Ian Jury
     */
    private void removePaperFromSubmissionMap(final UserProfile theUserProfile, final Paper thePaper) {
    	//if submission map has user profile, remove
    	if(myConferenceData.getPaperSubmissionMap().containsKey(theUserProfile)){
    		myConferenceData.getPaperSubmissionMap().get(theUserProfile).remove(thePaper);
    	}
    	
    }
    
    /**
     * Removes the paper to ConferenceData's paperAuthorshipMap.
     * @param thePaper the paper to be removed from the authorship map
     * @author Ian Jury
     */
    private void removePaperFromAuthorshipMap(final Paper thePaper) {
    	for(final String currentAuthor: thePaper.getAuthors()){
            if(myConferenceData.getPaperAuthorshipMap().containsKey(currentAuthor)){
            	myConferenceData.getPaperAuthorshipMap().get(currentAuthor).remove(thePaper);
            }           
        }  	
    }
}
