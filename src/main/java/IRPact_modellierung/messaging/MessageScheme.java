package IRPact_modellierung.messaging;

import IRPact_modellierung.agents.Agent;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.Set;

/**
 * A MessageScheme describes how messages of a given type are created,
 * depending on the sender of the message and the state of the simulation.
 * MessageSchemes describe this irrespective of the temporal context,
 * and how they are transformed to events is governed by the process model.
 *
 * @author Simon Johanning
 */
public abstract class MessageScheme {

    /**
     * Method to create the respective messages of a sending agent
     * depending on the state of the simulation.
     * The set of relevant messages are created but kept 'apart' from the simulation
     * and have to be scheduled as events in order to take effect.
     *
     * @param simulationContainer The container the simulation runs in
     * @param sendingAgent The agent from whom the messages originate
     * @return The set of messages originating from the sendingAgent in the current context
     */
    public abstract Set<Message> createMessages(SimulationContainer simulationContainer, Agent sendingAgent);

}
