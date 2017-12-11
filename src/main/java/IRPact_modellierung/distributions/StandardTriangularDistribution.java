package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

//TODO investigate whether this distribution can be used to construct general logistical distributions (such as is the case in normal distributions)
//TODO think about whether / how to connect to a bounded distribution or if irrelevant

/**
 * Class that represents a standard triangular distribution (in (-1,1)), taken from the COLT library.
 * Distribution has it triangular shape and support in (-1,1)
 *
 * @author Simon Johanning
 */
public class StandardTriangularDistribution extends COLTContinuousDistribution {

    /**
     * Creates an instance of a standard triangular distribution /\ with support in (-1,1) based on the COLT library
     *
     * @param name Name of the distribution (will be prefixed by StandardTriangularDistribution_)
     */
    public StandardTriangularDistribution(String name) {
        super(name);
    }

    public double draw() {
        return Distributions.nextTriangular(generator);
    }

}