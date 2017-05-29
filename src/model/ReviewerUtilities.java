package model;

import java.io.File;
import java.io.Serializable;

/**
 * A class to send reviews through.
 */
public class ReviewerUtilities implements Serializable {

    //private final ConferenceData myConferenceData;
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -1373683496531886231L;

	/**
     * Creates a ReviewerUtilities Object for a Conference. 
     * @param theConferenceData The ConferenceData Object to manipulate.
     */
    public ReviewerUtilities(final ConferenceData theConferenceData){
        //myConferenceData = theConferenceData;
    }
    
    public void sendReview(
            final UserProfile theReviewerProfile,
            Paper thePaper,
            File review, int score
            ) throws IllegalArgumentException {

    	
    	//added 5/29/2017
    	//Prevents a user from sending a review to their own paper
    	if (thePaper.getAuthors().contains(theReviewerProfile.getName())) {
    		throw new IllegalArgumentException();
    	} else {
    		thePaper.addReview(new Review(review, score));
    	}

    }
    
}
