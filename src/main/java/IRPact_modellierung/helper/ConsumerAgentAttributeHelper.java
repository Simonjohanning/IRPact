package IRPact_modellierung.helper;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgentAttribute;
import org.apache.logging.log4j.LogManager;

//TODO think about situating this stuff with the consumer agent and write a selector for agent attributes

/**
 * Class to implement a number of helper method related to attributes of ConsumerAgents.
 * Since consumer agent attributes come in a variety of forms, this class is supposed to bundle a number
 * of helper methods for different kinds of models.
 *
 * @author Simon Johanning
 */
public class ConsumerAgentAttributeHelper {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    /**
     * Method to load the subjective norm of an agent from its attributes
     *
     * @param consumerAgent The agent whose subjective norm is requested
     * @return The value of the subjective norm of the respective agent
     * @throws IllegalArgumentException Will be thrown when the agent doesn't dispose of an agent attribute for the subjective norm
     */
    public static double loadSubjectiveNorm(ConsumerAgent consumerAgent) throws IllegalArgumentException{
        //Placeholder to refer to the consumer agent attribute that represents the subjective norm of the agent
        ConsumerAgentAttribute correspondingAttribute = null;
        for(ConsumerAgentAttribute currentAttribute : consumerAgent.getAttributes()){
            if(currentAttribute.getAttributeName().equals("subjectiveNorm")){
                correspondingAttribute = currentAttribute;
                break;
            }
        }
        if(correspondingAttribute != null) return correspondingAttribute.getAttributeValue();
        else throw new IllegalArgumentException("Subjective norm  is not set for agent "+consumerAgent);
    }
}
