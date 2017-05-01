package methods_classes;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Conference;
import model.ErrorException;
import model.Paper;


/**
 * @author Dongsheng Han
 * @version 4/30/2017
 * 
 */
public class Test_getPapersAuthoredBy {

	private Conference new_conference;
	private Paper new_paper;
	private List<String> the_Authors;
	private List<Paper> papers;
	
	@Before
	public void setUp(){
		/*
		* Create a new conference
		*/
		@SuppressWarnings("deprecation")
		Date deadline = new Date(117, 6, 1, 23, 59, 59);
		int Author_Paper_Submission_Limit = 5;
		int Reviewer_Paper_Assignment_Limit = 8;
		new_conference = Conference.createConference("Test Conference", deadline, 
				Author_Paper_Submission_Limit, Reviewer_Paper_Assignment_Limit); 
		
		/*
		* Create a new a Paper 
		*/
		the_Authors = new ArrayList<String>();
		the_Authors.add("Malik, P");
		the_Authors.add("Melik, Q");
		the_Authors.add("Ca S");
		String file_name = "Hungry for life.txt";
		String the_Paper_Title = "Hungry for life";
		String the_Submitter_UID = "Malik55813";
		File the_Paper_File = null;
		try {
			the_Paper_File = new File(file_name);
		}catch(Exception e) {
			e.printStackTrace();
		}
		new_paper = Paper.createPaper(the_Paper_File, the_Authors, the_Paper_Title, the_Submitter_UID);
		
		/*
		* Add the paper to the Conference
		*/
		try {
			new_conference.addPaper(the_Submitter_UID, new_paper);
		} catch (ErrorException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_getPapersAuthoredBy() {
		/*
		* Test the getPapersAuthoredBy() method fully to check that addPaper() worked correctly.
		*/
		for(int i = 0; i < the_Authors.size(); i++){
			papers = new_conference.getPapersAuthoredBy(the_Authors.get(i));
			assertTrue(papers.contains(new_paper));
		}
	}

}
