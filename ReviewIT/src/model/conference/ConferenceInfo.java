package model.conference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Paper;
import model.UserProfile;

public class ConferenceInfo {
	private final String myConferenceName;
    /**
     * Maps a Submitter's UserID to a Paper.
     */
    private final Map<String, List<Paper>> myPaperSubmissionMap;
    /**
     * Maps an Author/Coauthor's name to a Paper.
     */
    private final Map<String, List<Paper>> myPaperAuthorshipMap;
    /**
     * Maps a Reviewer's UserID to a Paper.
     */
    private final Map<UserProfile, List<Paper>> myReviewerAssignmentMap;
    /**
     * Maps a Subprogram Chair's UserID to a Paper.
     */
    private final Map<String, List<Paper>> mySubprogramAssignmentMap;
    /**
     * Maps a User's UserID to a Role. 
     */
    private final Map<UserProfile, List<String>> myUserRoleMap;
    
    private final Date myPaperSubmissionDeadline;
    private final int myPaperSubmissionLimit;
    private final int myReviewerAssignmentLimit;
	
    
    public ConferenceInfo(final String theConferenceName,
            final Date thePaperDeadline,
            final int thePaperSubmissionLimit,
            final int thePaperAssignmentLimit,
    		final Map<String, List<Paper>> thePaperSubmissionMap,
    		final Map<String, List<Paper>> thePaperAuthorshipMap,
    		final Map<UserProfile, List<Paper>> theReviewerAssignmentMap,
    		final Map<String, List<Paper>> theSubprogramAssignmentMap,
    		final Map<UserProfile, List<String>> theUserRoleMap
    		) {
    	myConferenceName = theConferenceName;
    	myPaperSubmissionDeadline = thePaperDeadline;
    	myPaperSubmissionLimit = thePaperSubmissionLimit;
    	myReviewerAssignmentLimit = thePaperAssignmentLimit;
    	myPaperSubmissionMap = thePaperSubmissionMap;
    	myPaperAuthorshipMap = thePaperAuthorshipMap;
    	myReviewerAssignmentMap = theReviewerAssignmentMap;
    	mySubprogramAssignmentMap = theSubprogramAssignmentMap;
    	myUserRoleMap = theUserRoleMap;
    }
    /**
     * Acquires all the papers assigned to a reviewer
     * for a specific conference.
     * 
     * @param theReviewerUserID  The Reviewer's user ID.
     * @return an ArrayList of papers assigned to this reviewer.
     * 
     * @author Danielle Lambion
     * @author Dimitar Kumanov
     */
    public List<Paper> getPapersAssignedToReviewer(final String theReviewerUserID) {
    	
    	final List<Paper> papersAssignedForReview;
    	if(myReviewerAssignmentMap.containsKey(theReviewerUserID))
    		papersAssignedForReview = myReviewerAssignmentMap.get(theReviewerUserID);
    	else{
    		papersAssignedForReview = new ArrayList<>();
    	}
        return papersAssignedForReview;
    }
    
    /**
     * 
     * @param thePaper
     * @param theReviewerUserID
     * @return true iff all the number of Papers assigned
     * for review are strictly less than the reviewer assignment limit.
     * @author Dimitar Kumanov
     */
    public boolean isPaperInReviewerAssignmentLimit(final String theReviewerUserID,
    												final Paper thePaper){
    	return getPapersAssignedToReviewer(theReviewerUserID).size() < myReviewerAssignmentLimit;
    }
    
    /**
     * 
     * @param theReviewerName
     * @param thePaper
     * @return
     * @author Dimitar Kumanov
     */
    public boolean isPaperAuthoredByReviewer(final String theReviewerName,
    											final Paper thePaper){
    	boolean result = false;
    	
    	for(final String currentAuthor: thePaper.getAuthors()){
    		if(currentAuthor.equals(theReviewerName)){
    			result = true;
    			break;
    		}
    	}
    	return result;
 
    }
	/**
	 * @return the myPaperSubmissionMap
	 */
	protected Map<String, List<Paper>> getMyPaperSubmissionMap() {
		return myPaperSubmissionMap;
	}
	/**
	 * @return the myPaperAuthorshipMap
	 */
	protected Map<String, List<Paper>> getMyPaperAuthorshipMap() {
		return myPaperAuthorshipMap;
	}
	
    /**
     * Maps a Reviewer's UserID to a Paper.
     */
    protected Map<UserProfile, List<Paper>> getReviewerAssignmentMap(){
    	return myReviewerAssignmentMap;
    }
	/**
	 * @return the mySubprogramAssignmentMap
	 */
	protected Map<String, List<Paper>> getMySubprogramAssignmentMap() {
		return mySubprogramAssignmentMap;
	}
	/**
	 * @return the myUserRoleMap
	 */
	protected Map<UserProfile, List<String>> getMyUserRoleMap() {
		return myUserRoleMap;
	}
}
