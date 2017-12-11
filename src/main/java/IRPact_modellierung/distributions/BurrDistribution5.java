package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

/**
 * Class to model the Burr type V distribution based on the COLT library.
 * The provided name is prefixed by BurrDistribution_typeV_
 *
 * @author Simon Johanning
 */
public class BurrDistribution5 extends BurrDistribution {

	private double k;

	public BurrDistribution5(String name, double r, double k) {
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
	 * Draw a value from a Burr type V distribution using the COLT library
	 *
	 * @return value drawn from a distribution corresponding to Burr type V
	 */
	public double drawValue(){
		return (double) Distributions.nextBurr2(r,k,5,generator);
	}

}