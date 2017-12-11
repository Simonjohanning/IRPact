package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

/**
 * Class to model the Burr type VII distribution based on the COLT library.
 * The provided name is prefixed by BurrDistribution_typeVII_
 *
 * @author Simon Johanning
 */
public class BurrDistribution7 extends BurrDistribution {

	public BurrDistribution7(String name, double r) {
		super(name, r);
	}

	public double getR() {
		return this.r;
	}

	/**
	 * Draw a value from a Burr type VII distribution using the COLT library
	 *
	 * @return value drawn from a distribution corresponding to Burr type VII
	 */
	public double drawValue() {
		return (double) Distributions.nextBurr1(r, 7, generator);
	}

}