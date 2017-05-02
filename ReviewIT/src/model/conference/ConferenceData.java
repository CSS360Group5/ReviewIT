package model.conference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Paper;
import model.UserProfile;

public class ConferenceData implements ConferenceInfo{
	private final String myConferenceName;
    private final Date myPaperSubmissionDeadline;
    private final int myPaperSubmissionLimit;
    private final int myReviewerAssignmentLimit;
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
    
    protected ConferenceData(final String theConferenceName,
            final Date thePaperDeadline,
            final int thePaperSubmissionLimit,
            final int thePaperAssignmentLimit) {
		myPaperSubmissionMap = new HashMap<>();
		myPaperAuthorshipMap = new HashMap<>();
		myReviewerAssignmentMap = new HashMap<>();
		mySubprogramAssignmentMap = new HashMap<>();
		myUserRoleMap = new HashMap<>();
		myConferenceName = theConferenceName;
		myPaperSubmissionDeadline = thePaperDeadline;
		myPaperSubmissionLimit = thePaperSubmissionLimit;
		myReviewerAssignmentLimit = thePaperAssignmentLimit;
    }
    
	/**
	* A getter for the Conference name
	* @return the conference name
	* @author Dimitar Kumanov
	*/
	public String getName(){
		return myConferenceName;
	}
	
    /**
     * A method to acquire all papers submitted by the User with theUserID
     * @param theUserID The name of the Author to match with.
     * @return A list of all papers in this conference submitted by the User.
     * Returns an empty list if no papers found.
     * @author Kevin Ravana
     * @author Dimitar Kumanov 
     */
    public List<Paper> getPapersSubmittedBy(final String theUserID) {
    	final List<Paper> submittedPapers;
    	if(myPaperSubmissionMap.containsKey(theUserID))
    		submittedPapers = myPaperSubmissionMap.get(theUserID);
    	else{
    		submittedPapers = new ArrayList<>();
    	}
        return submittedPapers;
    }
    
    /**
     * A method to acquire all papers authored or
     * coauthored by the author with theAuthorName
     * @param theAuthorName The name of the Author to match with.
     * @return A list of all papers in this conference authored by the author.
     * Returns an empty list if no papers found.
     * @author Kevin Ravana
     * @author Dimitar Kumanov 
     */
    public List<Paper> getPapersAuthoredBy(final String theAuthorName) {
    	final List<Paper> authoredPapers;
    	if(myPaperAuthorshipMap.containsKey(theAuthorName))
    		authoredPapers = myPaperAuthorshipMap.get(theAuthorName);
    	else{
    		authoredPapers = new ArrayList<>();
    	}
        return authoredPapers;
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
    public List<Paper> getPapersAssignedToReviewer(final UserProfile theReviewerProfile) {
    	
    	final List<Paper> papersAssignedForReview;
    	if(myReviewerAssignmentMap.containsKey(theReviewerProfile))
    		papersAssignedForReview = myReviewerAssignmentMap.get(theReviewerProfile);
    	else{
    		papersAssignedForReview = new ArrayList<>();
    	}
        return papersAssignedForReview;
    }
    
    /**
     * @param thePaper The paper to check for.
     * @return true iff all the Paper submitted to this Conference by all
     * authors of thePaper is strictly less than
     * paper submission limit for the conference.
     * @author Dimitar Kumanov
     */
    public boolean isPaperInAuthorSubmissionLimit(final Paper thePaper){
    	boolean result = true;
    	
    	for(final String currentAuthor: thePaper.getAuthors()){
    		if(getPapersAuthoredBy(currentAuthor).size() >= myPaperSubmissionLimit){
    			result = false;
    			break;
    		}
    	}
    	return result;
    }
    
    
    
    /**
     * @param thePaper The paper to check for.
     * @return true iff thePaper's submission date
     * is strictly before the Conference submission deadline.
     * @author Dimitar Kumanov
     */
    public boolean isPaperInSubmissionDeadline(final Paper thePaper){
    	return thePaper.getSubmitDate().before(myPaperSubmissionDeadline);
    }
    
    /**
     * 
     * @param thePaper
     * @param theReviewerUserID
     * @return true iff all the number of Papers assigned
     * for review are strictly less than the reviewer assignment limit.
     * @author Dimitar Kumanov
     */
    public boolean isPaperInReviewerAssignmentLimit(
    		final UserProfile theReviewerProfile,
    		final Paper thePaper
    		){
    	return getPapersAssignedToReviewer(theReviewerProfile).size() < myReviewerAssignmentLimit;
    }
    
    /**
     * 
     * @param theReviewerName
     * @param thePaper
     * @return
     * @author Dimitar Kumanov
     */
    public boolean isPaperAuthoredByReviewer(
    		final String theReviewerName,
    		final Paper thePaper
    		){
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
	protected Map<String, List<Paper>> getPaperSubmissionMap() {
		return myPaperSubmissionMap;
	}
	/**
	 * @return the myPaperAuthorshipMap
	 */
	protected Map<String, List<Paper>> getPaperAuthorshipMap() {
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
	protected Map<String, List<Paper>> getSubprogramAssignmentMap() {
		return mySubprogramAssignmentMap;
	}
	/**
	 * @return the myUserRoleMap
	 */
	protected Map<UserProfile, List<String>> getUserRoleMap() {
		return myUserRoleMap;
	}
}
