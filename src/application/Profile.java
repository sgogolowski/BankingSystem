package application;

/**
 * 	Profile class to define the account holder 
 *  Contains the account holders first and last name
 *  @authors Szymon Gogolowski, Julio Collado
 */
public class Profile {

	private String fname;
	private String lname;
	
	/**
	 * Default constructor to initialize fname and lname to empty strings
	 */
	public Profile() {
		fname = "";
		lname = "";
	}
	
	/**
	 * Parameterized constructor to initialize a Profile object with the parameterized values
	 */
	public Profile(String fname, String lname) {
		this.fname = fname;
		this.lname = lname;
	}
	
	/**
	 * fname accessor method
	 * @return String fname
	 */
	public String getFname() {
		return fname;
	}
	
	/**
	 * fname mutator method
	 * @param fname is the new value of fname
	 */
	public void setFname(String fname) {
		this.fname = fname;
	}
	
	/**
	 * lname accessor method
	 * @return String lname
	 */
	public String getLname() {
		return lname;
	}

	/**
	 * lname mutator method
	 * @param lname is the new value of lname
	 */
	public void setLname(String lname) {
		this.lname = lname;
	}

	/**
	 * toString method to return the String representation of a Profile object
	 * @return "fname lname"
	 */
	@Override
	public String toString() {
		return fname + " " + lname;
	}
	
	/**
	 * equals method to compare 2 instances of a profile object.
	 * checks if fname and lnames are equals.
	 * @return "fname lname"
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Profile) {
			Profile equalsProfile = (Profile) obj;
			return (this.fname.equals(equalsProfile.getFname()) && this.lname.equals(equalsProfile.getLname()));
		}
		return false;
	}
}
