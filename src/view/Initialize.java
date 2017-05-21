package view;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import model.Conference;
import model.ConferenceSystem;
import model.Paper;
import model.UserProfile;

public class Initialize {

    @SuppressWarnings("deprecation")
	public static void main(String[] args) {
        
        ConferenceSystem sys = ConferenceSystem.getInstance();
        
        Date now = new Date();
        
        sys.addUserProfile(new UserProfile("zachac",    "Zachary Chandler"));
        sys.addUserProfile(new UserProfile("kvn96",     "Kevin Nguyen"));
        sys.addUserProfile(new UserProfile("ianjury",   "Ian Jury"));
        sys.addUserProfile(new UserProfile("briang5",   "Brian Geving"));
        sys.addUserProfile(new UserProfile("dimabliz",  "Dmitriy Bliznyuk"));
        
        UserProfile zachac = sys.getUserProfile("zachac");
        Conference c = Conference.createConference("ASME/BATH FPMC Symposium on Fluid Power and Motion", 
                new Date(now.getTime() + 100L), 5, 8);
        sys.addConference(c);
        
        List<String> authors = new LinkedList<String>();
        authors.add("Zachary Chandler");
        authors.add("Ian Jury");
        
        
        Paper p = Paper.createPaper(new File(""), authors, "Fuild Motion Powered Generators", zachac);
        c.getUserRole().addPaper(zachac, p);
        
        c.getDirectorRole().assignPaperToSubProgramChair(sys.getUserProfile("kvn96"), p);

        UserProfile briang5 = sys.getUserProfile("briang5");
        
        c.getInfo().getSubmissionDate().setTime(now.getTime() - 1);
        
        c.getSubprogramRole().assignReviewer(briang5, p);
        
        sys.addConference(Conference.createConference("Internal Combustion Engine Fall Technical Conference",
                new Date(now.getTime() + 10000000L), 5, 8));
        
        sys.addConference(Conference.createConference("International Conference on Nuclear Engineering (ICONE 26)",
                new Date(), 5, 8));
        
        sys.serializeModel();
    }

}
