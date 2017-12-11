package IRPact_modellierung.distributions;

import cern.jet.random.Binomial;
import cern.jet.random.engine.RandomEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * Class that models a bounded binomial distribution
 * using the cern.jet.random library for binomial distributions.
 * Describes the distribution to derive an integer number of successes from a
 * number of independent trials (Bernoulli experiments) with a success probability p.
 * Drawn values will be cut off until they fall within the bounds [lowerBounds,upperBounds]
 *
 * @author Simon Johanning
 */
public class BoundedBinomialDistribution extends BoundedDiscreteDistribution {

    private static final Logger logger = LogManager.getLogger("debugConsoleLogger");

    /**
     * numbers of (independent) trials (Bernoulli experiments)
     */
    private int n;
    /**
     * probability of success per individual trial
     */
    private double p;
    //The COLT distribution corresponding to the binomial distribution
    private Binomial coltDistribution;
    private boolean defaultBounds;

    /**
     * Generates the distribution for a fixed number of trials n with a success probability p.
     * Provides the possibility to provide upper and lower bounds that cut off the probability,
     * skewing the probability distribution!!
     *
     * @param name Name of the distribution to model (will be prefixed by Binomial_Distribution)
     * @param lowerBound the lower cutoff of the distribution (distribution will not generate values smaller than this)
     * @param upperBound the upper cutoff of the distribution (distribution will not generate values larger than this)
     * @param n The number of tries / independent trials of Bernoulli experiments
     * @param p probability of success for each Bernoulli experiment
     * @throws IllegalArgumentException Will be thrown when parameters are illegal (n negative or p not in unit interval ([0,1]))
     */
    public BoundedBinomialDistribution(String name, double lowerBound, double upperBound, int n, double p) throws IllegalArgumentException{
        super(name, lowerBound, upperBound);
        if(n<0) throw new IllegalArgumentException("The number of (independent) trials can not be negative!!");
        else if((p<0.0) || (p>1.0)) throw new IllegalArgumentException("Value for p must be within 0.0 and 1.0, is"+p+".\n Value will not be reset and "+p+" will be retained!");
        else{
            if(lowerBound < 0) logger.warn("The lower bound entered for the binomial distribution "+name+" < 0, which is out of the range of the probability distribution anyways (will return values in [0,"+n+"]). This might indicate a modeling error.");
            if(upperBound > n) logger.warn("The upper bound entered for the binomial distribution "+name+" > n, which is out of the range of the probability distribution anyways (will return values in [0,"+n+"]). This might indicate a modeling error.");
            defaultBounds = false;
            RandomEngine generator = new cern.jet.random.engine.MersenneTwister(new java.util.Date());
            coltDistribution = new Binomial(n,p,generator);
            this.n = n;
            this.p = p;
        }
    }

    /**
     * Generates the distribution for a fixed number of trials n with a success probability p.
     * Bounds of the distribution are set to 0 and n.
     *
     * @param name Name of the distribution to model (will be prefixed by Binomial_Distribution)
     * @param n The number of tries / independent trials of Bernoulli experiments
     * @param p probability of success for each Bernoulli experiment
     * @throws IllegalArgumentException Will be thrown when parameters are illegal (n negative or p not in unit interval ([0,1]))
     */
    public BoundedBinomialDistribution(String name, int n, double p) throws IllegalArgumentException{
        super(name, 0.0, (double) n);
        defaultBounds = true;
        if(n<0) throw new IllegalArgumentException("The number of (independent) trials can not be negative!!");
        else if((p<0.0) || (p>1.0)) throw new IllegalArgumentException("Value for p must be within 0.0 and 1.0, is"+p+".\n Value will not be reset and "+p+" will be retained!");
        else {
            this.n = n;
            this.p = p;
            RandomEngine generator = new cern.jet.random.engine.MersenneTwister(new java.util.Date());
            this.coltDistribution = new Binomial(n, p, generator);
        }
    }

    /**
     * Draws a value from the distribution randomly
     * through returning the value for which the cumulative distribution
     * of this Binomial distribution corresponds to a value drawn from a normal distribution
     *
     * @return integer value in [0:n] drawn with the probability of the binomial distribution
     */
    public double drawValue(){
        double uniformRandomDraw = Math.random();
        //see which k corresponds to a uniformly drawn value in the cumulative distribution
        for(int k=0;k<n;k++) {
            if ((double) coltDistribution.cdf(k) >= uniformRandomDraw) {
                return (double) k;
            }
        }
        return (double) n;
    }

    //TODO delete if resetting should not be permitted, uncomment if so
   /*
    *  *//**
     * Reset the numbers of independent trails used by this distribution
     *
     * @param n number of trials corresponding to this distribution replacing current number
     *//*
    public void resetN(int n) throws IllegalArgumentException{
        if(n<0) throw new IllegalArgumentException("The number of (independent) trials can not be negative!!");
        else {
            this.n = n;
            //replace aspects of the distribution linked to this parameter
            if (defaultBounds) upperBound = n;
            this.coltDistribution = new Binomial(n, p, new cern.jet.random.engine.MersenneTwister(new java.util.Date()));
        }
    }

    /**
     * Resets the probability of success for individual Bernoulli experiments of the distribution
     *
     * @param p Probability of success of each individual Bernoulli experiment
     * @throws IllegalArgumentException gets thrown when an illegal value is provided for p (outside of unit interval [0,1])

    public void resetP(double p) throws IllegalArgumentException{
        if((p<0.0) || (p>1.0)) throw new IllegalArgumentException("Value for p must be within 0.0 and 1.0, is"+p+".\n Value will not be reset and "+p+" will be retained!");
        else{
            this.p=p;
            this.coltDistribution = new Binomial(n, p, new cern.jet.random.engine.MersenneTwister(new java.util.Date()));
        }
    }*/

    public int getN() {
        return this.n;
    }

    public double getP() {
        return this.p;
    }

}