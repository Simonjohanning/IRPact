package IRPact_modellierung.distributions;

import cern.jet.random.Poisson;

//TODO improve class description

/**
 * Class to model a poisson distribution using the COLT distribution library
 * with an average number of events k.
 * Will find the number of events that correspond to the cumulative distribution
 * with respect to a uniformly distributed variable
 *
 * @author Simon Johanning
 */
public class PoissonDistribution extends COLTDiscreteDistribution {

	/**
	 * mean number of events the distribution is centered on
	 */
	private double lambda;

	//corresponding colt distribution
	private Poisson coltDistribution;

	/**
	 * Models a Poisson distribution with an average number of lambda events.
	 *
	 * @param name Name of the distribution (will be prefixed with PoissonDistribution_)
	 * @param lambda Average number of each draw.
	 * @throws IllegalArgumentException will be thrown if the average number of events (lambda) is negative
	 */
	public PoissonDistribution(String name, double lambda) throws IllegalArgumentException{
		super(name);
		if(lambda <= 0.0) throw new IllegalArgumentException("Lambda (mean) of the Poisson distribution needs to be (strictly) positive; was set to "+lambda+".\n The corresponding library would return 0.0 for every draw; If this is desired, consider implementing a distribution for this");
		this.lambda = lambda;
		coltDistribution = new Poisson(lambda,generator);
	}

	public double draw() {
		int k=0;
		/*value to compare the cumulative distribution against.
		if it equals or exceeds the cumulative distribution of k events,
		k is the number searched for*/
		double referenceValue = Math.random();
		while(coltDistribution.cdf(k) < referenceValue) k++;
		return (double) k;
	}

	public double getLambda() {
		return this.lambda;
	}

	//TODO remove test method

	/**
	 * Method to test whether my assumption of what the COLT implementation does.
	 * Should return something close to lambda (converging with number of draws)
	 *
	 * @param numberOfDraws number of samples taken
	 * @return the average number of events with numberOfDraws draws
	 */
	public double testDistribution(int numberOfDraws){
		double cumulatedKs = 0.0;
		for(int i=0;i<numberOfDraws;i++){
			cumulatedKs += draw();
		}
		return cumulatedKs/numberOfDraws;
	}
}