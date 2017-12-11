package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

/**
 * Class to model the Burr type VI distribution based on the COLT library.
 * The provided name is prefixed by BurrDistribution_typeVI_
 *
 * @author Simon Johanning
 */
public class BurrDistribution6 extends BurrDistribution {

	private double k;

	public BurrDistribution6(String name, double r, double k) {
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
	 * Draw a value from a Burr type VI distribution using the COLT library
	 *
	 * @return value drawn from a distribution corresponding to Burr type VI
	 */
	public double drawValue() {
		return (double) Distributions.nextBurr2(r, k, 6, generator);
	}

}