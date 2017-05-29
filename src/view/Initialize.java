package view;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import model.Conference;
import model.ConferenceSystem;
import model.Paper;
import model.Review;
import model.UserProfile;

public class Initialize {

	public static void main(String[] args) {
        ConferenceSystem sys = ConferenceSystem.getInstance();
        
        addUsers(sys);

        UserProfile zachac = sys.getUserProfile("zachac");
        UserProfile briang5 = sys.getUserProfile("briang5");
        UserProfile kvn96 = sys.getUserProfile("kvn96");
        UserProfile ianjury = sys.getUserProfile("ianjury");
//        UserProfile dimabliz = sys.getUserProfile("ianjury");
        

        Date now = new Date();
        
        Conference openDeadline = getConferenceWithOpenDeadline(now);
        Conference closedDeadline = getConferenceWithClosedDeadline();
        Conference withPapers = getAnotherConference(now);
        
        addSimplePaper(sys, withPapers, zachac, kvn96, briang5);
        
        addPaperLimit(sys, openDeadline, ianjury);

        sys.addConference(openDeadline);
        sys.addConference(closedDeadline);
        sys.addConference(withPapers);
        sys.serializeModel();
    }


    private static Conference getAnotherConference(Date now) {
        return Conference.createConference("ASME/BATH FPMC Symposium on Fluid Power and Motion", 
                new Date(now.getTime() + 9000L), 5, 8);
    }


    private static Conference getConferenceWithClosedDeadline() {
        return Conference.createConference("International Conference on Nuclear Engineering (ICONE 26)",
                new Date(), 5, 8);
    }


    private static Conference getConferenceWithOpenDeadline(Date now) {
        return Conference.createConference("Internal Combustion Engine Fall Technical Conference",
                new Date(now.getTime() + 10000000000L), 5, 8);
    }


    private static void addSimplePaper(ConferenceSystem sys, Conference withPapers, UserProfile author, 
                                                                                    UserProfile subchair,
                                                                                    UserProfile reviewer) {
        Date now = new Date();

        List<String> authors = new LinkedList<String>();
        authors.add("Zachary Chandler");
        authors.add("Ian Jury");        
        
        Paper simplePaper = Paper.createPaper(new File(""), authors, "Fuild Motion Powered Generators", author);
        
        withPapers.getUserRole().addPaper(author, simplePaper);
        withPapers.getDirectorRole().assignPaperToSubProgramChair(subchair, simplePaper);
        withPapers.getInfo().getSubmissionDate().setTime(now.getTime() - 1);
        withPapers.getSubprogramRole().assignReviewer(reviewer, simplePaper);
        simplePaper.addReview(new Review(new File(""), 5));
        simplePaper.addReview(new Review(new File(""), 2));
        //simplePaper.addReview(new Review(new File(""), 3));
    }
    
    private static void addPaperLimit(ConferenceSystem sys, Conference theConference, UserProfile author) {
    	List<String> authors = new LinkedList<String>();
    	authors.add("Ian Jury"); 
    	
    	theConference.getUserRole().addPaper(author, 
    			Paper.createPaper(new File(""), authors, "Plant Systems, Structures, Components and Materials", author));
    	
    	theConference.getUserRole().addPaper(author, 
    			Paper.createPaper(new File(""), authors, "Advanced and Next Generation Reactors, Fusion Technology", author));
    	
    	theConference.getUserRole().addPaper(author, 
    			Paper.createPaper(new File(""), authors, "Nuclear Safety, Security and Cyber Security", author));
    	
    	theConference.getUserRole().addPaper(author, 
    			Paper.createPaper(new File(""), authors, "Thermal-Hydraulics", author));
    	
    	//theConference.getUserRole().addPaper(author, 
    	//		Paper.createPaper(new File(""), authors, "Mitigation Strategies for Beyond Design Basis Events", author));
    }

	
    private static void addUsers(ConferenceSystem sys) {
        sys.addUserProfile(new UserProfile("zachac",    "Zachary Chandler"));
        sys.addUserProfile(new UserProfile("kvn96",     "Kevin Nguyen"));
        sys.addUserProfile(new UserProfile("ianjury",   "Ian Jury"));
        sys.addUserProfile(new UserProfile("briang5",   "Brian Geving"));
        sys.addUserProfile(new UserProfile("dimabliz",  "Dmitriy Bliznyuk"));
    }

}
