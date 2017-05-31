package view;

import model.Conference;
import model.Paper;
import model.UserProfile;

/**
 * An object to keep track of the context in which a panel is shown. Things such as the current conference, current
 * paper and current user.
 *
 * @author Zachary Chandler
 */
public class UserContext {

    private Conference currentConference;
    private UserProfile user;
    private Paper paper;
    
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

    /**
     * @return the paper
     */
    public Paper getPaper() {
        return paper;
    }

    /**
     * @param paper the paper to set
     */
    public void setPaper(Paper paper) {
        this.paper = paper;
    }
}
