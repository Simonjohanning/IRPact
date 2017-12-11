package IRPact_modellierung.messaging;

import IRPact_modellierung.agents.Agent;

/**
 * Messages model the communication activity between agents.
 * They are directed from a sending agent to a receiving one,
 * and describe their effect through the processMessage method.
 *
 * @author Simon Johanning
 */
public abstract class Message {

	protected Agent sender;
	protected Agent receiver;

	/**
	 * A message represent a communicative activity from the sender to the receiver
	 *
	 * @param sender The agent from which the communicative activity stems
	 * @param receiver The agent to which the communicative activity is directed to
	 */
	public Message(Agent sender, Agent receiver) {
		this.sender = sender;
		this.receiver = receiver;
	}

	public Agent getSender() {
		return this.sender;
	}

	public Agent getReceiver() {
		return this.receiver;
	}

	/**
	 * Processing the message will make the message take effect.
	 * This method captures the semantics of the respective message.
	 *
	 * @param systemTime The time at which the message is processed / evaluated
	 */
	public abstract void processMessage(double systemTime);

}