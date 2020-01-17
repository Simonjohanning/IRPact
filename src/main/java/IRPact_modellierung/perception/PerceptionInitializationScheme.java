package IRPact_modellierung.perception;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.Configuration;

/**
 * A PerceptionInitializationScheme describes how the (initial) perception of a ProductAttribute is derived.
 * As part of a PerceptionSchemeConfiguration, it describes the initial parameterization of the respetive
 * ProductAttributePerceptionSchemes (e.g. formalizing the perceived product group attribute value distribution).
 *
 * @author Simon Johanning
 */
public abstract class PerceptionInitializationScheme {

    /**
     * Method describing how the initial value of the perception is determined
     *
     * @param correspondingProductAttribute the ProductAttribute the perception relates to
     * @param associatedAgentGroup the ConsumerAgentGroup of the agents that perceive the product
     * @param configuration the Configuration of the simulation
     * @return a numerical value the perception is initialized with
     */
    public abstract double determineInitialValue(ProductAttribute correspondingProductAttribute, ConsumerAgentGroup associatedAgentGroup, Configuration configuration);
}
