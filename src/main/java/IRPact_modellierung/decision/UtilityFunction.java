package IRPact_modellierung.decision;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.products.ProductAttribute;

/**
 * An abstraction of a utility function to calculate the (partial) utility of a product attribute for a consumer agent at a given time.
 * Is used in utilitarian decision processes in order to allow more flexible modeling
 *
 * @author Simon Johanning
 */
public abstract class UtilityFunction {

    /**
     * Method to calculate the partial utility of correspondingProductAttribute for correspondingAgent at systemTime
     *
     * @param correspondingAgent The ConsumerAgent for which the utility is calculated
     * @param correspondingProductAttribute The ProductAttribute which utility is calculated
     * @param systemTime The system time of the calculation of the product attribute
     * @return the partial utility of correspondingProductAttribute for correspondingAgent at systemTime
     */
    public abstract double calculatePartialUtility(ConsumerAgent correspondingAgent, ProductAttribute correspondingProductAttribute, double systemTime);
}
