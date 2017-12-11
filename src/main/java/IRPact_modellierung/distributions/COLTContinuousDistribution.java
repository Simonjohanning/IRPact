package IRPact_modellierung.distributions;

import cern.jet.random.engine.RandomEngine;

//TODO check if a continuous distribution should be bounded (like it used to), and why!

/**
 * Abstraction for continuous distributions based on the COLT library.
 * Includes a random engine since all implementations make use of this
 * (since they are based on the COLT library).
 * Should be generalized in further versions.
 *
 * @author Simon Johanning
 */
public abstract class COLTContinuousDistribution extends ContinuousDistribution {

    protected RandomEngine generator;

    public COLTContinuousDistribution(String name) {
        super(name);
        generator = new cern.jet.random.engine.MersenneTwister(new java.util.Date());
    }
}