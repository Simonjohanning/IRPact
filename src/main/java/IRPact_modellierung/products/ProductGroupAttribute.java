package IRPact_modellierung.products;

import IRPact_modellierung.distributions.BoundedUnivariateDistribution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A ProductGroupAttribute describes a quality (and their attributes)
 * of a ProductGroup.
 * Different ProductAttributes derived from this ProductGroupAttribute can have different values
 * for this quality; Since they refer to the same ProductGroupAttribute however,
 * they share the attributes (i.e. mutability and observability) this ProductGroupAttribute has.
 *
 * @author Simon Johanning
 */
public class ProductGroupAttribute {

	private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	private String name;
	private BoundedUnivariateDistribution value;
	private boolean mutability;
	private double observability;

	/**
	 * A ProductGroupAttribute of the qualifier name is characterized by their mutability
	 * (whether the values of a derived ProductAttribute are allowed to change), their observability
	 * (how easy it is for a ConsumerAgent to derive the true value of the ProductAttribute), and
	 * the distribution of values for ProductAttributes exhibiting this quality.
	 *
	 * @param name The qualifier for this ProductGroupAttribute
	 * @param value The distribution of values for derived ProductAttributes
	 * @param mutability Whether derived ProductAttributes are allowed to change in value
	 * @param observability How easy it is for a ConsumerAgent to derive information about the true value of derived ProductAttributes
	 */
	public ProductGroupAttribute(String name, BoundedUnivariateDistribution value, boolean mutability, double observability) {
		this.name = name;
		this.value = value;
		this.mutability = mutability;
		this.observability = observability;
	}

	public String getName() {
		return this.name;
	}

	public BoundedUnivariateDistribution getValue() {
		return this.value;
	}

	public boolean getMutability() {
		return this.mutability;
	}

	public double getObservability() {
		return observability;
	}

	public String toString(){
		String returnString = ("Product attribute "+name+" has a mutability of "+mutability+" and an observability of "+observability+".\n");
		returnString += ("The distribution used is as follows: \n")+value.toString();
		return returnString;
	}
}