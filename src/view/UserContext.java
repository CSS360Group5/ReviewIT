package view;

import model.Conference;
import model.UserProfile;

public class UserContext {

    private Conference currentConference;
    private UserProfile user;

    /**
     * @return the currentConference
     */
    public Conference getCurrentConference() {
        return currentConference;
    }

    /**
     * @param currentConference the currentConference to set
     */
    public void setCurrentConference(Conference currentConference) {
        this.currentConference = currentConference;
    }

    /**
     * @return the user
     */
    public UserProfile getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserProfile user) {
        this.user = user;
    }

    
    
}
