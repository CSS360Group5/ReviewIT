package model.conference;

import java.util.List;

import model.Paper;
import model.UserProfile;

/**
 * An Interface for relevant Conference getter methods,
 * check methods and any other useful information related
 * to a Conference a client code might need.
 * @author Dimitar Kumanov
 * @version 5/2/2017
 */
public interface ConferenceInfo {
	public String getName();
	public List<Paper> getPapersAuthoredBy(final String theAuthorName);
	public List<Paper> getPapersAssignedToReviewer(final UserProfile theReviewerProfile);
	public boolean isPaperInAuthorSubmissionLimit(final Paper thePaper);
	public boolean isPaperInSubmissionDeadline(final Paper thePaper);
	public boolean isReviewerInAssignmentLimit(final UserProfile theReviewerProfile);
	public boolean isPaperAuthoredByReviewer(
    		final String theReviewerName,
    		final Paper thePaper
    		);
	public List<Paper> getPapersSubmittedBy(final String theUserID);
}
