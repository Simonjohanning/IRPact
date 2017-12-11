package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

/**
 * Class to model a geometric distribution based on the implementation in the COLT distribution library.
 *
 * @author Simon Johanning
 */
public class GeometricDistribution extends DiscreteDistribution {

	/**
	 * the argument to the probability distribution function
	 */
	private int k;
	/**
	 * the parameter of the probability distribution function
	 */
	private double p;

	//TODO check parameters

	/**
	 * Will create a geometric distribution based on the COLT library.
	 *
	 * @param name Name of the distribution (will be prefixed by GeometricDistribution_)
	 * @param k the argument to the probability distribution function for the COLT implementation
	 * @param p the parameter of the probability distribution function for the COLT implementation
	 */
	public GeometricDistribution(String name, int k, double p) {
		super(name);
		this.k = k;
		this.p = p;
	}

	public int getK() {
		return this.k;
	}

	public double getP() {
		return this.p;
	}

	public double draw() {
		return Distributions.geometricPdf(k, p);
	}

}