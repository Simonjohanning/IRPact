package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

/**
 * Class to model the Burr type IV distribution based on the COLT library.
 * The provided name is prefixed by BurrDistribution_typeIV_
 *
 * @author Simon Johanning
 */
public class BurrDistribution4 extends BurrDistribution {

	private double k;

	public BurrDistribution4(String name, double r, double k) {
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
	 * Draw a value from a Burr type IV distribution using the COLT library
	 *
	 * @return value drawn from a distribution corresponding to Burr type IV
	 */
	public double drawValue() {
		return (double) Distributions.nextBurr2(r, k, 4, generator);
	}

}