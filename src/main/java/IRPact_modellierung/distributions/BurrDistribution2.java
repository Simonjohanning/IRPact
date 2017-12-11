package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

/**
 * Class to model the Burr type II distribution based on the COLT library.
 * The provided name is prefixed by BurrDistribution_typeII_
 *
 * @author Simon Johanning
 */
public class BurrDistribution2 extends BurrDistribution {

	public double getR() {
		return this.r;
	}

	public BurrDistribution2(String name, double r) {
		super(name, r);
	}

	/**
	 * Draw a value from a Burr type II distribution using the COLT library
	 *
	 * @return value drawn from a distribution corresponding to Burr type II
	 */
	public double drawValue(){
		return (double) Distributions.nextBurr1(r,2,generator);
	}
}