package IRPact_modellierung.perception;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.Configuration;

//TODO check if its needed and remove if necessary
public class ProductAttributeValueInitializationScheme extends PerceptionInitializationScheme{

    public double determineInitialValue(ProductAttribute correspondingProductAttribute, ConsumerAgentGroup associatedAgentGroup, Configuration configuration) {
        return correspondingProductAttribute.getValue();
    }
}
