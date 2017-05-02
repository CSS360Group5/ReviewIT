package model.conference;

import java.util.ArrayList;

import model.ErrorException;
import model.Paper;

public class UserRole {
	private final ConferenceData myConferenceInfo;
    
    public UserRole(final ConferenceData theConferenceInfo){
    	myConferenceInfo = theConferenceInfo;
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
    	if(!myConferenceInfo.isPaperInAuthorSubmissionLimit(thePaper)){
    		throw new ErrorException("Paper exceeds paper submission limit.");
    	}
    	else if(!myConferenceInfo.isPaperInSubmissionDeadline(thePaper)){
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
    	if(!myConferenceInfo.getPaperSubmissionMap().containsKey(theUserID)){
    		myConferenceInfo.getPaperSubmissionMap().put(theUserID, new ArrayList<>());
    	}
    	myConferenceInfo.getPaperSubmissionMap().get(theUserID).add(thePaper);
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
            if(!myConferenceInfo.getPaperAuthorshipMap().containsKey(currentAuthor)){
            	myConferenceInfo.getPaperAuthorshipMap().put(currentAuthor, new ArrayList<>());
            }
            myConferenceInfo.getPaperAuthorshipMap().get(currentAuthor).add(thePaper);
        }
    }
}
