package IRPact_modellierung.distributions;

/**
 * A Bivariate distribution is the abstraction of a distribution in two variables.
 * It is the special case of a multivariate distribution with dim=2
 *
 * @author Simon Johanning
 */
public abstract class BivariateDistribution extends MultivariateDistribution {

    /**
     * Creates a bivariate distribution as a multivariate distribution with n=2
     *
     * @param name Name of the distribution
     */
    public BivariateDistribution(String name) {
        super(name, 2);
    }

}
