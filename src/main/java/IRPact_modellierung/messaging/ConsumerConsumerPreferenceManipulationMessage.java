package IRPact_modellierung.messaging;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.preference.Preference;
import IRPact_modellierung.helper.StructureEnricher;

/**
 * A ConsumerConsumerPreferenceManipulationMessage is a Message from a consumer agent to another that
 * changes the preference of the receiving consumer by the difference of their preferences,
 * weighted by the strength of the connection (edge weight) and a homogenization factor.
 *
 * @author Simon Johanning
 */
public class ConsumerConsumerPreferenceManipulationMessage extends Message {

	private Preference senderPreference;
	private double edgeWeight;
	private double preferenceHomogenizationFactor;

	public ConsumerConsumerPreferenceManipulationMessage(ConsumerAgent sender, ConsumerAgent receiver, Preference senderPreference, double preferenceHomogenizationFactor, double edgeWeight) {
		super(sender, receiver);
		this.senderPreference = senderPreference;
		this.edgeWeight = edgeWeight;
		this.preferenceHomogenizationFactor = preferenceHomogenizationFactor;
	}

	public Preference getSenderPreference() {
		return senderPreference;
	}

	public double getEdgeWeight() {
		return edgeWeight;
	}

	public double getPreferenceHomogenizationFactor() {
		return preferenceHomogenizationFactor;
	}

	/**
	 * Processing this message causes the receiver to increase (or decrease) their preference
	 * for the respective preference by the difference between the senders' and receivers' preference value,
	 * modified by the strength of their connection at sending (edgeWeight) and the preferenceHomogenizationFactor
	 *
	 * @param systemTime The time at which the message is processed / evaluated
	 */
	public void processMessage(double systemTime) {
		ConsumerAgent agentConcerned = (ConsumerAgent) getReceiver();
		Preference receiverPreference = StructureEnricher.attachPreferenceValues(agentConcerned.getPreferences()).get(senderPreference.getValue());
		agentConcerned.manipulatePreferenceRelative(receiverPreference, (senderPreference.getStrength() - receiverPreference.getStrength())*preferenceHomogenizationFactor*edgeWeight);
	}
}