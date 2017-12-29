package IRPact_modellierung.messaging;

import IRPact_modellierung.agents.Agent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.distributions.UnivariateDistribution;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.Set;

/**
 * Class representing an abstraction of message schemes originating from consumer agents.
 * This class is (currently) used only to structure the respective MessageSchemes
 * and doesn't offer any additional functionality.
 *
 * @author Simon Johanning
 */
public abstract class ConsumerAgentMessageScheme extends MessageScheme {

    UnivariateDistribution numberMessagesPerTimeUnit;

    public ConsumerAgentMessageScheme(UnivariateDistribution numberMessagesPerTimeUnit) {
        this.numberMessagesPerTimeUnit = numberMessagesPerTimeUnit;
    }

    public UnivariateDistribution getNumberMessagesPerTimeUnit() {
        return numberMessagesPerTimeUnit;
    }

    public abstract Set<Message> createMessages(SimulationContainer simulationContainer, ConsumerAgent sendingAgent);

    public Set<Message> createMessages(SimulationContainer simulationContainer, Agent sendingAgent) {
        if(ConsumerAgent.class.isAssignableFrom(sendingAgent.getClass())) return createMessages(simulationContainer, (ConsumerAgent) sendingAgent);
        else throw new IllegalArgumentException("Consumer Agent Message Scheme needs to get an agent of type ConsumerAgent, but is "+sendingAgent.getClass().getSimpleName()+"!!");
    }
}
