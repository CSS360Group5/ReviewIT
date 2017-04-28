package model;

public class UserProfile {
	private String myUID;
	private String myName;
	
	public UserProfile(final String theUID,
						final String theName){
		myUID = theUID;
		myName = theName;
	}
	
	public String getUID(){
		return myUID;
	}
	
	
	public String getName(){
		return myName;
	}
}
