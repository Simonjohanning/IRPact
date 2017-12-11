package IRPact_modellierung.perception;

import org.apache.logging.log4j.LogManager;

/**
 * Building on Kiesling, the model incorporates the possibility to use a histogram for the structuring the perception of actors in bins.
 Since the bins are equidistant (equal width), the histogram is bounded by its nature between a /minValue/ and a /maxValue/.
 In this form it also has a parameter \lambda associated with it (which is generally used to model the exponential decay of the value of information.
 However, this Historam class is of a more general nature and can be used in other contexts as well
 @author Simon Johanning
 */
public abstract class Histogram {

	private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	protected double lambda;
	protected int noBins;
	protected double minValue;
	protected double maxValue;

	/**
	 * Constructs an equidistant histogram of noBins bins for values in [minValue, maxValue] based on the histogramInitializationScheme
	 *
	 * @param lambda parameter associated with the Histogram; generally used (but not limited to) model temporal decay of importance of information
	 * @param noBins the number of histogram bins in the Histogram
	 * @param minValue the lower bound of the histogram
	 * @param maxValue the upper bound of the histogram
	 * @throws IllegalArgumentException Will be thrown when the minValue is larger or equal than the maxValue
	 */
	public Histogram(double lambda, int noBins, double minValue, double maxValue) throws IllegalArgumentException{
		this.lambda = lambda;
		this.noBins = noBins;
		if(minValue < maxValue){
			fooLog.debug("minValue is "+minValue+", maxValue is "+maxValue);
			this.minValue = minValue;
			this.maxValue = maxValue;
		} else throw new IllegalArgumentException("minValue >= maxValue, which doesn't make any sense.\nPlease make sure to provide valid parameters!!");
	}

	public int getNoBins() {
		return noBins;
	}

	public double getLambda() {
		return this.lambda;
	}

}