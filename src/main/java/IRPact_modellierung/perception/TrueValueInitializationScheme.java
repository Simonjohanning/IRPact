package IRPact_modellierung.perception;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.Configuration;

/**
 * A HistogramInitializationScheme that initializes the histogram with the true value of the product attribute as specified in the ProductConfiguration
 *
 * @author Simon Johanning
 */
public class TrueValueInitializationScheme extends HistogramInitializationScheme {

    /**
     * Determines the initial value of the histogram as the value of the correspondingProductAttribute
     *
     * @param correspondingProductAttribute the ProductAttribute associated with the histogram
     * @param associatedAgentGroup the ConsumerAgentGroup associated with the histogram
     * @param configuration the Configuration of the simulation
     * @return
     */
    public double determineInitialValue(ProductAttribute correspondingProductAttribute, ConsumerAgentGroup associatedAgentGroup, Configuration configuration) {
        return correspondingProductAttribute.getValue();
    }
}
