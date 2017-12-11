package IRPact_modellierung.distributions;

import cern.jet.random.engine.RandomEngine;

/**
 * Abstraction for discrete distributions based on the COLT library.
 * Includes a random engine since all implementations make use of this
 * (since they are based on the COLT library).
 * Should be generalized in further versions.
 *
 * @author Simon Johanning
 */

public abstract class COLTDiscreteDistribution extends DiscreteDistribution{

    protected RandomEngine generator;

    public COLTDiscreteDistribution(String name) {
        super(name);
        generator = new cern.jet.random.engine.MersenneTwister(new java.util.Date());
    }
}
