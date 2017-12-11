package IRPact_modellierung.distributions;

import java.util.ArrayList;

/**
 * Abstraction class to model distributions in several variables.
 *
 * @author Simon Johanning
 */
public abstract class MultivariateDistribution extends Distribution{

    protected int dimension;

    public MultivariateDistribution(String name, int dimension) {
        super(name);
        this.dimension = dimension;
    }

    public int getDimension() {
        return dimension;
    }

    public String getName() {
        return name;
    }

    public abstract ArrayList<Double> draw();
}
