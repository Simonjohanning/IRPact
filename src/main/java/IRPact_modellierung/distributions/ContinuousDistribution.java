package IRPact_modellierung.distributions;

//TODO check if a continuous distribution should be bounded (like it used to), and why!
//TODO check if it should necessarily be univariate

/**
 * Abstraction for continuous distributions based on the COLT library.
 * Includes a random engine since all implementations make use of this
 * (since they are based on the COLT library).
 * Should be generalized in further versions.
 *
 * @author Simon Johanning
 */
public abstract class ContinuousDistribution extends UnivariateDistribution {

    public ContinuousDistribution(String name) {
        super(name);
    }
}