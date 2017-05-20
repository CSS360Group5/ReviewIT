package view;

import java.util.Date;

import model.Conference;
import model.ConferenceSystem;
import model.UserProfile;

public class Initialize {

    public static void main(String[] args) {
        
        ConferenceSystem sys = ConferenceSystem.getInstance();
        
        sys.addConference(Conference.createConference("ASME/BATH FPMC Symposium on Fluid Power and Motion",
                new Date(), 5, 8));

        sys.addConference(Conference.createConference("Internal Combustion Engine Fall Technical Conference",
                new Date(), 5, 8));
        
        sys.addConference(Conference.createConference("International Conference on Nuclear Engineering (ICONE 26)",
                new Date(), 5, 8));

        sys.addUserProfile(new UserProfile("zachac",    "Zachary Chandler"));
        sys.addUserProfile(new UserProfile("kvn96",     "Kevin Nguyen"));
        sys.addUserProfile(new UserProfile("ianjury",   "Ian Jury"));
        sys.addUserProfile(new UserProfile("briang5",   "Brian Geving"));
        sys.addUserProfile(new UserProfile("dimabliz",  "Dmitriy Bliznyuk"));
        
        sys.serializeModel();
    }

}
