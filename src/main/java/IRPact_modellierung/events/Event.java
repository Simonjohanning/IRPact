package IRPact_modellierung.events;


import IRPact_modellierung.SimulationEntity;
import IRPact_modellierung.simulation.SimulationContainer;

/**
 * An event is an abstraction representing any model dynamics.
 * It is created by an entity involved in the (concrete) model dynamic,
 * and serves to interact with (not necessarily other) entities in that model dynamic.
 * It is closely tied to the temporal model by its scheduled time,
 * and managed by an EventScheduler.
 * Its effects are modeled within the event through their processing method.
 * Every (distinct) model dynamic should model their own type of event,
 * formalizing their dynamics in its processing method.
 *
 * @author Simon Johanning
 */
public abstract class Event extends SimulationEntity implements Comparable<Event> {

	private double scheduledForTime;

	/**
	 * Abstraction of an event representing a model dynamic at a specified time
	 *
	 * @param simulationContainer The simulation container the model dynamic is to take place in
	 * @param scheduledForTime The simulation time at which the event shall be processed
	 *
	 * @throws IllegalArgumentException When the simulation container is null or the scheduled time lies before the (reference) time within the simulation container
	 */
	public Event(SimulationContainer simulationContainer, double scheduledForTime) throws IllegalArgumentException{
		super(simulationContainer);
		if(simulationContainer == null) throw new IllegalArgumentException("Simulation container is null. Event to instantiate has no context in which it is executed!!");
		else if(simulationContainer.getTimeModel().getSimulationTime() > scheduledForTime) throw new IllegalArgumentException("Event is scheduled for the past ("+scheduledForTime+")!; Since time is unidirectional this is a violation of semantics!!\nSimulation time is "+simulationContainer.getTimeModel().getSimulationTime());
		this.scheduledForTime = scheduledForTime;
	}

	public double getScheduledForTime() {
		return this.scheduledForTime;
	}

	/**
	 * Method to compare the execution time of this event to another event for sorting events by execution time
	 *
	 * @param otherEvent Event that to reference the scheduled time of this event to
	 * @return -1 if this event is scheduled before otherEvent, 0 if both events are scheduled for the same time, 1 if event is scheduled to be executed after otherEvent
	 */
	public int compareTo(Event otherEvent) {
		if(this.getScheduledForTime() - otherEvent.getScheduledForTime() < 0.0) return -1;
		else if(this.getScheduledForTime() == otherEvent.getScheduledForTime()) return 0;
		else return 1;
	}

	/**
	 * Method to execute the event; Will make the model dynamics associated to this event come into effect
	 *
	 * @param systemTime The current time of the system for execution
	 */
	public abstract void processEvent(double systemTime);
}