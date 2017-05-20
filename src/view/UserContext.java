package view;

import model.Conference;

public class UserContext {

    private Conference currentConference;

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
    
    
}
