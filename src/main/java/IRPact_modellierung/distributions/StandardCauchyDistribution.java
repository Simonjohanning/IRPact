package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;
import cern.jet.random.engine.RandomEngine;

//TODO check for other provider for distribution since parameters cant be set!!
//TODO research whether this distribution can be used to construct general Cauchy distributions (such as is the case in normal distributions)

/**
 * Class that represents a standard Cauchy distribution (with a peak at 0 and scale parameter 1.0
 * (scale parameter is half-width at half maximum (HWHM)) based on the COLT library
 * Parameters can't be changed; for Cauchy distributions parameterized differently,
 * the reader is referred to other implementations or needs to implement their own distribution.
 *
 * @author Simon Johanning
 */
public class StandardCauchyDistribution extends CauchyDistribution {

    private RandomEngine generator;

    /**
     * A Cauchy distribution with a peak at 0.0 and a scale parameter (HWHM) of 1.0.
     * Implementation is based on the COLT library
     *
     * @param name Name of the standard Cauchy distribution (prefixed with StandardCauchy_distribution_)
     */
    public StandardCauchyDistribution(String name) {
        super(name, 0.0, 1.0);
        generator = new cern.jet.random.engine.MersenneTwister(new java.util.Date());
    }

    public double draw(){ return (double) Distributions.nextCauchy(generator);}

}