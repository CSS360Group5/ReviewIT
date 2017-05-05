package model;

/**
 * This is a custom Error class that can be used to throw exceptions with custom error messages.
 * [LAST EDIT: 4-25-17}
 * @author Harlan Stewart
 * @version 1.0
 */
public class IllegalOperationException extends Exception {

	/**
	 * Auto generated UID
	 */
	private static final long serialVersionUID = -5428469107593029121L;
	
	/**
	 * Do nothing constructor
	 */
	public IllegalOperationException() {
		
	}
	
	/** Public error exception constructor that excepts a string as a 
	 * parameter which can be use to display any error message.
	 * @author Harlan Stewart 
	 * @param theErrorMessage
	 */
	public IllegalOperationException(final String theErrorMessage) {
		super(theErrorMessage);
	}
	

}
