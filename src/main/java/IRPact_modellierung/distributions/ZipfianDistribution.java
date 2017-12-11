package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

/**
 * Class to model a zipfian distribution based on the COLT library.
 * Models contexts that follow the Zipfian law
 *
 * @author Simon Johanning
 */
public class ZipfianDistribution extends COLTDiscreteDistribution {

	/**
	 * the skew of the distribution
	 */
	private double z;

	/**
	 * Creates an instance of a Zipfian distribution based on the COLT library.
	 * Needs a skew larger than unity of the skew of the distribution it is to model
	 *
	 * @param name The name of the distribution (will be prefixed by ZipfianDistribution_)
	 * @param z The skew of the distribution to model
	 *
	 * @throws IllegalArgumentException An exception is thrown when the skew of the distribution is not larger than unity, since otherwise the COLT distribution won't terminate
	 */
	public ZipfianDistribution(String name, double z) throws IllegalArgumentException{
		super(name);
		if(z <= 1.0) throw new IllegalArgumentException("Parameter z (skew) is not larger than unity (needs to be > 1.0), but is "+z);
		this.z = z;
	}

	public double getZ() {
		return this.z;
	}

	public double draw() {
		return (double) Distributions.nextZipfInt(z, generator);
	}

}