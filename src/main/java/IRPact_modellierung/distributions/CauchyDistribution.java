package IRPact_modellierung.distributions;

//TODO find a more general distribution provider to base general Cauchy distribution on.

/**
 * Abstraction to represent a generalized CauchyDistribution.
 * For now more of a helping class for the standard cauchy distribution
 *
 * @author Simon Johanning
 */
public abstract class CauchyDistribution extends ContinuousDistribution {

	/**
	 * location parameter (peak of distribution)
	 */
	private double x0;

	/**
	 * scale parameter (half-width at half maximum (HWHM))
	 */
	private double gamma;

	public CauchyDistribution(String name, double x0, double gamma) throws IllegalArgumentException{
		super(name);
		if(gamma <= 0) throw new IllegalArgumentException("Illegal value entered for the scale parameter gamma ("+gamma+") in the Cauchy distribution "+name+"!! Gamma needs to be strictly positive!");
		this.x0 = x0;
		this.gamma = gamma;
	}

	public double getX0() {
		return this.x0;
	}

	public double getGamma() {
		return this.gamma;
	}

}