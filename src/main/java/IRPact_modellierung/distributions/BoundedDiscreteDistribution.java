package IRPact_modellierung.distributions;

//TODO model bounded discrete and continuous distributions separately or bounded distributions?
//TODO go back to general case where distributions are always bounded with potentially unset bounds?
//TODO think about removing booleans indicating a bound is set
//TODO think about whether to allow bounds to be reset

/**
 * Abstraction that models discrete distributions that are cut off at certain bounds.
 * Allows for bounds to be set to only one side (or neither (in which case it is equivalent to a non-bounded distribution)).
 *
 * @author Simon Johanning
 */
public abstract class BoundedDiscreteDistribution extends DiscreteDistribution{

    private double lowerBound;
    private double upperBound;
    private boolean lowerBoundSet;
    private boolean upperBoundSet;

    /**
     * Will create a bounded distribution, which will reject any values drawn from the distribution not lying within these bounds.
     * The user should take care that a reasonable amount of mass of the distribution lies within the bounds,
     * since otherwise the process of drawing a number could take very long
     *
     * @param name The name of the distribution
     * @param lowerBound The lower bound of the distribution (will only create values at least as big as this)
     * @param upperBound The upper bound of the distribution (will only create values at most as big as this)
     * @throws IllegalArgumentException Will be thrown when lowerBound > upperBound making no sense and leading to infinite loops
     */
    public BoundedDiscreteDistribution(String name, double lowerBound, double upperBound) throws IllegalArgumentException{
        super(name);
        if(lowerBound > upperBound) throw new IllegalArgumentException("Lower bound of the distribution ("+lowerBound+") > upper bound of the distribution ("+upperBound+"), which doesnt make sense and would lead to infinite loops!!!");
        this.lowerBound = lowerBound;
        lowerBoundSet = true;
        this.upperBound = upperBound;
        upperBoundSet = true;
    }

    /**
     * Will create a distribution bound to one side (other side will be +- infinity (Double.MAX)),
     * which will reject any values drawn from the distribution not lying within these bounds.
     * Since Java prohibits overloading with same data types, the user has to provide the value of the bound
     * as well whether it is a lower or upper bound.
     *
     * The user should take care that a reasonable amount of mass of the distribution lies within the bounds,
     * since otherwise the process of drawing a number could take very long
     *
     * @param name The name of the distribution
     * @param bound The bound of the distribution (will only create values at most as big as this (if upper bound) or as small as this (if lower bound)
     * @param boundUpper Indicates what direction of the interval the bound is located (true for upperBound, false for lower bound)
     */
    public BoundedDiscreteDistribution(String name, double bound, boolean boundUpper) {
        super(name);
        if(boundUpper){
            upperBound=bound;
            lowerBound = -Double.MAX_VALUE;
        }
        else{
            lowerBound=bound;
            upperBound=Double.MAX_VALUE;
        }
        lowerBoundSet = !boundUpper;
        upperBoundSet = boundUpper;
    }

    /**
     * Will draw a value corresponding to the (cut off) underlying probability distribution lying within the bounds.
     * This will skew the distribution though!!!
     *
     * @return A realization of the random variable corresponding to the underlying distribution, lying within [lowerBound, upperBound]
     */
    public double draw(){
        if(lowerBound==upperBound) return lowerBound;
        boolean validValueDrawn = false;
        double valueDrawn = 0.0;
        while(!validValueDrawn){
            valueDrawn=drawValue();
            if((valueDrawn >= lowerBound) && (valueDrawn <= upperBound)){
                validValueDrawn=true;
            }
        }
        return valueDrawn;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public boolean isLowerBoundSet() {
        return lowerBoundSet;
    }

    public boolean isUpperBoundSet() {
        return upperBoundSet;
    }

    //Method that will be realized by the underlying distribution
    protected abstract double drawValue();
}
