package IRPact_modellierung.distributions;

/**
 * Class that models a uniform distribution (thus is necessarily bounded).
 * Will return a value in [lowerBound, upperBound] with equal probability.
 *
 * @author Simon Johanning
 */
public class UniformContinuousDistribution extends BoundedContinuousDistribution {

    /**
     * Uniform continuous distribution that draws each value in [lowerBound, upperBound] with equal probability.
     * Will return an error when instantiated with illegal bounds.
     *
     * @param name Name of the distribution (will be prefixed by UniformContinuousDistribution_)
     * @param lowerBound lower bound of the interval the values are drawn from
     * @param upperBound upper bound of the interval drawn from
     * @throws IllegalArgumentException An error will be created when the bounds are inconsistent (i.e. upperBound < lowerBound)
     */
    public UniformContinuousDistribution(String name, double lowerBound, double upperBound) throws IllegalArgumentException{
        super(name, lowerBound, upperBound);
    }

    //insert failcheck when manipulating this!
    public double drawValue() {
        //It can be expected that upperBound>lowerBound since draw in superclass checks for this
        return (Math.random()*(upperBound - lowerBound) + lowerBound);
    }
}
