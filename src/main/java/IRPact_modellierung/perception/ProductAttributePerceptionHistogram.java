package IRPact_modellierung.perception;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * A histogram describing how product attributes are perceived by actors (generally of type ConsumerAgent).
 * This class brings together the concepts of product attribute perception and of histogram.
 * While product attribute perception is the model mechanism this class intends to capture, the histogram is the data structure it is based upon.
 * Perceptions of the corresponding ProductAttribute are stored in bins corresponding to the values in a given range (based on the histograms mininum and maximum value, as well as the number of histogram bins)
 *
 * @author Simon Johanning
 */
public class ProductAttributePerceptionHistogram extends Histogram implements ProductAttributePerceptionScheme{

	private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	private Map<Integer, HistogramBin> binValues;
	private ProductAttribute associatedProductAttribute;

	/**
	 * A ProductAttributePerceptionHistogram models the perception of a product attribute with the use of a histogram (as data structure).
	 * It is assumed that the histogram is initialized at simulation time 0.0
	 *
	 * @param lambda The lambda parameter associated with the histogram
	 * @param noBins The number of (equidistant) bins in the histogram
	 * @param minValue The lower bound of values in the histogram
	 * @param maxValue The upper bound of values in the histogram
	 * @param associatedProductAttribute The ProductAttribute associated with this histogram
	 * @param histogramInitializationScheme The HistogramInitializationScheme used to provide the initial value of the histogram
	 * @param associatedAgentGroup The ConsumerAgentGroup of the ConsumerAgent using this histogram
	 * @param configuration The configuration of the simulation
	 */
	public ProductAttributePerceptionHistogram(double lambda, int noBins, double minValue, double maxValue, ProductAttribute associatedProductAttribute, HistogramInitializationScheme histogramInitializationScheme, ConsumerAgentGroup associatedAgentGroup, Configuration configuration) {
		//create the associated histogram
		super(lambda, noBins, minValue, maxValue);
		this.associatedProductAttribute = associatedProductAttribute;
		binValues = new HashMap<Integer, HistogramBin>(noBins);
		for(int binIndex=0; binIndex<noBins;binIndex++){
			fooLog.debug("Creating bin {} in range [{},{}] with papHistogram min {}, max {}", binIndex,this.minValue+(binIndex*((double) ((this.maxValue-this.minValue) / noBins))), this.minValue+((binIndex+1)*((double) ((this.maxValue-this.minValue) / noBins))), this.minValue, this.maxValue);
			HistogramBin currentBin = new HistogramBin(this.minValue+(binIndex*((this.maxValue-this.minValue) / noBins)), this.minValue+((binIndex+1)*((this.maxValue-this.minValue) / noBins)), this) ;
			binValues.put(binIndex, currentBin);
		}
		//initialize the histogram with the initial value
		modifyValue(histogramInitializationScheme.determineInitialValue(associatedProductAttribute, associatedAgentGroup, configuration), 1.0, 0.0);
	}

	/*public ProductAttributePerceptionHistogram(double lambda, int noBins, double minValue, double maxValue, ProductAttribute associatedProductAttribute, double initialValue, HistogramInitializationScheme histogramInitializationScheme) {
		super(lambda, noBins, minValue, maxValue);
		this.associatedProductAttribute = associatedProductAttribute;
		binValues = new HashMap<Integer, HistogramBin>(noBins);
		for(int binIndex=1; binIndex<=noBins;binIndex++){
			fooLog.debug("Creating bin {} in range [{},{}] with papHistogram min {}, max {}", binIndex,this.minValue+(binIndex*((double) ((this.maxValue-this.minValue) / noBins))), this.minValue+((binIndex+1)*((double) ((this.maxValue-this.minValue) / noBins))), this.minValue, this.maxValue);
			HistogramBin currentBin = new HistogramBin(this.minValue+(binIndex*((this.maxValue-this.minValue) / noBins)), this.minValue+((binIndex+1)*((this.maxValue-this.minValue) / noBins)), this) ;
			binValues.put(binIndex, currentBin);
		}
		fooLog.debug("Adding initial value {} to papHistogram for PA {}",initialValue, associatedProductAttribute.getCorrespondingProductGroupAttribute().getName());
		addValue(initialValue, 1.0, 0.0);
	}*/

