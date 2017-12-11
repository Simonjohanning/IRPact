package IRPact_modellierung.perception;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.Configuration;

/**
 * The UniformRandomBoundedHistogramInitializationScheme will initialize the histogram with a (random, uniformly distributed) possible value valid for the corresponding histogram
 *
 * @author Simon Johanning
 */
public class UniformRandomBoundedHistogramInitializationScheme extends HistogramInitializationScheme {

    /**
     * Initial value of the Histogram will be any valid value for the Histogram (within the histograms range).
     * The value is drawn uniformly from this range.
     *
     * @param correspondingProductAttribute the ProductAttribute associated with the histogram
     * @param associatedAgentGroup the ConsumerAgentGroup associated with the histogram
     * @param configuration the Configuration of the simulation
     * @return a uniformly drawn value within [histogramLowerBound, histogramUpperBound]
     */
    public double determineInitialValue(ProductAttribute correspondingProductAttribute, ConsumerAgentGroup associatedAgentGroup, Configuration configuration) {
        return (Math.random()*(correspondingProductAttribute.getCorrespondingProductGroupAttribute().getValue().getUpperBound()-correspondingProductAttribute.getCorrespondingProductGroupAttribute().getValue().getLowerBound()) + correspondingProductAttribute.getCorrespondingProductGroupAttribute().getValue().getLowerBound());
    }
}
