package IRPact_modellierung.perception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class to describe the bins in a histogram as a discretization of the range / height of values in the histogram.
 * The histogram bins associate the lambda parameter of the corresponding histogram to the strength of exponential decay (over time) of new data.
 *
 * @author Simon Johanning
 */
public class HistogramBin {

	private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	private double binHeight;
	private double binMinValue;
	private double binMaxValue;
	private ProductAttributePerceptionHistogram correspondingHistogram;

	/**
	 *
	 * @param binMinValue minimum value corresponding to the bin
	 * @param binMaxValue maximum value corresponding to the bin
	 * @param correspondingHistogram the histogram this bin is associated with
	 */
	public HistogramBin(double binMinValue, double binMaxValue, ProductAttributePerceptionHistogram correspondingHistogram) {
		binHeight = 0.0;
		this.binMaxValue = binMaxValue;
		this.binMinValue = binMinValue;
		this.correspondingHistogram = correspondingHistogram;
	}

	public double getBinHeight() {
		return binHeight;
	}

	public double getBinMinValue() {
		return binMinValue;
	}

	public double getBinMaxValue() {
		return binMaxValue;
	}

	/**
	 * adds a value to the bin based on the weight of the value, simulation time and lambda parameter of the corresponding histogram.
	 * New information is incorporated on an exponentially increasing / decreasing fashion (lambda > 0 / lambda <0) with the weight scaling multiplically
	 *
	 * @param weight The weight of the value to be added (multiplicable scaling)
	 * @param timestamp The simulation time the value is incorporated in the histogram (scales exponentially with lambda of the corresponding histogram)
	 */
	public void addValueToBin(double weight, double timestamp){
		fooLog.debug("Adding {} to current bin", weight*Math.exp(timestamp*correspondingHistogram.getLambda()));
		binHeight += weight*Math.exp(timestamp*correspondingHistogram.getLambda());
	}

}