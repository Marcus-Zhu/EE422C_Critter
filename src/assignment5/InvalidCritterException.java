package assignment5;

public class InvalidCritterException extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	String offending_class;

	public InvalidCritterException(String critter_class_name) {
		offending_class = critter_class_name;
	}

	public String toString() {
		return "Invalid Critter Class: " + offending_class;
	}

}
