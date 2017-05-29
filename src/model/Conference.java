package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

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
	
	public static final String AUTHOR_ROLE = "Author";
	public static final String REVIEW_ROLE = "Reviewer";
	public static final String SUBPROGRAM_ROLE = "Subprogram Chair";
	public static final String PROGRAM_ROLE = "Program Chair";
	public static final String DIRECTOR_ROLE = "Director";

	/**
	 * A ConferenceData Object containing all the Data associated with this Conference.
	 */
    private final ConferenceData myInfo;
    private final UserUtilities myUserRole;
    private final SubprogramUtilities mySubprogramRole;
    private final DirectorUtilities myDirectorRole;
    private final ReviewerUtilities myReviewerRole;
    
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
        
        myUserRole = new UserUtilities(myInfo);
        mySubprogramRole = new SubprogramUtilities(myInfo);
        myDirectorRole = new DirectorUtilities(myInfo);
        myReviewerRole = new ReviewerUtilities(myInfo);
    }
    
    /**
     * A factory method for creating a Conference Object.
     * @param thePaperDeadline The submission deadline for Authors submitting papers.
     * @param thePaperSubmissionLimit The total amount of papers than an Author may submit.
     * @param thePaperAssignmentLimit The total amount of papers that a Reviewer can be assigned.
     * @return a Conference Object
     * 
     * @throws NullPointerException if theConferenceName or thePaperDeadline are null
     * @throws IllegalArgumentException if thePaperSubmissionLimit or thePaperAssignmentLimit or zero or negative
     */
    public static Conference createConference(
    		final String theConferenceName,
    		final Date thePaperDeadline,
    		final int thePaperSubmissionLimit,
    		final int thePaperAssignmentLimit
    		) {

        Objects.requireNonNull(theConferenceName);
        Objects.requireNonNull(thePaperDeadline);
        
        
        if (thePaperSubmissionLimit <= 0 || thePaperAssignmentLimit <= 0) {
            throw new IllegalArgumentException();
        }
        
        
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
    public ConferenceData getInfo(){
    	return myInfo;
    }
    
    /**
     * A getter method for associated UserUtilities Object.
     * {@link UserUtilities}
     * @return A UserUtilities Object containing all general User functionality.
     */
    public UserUtilities getUserRole(){
    	return myUserRole;
    }
    
    /**
     * A getter method for associated SubprogramUtilities Object.
     * {@link SubprogramUtilities}
     * @return A SubprogramUtilities Object containing all Reviewer's functionality.
     */
    public SubprogramUtilities getSubprogramRole(){
    	return mySubprogramRole;
    }
    
    /**
     * A getter method for associated DirectorUtilities Object.
     * {@link SubprogramUtilities}
     * @return A ReviewRole Object containing all Director's functionality.
     */
    public DirectorUtilities getDirectorRole(){
    	return myDirectorRole;
    }

    /**
     * @return the myReviewerRole
     */
    public ReviewerUtilities getReviewerRole() {
        return myReviewerRole;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(myInfo.getName());
        result.append(" (");
        
        if (myInfo.isSubmissionOpen(new Date())) {
            result.append(myInfo.getSubmissionDate());
        } else {
            result.append("CLOSED");
        }
        
        result.append(')');
        
        return  result.toString();
    }
}