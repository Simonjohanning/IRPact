package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

/**
 * Class to model the Burr type XII distribution based on the COLT library.
 * The provided name is prefixed by BurrDistribution_typeXII_
 *
 * @author Simon Johanning
 */
public class BurrDistribution12 extends BurrDistribution {

	private double k;

	public BurrDistribution12(String name, double r, double k) {
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
	 * Draw a value from a Burr type XII distribution using the COLT library
	 *
	 * @return value drawn from a distribution corresponding to Burr type XII
	 */
	public double drawValue(){
		return (double) Distributions.nextBurr2(r,k,12,generator);
	}
}