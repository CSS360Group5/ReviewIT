package model;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;
/**
 * A class for storing all the information associated with a Paper.
 * For instantiation use to .createPaper() method.(see factory pattern)
 * @author Dimitar Kumanov +
 * @version 04/24/2017
 */
public class Paper {
	private final File myPaperFile;
	private final Date mySubmissionDate;
	private final List<String> myAuthors;
	private final String myTitle;
	private final String mySubmitter;
	
	
	private Paper(final File thePaper,
				  final Date theSubmissionDate,
				  final List<String> theAuthors,
				  final String thePaperTitle,
				  final String theSubmitterUID){
		myPaperFile = Objects.requireNonNull(thePaper);
		mySubmissionDate = Objects.requireNonNull(theSubmissionDate);
		myAuthors = Objects.requireNonNull(theAuthors);
		myTitle = Objects.requireNonNull(thePaperTitle);
		mySubmitter = Objects.requireNonNull(theSubmitterUID);
	}
	
	/**
	 * A factory method for creating a Paper Object.
	 * @param thePaperFile The file associated with the text itself.
	 * @param theAuthors The list of Authors of this paper.
	 * @param thePaperTitle The title of the paper.
	 * @param theSubmitterUID The System UID of the person actually submitting the paper.
	 * @return a Paper Object that has all the information associated with the paper.
	 */
	public static Paper createPaper(final File thePaperFile,
									final List<String> theAuthors,
									final String thePaperTitle,
									final String theSubmitterUID){
		return new Paper(thePaperFile,
						new Date(),
						theAuthors,
						thePaperTitle,
						theSubmitterUID);
	}
	
	public File getPaperFile(){
		return myPaperFile;
	}
	
	public Date getSubmitDate(){
		return mySubmissionDate;
	}
	
	public String getTitle(){
		return myTitle;
	}
	
	public String getSubmitterUID(){
		return mySubmitter;
	}
	
	public List<String> getAuthors(){
		return myAuthors;
	}
}

