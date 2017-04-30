package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javafx.print.Paper;

/**
 * A class for storing all information associated with
 * a particular conference.
 * @author Kevin Ravana
 * @version 04/25/2017
 */
public class Conference implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -836524014001898470L;
	
	private static final String SUBMIT_ACTION = "Submit Paper";
    private static final String ASSIGN_ACTION = "Assign Paper";

    private final String myConferenceName;
    private final HashMap<String, ArrayList<Paper>> myAuthorSubmissionMap;
    private final HashMap<String, ArrayList<Paper>> myReviewerAssignmentMap;
    private final HashMap<String, ArrayList<Paper>> mySubprogramAssignmentMap;

    /**
     * Maps a UserID to a Role. 
     */
    private final HashMap<String, ArrayList<String>> myUserRoleMap;
    private final Date myPaperSubmissionDeadline;
    private final int myPaperSubmissionLimit;
    private final int myPaperAssignmentLimit;

    private Conference(final String theConferenceName,
                       final Date thePaperDeadline,
                       final int thePaperSubmissionLimit,
                       final int thePaperAssignmentLimit) {
        myAuthorSubmissionMap = new HashMap<>();
        myReviewerAssignmentMap = new HashMap<>();
        mySubprogramAssignmentMap = new HashMap<>();
        myUserRoleMap = new HashMap<>();
        myConferenceName = theConferenceName;
        myPaperSubmissionDeadline = thePaperDeadline;
        myPaperSubmissionLimit = thePaperSubmissionLimit;
        myPaperAssignmentLimit = thePaperAssignmentLimit;
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
     * A method to acquire all papers submitted by a specified
     * Author to this particular Conference.
     * @param theUserID The Author of the requested papers.
     * @return an ArrayList of Paper Objects.
     */
    public ArrayList<Paper> getPapersAuthoredBy(final String theUserID) {
        return myAuthorSubmissionMap.get(theUserID);
    }

    /**
     * A method to change the roles associated with a
     * particular user.
     * @param theUserID The user whose roles are to be altered.
     * @param theRoles The collection of roles to be assigned.
     */
    public void setUserRoles(final String theUserID,
                             final ArrayList<String> theRoles) {
        myUserRoleMap.remove(theUserID);
        myUserRoleMap.put(theUserID, theRoles);
    }


    public void addPaper(final String theUserID, final Paper thePaper) throws ErrorException {
        if (myAuthorSubmissionMap.containsKey(theUserID)) {

            ArrayList<Paper> paperList = myAuthorSubmissionMap.get(theUserID);

            if (isSubmissionUnderLimit(paperList.size())) {
                throw new ErrorException("Sorry, you have already submitted the maximum number of papers!");
            } else if (isSubmissionBeforeDeadline()) {
                throw new ErrorException("Sorry, the deadline for submitting papers has passed!");
            }

            for (Paper p : paperList) {
                if (p.getName().equals(thePaper.getName())) {
                    paperList.remove(p);
                }
            }
            paperList.add(thePaper);
        } else {
            myAuthorSubmissionMap.put(theUserID, new ArrayList<>());
            ArrayList<Paper> paperList = myAuthorSubmissionMap.get(theUserID);
            paperList.add(thePaper);
        }
    }

    private boolean isSubmissionUnderLimit(final int theLimit) {
        return theLimit < myPaperSubmissionLimit;
    }

    private boolean isSubmissionBeforeDeadline() {
        return myPaperSubmissionDeadline.before(new Date());
    }

//    public boolean addPaper(final String theUserID,
//                              final Paper thePaper) {
//        ArrayList<Paper> paperList = myAuthorSubmissionMap.get(theUserID);
//        //TODO: Convert to ErrorCode comparison
//        if (createErrorCode(SUBMIT_ACTION,
//                theUserID,
//                paperList.size(),
//                thePaper)) {
//            paperList.add(thePaper);
//            return true;
//        }
//
//        return false;
//    }

    /**
     * A getter for the Conference name
     * @return the conference name
     * @author Dimitar Kumanov
     */
    public String getName(){
    	return myConferenceName;
    }
    
//    /*
//    Lots of ways to divide the ErrorCode generation.
//     */
//    /**
//     *
//     * @param theCheckedAction The action being performed.
//     * @param theUserID The user subject of the action.
//     * @param theCount The amount of papers already submitted/assigned.
//     * @param thePaper The Paper Object subject of the action.
//     * @return
//     */
//    private boolean createErrorCode(final String theCheckedAction,
//                                    final String theUserID,
//                                    final int theCount,
//                                    final Paper thePaper) {
//        //TODO: Convert return type to ErrorCode
//
//        boolean dateCheck = false;
//        boolean limitCheck = false;
//        boolean authorCheck = true;
//
//        if (theCheckedAction.equals(SUBMIT_ACTION)) {
//            dateCheck = myPaperSubmissionDeadline.before(new Date());
//            limitCheck = myPaperSubmissionLimit > theCount;
//            authorCheck = false;
//        } else if (theCheckedAction.equals(ASSIGN_ACTION)) {
//            dateCheck = true;
//            limitCheck = myPaperAssignmentLimit > theCount;
//            authorCheck = theUserID.equals(thePaper.getSubmitterUID());
//        }
//
//        return dateCheck && limitCheck && !authorCheck;
//    }
    
    /**
     * Acquires all the papers assigned to a reviewer
     * for a specific conference.
     * 
     * @param theReviewerID  The Reviewer's user ID.
     * @return an ArrayList of papers assigned to this reviewer.
     * 
     * @author Danielle Lambion
     */
    public ArrayList<Paper> getPapersAssignedForReviewer(final String theReviewerID) {
        return myReviewerAssignmentMap.get(theUserID);
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
    public ArrayList<Paper> getPapersAssignedForSubprogram(final String theUserID) {
        return mySubprogramAssignmentMap.get(theUserID);
    }
    
    /**
     * Assigns papers to a selected reviewer
     * 
     * @param theReviewerID the ID 
     * @param thePaper the paper object to be assigned to a reviewer.
     * 
     * @author Danielle Lambion
     */
    public void assignReviewer(final String theReviewerID, Paper thePaper)throws ErrorException {
    	ArrayList<Paper> paperList = myReviewerAssignmentMap.get(theReviewerID);
    	
    	if(myPaperAssignmentLimit > paperList.size()) {
    		throw new ErrorException("Sorry, you have already assigned the maximum number of papers for this reviewer!");
    	}
    	else {
    		paperList.add(thePaper);
    		myReviewerAssignmentMap.put(theReviewerID, paperList);
    	}
    }
}