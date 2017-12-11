package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

/**
 * Class to model the Burr type III distribution based on the COLT library.
 * The provided name is prefixed by BurrDistribution_typeIII_
 *
 * @author Simon Johanning
 */
public class BurrDistribution3 extends BurrDistribution {

	private double k;

	public BurrDistribution3(String name, double r, double k) {
		super(name, r);
		this.k = k;
	}

	public double getR() {
		return this.r;
	}

	public double getK() {
		return this.k;
	}

	/**
	 * Draw a value from a Burr type III distribution using the COLT library
	 *
	 * @return value drawn from a distribution corresponding to Burr type III
	 */
	public double drawValue() {
		return (double) Distributions.nextBurr2(r, k, 3, generator);
	}

}