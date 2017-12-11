package IRPact_modellierung.messaging;

import IRPact_modellierung.events.CommunicationEvent;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.Set;

/**
 * A CommunicationScheme describes how a message is turned into a communication event.
 * Since CommunicationEvents link messages with their execution time,
 * they basically specify the temporal patterns of messages.
 *
 * @author Simon Johanning
 */
public abstract class CommunicationScheme {

    protected MessageScheme correspondingMessageScheme;

    public CommunicationScheme(MessageScheme correspondingMessageScheme){
        this.correspondingMessageScheme = correspondingMessageScheme;
    }

    /**
     * Method describing how the messages are transformed into communications event.
     * The events will correspond to the messages and be within the simulation container,
     * so the only interesting behaviour takes place in the temporal delay of the messages.
     *
     * @param correspondingMessages The messages to transform into communication events
     * @param currentTime The current time of the simulation
     * @param simulationContainer The container the simulation runs in
     * @return The CommunicationEvents corresponding to the Messages based on this scheme
     */
    public abstract Set<CommunicationEvent> createCommunicationEvents(Set<Message> correspondingMessages, double currentTime, SimulationContainer simulationContainer);

    public MessageScheme getCorrespondingMessageScheme() {
        return correspondingMessageScheme;
    }
}
