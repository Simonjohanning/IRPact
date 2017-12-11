package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

/**
 * Class to model the Burr type VIII distribution based on the COLT library.
 * The provided name is prefixed by BurrDistribution_typeVIII_
 *
 * @author Simon Johanning
 */
public class BurrDistribution8 extends BurrDistribution {

	public BurrDistribution8(String name, double r) {
		super(name, r);
	}

	public double getR() {
		return this.r;
	}

	/**
	 * Draw a value from a Burr type VIII distribution using the COLT library
	 *
	 * @return value drawn from a distribution corresponding to Burr type VIII
	 */
	public double drawValue() {
		return (double) Distributions.nextBurr1(r, 8, generator);
	}

}