package model.conference;

import java.util.ArrayList;

import model.IllegalOperationException;
import model.Paper;
import model.UserProfile;

/**
 * A class containing all the functionality a Subprogram Chair has related to a Conference.
 * @author Dimitar Kumanov
 * @version 5/2/2017
 */
public class SubprogramUtilities {
	
	private final ConferenceData myConferenceInfo;
    
	/**
	 * Creates a ReviewerUtilities Object for a Conference. 
	 * @param theConferenceData The ConferenceData Object to manipulate.
	 */
    public SubprogramUtilities(final ConferenceData theConferenceData){
    	myConferenceInfo = theConferenceData;
    }
    
    /**
     * Assigns a paper to a reviewer.
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
    	
    	if(!myConferenceInfo.isReviewerInAssignmentLimit(theReviewerProfile) ||
    			myConferenceInfo.isPaperAuthoredByReviewer(theReviewerProfile.getName(), thePaper)) {
    		throw new IllegalOperationException("Cannot assign reviewer to paper");
    	}
    	
    	if(!myConferenceInfo.getReviewerAssignmentMap().containsKey(theReviewerProfile)){
    		myConferenceInfo.getReviewerAssignmentMap().put(theReviewerProfile, new ArrayList<>());
    	}
    	myConferenceInfo.getReviewerAssignmentMap().get(theReviewerProfile).add(thePaper);
    }
}
