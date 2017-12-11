package IRPact_modellierung.messaging;

import IRPact_modellierung.events.CommunicationEvent;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.HashSet;
import java.util.Set;

/**
 * The ImmediateCommunicationScheme is a very basic CommunicationScheme
 * which assigns every message the current time as execution time.
 *
 * It is thus a very basic communication scheme that can be used
 * as a base line to compare more sophisticated ones to.
 *
 * @author Simon Johanning
 */
public class ImmediateCommunicationScheme extends CommunicationScheme{


    public ImmediateCommunicationScheme(MessageScheme correspondingMessageScheme) {
        super(correspondingMessageScheme);
    }

    /**
     * The ImmediateCommunicationScheme schedules all respective CommunicationEvents
     * at the current time irrespective of the message.
     *
     * @param correspondingMessages The messages to transform into communication events
     * @param currentTime The current time of the simulation
     * @param simulationContainer The container the simulation runs in
     * @return CommunicationEvents corresponding to the messages scheduled for immediate execution
     */
    public Set<CommunicationEvent> createCommunicationEvents(Set<Message> correspondingMessages, double currentTime, SimulationContainer simulationContainer) {
        Set<CommunicationEvent> events = new HashSet<>();
        for(Message currentMessage : correspondingMessages){
            events.add(new CommunicationEvent(currentMessage, currentTime, simulationContainer));
        }
        return events;
    }
}
