package IRPact_modellierung.messaging;

import IRPact_modellierung.agents.Agent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.network.SNEdge;
import IRPact_modellierung.network.SNNode;
import IRPact_modellierung.network.SocialGraph;

import java.util.Map;

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

	protected double determineEdgeWeight(ConsumerAgent sender, ConsumerAgent receiver) {
		if(sender.getAssociatedSimulationContainer() != receiver.getAssociatedSimulationContainer()) throw new IllegalArgumentException("Message sender and receiver are in different simulation containers!!");
		else{
			SocialGraph socialGraph = sender.getAssociatedSimulationContainer().getSocialNetwork().getSocialGraph();
			Map<ConsumerAgent, SNNode> casNMap = sender.getAssociatedSimulationContainer().getCasNMap();
			try {
				SNEdge respectiveEdge = socialGraph.retrieveEdge(casNMap.get(sender), casNMap.get(receiver), SocialGraph.EDGEMEDIUM.COMMUNICATION);
				return respectiveEdge.getEdgeWeight();
			} catch (IllegalArgumentException e) {
				throw e;
			}
		}
	}

}