package model;

import java.util.ArrayList;

/**
 * A class containing all the functionality a Subprogram Chair has related to a Conference.
 * @author Dimitar Kumanov
 * @version 5/2/2017
 */
public class SubprogramUtilities {
	
	private final ConferenceData myConferenceData;
    
	/**
	 * Creates a ReviewerUtilities Object for a Conference. 
	 * @param theConferenceData The ConferenceData Object to manipulate.
	 */
    public SubprogramUtilities(final ConferenceData theConferenceData){
    	myConferenceData = theConferenceData;
    }
    
    /**
     * Assigns a paper to a reviewer.
     * Also adds theReviewerProfile to the Reviewers for this Conference. 
     * 
     * PRECONDITION: isPaperInReviewerAssignmentLimit and !isPaperAuthoredByReviewer
     * @param theReviewerID the ID 
     * @param thePaper the paper object to be assigned to a reviewer.
     * @exception Precondition violated
     * 
     * @author Danielle Lambion
     * @author Dimitar Kumanov
     */
    public void assignReviewer(
    		final UserProfile theReviewerProfile,
    		Paper thePaper
    		) throws IllegalOperationException {
    	
    	if(!myConferenceData.isReviewerInAssignmentLimit(theReviewerProfile) ||
    			myConferenceData.isPaperAuthoredByReviewer(theReviewerProfile.getName(), thePaper)) {
    		throw new IllegalOperationException("Cannot assign reviewer to paper");
    	}
    	
    	if(!myConferenceData.getReviewerAssignmentMap().containsKey(theReviewerProfile)){
    		myConferenceData.getReviewerAssignmentMap().put(theReviewerProfile, new ArrayList<>());
    	}
    	
    	myConferenceData.addUserToRole(theReviewerProfile, Conference.REVIEW_ROLE);
    	
    	myConferenceData.getReviewerAssignmentMap().get(theReviewerProfile).add(thePaper);
    }
}
