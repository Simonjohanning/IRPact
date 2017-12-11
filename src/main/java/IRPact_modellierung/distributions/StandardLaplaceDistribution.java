package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

//TODO research whether this distribution can be used to construct general Laplacion distributions (such as is the case in normal distributions)

/**
 * Class that realizes a standard Laplace distribution (double exponential distribution) L(0,1),
 * centered at 0 with scale parameter b=1.
 * Implementation is taken from the COLT library
 *
 * @author Simon Johanning
 */
public class StandardLaplaceDistribution extends COLTContinuousDistribution {

    /**
     * Generates a standard Laplace distribution (L(0,1)) based on the implementation in the COLT library.
     * Will generate values around 0 with probability exponentially decaying with distance to 0
     *
     * @param name Name of the standard Laplace Distribution (will be prefixed with StandardLaplaceDistribution_)
     */
    public StandardLaplaceDistribution(String name) {
        super(name);
    }

    public double draw() {
        return Distributions.nextLaplace(generator);
    }

}