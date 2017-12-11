package IRPact_modellierung.distributions;

import org.apache.commons.math3.distribution.NormalDistribution;

//TODO check whether to allow to manipulate mean and variance of this distribution or whether creating a new distribution for this should be force. If the latter, change this for distributions with names < Norm

/**
 * Class that models a (univariate) normal distribution based on the math3.distribution library of the apache commons.
 *
 * @author Simon Johanning
 */
public class NormDistribution extends ContinuousDistribution {

    private NormalDistribution correspondingDistribution;
    private double mean;
    private double variance;

    /**
     * Creates a (univariate) normal distribution based on the math3.distribution library of the apache commons.
     *
     * @param name Name associated with the normal distribution (will be prefixed by NormalDistribution_)
     * @param mean Mean value of the distribution
     * @param variance Variance of the distribution
     *
     * @throws IllegalArgumentException will be thrown when the variance is negative (which doesnt make any sense)
     */
    public NormDistribution(String name, double mean, double variance) throws IllegalArgumentException{
        super(name);
        if(variance < 0) throw new IllegalArgumentException("Variance of the normal distribution needs to be positive; was intended to be set at "+variance);
        correspondingDistribution = new NormalDistribution(mean, variance);
        this.mean = mean;
        this.variance = variance;
    }

    public double getMean() {
        return mean;
    }

    public double getVariance() {
        return variance;
    }

    public double draw(){
        return correspondingDistribution.sample();
    }
}
