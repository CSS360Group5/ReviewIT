package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
     * @author Dmitriy Bliznyuk
     */
    public void assignReviewer(
            final UserProfile theReviewerProfile,
            Paper thePaper
            ) throws IllegalOperationException {
        
        if(!myConferenceData.isReviewerInAssignmentLimit(theReviewerProfile) ||
                myConferenceData.isPaperAuthoredByReviewer(theReviewerProfile.getName(), thePaper)) {
            throw new IllegalOperationException("Cannot assign reviewer to paper");
        }
        
        // A Reviewer cannot be assigned until after the author submission deadline.
        Date assignReviewerDate = new Date();
        if(!assignReviewerDate.after(myConferenceData.getSubmissionDate())) {
        	throw new IllegalOperationException("Reviewer cannot be assigned before author submission deadline.");
        }
        
        if(!myConferenceData.getReviewerAssignmentMap().containsKey(theReviewerProfile)){
            myConferenceData.getReviewerAssignmentMap().put(theReviewerProfile, new ArrayList<>());
        }
        
        myConferenceData.addUserToRole(theReviewerProfile, Conference.REVIEW_ROLE);
        
        myConferenceData.getReviewerAssignmentMap().get(theReviewerProfile).add(thePaper);
    }
    
    /**
     * Submits a recommendation for thePaper.
     * @param theReviewerProfile the user submitting the recommendation
     * @param thePaper the paper to recommend for.
     * @throws IllegalOperationException if there aren't three reviews on the paper yet.
     */
    public void recommend(
            final UserProfile theSubProgramProfile,
            Paper thePaper
            ) throws IllegalOperationException {
    	
    	Map reviewerMap = myConferenceData.getReviewerAssignmentMap();
    	
    	int numberOfReviewers = 0;
    	
//    	Iterator iter = (Iterator) reviewerMap.keySet().iterator();
//    	while(iter.hasNext()) {
//    	    Map.Entry entry = (Map.Entry) iter.next();
//    	    
//    	    List<Paper> paperList = entry.getValue();	    
//    	    System.out.println(entry.getKey() + " - " + entry.getValue());
//    	}
    	
//    	for(final Entry<UserProfile, List<Paper>> currentUserPaperEntry: reviewerMap.entrySet()) {
//    		List<Paper> paperList = new ArrayList<>();
//			paperList.addAll(currentUserPaperEntry.getValue());
//		}
    	
    	//A manuscript recommendation cannot be submitted unless there exists a minimum of three reviews for this manuscript.
        
    }
}
