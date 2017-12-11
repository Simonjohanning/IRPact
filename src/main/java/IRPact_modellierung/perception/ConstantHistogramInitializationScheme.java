package IRPact_modellierung.perception;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.products.ProductGroupAttribute;
import IRPact_modellierung.simulation.Configuration;

/**
 * Histogram initialization scheme that assigns a single (constant) value to any histogram
 *
 * @author Simon Johanning
 */
public class ConstantHistogramInitializationScheme extends HistogramInitializationScheme {

    //value to assign to each histogram
    private double valueToAssign;

    public ConstantHistogramInitializationScheme(double valueToAssign) {
        this.valueToAssign = valueToAssign;
    }

    /**
     * Since every histogram is assigned the same (constant) value, the initial value
     * (given to the constructor) will be the value associated with this initialization scheme.
     *
     * @param correspondingProductAttribute The product (group) attribute the histogram is set for
     * @param associatedAgentGroup The ConsumerAgentGroup the agent for which the histogram is set is part of
     * @param configuration The configuration object for the simulation
     * @return A double value signifying the initial value of the histogram
     */
    public double determineInitialValue(ProductAttribute correspondingProductAttribute, ConsumerAgentGroup associatedAgentGroup, Configuration configuration) {
        return valueToAssign;
    }
}
