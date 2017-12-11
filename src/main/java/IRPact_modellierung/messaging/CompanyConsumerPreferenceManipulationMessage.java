package IRPact_modellierung.messaging;

import IRPact_modellierung.agents.companyAgents.CompanyAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.preference.Value;

/**
 * A CompanyConsumerPreferenceManipulationMessage is a message originating from company agents
 * which is directed at ConsumerAgents in order to manipulate the consumers' message.
 * It is targeted at one value and will increase the preference of the agent
 * for the value by a parameterized amount.
 *
 * @author Simon Johanning
 */
public class CompanyConsumerPreferenceManipulationMessage extends Message {

	private Value valueConcerned;
	private double preferenceIncreaseOfMessage;
	private boolean relative;

	public CompanyConsumerPreferenceManipulationMessage(CompanyAgent sender, ConsumerAgent receiver, Value valueConcerned, double preferenceIncreaseOfMessage, boolean relative) {
		super(sender, receiver);
		this.preferenceIncreaseOfMessage = preferenceIncreaseOfMessage;
		this.valueConcerned = valueConcerned;
		this.relative = relative;
	}

	public Value getValueConcerned() {
		return this.valueConcerned;
	}

	public double getPreferenceIncreaseOfMessage() {
		return this.preferenceIncreaseOfMessage;
	}

	public boolean isRelative() {
		return relative;
	}

	public void processMessage(double systemTime) {
		ConsumerAgent receivingAgent = (ConsumerAgent) receiver;
		if(relative) receivingAgent.manipulatePreferenceRelative(valueConcerned, preferenceIncreaseOfMessage);
		else receivingAgent.manipulatePreferenceAbsolute(valueConcerned, preferenceIncreaseOfMessage);
	}
}