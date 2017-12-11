package IRPact_modellierung.decision;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.products.ProductAttribute;
import org.apache.logging.log4j.LogManager;

//TODO write better description

/**
 * The linear utility function is a function that attributes the perception of a product attribute to the actor.
 * It is linear in the perception of this attribute.
 *
 * @author Simon Johanning
 */
public class LinearUtilityFunction extends UtilityFunction {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    /**
     * The partial utility of the respective product attribute is the perception of the product attribute by the respective agent at system time.
     *
     * @param correspondingAgent The agent that evaluates the respective product attribute
     * @param correspondingProductAttribute The product attribute being evaluated
     * @param systemTime The time of the simulation at which the product attribute is evaluated.

     * @return The partial utility of the product attribute perception of the corresponding agent at system time
     */
    public double calculatePartialUtility(ConsumerAgent correspondingAgent, ProductAttribute correspondingProductAttribute, double systemTime) {
        fooLog.debug("In linear utility function, calculating "+correspondingAgent.getPerceivedProductAttributeValues().get(correspondingProductAttribute).calculateProductAttributePerception(systemTime));
        return correspondingAgent.getPerceivedProductAttributeValues().get(correspondingProductAttribute).calculateProductAttributePerception(systemTime);
    }
}
