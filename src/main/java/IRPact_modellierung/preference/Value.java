package IRPact_modellierung.preference;

/**
 * A value is a very simple data structure to represent a value in the ethical sense,
 * as an aspiration for right conduct and good life.
 * Despite it's meaning in ethics, where it describes the importance of things and actions,
 * in this sense a value is the 'quality' that is aspired for.
 * The importance of values is expressed through Preferences.
 *
 * @author Simon Johanning
 */
public class Value {

	private String name;

	/**
	 * A Value represents a concept a Preference attributes a strength to in order to form a conviction (or not)
	 *
	 * @param name The name/identifier of the respective Value
	 */
	public Value(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String toString() {
		return name;
	}
}