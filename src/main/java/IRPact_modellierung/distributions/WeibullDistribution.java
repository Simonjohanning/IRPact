package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

//TODO check how alpha and beta relate to lambda and k on wikipedia; first experiments dont reproduce data on wiki

/**
 * Class to model a Weibull distribution using the COLT distribution library.
 *
 * @author Simon Johanning
 */
public class WeibullDistribution extends COLTContinuousDistribution {

	private double alpha;
	private double beta;

	/**
	 * Object modeling a Weibull distribution with parameters alpha and beta,
	 * as described in https://dst.lbl.gov/ACSSoftware/colt/api/cern/jet/random/Distributions.html#nextWeibull(double,%20double,%20cern.jet.random.engine.RandomEngine)
	 *
	 * @param name Name of the instance of the distribution (will be prefixed with Weibull_)
	 * @param alpha alpha parameter as noted in https://dst.lbl.gov/ACSSoftware/colt/api/cern/jet/random/Distributions.html#nextWeibull(double,%20double,%20cern.jet.random.engine.RandomEngine)
	 * @param beta beta parameter as noted in https://dst.lbl.gov/ACSSoftware/colt/api/cern/jet/random/Distributions.html#nextWeibull(double,%20double,%20cern.jet.random.engine.RandomEngine)
	 */
	public WeibullDistribution(String name, double alpha, double beta) {
		super(name);
		//check if parameters need to be checked in order to make sense (dont understand their meaning, and negative values don't seem to break anything
		/*if(alpha <= 0.0) throw new IllegalArgumentException("Illegal value provided for parameter alpha of Weibull distribution "+name+": "+alpha+".\nNeeds to be a (strictly) positive value!");
		else if(beta <= 0.0) throw new IllegalArgumentException("Illegal value provided for parameter alpha of Weibull distribution "+name+": "+alpha+".\nNeeds to be a (strictly) positive value!");
		*/this.alpha = alpha;
		this.beta = beta;
	}

	public double getAlpha() {
		return this.alpha;
	}

	public double getBeta() {
		return this.beta;
	}

	public double draw() {
		return Distributions.nextWeibull(alpha, beta, generator);
	}

}