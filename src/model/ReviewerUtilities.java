package model;

/**
 * A class to send reviews through.
 */
public class ReviewerUtilities {

    private final ConferenceData myConferenceData;
    
    /**
     * Creates a ReviewerUtilities Object for a Conference. 
     * @param theConferenceData The ConferenceData Object to manipulate.
     */
    public ReviewerUtilities(final ConferenceData theConferenceData){
        myConferenceData = theConferenceData;
    }
    
    public void sendReview(
            final UserProfile theReviewerProfile,
            Paper thePaper,
            String review
            ) throws IllegalOperationException {
        //TODO
    }
    
}
