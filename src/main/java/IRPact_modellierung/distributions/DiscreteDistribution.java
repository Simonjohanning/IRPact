package IRPact_modellierung.distributions;

//TODO check if bounds are needed for the distribution

/**
 * Abstraction to describe a discrete distribution.
 *
 * @author Simon Johanning
 */
public abstract class DiscreteDistribution extends UnivariateDistribution {

    public DiscreteDistribution(String name) {
        super(name);
    }
}