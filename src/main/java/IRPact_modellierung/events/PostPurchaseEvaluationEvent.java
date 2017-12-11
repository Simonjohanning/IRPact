package IRPact_modellierung.events;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;

/**
 * A PostPurchaseEvaluationEvent is an event that represents the evaluation of an adopted product (after its purchased).
 * It models the observation that the qualities of an adopted product
 * (i.e. a product that the adopter uses and has unlimited access to),
 * can be assessed rather well by the consumer agent that adopted it (depending on its product attributes observabbility).
 * Upon processing, this event will add a product perception to the adopters perception model
 * with the true value of the attribute as a value and the observability of it as a weight.
 *
 * @author Simon Johanning
 */
public class PostPurchaseEvaluationEvent extends Event {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    private Product productConcerned;
    private ConsumerAgent agentConcered;

    /**
     * Event that represents the evaluation of a product based on its true values.
     * Will add a perception of the true value of each of its product attribute values
     * to the perception model of the respective consumer agent, based on that product attributes observability
     *
     * @param simulationContainer The container the simulation takes place in
     * @param productConcerned The product whose qualities are to be evaluated
     * @param agentConcerned The agent evaluating the product
     * @param scheduledForTime The time for which the evaluation is scheduled
     *
     * @throws IllegalArgumentException
     */
    public PostPurchaseEvaluationEvent(SimulationContainer simulationContainer, Product productConcerned, ConsumerAgent agentConcerned, double scheduledForTime) throws IllegalArgumentException {
        super(simulationContainer, scheduledForTime);
        if(!LazynessHelper.anyAdoptedProductRefersToProduct(agentConcerned.getAdoptedProducts(), productConcerned)) throw new IllegalArgumentException("Product to be evaluated "+productConcerned+" is not adopted by the agent to be evaluating it ("+agentConcerned+")!!.\nPPE is only possible when the product is actually adopted! ");
        else if(productConcerned == null) throw new IllegalArgumentException("Error!!! A non-existing product is asking to be evaluated! This should never happen!!");
        this.productConcerned = productConcerned;
        fooLog.debug("PPE event constructor: product concerned is "+productConcerned);
        this.agentConcered = agentConcerned;
    }

    /**
     * Processing a PPE event will add the true value of each product attribute
     * to the perception of the agent referred to with a weight of that attributes observability.
     *
     * @param systemTime The current time of the system for execution
     */
    public void processEvent(double systemTime) {
        //only add the perception if the agent still has he product adopted
        if(LazynessHelper.anyAdoptedProductRefersToProduct(agentConcered.getAdoptedProducts(), productConcerned)) {
            fooLog.debug("product {} has {} attributes", productConcerned.getName(), productConcerned.getProductAttributes().size());
            for (ProductAttribute currentAttribute : productConcerned.getProductAttributes()) {
                fooLog.debug("product concerned in ppe-event is {} with perceived attribute value of {} for attribute {} (before)", productConcerned, agentConcered.getPerceivedProductAttributeValues().get(currentAttribute).calculateProductAttributePerception(systemTime), currentAttribute.getCorrespondingProductGroupAttribute().getName());
                agentConcered.addPerceivedProductAttributeValue(currentAttribute, currentAttribute.getValue(), systemTime, currentAttribute.getCorrespondingProductGroupAttribute().getObservability());
                fooLog.debug("product concerned in ppe-event is {} with perceived attribute value of {} for attribute {} (after)", productConcerned, agentConcered.getPerceivedProductAttributeValues().get(currentAttribute).calculateProductAttributePerception(systemTime), currentAttribute.getCorrespondingProductGroupAttribute().getName());
            }
        }
    }
}
