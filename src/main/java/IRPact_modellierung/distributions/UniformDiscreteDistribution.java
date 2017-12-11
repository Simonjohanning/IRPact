package IRPact_modellierung.distributions;

import IRPact_modellierung.helper.ValueConversionHelper;

import java.util.ArrayList;
import java.util.Set;

/**
 * Class to represent a distribution made of a finite number of mass points
 * of which each is as likely to be drawn than any other.
 * Although functionally equivalent to a FiniteMassPointDistribution,
 * the values with be drawn in constant time, since no information about the mass of the points is needed.
 *
 * @author Simon Johanning
 */
public class UniformDiscreteDistribution extends DiscreteDistribution {

    private ArrayList<Double> massPoints;

    /**
     * Distribution to choose from a set of points with equal probability
     *
     * @param name Name of the distribution (will be prefixed with UniformDiscreteDistribution_)
     * @param massPoints The set of point to choose from
     */
    public UniformDiscreteDistribution(String name, Set<Double> massPoints) {
        super(name);
        this.massPoints = ValueConversionHelper.SetToArrayList(massPoints);
    }

    public double draw() {
        //draws a random point (return the value of a random entry)
        return massPoints.get((int) Math.floor(Math.random()*massPoints.size()));
    }
}
