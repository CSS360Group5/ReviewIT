package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

/**
 * A class for specifically holding all the Data related to a Conference.
 * Implements ConferenceInfo which allows this ConferenceData to serve as a public interface
 * which client code can interact with.
 * Allows protected level access of internal data structures for modification purposes.
 * @author Dimitar Kumanov
 * @version 5/2/2017
 */
public class ConferenceData implements ConferenceInfo, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3694105038281388854L;
	private final String myConferenceName;
    private final Date myPaperSubmissionDeadline;
    private final int myPaperSubmissionLimit;
    private final int myReviewerAssignmentLimit;
    /**
     * Maps a UserProfile to a Paper.
     */
    private final Map<UserProfile, List<Paper>> myPaperSubmissionMap;
    /**
     * Maps an Author/Coauthor's name to a Paper.
     */
    private final Map<String, List<Paper>> myPaperAuthorshipMap;
    /**
     * Maps a Reviewer's UserProfile to a Paper.
     */
    private final Map<UserProfile, List<Paper>> myReviewerAssignmentMap;
    /**
     * Maps a Subprogram Chair's UserProfile to a Paper.
     */
    private final Map<UserProfile, List<Paper>> mySubprogramAssignmentMap;
    /**
     * Maps a User's UserProfile to a Role. 
     */
    private final Map<UserProfile, List<String>> myUserRoleMap;
    
    public ConferenceData(final String theConferenceName,
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
	
	@Override
	public Date getSubmissionDate() {
		return myPaperSubmissionDeadline;
	}
	
	/**
	 * A getter method for  all user roles
	 * associated with theUserProfile for this Conference.
	 * @return A list of roles associated with theUserProfile for this Conference.
	 * returns an empty list if no roles are associated with theUserProfile. 
	 */
	@Override
	public List<String> getUserRoles(final UserProfile theUserProfile) {
		final List<String> userRoles;
    	if(myUserRoleMap.containsKey(theUserProfile))
    		userRoles = myUserRoleMap.get(theUserProfile);
    	else{
    		userRoles = new ArrayList<>();
    	}
        return userRoles;
	}
	
    /**
     * A method to acquire all papers submitted by the User with theUserProfile
     * @param theUserProfile The UserProfile of the submitter to match with.
     * @return A list of all papers in this conference submitted by the User.
     * Returns an empty list if no papers found.
     * @author Kevin Ravana
     * @author Dimitar Kumanov 
     */
    public List<Paper> getPapersSubmittedBy(final UserProfile theUserProfile) {
    	final List<Paper> submittedPapers;
    	if(myPaperSubmissionMap.containsKey(theUserProfile))
    		submittedPapers = myPaperSubmissionMap.get(theUserProfile);
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
     * @param theReviewerProfile  The Reviewer's theReviewerProfile
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
     * Checks whether thePaper is within the submission limit of all its authors.
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
    
	@Override
	public boolean isSubmissionOpen(final Date theDate) {
		return theDate.before(myPaperSubmissionDeadline);
	}
    
    /**
     * Checks whether thePaper is within the submission deadline of this Conference.
     * @param thePaper The paper to check for.
     * @return true iff thePaper's submission date
     * is strictly before the Conference submission deadline.
     * @author Dimitar Kumanov
     */
    public boolean isPaperInSubmissionDeadline(final Paper thePaper){
    	return thePaper.getSubmitDate().before(myPaperSubmissionDeadline);
    }
    
    /**
     * Checks whether the Reviewer is within this Conference's assignment limit.
     * @param theReviewerProfile The UserProfile corresponding to the Reviewer.
     * @return true iff all the number of Papers assigned
     * for review are strictly less than the reviewer assignment limit.
     * @author Dimitar Kumanov
     */
    public boolean isReviewerInAssignmentLimit(final UserProfile theReviewerProfile){
    	return getPapersAssignedToReviewer(theReviewerProfile).size() < myReviewerAssignmentLimit;
    }
    
    /**
     * Checks whether any of the authors of thePaper  match with theReviewerName.
     * @param theReviewerName The name of the Reviewer to check for.
     * @param thePaper the Paper to check for
     * @return true iff no author of thePaper matches theReviewerName
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
    @Override
	public boolean isUserAuthor(final UserProfile theUserProfile) {
		if(!myUserRoleMap.containsKey(theUserProfile)){
			return false; //User doesn't have ANY role
		}
		return myUserRoleMap.get(theUserProfile).contains(Conference.AUTHOR_ROLE);
	}

	@Override
	public boolean isUserReviewer(final UserProfile theUserProfile) {
		if(!myUserRoleMap.containsKey(theUserProfile)){
			return false; //User doesn't have ANY role
		}
		return myUserRoleMap.get(theUserProfile).contains(Conference.REVIEW_ROLE);
	}

	@Override
	public boolean isUserSubprogramChair(final UserProfile theUserProfile) {
		if(!myUserRoleMap.containsKey(theUserProfile)){
			return false; //User doesn't have ANY role
		}
		return myUserRoleMap.get(theUserProfile).contains(Conference.SUBPROGRAM_ROLE);
	}

	@Override
	public boolean isUserProgramChair(final UserProfile theUserProfile) {
		if(!myUserRoleMap.containsKey(theUserProfile)){
			return false; //User doesn't have ANY role
		}
		return myUserRoleMap.get(theUserProfile).contains(Conference.PROGRAM_ROLE);
	}

	@Override
	public boolean isUserDirector(final UserProfile theUserProfile) {
		if(!myUserRoleMap.containsKey(theUserProfile)){
			return false; //User doesn't have ANY role
		}
		return myUserRoleMap.get(theUserProfile).contains(Conference.DIRECTOR_ROLE);
	}
	
    /**
     * Adds theRole to theUserProfile for this Conference.
     */
    protected void addUserToRole(
    		final UserProfile theUserProfile,
    		final String theUserRole){
    	if(!myUserRoleMap.containsKey(theUserProfile)){
    		myUserRoleMap.put(theUserProfile, new ArrayList<>());
    	}
    	
    	if(!myUserRoleMap.get(theUserProfile).contains(theUserRole)){
    		myUserRoleMap.get(theUserProfile).add(theUserRole);
    	}
    }
	/**
	 * @return the myPaperSubmissionMap
	 */
	protected Map<UserProfile, List<Paper>> getPaperSubmissionMap() {
		return myPaperSubmissionMap;
	}
	/**
	 * @return the myPaperAuthorshipMap
	 */
	protected Map<String, List<Paper>> getPaperAuthorshipMap() {
		return myPaperAuthorshipMap;
	}
	
    /**
     * Maps a Reviewer's UserProfile to a Paper.
     */
    protected Map<UserProfile, List<Paper>> getReviewerAssignmentMap(){
    	return myReviewerAssignmentMap;
    }
	/**
	 * @return the mySubprogramAssignmentMap
	 */
	protected Map<UserProfile, List<Paper>> getSubprogramAssignmentMap() {
		return mySubprogramAssignmentMap;
	}
	/**
	 * @return the myUserRoleMap
	 */
	protected Map<UserProfile, List<String>> getUserRoleMap() {
		return myUserRoleMap;
	}

	/**
	 * Empty list if no reviewers exist for this Conference.
	 */
	@Override
	public List<UserProfile> getReviewers() {
		final List<UserProfile> reviewerList = new ArrayList<>();
		for(final UserProfile currentUserProfile: myUserRoleMap.keySet()){
			if(myUserRoleMap.get(currentUserProfile).contains(Conference.REVIEW_ROLE)){
				reviewerList.add(currentUserProfile);
			}
		}
		
		return reviewerList;
	}

	/**
	 * Empty list if no papers have been submitted to this Conference.
	 */
	@Override
	public List<Paper> getAllPapers() {
		final List<Paper> paperList = new ArrayList<>();
		for(final Entry<UserProfile, List<Paper>> currentUserPaperEntry: myPaperSubmissionMap.entrySet()) {
			paperList.addAll(currentUserPaperEntry.getValue());
		}
		return paperList;
	}

	/**
	 * Get the papers assigned to a subprogram chair or null if the user isn't a subprogramchair.
	 * @return an unmodifiable list of papers assigned to a subprogramchair.
	 */
    @Override
    public List<Paper> getPapersAssignedToSubProgramChair(UserProfile theSubProgramChair) {
        List<Paper> result = mySubprogramAssignmentMap.get(theSubProgramChair);
        
        if (result != null) {
            result = Collections.unmodifiableList(result);
        }
        
        return result;
    }
    
    /**
     * Assigns a paper to a subprogram chair, if the user isn't a subprogram chair they are assigned the role. 
     * 
     * @param theSubProgramChair the user to assign the paper to.
     * @param p the paper to assign.
     * @throws NullPointerException if theSubProgramChair or p is null.
     */
    public void assignSubprogramchairToPaper(UserProfile theSubProgramChair, Paper p) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(theSubProgramChair);
        
        List<Paper> papers = mySubprogramAssignmentMap.get(theSubProgramChair);
        if (papers == null) {
            papers = new LinkedList<>();
            addUserToRole(theSubProgramChair, Conference.PROGRAM_ROLE);
            mySubprogramAssignmentMap.put(theSubProgramChair, papers);
        }
        
        if (!papers.contains(p)) {
            papers.add(p);
        }
    }

    
    public List<UserProfile> getReviewersForPaper(Paper p) {
        List<UserProfile> result = new LinkedList<UserProfile>();
        
        
        for (UserProfile reviewer : this.myReviewerAssignmentMap.keySet()) {
            
            if (myReviewerAssignmentMap.get(reviewer).contains(p) && !result.contains(reviewer)) {
                result.add(reviewer);
            }
        }
        
        
        return result;
    }
}
