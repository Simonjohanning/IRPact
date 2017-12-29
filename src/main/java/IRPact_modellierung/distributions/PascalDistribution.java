package IRPact_modellierung.distributions;

//TODO integrate with negative distribution; check whether to use the apache common math or COLT library

/**
 * The PascalDistribution class is a wrapper class for the pascal distribution
 * as modeled in the common math distribution library.
 * As a special (discrete) case of a negative binomial distribution, it represents
 * the number of failed Bernoulli experiments until r successes are reached,
 * with a success probability of p for each (iid) Bernoulli experiment.
 *
 * @author Simon Johanning
 */
public class PascalDistribution extends UnivariateDistribution{

    private int r;
    private double p;
    private org.apache.commons.math3.distribution.PascalDistribution correspondingDistribution;

    /**
     * An instance of a pascal distribution, describing the number of failures
     * till r successes are reached with success parameter p
     *
     * @param name The name of the distribution to create
     * @param r The number of successes needed
     * @param p The success parameter for each iid Bernoulli experiment
     */
    public PascalDistribution(String name, int r, double p) throws IllegalArgumentException{
        super(name);
        if((p <= 0.0) && (r > 0)) throw new IllegalArgumentException("Parameters for this distribution are invalid; Will run into an endless loop!!\n(r,p) = ("+r+","+p+")!!");
        this.r = r;
        this.p = p;
        this.correspondingDistribution = new org.apache.commons.math3.distribution.PascalDistribution(r,p);
    }

    /**
     * Method to determine the number of trials until r successes are reached.
     * Will test which is the first x so that the cumulative probability of the distribution
     * exceeds a realization of a uniformaly drawn random value
     *
     * @return Returns the number of trials until r successes are reached
     */
    public double draw(){
        int trials = 0;
        double valueDrawn = Math.random();
        while(valueDrawn > correspondingDistribution.cumulativeProbability(trials)){
            trials++;
        }
        return trials;
    }
}
