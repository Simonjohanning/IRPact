package IRPact_modellierung.preference;

//TODO find out why I should care whether the same value is concerned

/**
 * A preference in IRPact is modeled via associating a (numerical) strength to a value, signifying how important a value is for a certain agent.
 * Since a Preference implements the Comparable interface, preferences can be compared with one another.
 * HOWEVER, THIS WILL JUST REGARD THE STRENGTH AND NO CHECKING IS DONE WHETHER THE SAME VALUE IS CONCERNED!!!!
 *
 * @author Simon Johanning
 */
public class Preference implements Comparable<Preference> {

	private Value value;
	private double strength;

	/**
	 * Preference is modeled as a coupling of a Value and the strength of the value (as a numerical value).
	 * There is no inherit value limitation, this is limited to the modeler
	 *
	 * @param value The value this preference refers to
	 * @param strength The strength of the preference / the value (as numerical value)
	 */
	public Preference(Value value, double strength) {
		this.value = value;
		this.strength = strength;
	}

	public Value getValue() {
		return this.value;
	}

	public double getStrength() {
		return this.strength;
	}

	public void setStrength(double strength) {
		this.strength = strength;
	}

	public String toString(){
		return ("The strength of value "+value.getName()+" is "+strength);
	}

	/**
	 * Compare preferences via the strength of preferences.
	 * Does not check whether preferences refer to the same value!!
	 *
	 * @param referencePreference The preference this preference is to be compared to
	 * @return will return -1 if this preference is weaker, 0 if they are just as strong and 1 if it is stronger than the reference preference
	 */
	public int compareTo(Preference referencePreference) {
		if(strength < referencePreference.getStrength()) return -1;
		else if(strength == referencePreference.getStrength()) return 0;
		else return 1;
	}
}