	public Map<Integer, HistogramBin> getBinValues() {
		return this.binValues;
	}

	public ProductAttribute getAssociatedProductAttribute() {
		return this.associatedProductAttribute;
	}

	/**
	 * method to modify the value of the histogram in adding the productAttributePerceptionValue to the histogram in the corresponding bin with weight weight at simulation time timeStamp
	 * 
	 * @param productAttributePerceptionValue The numerical value associated with the perception of the product attribute
	 * @param weight The weight associated with the product perception
	 * @param timeStamp The simulation time the productAttributePerceptionValue is added
	 */
	public void modifyValue(double productAttributePerceptionValue, double weight, double timeStamp) {
		boolean validBinFound = false;
		fooLog.debug("In addValue with papValue {} and {} binValues", productAttributePerceptionValue, binValues.keySet().size());
		//find the bin corresponding to the productAttributePerceptionValue
		for(Integer index : binValues.keySet()){
			fooLog.debug("for bin {}: bin min value is {}, bin max value is {}, value to add is {} ",index,  binValues.get(index).getBinMinValue(), binValues.get(index).getBinMaxValue(), productAttributePerceptionValue);
			if((productAttributePerceptionValue >= binValues.get(index).getBinMinValue()) && (productAttributePerceptionValue <= binValues.get(index).getBinMaxValue())){
				fooLog.debug("Adding value ({}) with weight {} to bin {}", productAttributePerceptionValue, weight, index);
				binValues.get(index).addValueToBin(weight, timeStamp);
				validBinFound = true;
				break;
			}
		}
		if(!validBinFound) fooLog.error("ERROR!!! perceived value {} does not fall into a bin!!! The value is outside of the allowed range of [{},{}]", productAttributePerceptionValue, minValue, maxValue);
	}

	/**
	 * Method to calculate the perception of a product attribute at simulation time system time.
	 * Values associated with each bin are the median value of the bin weighted (multiplicatively) with the height of the bin.
	 * Method sums over all values associated with the bins and normalizes with the total height of all bins..
	 * Thus a normalized weighted (by bin height) average for the histogram is calculated as product attribute perception.
	 *
	 * @param systemTime The time the value is calculated at
	 */
	public double calculateProductAttributePerception(double systemTime) {
		double binSum = 0.0;
		double totalHeight = 0.0;
		fooLog.debug("calculateProductAttributePerception for {} binValues with min: {} and max: {}",getBinValues().keySet().size(), minValue, maxValue);
		for(Integer binIndex : binValues.keySet()){
			//binSum += binValues.get(binIndex).getBinHeight()*(minValue+((double) binIndex - 0.5)*((maxValue-minValue)/noBins));
			binSum += binValues.get(binIndex).getBinHeight()*(binValues.get(binIndex).getBinMinValue()+(binValues.get(binIndex).getBinMaxValue()-binValues.get(binIndex).getBinMinValue())/2);
			fooLog.debug("Adding {} to the binSum to amount it to {} for bin {}",binValues.get(binIndex).getBinHeight()*(minValue+((double) binIndex - 0.5)*((maxValue-minValue)/noBins)), binSum, binIndex);
			totalHeight += binValues.get(binIndex).getBinHeight();
		}
		fooLog.debug("Calculating product perception with binsum {} and totalheight {}", binSum, totalHeight);
		if(totalHeight == 0.0) fooLog.error("ERROR!!! totalHeight is 0 and thus product perception will be NaN");
		return binSum/totalHeight;
	}

}