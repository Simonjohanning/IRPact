package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

//TODO think about whether parameters should be mutable

/**
 * Class to represent an Erlang distribution based on the COLT distribution library.
 * Because of this, the parameters are modeled as double values
 *
 */
public class ErlangDistribution extends COLTContinuousDistribution {

	/**
	 * Parameters of the Erlang distribution with notation used in
	 * (https://en.wikipedia.org/wiki/Erlang_distribution).
	 * Will be translated to the variance and mean used by the COLT library
	 * according to wikipedia as well
	 */
	private int k;
	private double lambda;

	/**
	 * Erlang implementation of the COLT library requires the variance and mean,
	 * which are calculated from the set parameters k and n
	 */
	private double variance;
	private double mean;

	/**
	 * Generate an Erlang distribution from the parameters k (shape) and lambda (rate),
	 * which is used to draw values based on the mean and variance of the distribution as implemented in the COLT library
	 *
	 * @param name The name of the distribution used (will be prefixed by ErlangDistribution_)
	 * @param k The shape parameter of the Erlang distribution
	 * @param lambda The rate parameter of the Erlang distribution
	 *
	 * @throws IllegalArgumentException will be thrown when either parameter is out of range (<= 0)
	 */
	public ErlangDistribution(String name, int k, double lambda) {
		super(name);
		if(k <= 0) throw new IllegalArgumentException("Parameter k has to be a natural number, is "+k);
		else if(lambda <= 0) throw new IllegalArgumentException("Parameter lambda has to be a positive number, is "+lambda);
		else{
			this.k=k;
			this.lambda=lambda;
			recalculateCOLTParameters();
		}
	}

	public int k() {
		return this.k;
	}

	public double lambda() {
		return this.lambda;
	}

	/**
	 * Resets the shape parameter k of the distribution.
	 * Will also recalculate the parameters the COLT distribution used is based upon.
	 *
	 * @param k shape parameter to replace current one
	 * @throws IllegalArgumentException Will be thrown when k <= 0
	 */
	public void setK(int k) throws IllegalArgumentException{
		if(k <= 0) throw new IllegalArgumentException("Parameter k has to be a natural number, is "+k);
		this.k=k;
		recalculateCOLTParameters();
	}

	/**
	 * Resets the rate parameter lambda of the distribution.
	 * Will also recalculate the parameters the COLT distribution used is based upon.
	 *
	 * @param lambda rate parameter to replace current one
	 * @throws IllegalArgumentException Will be thrown when lambda <= 0
	 */
	public void setLambda(double lambda) throws IllegalArgumentException{
		if(lambda <= 0) throw new IllegalArgumentException("Parameter lambda has to be a positive number, is "+lambda);
		this.lambda = lambda;
		recalculateCOLTParameters();
	}

	private void recalculateCOLTParameters() {
		mean = calculateMean(k,lambda);
		variance = calculateVariance(k,lambda);
	}

	/**
	 * Value is drawn using the nextErlang-function of the COLT distributions
	 *
	 * @return value according to the Erlang distribution using the COLT library
	 */
	public double drawValue(){
		return (double) Distributions.nextErlang(variance, mean, generator);
	}

	/**
	 * Calculates the mean of the Erlang distribution with the respective parameters k and lambda.
	 * Will not do checking of value (assume parameters are valid values).
	 *
	 * @param k shape parameter of Erlang distribution
	 * @param lambda rate parameter of Erlang distribution
	 * @return Mean value of Erlang distribution
	 */
	private double calculateMean(int k, double lambda){
		return k/lambda;
	}

	/**
	 * Calculates the variance of the Erlang distribution with the respective parameters k and lambda.
	 * Will not do checking of value (assume parameters are valid values).
	 *
	 * @param k shape parameter of Erlang distribution
	 * @param lambda rate parameter of Erlang distribution
	 * @return Variance of Erlang distribution
	 */
	private double calculateVariance(int k, double lambda){
		return k/Math.pow(lambda,2);
	}

	public double draw() {
		return drawValue();
	}
}