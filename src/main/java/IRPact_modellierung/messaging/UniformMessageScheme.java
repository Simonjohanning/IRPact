package IRPact_modellierung.messaging;

import IRPact_modellierung.agents.Agent;
import IRPact_modellierung.agents.companyAgents.CompanyAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.HashSet;
import java.util.Set;

/**
 * The UniformMessageScheme is the most basic CompanyAgentMessageScheme implemented,
 * since it doesn't take any parameters.
 * For CompanyAgents it will create a set of messages with one CompanyConsumerPerceptionManipulationMessage
 * for each consumerAgent in the simulationContainer and each ProductAttributes of the products
 * managed by the sender, with the true value of the product attribute and an advertisement factor of 1.0.
 *
 * The messages are thus created uniformly, and no parameterization of the scheme is possible.
 *
 */
public class UniformMessageScheme extends CompanyAgentMessageScheme {

    public UniformMessageScheme() {
    }

    /**
     * The UniformMessageScheme will uniformly (for each ConsumerAgent and each product managed
     * by the CompanyAgent and each of its ProductAttributes) create a CompanyConsumerPerceptionManipulationMessage
     * from the respective CompanyAgent to the respective ConsumerAgent with the true value
     * of the product attribute and an advertisement factor of 1.0.
     *
     * @param simulationContainer The container the scheme is associated with
     * @param sender The CompanyAgent sending the messages
     * @return A set of messages with one CompanyConsumerPerceptionManipulationMessage for each consumerAgent in the simulationContainer and each ProductAttributes of the products managed by the sender, with the true value of the product attribute and an advertisement factor of 1.0
     */
    public Set<Message> createMessages(SimulationContainer simulationContainer, CompanyAgent sender) {
        Set<Message> uniformMessages = new HashSet<>();
        for(ConsumerAgent currentAgent : simulationContainer.getConsumerAgents()){
            for(Product currentProduct : sender.getProductPortfolio()){
                for(ProductAttribute currentPA : currentProduct.getProductAttributes()){
                    uniformMessages.add(new CompanyConsumerPerceptionManipulationMessage(sender, currentAgent, currentPA, currentProduct, currentPA.getValue(), 1.0));
                }
            }
        }
        return uniformMessages;
    }

    /**
     * For agents of an unspecified type, the UniformMessageScheme will create the messages
     * according to how it does for a CompanyAgent if the sendingAgent is a CompanyAgent
     * and throw an UnsupportedOperationException if the agent is of a different type
     * (for whom this scheme is not yet implemented)
     *
     * @param simulationContainer The container the simulation runs in
     * @param sendingAgent The agent from whom the messages originate
     * @return One message for each consumer agent in the simulationContainer for each product attibute of each product managed by the sendingAgent (if its a CompanyAgent).
     * @throws UnsupportedOperationException Will be thrown when the sendingAgent is not a CompanyAgent
     */
    public Set<Message> createMessages(SimulationContainer simulationContainer, Agent sendingAgent) throws UnsupportedOperationException{
        if(sendingAgent.getClass().equals(CompanyAgent.class)) return createMessages(simulationContainer, (CompanyAgent) sendingAgent);
        else throw new UnsupportedOperationException("Creating message for agents of type "+sendingAgent.getClass()+" is currently not supported.\nPlease provide a valid agent type!");
    }
}
