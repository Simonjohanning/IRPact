package IRPact_modellierung.perception;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.distributions.BoundedUnivariateDistribution;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.products.ProductGroupAttribute;
import IRPact_modellierung.simulation.Configuration;

/**
 * The ConsumerPerceivedValueHistogramInitializationScheme is a HistogramInitializationScheme that
 * assigns a value drawn from the perceived product attribute value distribution of the ConsumerAgentGroup
 * to the histogram.
 *
 * @author Simon Johanning
 */
public class ConsumerPerceivedValueHistogramInitializationScheme extends HistogramInitializationScheme{

    private BoundedUnivariateDistribution perceivedProductAttributeValueDistribution;

    public ConsumerPerceivedValueHistogramInitializationScheme(BoundedUnivariateDistribution perceivedProductAttributeValueDistribution) {
        this.perceivedProductAttributeValueDistribution = perceivedProductAttributeValueDistribution;
    }


    /**
     * Since the value is derived from the perceived product attribute value distribution, the initial value will be drawn from this distribution
     *
     * @param correspondingProductAttribute The product attribute the histogram is set for
     * @param associatedAgentGroup The ConsumerAgentGroup the agent for which the histogram is set is part of
     * @param configuration The configuration object for the simulation
     * @return A double value signifying the initial value of the histogram
     */
    public double determineInitialValue(ProductAttribute correspondingProductAttribute, ConsumerAgentGroup associatedAgentGroup, Configuration configuration) {
        return perceivedProductAttributeValueDistribution.draw();
    }
}
