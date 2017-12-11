package IRPact_modellierung.distributions;

import cern.jet.random.Binomial;
import cern.jet.random.engine.RandomEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * Class that models a binomial distribution
 * using the cern.jet.random library for binomial distributions.
 * Describes the distribution to derive an integer number of successes from a
 * number of independent trials (Bernoulli experiments) with a success probability p.
 *
 */
public class BinomialDistribution extends DiscreteDistribution {

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

	/**
	 * Generates the distribution for a fixed number of trials n with a success probability p.
	 *
	 * @param name Name of the distribution to model (will be prefixed by Binomial_Distribution)
	 * @param n The number of tries / independent trials of Bernoulli experiments
	 * @param p probability of success for each Bernoulli experiment
	 * @throws IllegalArgumentException Will be thrown when parameters are illegal (n negative or p not in unit interval ([0,1]))
	 */
	public BinomialDistribution(String name, int n, double p) throws IllegalArgumentException{
		super(name);
		if(n<0) throw new IllegalArgumentException("The number of (independent) trials can not be negative!!");
		else if((p<0.0) || (p>1.0)) throw new IllegalArgumentException("Value for p must be within 0.0 and 1.0, is"+p+".\n Value will not be reset and "+p+" will be retained!");
		else{
			RandomEngine generator = new cern.jet.random.engine.MersenneTwister(new java.util.Date());
			coltDistribution = new Binomial(n,p,generator);
			this.n = n;
			this.p = p;
		}
	}

	/**
	 * Draws a value from the distribution randomly
	 * through returning the value for which the cumulative distribution
	 * of this Binomial distribution corresponds to a value drawn from a normal distribution
	 *
	 * @return integer value in [0:n] drawn with the probability of the binomial distribution
	 */
	public double draw(){
		double uniformRandomDraw = Math.random();
		//see which k corresponds to a uniformly drawn value in the cumulative distribution
		for(int k=0;k<n;k++) {
			if ((double) coltDistribution.cdf(k) >= uniformRandomDraw) {
				return (double) k;
			}
		}
		return (double) n;
	}

	//TODO think about reactivating or deleting once a decision is taken on whether distributions should be manipulatable
/*	*//**
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

	*//**
	 * Resets the probability of success for individual Bernoulli experiments of the distribution
	 *
	 * @param p Probability of success of each individual Bernoulli experiment
	 * @throws IllegalArgumentException gets thrown when an illegal value is provided for p (outside of unit interval [0,1])
	 *//*
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