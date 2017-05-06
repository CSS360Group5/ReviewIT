package persistance;

import model.Paper;
import model.UserProfile;
import model.conference.Conference;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private static Map<String, Conference> myConferenceMap;
	
	/**
	 * Maps UserID to a UserProfile.
	 */
	private static Map<String, UserProfile> myUserMap;
	
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
	private void deserializeData() {
		/*
		 * TO DO: Code for deserializing Data, aka loading up our
		 * Conference/UserProfile objects should happen here
		 */
		try {
			FileInputStream fisUser = new FileInputStream("UserMap.ser");
			ObjectInputStream oisUser = new ObjectInputStream(fisUser);
			FileInputStream fisCon = new FileInputStream("ConferenceMap.ser");
			ObjectInputStream oisCon = new ObjectInputStream(fisCon);

			Map<String, UserProfile> newUserMap = new HashMap<>((HashMap<String, UserProfile>) oisUser.readObject());
			Map<String, Conference> newConfMap = new HashMap<>((HashMap<String, Conference>) oisCon.readObject());

			if (newUserMap != null && newConfMap != null) {
				myUserMap = new HashMap<>(newUserMap);
				myConferenceMap = new HashMap<>(newConfMap);
			}
			oisUser.close();
			fisUser.close();
			oisCon.close();
			fisCon.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.print("Deserialization successful.");
	}
	
	/**
	 * Saves all state(data) into files for a later session of the program.
	 * This method should be called before closing the
	 * application to save all of its Conference/UserProfile Objects.
	 */
	public static void serializeModel() {
		/*
		 * TO DO: Code for serializing Data, aka saving up our
		 * Conference/UserProfile objects should happen here
		 */
		try {
			FileOutputStream fosUser = new FileOutputStream("UserMap.ser");
			ObjectOutputStream oosUser = new ObjectOutputStream(fosUser);
			FileOutputStream fosCon = new FileOutputStream("ConferenceMap.ser");
			ObjectOutputStream oosCon = new ObjectOutputStream(fosCon);

			oosUser.writeObject(myUserMap);
			oosCon.writeObject(myConferenceMap);

			oosUser.close();
			fosUser.close();
			oosCon.close();
			fosCon.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.print("Serialization successful.");
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
		try {
			return myUserMap.get(theUserID);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * Adds a UserProfile to the RSystem.
	 * @param theUserProfile  the Conference to add to the RSystem.
	 * @throws IllegalArgumentException  if theUserProfile's userID matches a UserProfile's userID already in the RSystem.
	 */
	public void addUserProfile(final UserProfile theUserProfile) throws IllegalArgumentException{
		if(myUserMap.containsKey(theUserProfile.getUID())){
			throw new IllegalArgumentException("There exists a UserProfile with userID in the RSystem already!");
		}
		myUserMap.put(theUserProfile.getUID(), theUserProfile);
	}
	
	/**
	 * Gets ALL Papers from ALL Conferences submitted by theUserProfile
	 * @param theUserProfile The UserProfile of the person who submitted the papers
	 * @return ALL Papers from ALL Conferences submitted by theUserProfile 
	 */
	public List<Paper> getAllPapersSubmitted(final UserProfile theUserProfile){
		final List<Paper> submittedPapers = new ArrayList<>();
		for(final Conference currentConference: myConferenceMap.values()){
			submittedPapers.addAll(currentConference.getInfo().getPapersSubmittedBy(theUserProfile));
		}
		return submittedPapers;
	}
}
