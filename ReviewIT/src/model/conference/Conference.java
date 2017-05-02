package model.conference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.ErrorException;
import model.Paper;
import model.UserProfile;


/**
 * A class for storing all information associated with
 * a particular conference.
 * @author Kevin Ravana
 * @author Dimitar Kumanov
 * @author Danielle Lambion
 * @version 04/25/2017
 */
public class Conference implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -836524014001898470L;

    
    private final ConferenceInfo myInfo;
    private final ReviewRole myReviewRole;
    
//    private final String myConferenceName;
//    
//    /**
//     * Maps a Submitter's UserID to a Paper.
//     */
//    private final Map<String, List<Paper>> myPaperSubmissionMap;
//    /**
//     * Maps an Author/Coauthor's name to a Paper.
//     */
//    private final Map<String, List<Paper>> myPaperAuthorshipMap;
//    /**
//     * Maps a Reviewer's UserID to a Paper.
//     */
//    private final Map<String, List<Paper>> myReviewerAssignmentMap;
//    /**
//     * Maps a Subprogram Chair's UserID to a Paper.
//     */
//    private final Map<String, List<Paper>> mySubprogramAssignmentMap;
//    /**
//     * Maps a User's UserID to a Role. 
//     */
//    private final HashMap<String, ArrayList<String>> myUserRoleMap;
//    private final Date myPaperSubmissionDeadline;
//    private final int myPaperSubmissionLimit;
//    private final int myReviewerAssignmentLimit;

    private Conference(final String theConferenceName,
                       final Date thePaperDeadline,
                       final int thePaperSubmissionLimit,
                       final int thePaperAssignmentLimit) {
    	final Map<UserProfile, List<Paper>> aPaperSubmissionMap = new HashMap<>();
    	final Map<String, List<Paper>> aPaperAuthorshipMap = new HashMap<>();
    	final Map<UserProfile, List<Paper>> aReviewerAssignmentMap = new HashMap<>();
    	final Map<String, List<Paper>> aSubprogramAssignmentMap = new HashMap<>();
    	final Map<UserProfile, List<String>> aUserRoleMap = new HashMap<>();
        aConferenceName = theConferenceName;
        aPaperSubmissionDeadline = thePaperDeadline;
        aPaperSubmissionLimit = thePaperSubmissionLimit;
        aReviewerAssignmentLimit = thePaperAssignmentLimit;
        
        
        myInfo = new ConferenceInfo();
        
        myReviewRole = new ReviewRole(
        		myInfo,
        		myPaperSubmissionMap,
        		myPaperAuthorshipMap,
        		myReviewerAssignmentMap,
        		mySubprogramAssignmentMap
        		);
    }

    private static ConferenceInfo createConferenceInfo(
    		final String theConferenceName,
            final Date thePaperDeadline,
            final int thePaperSubmissionLimit,
            final int thePaperAssignmentLimit
            ){
    	
    }
    
    /**
     * A factory method for creating a Conference Object.
     * @param thePaperDeadline The submission deadline for Authors submitting papers.
     * @param thePaperSubmissionLimit The total amount of papers than an Author may submit.
     * @param thePaperAssignmentLimit The total amount of papers that a Reviewer can be assigned.
     * @return a Conference Object
     */
    public static Conference createConference(final String theConferenceName,
                                       final Date thePaperDeadline,
                                       final int thePaperSubmissionLimit,
                                       final int thePaperAssignmentLimit) {
        return new Conference(theConferenceName,
                thePaperDeadline,
                thePaperSubmissionLimit,
                thePaperAssignmentLimit);
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
     * A method to change the roles associated with a
     * particular user.
     * @param theUserID The user whose roles are to be altered.
     * @param theRoles The collection of roles to be assigned.
     * @author Kevin Ravana
     */
    public void setUserRoles(final String theUserID,
                             final ArrayList<String> theRoles) {
        myUserRoleMap.remove(theUserID);
        myUserRoleMap.put(theUserID, theRoles);
    }

	/**
	 * Adds a Paper Object to myAuthorSubmissionMap and
     * to myPaperAuthorshipMap.
	 * PRECONDITION: thePaper is isPaperInAuthorSubmissionLimit()
	 * AND isPaperInSubmissionDeadline()
	 * @param theUserID
	 * @param thePaper
	 * @throws ErrorException If the precondition is violated.
	 * @author Kevin Ravana
	 * @author Dimitar Kumanov
	 */
    public void addPaper(final String theUserID,
    						final Paper thePaper) throws ErrorException {
    	if(!isPaperInAuthorSubmissionLimit(thePaper)){
    		throw new ErrorException("Paper exceeds paper submission limit.");
    	}
    	else if(!isPaperInSubmissionDeadline(thePaper)){
    		throw new ErrorException("Paper exceeds submission deadline.");
    	}
    	//Add paper to submission map:
        addPaperToSubmissionMap(theUserID, thePaper);

    	//Add paper to author map:
    	addPaperToAuthorshipMap(thePaper);
    }

    /**
     * Adds paper to myPaperSubmissionMap. If the Author has not yet submitted a paper
     * to this Conference, the Author's userID will be put into the map along with a
     * list for storing their papers. If a paper with the same name has already been
     * submitted, it will be replaced. (Add prompt to ask user if this should occur).
     * @param theUserID
     * @param thePaper
     * @author Kevin Ravana
     * @author Dimitar Kumanov
     */
    private void addPaperToSubmissionMap(final String theUserID, final Paper thePaper){
    	if(!myPaperSubmissionMap.containsKey(theUserID)){
    		myPaperSubmissionMap.put(theUserID, new ArrayList<>());
    	}
    	myPaperSubmissionMap.get(theUserID).add(thePaper);
    }

    /**
     * Adds the paper to myPaperAuthorshipMap. This is a map that records all coauthors of
     * a particular paper.
     * @param thePaper
     * @author Kevin Ravana
     * @author Dimitar Kumanov
     */
    private void addPaperToAuthorshipMap(final Paper thePaper) {
        for(final String currentAuthor: thePaper.getAuthors()){
            if(!myPaperAuthorshipMap.containsKey(currentAuthor)){
                myPaperAuthorshipMap.put(currentAuthor, new ArrayList<>());
            }
            myPaperAuthorshipMap.get(currentAuthor).add(thePaper);
        }
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
     * A getter for the Conference name
     * @return the conference name
     * @author Dimitar Kumanov
     */
    public String getName(){
    	return myConferenceName;
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
     * Acquires all the papers assigned to a subprogram chair
     * for a specific conference.
     * 
     * @param theUserID  The user ID of the subprogram chair user.
     * @return an ArrayList of papers assigned to this subprogram chair.
     * 
     * @author Danielle Lambion
     */
    public List<Paper> getPapersAssignedForSubprogram(final String theUserID) {
    	//TO DO: Fix me to work like other methods that get Lists from a Map
        return mySubprogramAssignmentMap.get(theUserID);
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
}