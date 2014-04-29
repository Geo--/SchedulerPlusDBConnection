
public abstract class UniversityPerson implements Person {

	private String forename;
	private String surname;

	/**
	 * UnicersityPerson Constructor, taking forename and surname as parameters
	 * (strings)
	 * 
	 * @param forename
	 * @param surname
	 */
	public UniversityPerson(String forename, String surname) {

		this.forename = forename;
		this.surname = surname;

	}

	/**
	 * overrides the toString() method
	 */
	public String toString() {

		return forename + " " + surname;
	}

	/**
	 * getName() method returns a string of persons name
	 */
	public String getName() {

		return forename + " " + surname;
	}

}
