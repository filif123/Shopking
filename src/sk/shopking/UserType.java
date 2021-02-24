package sk.shopking;

/**
 * Voľba typu používateľa
 * @author Filip
 *
 */
public enum UserType {

	/**
	 * Používateľ je/bude administrátor
	 */
	ADMIN("Administrátor"),
	/**
	 * Používateľ je/bude pokladník
	 */
	POKLADNIK("Pokladník");
	
	private final String usertype;
	
	UserType(String usertype) {
		this.usertype = usertype;
	}
	
	public String getUsertype() {
        return usertype;
    }
	
	@Override 
	public String toString() { 
		return usertype; 
	}
}
