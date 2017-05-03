package model.conference;

import java.util.ArrayList;

import model.ErrorException;
import model.Paper;
import model.UserProfile;

/**
 * A class containing all the functionality a Reviewer has related to a Conference.
 * @author Dimitar Kumanov
 * @version 5/2/2017
 */
public class ReviewRole {
	
	private final ConferenceData myConferenceInfo;
    
    public ReviewRole(final ConferenceData theConferenceInfo){
    	myConferenceInfo = theConferenceInfo;
    }
    
    /**
     * Assigns a paper to a reviewer
     * 
     * PRECONDITION: isPaperInReviewerAssignmentLimit and !isPaperAuthoredByReviewer
     * @param theReviewerID the ID 
     * @param thePaper the paper object to be assigned to a reviewer.
     * @exception Precondition violated
     * 
     * @author Danielle Lambion
     * @author Dimitar Kumanov
     */
    public void assignReviewer(final UserProfile theReviewerProfile,
//    		final String theReviewerID,
//    							final String theReviewerName,
    							Paper thePaper) throws ErrorException {
    	if(!myConferenceInfo.isReviewerInAssignmentLimit(theReviewerProfile) ||
    			myConferenceInfo.isPaperAuthoredByReviewer(theReviewerProfile.getName(), thePaper)) {
    		throw new ErrorException("Cannot assign reviewer to paper");
    	}
    	
    	if(!myConferenceInfo.getReviewerAssignmentMap().containsKey(theReviewerProfile)){
    		myConferenceInfo.getReviewerAssignmentMap().put(theReviewerProfile, new ArrayList<>());
    	}
    	myConferenceInfo.getReviewerAssignmentMap().get(theReviewerProfile).add(thePaper);
    }
}
