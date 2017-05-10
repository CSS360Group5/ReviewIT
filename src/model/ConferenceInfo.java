package model;

import java.util.Date;
import java.util.List;

/**
 * An Interface for relevant Conference getter methods,
 * check methods and any other useful information related
 * to a Conference a client code might need.
 * @author Dimitar Kumanov
 * @version 5/2/2017
 */
public interface ConferenceInfo {
	public String getName();
	
	public Date getSubmissionDate();
	public List<String> getUserRoles(final UserProfile theUserProfile);
	public List<Paper> getAllPapers();
	public List<Paper> getPapersSubmittedBy(final UserProfile theUserProfile);
	public List<Paper> getPapersAuthoredBy(final String theAuthorName);
	public List<Paper> getPapersAssignedToReviewer(final UserProfile theReviewerProfile);
	public List<UserProfile> getReviewers();
	
	public boolean isUserAuthor(final UserProfile theUserProfile);
	public boolean isUserReviewer(final UserProfile theUserProfile);
	public boolean isUserSubprogramChair(final UserProfile theUserProfile);
	public boolean isUserProgramChair(final UserProfile theUserProfile);
	public boolean isUserDirector(final UserProfile theUserProfile);
	
	public boolean isSubmissionOpen(final Date theDate);
	
	public boolean isPaperInAuthorSubmissionLimit(final Paper thePaper);
	public boolean isPaperInSubmissionDeadline(final Paper thePaper);
	public boolean isReviewerInAssignmentLimit(final UserProfile theReviewerProfile);
	public boolean isPaperAuthoredByReviewer(
    		final String theReviewerName,
    		final Paper thePaper
    		);
	
}
