package IRPact_modellierung.distributions;

/**
 * Abstraction for distributions in one variable.
 * Every univariate distribution will provide a draw function, drawing a value from the distribution.
 * This function is parameter-free; however concrete instances should offer more meaningful
 * overloaded alternatives for this to give richer semantics to this.
 *
 * @author Simon Johanning
 */
public abstract class UnivariateDistribution extends Distribution{

    public UnivariateDistribution(String name) {
        super(name);
    }

    /**
     * Draw a value from the distribution according to the distribution at hand
     *
     * @return A double number drawn from the modeled distribution
     */
    public abstract double draw();
}
