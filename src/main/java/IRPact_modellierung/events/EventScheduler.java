package IRPact_modellierung.events;

import IRPact_modellierung.SimulationEntity;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.*;

/**
 * Abstraction of an event scheduler.
 * Keeps an ordered list of events to be executed and offers functionality for accessing them.
 * Scheduling and construction is left to the implementation of concrete schedulers.
 *
 * @author Simon Johanning
 */
public abstract class EventScheduler extends SimulationEntity {

    protected LinkedList<Event> scheduledEvents;
    //The event set is the set of all events ever scheduled in the simulation, and is used in order to ensure that an event doesn't get scheduled twice
    private Set<Event> eventSet;

    /**
     * Initiates the list of scheduled events and adds the scripted events to this list.
     * The (scheduling) order of the scripted events should be established in the constructor of the implementation
     * or the implementation should overwrite the scheduleScriptedEvents method
     *
     * @param associatedSimulationContainer The simulation container in which the simulation takes place
     */
    public EventScheduler(SimulationContainer associatedSimulationContainer) {
        super(associatedSimulationContainer);
        scheduledEvents = new LinkedList<Event>();
        eventSet = new HashSet<>();
    }

    /**
     * Schedules a new event.
     * Will generally put the event in the list based on its temporal order;
     * However details are left to the implementation of schedulers.
     *
     * @param eventToSchedule The event that is to be scheduled
     * @throws IllegalArgumentException Will be thrown when event has been scheduled before or is scheduled for the past
     */
    public void scheduleEvent(Event eventToSchedule) throws IllegalArgumentException{
        if(eventSet.contains(eventToSchedule)) throw new IllegalArgumentException("Event "+eventToSchedule+" was scheduled before!!");
        else if(eventToSchedule.getAssociatedSimulationContainer().getTimeModel().getSimulationTime() > eventToSchedule.getScheduledForTime()) throw new IllegalArgumentException("Event "+eventToSchedule+" is scheduled for a time already passed!!");
        else{
            eventSet.add(eventToSchedule);
            scheduleValidEvent(eventToSchedule);
        }
    }

    /**
     * Schedules a new event.
     * Will generally put the event in the list based on its temporal order;
     * However details are left to the implementation of schedulers.
     * Method is basically an alias to scheduleEvent and only used for consistency with other methods
     *
     * @param eventToSchedule The event that is to be scheduled
     * @throws IllegalArgumentException Will be thrown when event has been scheduled before or is scheduled for the past
     */
    public void pushEvent(Event eventToSchedule) throws IllegalArgumentException{
        try{
            scheduleEvent(eventToSchedule);
        }catch (IllegalArgumentException iae){
            throw iae;
        }
    }

    /**
     * Pushes a (valid) event (i.e. not scheduled before and not lying in the past) in the event list.
     * How this is done exactly depends on the implementation.
     *
     * @param evenToSchedule The event that is to be scheduled
     */
    protected abstract void scheduleValidEvent(Event evenToSchedule);

    /**
     * pops (removes and returns) the event scheduled next.
     * Assumes that the next scheduled event is the first element in the list.
     * With implementations that violate this assumption, the element returned might not the 'next' (in the sense of temporal order) element
     *
     * @return The first entry of the list of scheduled events (generally the next event)
     * @throws IndexOutOfBoundsException Will be thrown when the list of scheduled events is empty
     */
    public Event popNextEvent() throws IndexOutOfBoundsException{
        if(scheduledEvents.isEmpty()) throw new IndexOutOfBoundsException("No next event is present. Please check for existence before calling the pop method");
        else{
            Event returnEvent = scheduledEvents.getFirst();
            scheduledEvents.removeFirst();
            return returnEvent;
        }
    }

    /**
     * Method to retrieve the next event is scheduled.
     * Assumes that the next scheduled event is the first element in the list.
     * With implementations that violate this assumption, the element returned might not the 'next' (in the sense of temporal order) element
     * Unlike pop the element is not taken off the list, so it should be used to get information about
     * the event, but not to execute the event, since otherwise an event might be executed multiple times.
     *
     * @return The first entry of the list of scheduled events (generally the next event)
     * @throws IndexOutOfBoundsException Will be thrown when the list of scheduled events is empty
     */
    public Event topNextEvent(){
        if(scheduledEvents.isEmpty()) throw new IndexOutOfBoundsException("No next event is present. Please check for existence before calling the pop method");
        else {
            return scheduledEvents.getFirst();
        }
    }

    /**
     * Method to check whether a next event is present (whether the list is not empty).
     *
     * @return Will return true if the list is not empty, false if so.
     */
    public boolean existsNextEvent() {
        return !scheduledEvents.isEmpty();
    }

    /**
     * Method to check if an event was ever an event scheduled by the EventScheduler.
     * All events scheduled are kept in an event set.
     * This method will check for membership of the eventToCheck within this set.
     *
     * @param eventToCheck Event to check whether it ever existed (within this EventScheduler in this simulation)
     * @return True if event was ever scheduled by this EventScheduler, false if not.
     */
    public boolean everScheduledEvent(Event eventToCheck){
        return eventSet.contains(eventToCheck);
    }
}