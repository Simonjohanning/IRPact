package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

//TODO check whether through the cutoff, it is a bounded distribution / needs to be bounded through a bounded distribution abstraction (and also because for positive cut-off values will be >=0.0)

/**
 * Class that models a power law distribution (with cut off) based on the COLT library (see https://dst.lbl.gov/ACSSoftware/colt/api/cern/jet/random/Distributions.html#nextPowLaw(double,%20double,%20cern.jet.random.engine.RandomEngine))
 * Should return values following a power law (probably not exactly, since it does not asymptotically go to a power law, see https://en.wikipedia.org/wiki/Power_law#Power-law_probability_distributions)
 *
 * @author Simon Johanning
 */
public class PowerlawDistribution extends COLTContinuousDistribution {

	/**
	 * exponent of the power law distribution
	 */
	private double alpha;
	/**
	 * lower cut-off (for cut off in general see https://en.wikipedia.org/wiki/Power_law#Power-law_probability_distributions)
	 */
	private double cut;

	/**
	 * Generates a wrapper for the power law distribution as implemented in the COLT library
	 *
	 * @param name Name of the power law distribution (will be prefixed by PowerlawDistribution_)
	 * @param alpha The exponent of the power law distribution
	 * @param cut The cut-off of the corresponding distribution (as realized in the COLT library). No values larger than this will be returned.
	 *
	 * @throws IllegalArgumentException Will be thrown when cut off is <= 0
	 */
	public PowerlawDistribution(String name, double alpha, double cut) throws IllegalArgumentException{
		super(name);
		if(cut <= 0.0) throw new IllegalArgumentException("Cut-off shouldn't be negative since the power law should model positive values. Cut-off is"+cut);
		this.alpha = alpha;
		this.cut = cut;
	}

	public double getAlpha() {
		return this.alpha;
	}

	public double getCut() {
		return this.cut;
	}

	public double draw() {
		return Distributions.nextPowLaw(alpha, cut, generator);
	}
}