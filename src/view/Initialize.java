package view;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import model.Conference;
import model.ConferenceSystem;
import model.DirectorUtilities;
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
        UserProfile dimabliz = sys.getUserProfile("ianjury");
        
        Date now = new Date();
        
        Conference openDeadline = getConferenceWithOpenDeadline(now);
        Conference closedDeadline = getConferenceWithClosedDeadline();
        Conference withPapers = getAnotherConference(now);
        Conference withOnly2Reviews = getAnotherConferenceWithLessReviews(now);
        
        addRecommendablePaper(sys, withPapers, zachac, kvn96, briang5, dimabliz, ianjury);
        addNonRecommendablePaper(sys, withPapers, ianjury, kvn96, briang5, dimabliz, zachac);
        addPaperWithOneReviewer(sys, withPapers, dimabliz, kvn96, zachac);
        
        addPaperLimit(sys, openDeadline, ianjury);

        
        Collection<Conference> conferences = new LinkedList<>();
        conferences.add(openDeadline);
        conferences.add(closedDeadline);
        conferences.add(withPapers);
        conferences.add(withOnly2Reviews);
        
        
        for (Conference c : conferences) {
            DirectorUtilities dir = c.getDirectorRole();
            dir.addUserRole(zachac, Conference.REVIEW_ROLE);
            dir.addUserRole(briang5, Conference.REVIEW_ROLE);
            dir.addUserRole(kvn96, Conference.REVIEW_ROLE);
            dir.addUserRole(ianjury, Conference.REVIEW_ROLE);
            dir.addUserRole(dimabliz, Conference.REVIEW_ROLE);
            
            sys.addConference(c);
        }
        
        sys.serializeModel();
    }



    private static Conference getAnotherConference(Date now) {
        return Conference.createConference("ASME/BATH FPMC Symposium on Fluid Power and Motion", 
                new Date(now.getTime() + 9000L), 5, 8);
    }
    private static Conference getAnotherConferenceWithLessReviews(Date now) {
        return Conference.createConference("International Conference on Control, Automation, Robotics and Vision Engineering", 
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


    private static void addRecommendablePaper(ConferenceSystem sys, Conference withPapers, UserProfile author,
            UserProfile subchair, UserProfile reviewer1,  UserProfile reviewer2,  UserProfile reviewer3) {
        
        Date now = new Date();

        List<String> authors = new LinkedList<String>();
        authors.add(author.getName());    
        String paperName = "Fuild Motion Powered Generators";
        
        Paper simplePaper = Paper.createPaper(new File(""), authors, paperName, author);
        
        withPapers.getUserRole().addPaper(author, simplePaper);
        withPapers.getDirectorRole().assignPaperToSubProgramChair(subchair, simplePaper);
        withPapers.getInfo().getSubmissionDate().setTime(now.getTime() - 1);
        withPapers.getSubprogramRole().assignReviewer(reviewer1, simplePaper);
        withPapers.getSubprogramRole().assignReviewer(reviewer2, simplePaper);
        withPapers.getSubprogramRole().assignReviewer(reviewer3, simplePaper);
        
        simplePaper.addReview(new Review(new File(""), 5));
        simplePaper.addReview(new Review(new File(""), 2));
        simplePaper.addReview(new Review(new File(""), 3));
        
    }
    
    private static void addNonRecommendablePaper(ConferenceSystem sys, Conference conference, UserProfile author,
            UserProfile subchair, UserProfile reviewer1,  UserProfile reviewer2,  UserProfile reviewer3) {
        
        Date now = new Date();

        List<String> authors = new LinkedList<String>();
        authors.add(author.getName());
        
        String paperName = "Simplified Data Processing on Large Clusters";
        
        Paper simplePaper = Paper.createPaper(new File(""), authors, paperName, author);
        simplePaper.getSubmitDate().setTime(new Date().getTime() - 100);
        
        conference.getUserRole().addPaper(author, simplePaper);
        conference.getDirectorRole().assignPaperToSubProgramChair(subchair, simplePaper);
        conference.getInfo().getSubmissionDate().setTime(now.getTime() - 1);
        conference.getSubprogramRole().assignReviewer(reviewer1, simplePaper);
//        conference.getSubprogramRole().assignReviewer(reviewer2, simplePaper);
        conference.getSubprogramRole().assignReviewer(reviewer3, simplePaper);
        
        simplePaper.addReview(new Review(new File(""), 6));
        simplePaper.addReview(new Review(new File(""), 6));
    }

    private static void addPaperWithOneReviewer(ConferenceSystem sys, Conference conference, UserProfile author,
            UserProfile subchair, UserProfile reviewer1) {
        Date now = new Date();

        List<String> authors = new LinkedList<String>();
        authors.add(author.getName());
        
        String paperName = "Super Fluids in Motion";
        
        Paper simplePaper = Paper.createPaper(new File(""), authors, paperName, author);
        simplePaper.getSubmitDate().setTime(new Date().getTime() - 100);
        conference.getUserRole().addPaper(author, simplePaper);
        conference.getDirectorRole().assignPaperToSubProgramChair(subchair, simplePaper);
        conference.getInfo().getSubmissionDate().setTime(now.getTime() - 1);
        conference.getSubprogramRole().assignReviewer(reviewer1, simplePaper);
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
