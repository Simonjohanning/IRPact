package IRPact_modellierung.perception;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.Configuration;

/**
 * A HistogramInitializationScheme that assumes the initial value of a histogram to be based on the distribution of the ProductGroupAttribute of the corresponding product group attribute.
 * From this distribution, a value is drawn for the initialization of a histogram.
 */
public class RandomPADistributionHistogramInitializationScheme extends HistogramInitializationScheme {

    /**
     * Initial value is a value drawn from the distribution associated with the product group attribute
     *
     * @param correspondingProductAttribute the ProductAttribute associated with the histogram
     * @param associatedAgentGroup the ConsumerAgentGroup associated with the histogram
     * @param configuration the Configuration of the simulation
     * @return a numerical value drawn from the distribution associated with the product group attribute
     */
    public double determineInitialValue(ProductAttribute correspondingProductAttribute, ConsumerAgentGroup associatedAgentGroup, Configuration configuration) {
        return correspondingProductAttribute.getCorrespondingProductGroupAttribute().getValue().draw();
    }
}
