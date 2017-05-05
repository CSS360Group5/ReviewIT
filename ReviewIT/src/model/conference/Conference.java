package model.conference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.IllegalOperationException;
import model.Paper;
import model.UserProfile;


/**
 * A master class for Creating and using a Conference.
 * Relevant getter methods can be obtained via .getInfo().
 * Relevant mutators are located by User Role Access in:
 * .getUserRole()
 * .getReviewRole()
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

	/**
	 * A ConferenceData Object containing all the Data associated with this Conference.
	 */
    private final ConferenceData myInfo;
    private final ReviewerUtilities myReviewRole;
    private final UserUtilities myUserRole;
    
    private Conference(
    		final String theConferenceName,
    		final Date thePaperDeadline,
    		final int thePaperSubmissionLimit,
    		final int thePaperAssignmentLimit
    		) {
        myInfo = new ConferenceData(
        		theConferenceName,
                thePaperDeadline,
                thePaperSubmissionLimit,
                thePaperAssignmentLimit
                );
        
        myReviewRole = new ReviewerUtilities(myInfo);
        myUserRole = new UserUtilities(myInfo);
    }
    
    /**
     * A factory method for creating a Conference Object.
     * @param thePaperDeadline The submission deadline for Authors submitting papers.
     * @param thePaperSubmissionLimit The total amount of papers than an Author may submit.
     * @param thePaperAssignmentLimit The total amount of papers that a Reviewer can be assigned.
     * @return a Conference Object
     */
    public static Conference createConference(
    		final String theConferenceName,
    		final Date thePaperDeadline,
    		final int thePaperSubmissionLimit,
    		final int thePaperAssignmentLimit
    		) {
        return new Conference(
        		theConferenceName,
                thePaperDeadline,
                thePaperSubmissionLimit,
                thePaperAssignmentLimit
                );
    }

    /**
     * {@link ConferenceInfo}
     * {@link ConferenceData}
     * @return a ConferenceInfo Object containing all useful Conference information.
     */
    public ConferenceInfo getInfo(){
    	return ((ConferenceInfo) myInfo);
    }
    
    /**
     * A getter method for associated ReviewRole Object.
     * {@link ReviewerUtilities}
     * @return A ReviewRole Object containing all Reviewer's functionality.
     */
    public ReviewerUtilities getReviewRole(){
    	return myReviewRole;
    }
    
    /**
     * A getter method for associated UserRole Object.
     * {@link UserUtilities}
     * @return A UserRole Object containing all general User functionality.
     */
    public UserUtilities getUserRole(){
    	return myUserRole;
    }
}