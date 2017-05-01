package methods_classes;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import model.Conference;
import model.ErrorException;
import model.Paper;


/**
 * Dongsheng Han
 */
public class Test_getPapersAuthoredBy {

	@Test
	public void test() {
		// Convert string to date and set deadline to str_date
		@SuppressWarnings("deprecation")
		Date deadline = new Date(117, 6, 1, 23, 59, 59);
		assertTrue(deadline.after(new Date()));

		//Create a new conference
		int Author_Paper_Submission_Limit = 5;
	    int Reviewer_Paper_Assignment_Limit = 10;
		Conference new_conference = Conference.createConference("Test Conference", 
													 deadline, 
													 Author_Paper_Submission_Limit, 
													 Reviewer_Paper_Assignment_Limit);
		assertNotNull(new_conference);
		//Create a new a Paper 
		List<String> the_Authors = new ArrayList<String>();
		the_Authors.add("Malik, P");
		String file_name = "Hungry for life.txt";
		File the_Paper_File = null;
		try {
			// create new file
			the_Paper_File = new File(file_name);
		}catch(Exception e) {
			// if any I/O error occurs
			e.printStackTrace();
		}
		
		String the_Paper_Title = "Hungry for life";
		String the_Submitter_UID = "Malik55813";
		Paper new_paper = Paper.createPaper(the_Paper_File, the_Authors, the_Paper_Title, the_Submitter_UID);
		assertNotNull(new_paper);
		
		//Add the paper to the Conference
		try {
			new_conference.addPaper(the_Submitter_UID, new_paper);
		} catch (ErrorException e) {
			e.printStackTrace();
		}
	    
		//Test the getPapersAuthoredBy() method fully to check that addPaper() worked correctly.
		List<Paper> papers = new_conference.getPapersAuthoredBy(the_Submitter_UID);

		assertNotNull(papers);
	}

}
