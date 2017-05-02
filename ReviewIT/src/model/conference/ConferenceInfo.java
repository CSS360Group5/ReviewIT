package model.conference;

import java.util.List;

import model.Paper;
import model.UserProfile;

public interface ConferenceInfo {
	public String getName();
	public List<Paper> getPapersAuthoredBy(final String theAuthorName);
	public List<Paper> getPapersAssignedToReviewer(final UserProfile theReviewerProfile);
	public boolean isPaperInAuthorSubmissionLimit(final Paper thePaper);
	public boolean isPaperInSubmissionDeadline(final Paper thePaper);
	public boolean isPaperInReviewerAssignmentLimit(
			final UserProfile theReviewerProfile,
			final Paper thePaper
			);
	public boolean isPaperAuthoredByReviewer(
    		final String theReviewerName,
    		final Paper thePaper
    		);
	public List<Paper> getPapersSubmittedBy(final String theUserID);
}
