package IRPact_modellierung.distributions;

import cern.jet.random.NegativeBinomial;

/**
 * Class to represent a negative binomial distribution based on the COLT library.
 *
 * @author Simon Johanning
 */
public class NegativeBinomialDistribution extends COLTDiscreteDistribution {

	/**
	 * number of trials
	 */
	private int n;

	/**
	 * probability of success
	 */
	private double p;

	private NegativeBinomial coltDistribution;

	/**
	 * Creates a negative binomial distribution wrapping the corresponding COLT distribution.
	 * Will throw an error with invalid parameters (n < 1 or p not in unit interval)
	 *
	 * @param name Name of the distribution (will be prefixed with NegativeBinomialDistribution_)
	 * @param n number trials modeled
	 * @param p success probability of each trial
	 *
	 * @throws IllegalArgumentException will be thrown when one of the parameter is out of range (n < 1 or p not in unit interval)
	 */
	public NegativeBinomialDistribution(String name, int n, double p) throws IllegalArgumentException{
		super(name);
		if((p <= 0.0) || (p >= 1.0)) throw new IllegalArgumentException("sucess probability for each experiment is invalid (needs to be within the open unit interval (between 0 and 1), is "+p);
		else if(n <= 0) throw new IllegalArgumentException("number of tries is invalid (needs to be positive, is "+n);
		else{
			this.n = n;
			this.p = p;
			coltDistribution = new NegativeBinomial(n,p,generator);
		}
	}

	public double draw(){
		return (double)  coltDistribution.nextInt();
	}

	public void setN(int n){
		if(n <= 0) throw new IllegalArgumentException("number of tries is invalid (needs to be positive, is "+n);
		else{
			this.n=n;
			coltDistribution = new NegativeBinomial(n,p,new cern.jet.random.engine.MersenneTwister(new java.util.Date()));
		}
	}

	public void setP(double p){
		if((p <= 0.0) || (p >= 1.0)) throw new IllegalArgumentException("sucess probability for each experiment is invalid (needs to be within the open unit interval (between 0 and 1), is "+p);
		else{
			this.p=p;
			coltDistribution = new NegativeBinomial(n,p,generator);
		}
	}

	public int getN() {
		return this.n;
	}

	public double getP() {
		return this.p;
	}


}