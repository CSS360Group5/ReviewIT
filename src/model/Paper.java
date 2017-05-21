package model;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
/**
 * A class for storing all the information associated with a Paper.
 * For instantiation use to .createPaper() method.(see factory pattern)
 * 
 * INVARIANT: All getters return non-null objects.
 * All strings are non-empty.
 * @author Dimitar Kumanov
 * @version 04/24/2017
 */
public class Paper implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1981414852704428147L;
	private final File myPaperFile;
	private Date mySubmissionDate;
	private final List<String> myAuthors;
	private final List<File> myReviews;
	private final String myTitle;
	private final UserProfile mySubmitter;
	private File myRecommendation;
	
	/**
	 * Private; see createPaper()
	 */
	private Paper(
			final File thePaper,
			final Date theSubmissionDate,
			final List<String> theAuthors,
			final String thePaperTitle,
			final UserProfile theSubmitterUserProfile
			) throws IllegalArgumentException{
		
		myPaperFile = Objects.requireNonNull(thePaper);
		mySubmissionDate = Objects.requireNonNull(theSubmissionDate);
		myAuthors = Objects.requireNonNull(theAuthors);
		myTitle = Objects.requireNonNull(thePaperTitle);
		mySubmitter = Objects.requireNonNull(theSubmitterUserProfile);
		myReviews = new LinkedList<File>();
		if(thePaperTitle.isEmpty())
			throw new IllegalArgumentException();
	}
	
	/**
	 * A factory method for creating a Paper Object.
	 * PRECONDITION: All parameters must be non-null,
	 * Strings can't be empty.
	 * @param thePaperFile The file associated with the text itself.
	 * @param theAuthors The list of Authors of this paper.
	 * @param thePaperTitle The title of the paper.
	 * @param theSubmitterUserProfile The System UserProfile of the person actually submitting the paper.
	 * @exception When the precondition is violated.
	 * @return a Paper Object that has all the information associated with the paper.
	 */
	public static Paper createPaper(
			final File thePaperFile,
			final List<String> theAuthors,
			final String thePaperTitle,
			final UserProfile theSubmitterUserProfile
			) throws IllegalArgumentException{

		return new Paper(
				thePaperFile,
				new Date(),
				theAuthors,
				thePaperTitle,
				theSubmitterUserProfile
				);
	}
	
	/**
	 * Gets the (non-null) file pointing to the Paper.
	 * @return the (non-null) file pointing to the Paper.
	 */
	public File getPaperFile(){
		return myPaperFile;
	}
	
	/**
	 * Gets the Date this paper was submitted(Date of createPaper()).
	 * @return the Date this paper was submitted(Date of createPaper()).
	 */
	public Date getSubmitDate(){
		return mySubmissionDate;
	}
	
	/**
	 * Gets the (non-null, non-empty) Paper title's String.
	 * @return the (non-null, non-empty) Paper title's String.
	 */
	public String getTitle(){
		return myTitle;
	}
	
	/**
	 * Gets the (non-null) UserProfile of the user submitting this Paper.
	 * @return the (non-null) UserProfile of the user submitting this Paper.
	 */
	public UserProfile getSubmitterUserProfile(){
		return mySubmitter;
	}
	
	/**
	 * Gets a List(non-null but possibly empty) of Strings with the names of all the authors of this Paper. 
	 * @return a List of Strings(non-null but possibly empty) with the names of all the authors of this Paper.
	 */
	public List<String> getAuthors(){
		return myAuthors;
	}
	
	public List<File> getReviews() {
		return myReviews;
	}
	
	public void addReviewer(String reviewer) {
		
		
	}

	/**
	 * Changes the submission Date of this paper to the newSubDate provided.
	 * @param newSubDate The Date to change the submission Date of this Paper.
	 */
	public void setSubmissionDate(final Date newSubDate) {
		mySubmissionDate = newSubDate;
	}

	/**
	 * @return the myRecommendation
	 */
	public File getMyRecommendation() {
		return myRecommendation;
	}

	/**
	 * @param recommendation the myRecommendation to set
	 */
	public void setMyRecommendation(File recommendation) {
		if(myReviews.size() < 3) {
			throw new IllegalArgumentException();
		}
		
		this.myRecommendation = recommendation;
		
	}
	
	public void addReview(File review) {
		Objects.requireNonNull(review);
		myReviews.add(review);
	}
	
	@Override
	public String toString() {
	    StringBuilder result = new StringBuilder();
	    result.append(myTitle);
        result.append(", by ");
        
        for (String s : myAuthors) {
            result.append(s);
            result.append(", ");
        }
        
        result.append(mySubmissionDate.toString());
	    
	    return result.toString();
	}
}

