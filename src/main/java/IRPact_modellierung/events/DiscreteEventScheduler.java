package IRPact_modellierung.events;

import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;

import java.util.Collections;

//TODO make this an implementation and make DES abstract
//TODO think about what makes it discrete

/**
 * Event scheduler to manage events in a discrete temporal scheme.
 * Will sort the scheduled events depending on their scheduled time every time an event is added.
 *
 * @author Simon Johanning
 */
public class DiscreteEventScheduler extends EventScheduler {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    /**
     * Event scheduler to manage events in a discrete temporal scheme.
     * Will sort the scheduled events depending on their scheduled time every time an event is added.
     *
     * @param associatedSimulationContainer The simulation container in which the simulation takes place
     */
    public DiscreteEventScheduler(SimulationContainer associatedSimulationContainer) {
        super(associatedSimulationContainer);
        Collections.sort(scheduledEvents);
    }

    /**
     * Schedules the specified event and orders the set of scheduled event
     * by execution time, so that the tip of the list is the next event.
     *
     * @param eventToSchedule The event that is to be scheduled
     */
    protected void scheduleValidEvent(Event eventToSchedule) {
        scheduledEvents.add(eventToSchedule);
        Collections.sort(scheduledEvents);
    }
    //TODO check if there is a more efficient way to order the events (since before adding the event the list should be sorted anyways)

}