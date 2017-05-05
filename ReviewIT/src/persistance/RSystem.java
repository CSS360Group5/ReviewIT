package persistance;

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
	
	/**
	 * Gets THE RSystem instance guaranteed to be the only one in existence.
	 * @return a RSystem Object which keeps track of our Conferences/UserProfiles/etc.
	 * @author Dimitar Kumanov
	 */
	public static RSystem getInstance(){
		if(myInstance == null)
			myInstance = new RSystem();
		return myInstance;
	}
	
	/**
	 * Loads up all Conference/UserProfile objects into the RSYstem.
	 */
	private void deserializeData(){
		/*
		 * TO DO: Code for deserializing Data, aka loading up our
		 * Conference/UserProfile objects should happen here
		 */
	}
	
	/**
	 * Saves all state(data) into files for a later session of the program.
	 * This method should be called before closing the
	 * application to save all of its Conference/UserProfile Objects.
	 */
	public static void serializeModel(){
		/*
		 * TO DO: Code for serializing Data, aka saving up our
		 * Conference/UserProfile objects should happen here
		 */
	}
	
	/**
	 * Gets all of the Conferences in the system.
	 * @return a (non-null) List of Conferences in the system.
	 */
	public List<Conference> getConferences(){
		return new ArrayList<>(myConferenceMap.values());
	}
	
	/**
	 * Gets a specific Conference in system based on theConferenceName
	 * @param theConferenceName the Conference name to match a Conference with
	 * @return the specific Conference in system based on theConferenceName. null if no Conference matches.
	 * @author Dimitar Kumanov
	 */
	public Conference getConference(final String theConferenceName){
		return myConferenceMap.get(theConferenceName);
	}
	
	/**
	 * Adds a Conference to the System.
	 * @param theConference the Conference to add to the RSystem.
	 * @throws IllegalArgumentException if theConference name matches a Conference already in the RSystem.
	 */
	public void addConference(final Conference theConference) throws IllegalArgumentException{
		if(myConferenceMap.containsKey(theConference.getInfo().getName())){
			throw new IllegalArgumentException("There exists a Conference with this name in the RSystem already!");
		}
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
	
	/**
	 * Adds a UserProfile to the RSystem.
	 * @param theUserProfile  the Conference to add to the RSystem.
	 * @throws IllegalArgumentException  if theUserProfile's userID matches a UserProfile's userID already in the RSystem.
	 */
	public void addUserProfile(final UserProfile theUserProfile) throws IllegalArgumentException{
		if(myUserMap.containsKey(theUserProfile.getName())){
			throw new IllegalArgumentException("There exists a UserProfile with userID in the RSystem already!");
		}
		myUserMap.put(theUserProfile.getUID(), theUserProfile);
	}
	
}
