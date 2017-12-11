package IRPact_modellierung.events;

import IRPact_modellierung.messaging.Message;
import IRPact_modellierung.simulation.SimulationContainer;

/**
 * A communication event is an event that represents a (unidirectional) communicative act
 * from one model entity to another.
 * It is associated with a message and practically serves as a link between messages and the event scheduler
 * (and thus indirectly the temporal and process model).
 * The execution of a communication event consists of processing the associated message.
 *
 * @author Simon Johanning
 */
public class CommunicationEvent extends Event {

	private Message correspondingMessage;

	/**
	 * A communication event binds a message to the temporal and process model.
	 * It schedules the execution of a message for a given time within the simulation container.
	 *
	 * @param correspondingMessage The message corresponding to the communication event
	 * @param scheduledForTime The time at which the execution of the message is scheduled
	 * @param simulationContainer The container in which the simulation takes place
	 *
	 * @throws IllegalArgumentException Gets thrown when the message corresponding to this event is null
	 */
	public CommunicationEvent(Message correspondingMessage, double scheduledForTime, SimulationContainer simulationContainer) throws IllegalArgumentException{
		super(simulationContainer, scheduledForTime);
		if(correspondingMessage == null) throw new IllegalArgumentException("The message associated with this event is null!! This should not occur!!");
		this.correspondingMessage = correspondingMessage;
	}

	public Message getCorrespondingMessage() {
		return this.correspondingMessage;
	}

	/**
	 * Processing the communication event is semantically equivalent to processing the message associated with it
	 *
	 * @param systemTime The current time of the system for execution
	 */
	public void processEvent(double systemTime) {
		correspondingMessage.processMessage(systemTime);
	}
}