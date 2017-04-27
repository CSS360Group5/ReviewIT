package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * A class for storing all information associated with
 * a particular conference.
 * @author Kevin Ravana
 * @version 04/25/2017
 */
public class Conference {
    private static final String SUBMIT_ACTION = "Submit Paper";
    private static final String ASSIGN_ACTION = "Assign Paper";

    private final String myConferenceName;
    private final HashMap<String, ArrayList<Paper>> myAuthorSubmissionMap;
    private final HashMap<String, ArrayList<String>> myUserRoleMap;
    private final Date myPaperSubmissionDeadline;
    private final int myPaperSubmissionLimit;
    private final int myPaperAssignmentLimit;

    private Conference(final String theConferenceName,
                       final Date thePaperDeadline,
                       final int thePaperSubmissionLimit,
                       final int thePaperAssignmentLimit) {
        myAuthorSubmissionMap = new HashMap<>();
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
    public Conference createConference(final String theConferenceName,
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



    public boolean addPaper(final String theUserID,
                              final Paper thePaper) {
        ArrayList<Paper> paperList = myAuthorSubmissionMap.get(theUserID);
        //TODO: Convert to ErrorCode comparison
        if (createErrorCode(SUBMIT_ACTION,
                theUserID,
                paperList.size(),
                thePaper)) {
            paperList.add(thePaper);
            return true;
        }

        return false;
    }

    /*
    Lots of ways to divide the ErrorCode generation.
     */
    /**
     *
     * @param theCheckedAction The action being performed.
     * @param theUserID The user subject of the action.
     * @param theCount The amount of papers already submitted/assigned.
     * @param thePaper The Paper Object subject of the action.
     * @return
     */
    private boolean createErrorCode(final String theCheckedAction,
                                    final String theUserID,
                                    final int theCount,
                                    final Paper thePaper) {
        //TODO: Convert return type to ErrorCode

        boolean dateCheck = false;
        boolean limitCheck = false;
        boolean authorCheck = true;

        if (theCheckedAction.equals(SUBMIT_ACTION)) {
            dateCheck = myPaperSubmissionDeadline.before(new Date());
            limitCheck = myPaperSubmissionLimit > theCount;
            authorCheck = false;
        } else if (theCheckedAction.equals(ASSIGN_ACTION)) {
            dateCheck = true;
            limitCheck = myPaperAssignmentLimit > theCount;
            authorCheck = theUserID.equals(thePaper.getSubmitterUID());
        }

        return dateCheck && limitCheck && !authorCheck;
    }
}
