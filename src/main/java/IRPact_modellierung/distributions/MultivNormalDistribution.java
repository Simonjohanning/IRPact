package IRPact_modellierung.distributions;

import java.util.ArrayList;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

/**
 * Class to model a multivariate normal distribution using the multivariate normal distribution in the COLT library.
 * Normal distribution is based on an array of means and a covariance matrix in the specified number of dimensions.
 * Dimensionality errors are reported from the constructor as IllegalArgumentExceptions
 *
 * @author Simon Johanning
 */
public class MultivNormalDistribution extends MultivariateDistribution {

    private MultivariateNormalDistribution correspondingDistribution;
    private double[] means;
    private double[][] covariances;

    //TODO check parameters??

    /**
     * Creates an object representing a multivariate normal distribution based on the means and covariances provided
     *
     * @param name name of the distribution to create (will be prefixed by MultivariateNormalDistribution_)
     * @param means double array describing the vector of the means of the normal distributions
     * @param covariances 2D double matrix describing the covariances of between the dimensions
     * @throws IllegalArgumentException will be thrown when the dimensionality of the means or covariance dont match
     */
    public MultivNormalDistribution(String name, double[] means, double[][] covariances) throws IllegalArgumentException{
        super(name, means.length);
        int meansDim = means.length;
        if(covariances.length != meansDim) throw new IllegalArgumentException("dimensions of means and covariances doen't coincide!! dim(means)="+meansDim+", dim(covj)="+covariances.length);
        for(int i=0;i<meansDim;i++){
            if(covariances[i].length != meansDim) throw new IllegalArgumentException("dimensions of means and covariances doen't coincide!! dim(means)="+meansDim+", dim(cov,"+i+")="+covariances[i].length);
        }
        this.means = means;
        this.covariances = covariances;
        correspondingDistribution = new org.apache.commons.math3.distribution.MultivariateNormalDistribution(means, covariances);
    }

    //TODO make nicer
    public ArrayList<Double> draw() {
        double[] sample = correspondingDistribution.sample();
        ArrayList<Double> sampleList = new ArrayList<Double>(dimension);
        for (double aSample : sample) {
            sampleList.add(aSample);
        }
        return sampleList;
    }

    public MultivariateNormalDistribution getCorrespondingDistribution() {
        return correspondingDistribution;
    }

    public double[] getMeans() {
        return means;
    }

    public double[][] getCovariances() {
        return covariances;
    }
}
