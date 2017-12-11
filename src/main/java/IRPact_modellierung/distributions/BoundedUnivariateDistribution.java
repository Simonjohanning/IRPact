package IRPact_modellierung.distributions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstraction to model a univariate distribution that has a lower and an upper bound.
 * The implementation of the bounded univariate distribution is used to generate values;
 * However this abstraction checks that the values are within the specified bounds,
 * and will otherwise generate values until this is achieved.
 * Thus the modeler has to be careful that legal values can be derived from the distribution
 * in a reasonable amount of time (meaning that a reasonable amount of mass of the distribution
 * should be concentrated within the [lowerBounds, upperBounds]
 *
 * @author Simon Johanning
 */
public abstract class BoundedUnivariateDistribution extends UnivariateDistribution {

    private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    protected double lowerBound;
    protected double upperBound;

    /**
     * Generates a univariate distribution generating values within the interval [lowerBound, upperBound]
     *
     * @param name Name of the distribution
     * @param lowerBound The lower bound for the distribution (guarantees that derived values will be >= lowerBound)
     * @param upperBound The upper bound for the distribution (guarantees that derived values will be <= upperBound)
     * @throws IllegalArgumentException Will be thrown when lowerBound > upperBound
     */
    public BoundedUnivariateDistribution(String name, double lowerBound, double upperBound) throws IllegalArgumentException{
        super(name);
        fooLog.debug("In bounded univar distr with lower bound "+lowerBound+" and upper bound "+upperBound);
        if(lowerBound > upperBound) throw new IllegalArgumentException("Lower bound ("+lowerBound+") of the bounded distribution is higher than upper bound ("+upperBound+")!!!\n Something went terribly wrong!!");
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     * Draws a value from the underlying distribution.
     * Will keep drawing values until it is provided with a legal value
     * (that lies in [lowerBound, upperBound]).
     *
     * @return Legal value of the distribution (within [lowerBound, upperBound]).
     */
    public double draw(){
        if(lowerBound == upperBound) return lowerBound;
        boolean validValueDrawn = false;
        double returnValue = -Double.MAX_VALUE;
        //will draw values from the underlying distribution until it is provided with a legal value
        while(!validValueDrawn){
            returnValue = drawValue();
            if((returnValue >= lowerBound) && (returnValue <= upperBound)){
                validValueDrawn = true;
            }
        }
        return returnValue;
    }

    /**
     * Method that generates the values from the underlying distributions for the draw-method.
     * Will be called to derive values of instances
     *
     * @return A value provided by the underlying distribution
     */
    public abstract double drawValue();

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }
}

