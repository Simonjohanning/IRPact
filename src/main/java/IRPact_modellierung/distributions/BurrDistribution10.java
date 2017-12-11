package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

/**
 * Class to model the Burr type X distribution based on the COLT library.
 * The provided name is prefixed by BurrDistribution_typeX_
 *
 * @author Simon Johanning
 */
public class BurrDistribution10 extends BurrDistribution {

	public BurrDistribution10(String name, double r) {
		super(name, r);
	}

	public double getR() {
		return this.r;
	}

	/**
	 * Draw a value from a Burr type X distribution using the COLT library
	 *
	 * @return value drawn from a distribution corresponding to Burr type X
	 */
	public double drawValue(){
			return (double) Distributions.nextBurr1(r,10,generator);
	}
}