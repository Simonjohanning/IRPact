package IRPact_modellierung.distributions;

/**
 * Abstraction to model a probability distribution.
 * Due to the richness of distributions, few semantics is given to this abstraction.
 *
 * @author Simon Johanning
 */
public abstract class Distribution {

	protected String name;

	public Distribution(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString(){
		return ("Distribution "+name+" of type "+this.getClass().getSimpleName());
	}
}