package IRPact_modellierung.perception;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.products.ProductGroupAttribute;
import IRPact_modellierung.simulation.Configuration;

/**
 * A PerceptionInitializationScheme that initializes all perceptions based on it with
 * a constant value assigned to it at the beginning of the simulation.
 *
 * @author Simon Johanning
 */
public class ConstantPerceptionInitializationScheme extends PerceptionInitializationScheme{

    private double initializationValue;

    public ConstantPerceptionInitializationScheme(double initializationValue) {
        this.initializationValue = initializationValue;
    }

    /**
     * The initial value for all perceptions based on this scheme are the ones given in the constructor.
     *
     * @param correspondingProductAttribute the ProductAttribute associated with the perception
     * @param associatedAgentGroup the ConsumerAgentGroup of the agents that perceive the product
     * @param configuration the Configuration of the simulation
     * @return The value the constructor is initialized with.
     */
    public double determineInitialValue(ProductAttribute correspondingProductAttribute, ConsumerAgentGroup associatedAgentGroup, Configuration configuration) {
        return initializationValue;
    }
}
