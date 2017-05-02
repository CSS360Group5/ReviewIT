package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.UserProfile;
import model.conference.Conference;

/**
 * A singleton system class which is responsible for holding
 * global information such as user profiles and conferences.
 * 
 * Additionally, this class can be made to
 * serialize and deserialize all pertinent information when needed.
 * @author Dimitar Kumanov
 * @version 4/27/2017
 */
public class RSystem {
	
	/**
	 * My only Object instance. This is what is returned from getInstance().
	 */
	private static RSystem myInstance;
	
	/**
	 * Maps Conference name to a Conference.
	 */
	Map<String, Conference> myConferenceMap;
	
	/**
	 * Maps UserID to a UserProfile.
	 */
	Map<String, UserProfile> myUserMap;
	
	private RSystem(){
		myConferenceMap = new HashMap<>();
		myUserMap = new HashMap<>();
		deserializeData();
	}
	
	private void deserializeData(){
		/*
		 * TO DO: Code for deserializing Data, aka loading up our
		 * Conference/UserProfile objects should happen here
		 */
	}
	
	/**
	 * Gets the only instance that exists for this class.
	 * @return a RSystem Object which knows about our Conferences/UserProfiles/etc.
	 * @author Dimitar Kumanov
	 */
	public static RSystem getInstance(){
		if(myInstance == null)
			myInstance = new RSystem();
		return myInstance;
	}
	
	/**
	 * This method should be called before closing the
	 * application to save all of its state(data) for future sessions.
	 */
	public static void serializeModel(){
		/*
		 * TO DO: Code for serializing Data, aka saving up our
		 * Conference/UserProfile objects should happen here
		 */
	}
	public List<Conference> getConferences(){
		return new ArrayList<>(myConferenceMap.values());
	}
	
	public Conference getConference(final String theConferenceName){
		return myConferenceMap.get(theConferenceName);
	}
	
	public void addConference(final Conference theConference){
		myConferenceMap.put(theConference.getInfo().getName(), theConference);
	}
	
	/**
	 * Gets a UserProfile based on the User ID.
	 * If no profile exists returns null
	 * @param theUserID corresponding to the profile
	 * @return the Profile of the User. null if no profile found.
	 */
	public UserProfile getUserProfile(final String theUserID){
		return myUserMap.get(theUserID);
	}
	
	public void addUserProfile(final UserProfile theUserProfile){
		myUserMap.put(theUserProfile.getUID(), theUserProfile);
	}
	
}
