package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

//TODO research whether this distribution can be used to construct general logistical distributions (such as is the case in normal distributions)

/**
 * Class that represents a standard logistic distribution (Log(0,1)), taken from the COLT library.
 * Distribution has its peak at 0 and a scale parameter of 1.
 *
 * @author Simon Johanning
 */
public class StandardLogisticDistribution extends COLTContinuousDistribution {

    /**
     * Generates the standard logistic distribution (Log(0,1)) from the COLT library
     *
     * @param name The name of the distribution (will be prefixed with StandardLogisticDistribution_)
     */
    public StandardLogisticDistribution(String name) {
        super(name);
    }

    public double draw() {
        return Distributions.nextLogistic(generator);
    }

}