package model.conference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.ErrorException;
import model.Paper;
import model.UserProfile;

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
    	if(!myConferenceInfo.isPaperInReviewerAssignmentLimit(theReviewerProfile, thePaper) ||
    			myConferenceInfo.isPaperAuthoredByReviewer(theReviewerProfile.getName(), thePaper)) {
    		throw new ErrorException("Cannot assign reviewer to paper");
    	}
    	
    	if(!myConferenceInfo.getReviewerAssignmentMap().containsKey(theReviewerProfile)){
    		myConferenceInfo.getReviewerAssignmentMap().put(theReviewerProfile, new ArrayList<>());
    	}
    	myConferenceInfo.getReviewerAssignmentMap().get(theReviewerProfile).add(thePaper);
    }
}
