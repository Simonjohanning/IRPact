package IRPact_modellierung.messaging;

import IRPact_modellierung.agents.companyAgents.CompanyAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductAttribute;

/**
 * A CompanyConsumerPreferenceManipulationMessage is a message originating from company agents
 * which is directed at ConsumerAgents in order to manipulate the consumers' message.
 * It is targeted at one value and will increase the preference of the agent
 * for the value by a parameterized amount.
 *
 * @author Simon Johanning
 */
public class CompanyConsumerPerceptionManipulationMessage extends Message {

	private ProductAttribute productAttributeConcerned;
	private double communicatedValue;
	private double advertisingImpactFactor;
	private Product productConcerned;


	public CompanyConsumerPerceptionManipulationMessage(CompanyAgent sender, ConsumerAgent receiver, ProductAttribute productAttributeConcerned, Product productConcerned, double communicatedValue, double advertisingImpactFactor) {
		super(sender, receiver);
		this.productAttributeConcerned = productAttributeConcerned;
		this.communicatedValue = communicatedValue;
		this.advertisingImpactFactor = advertisingImpactFactor;
		this.productConcerned = productConcerned;
	}

	public ProductAttribute getProductAttributeConcerned() {
		return this.productAttributeConcerned;
	}

	public double getCommunicatedValue() {
		return this.communicatedValue;
	}

	public double getAdvertisingImpactFactor() {
		return advertisingImpactFactor;
	}

	public Product getProductConcerned() {
		return productConcerned;
	}

	public void processMessage(double systemTime) {
		ConsumerAgent agentConcerned = (ConsumerAgent) getReceiver();
		if(!(agentConcerned.isAwareOfProduct(productConcerned))) agentConcerned.makeAwareOfProduct(productConcerned);
		agentConcerned.addPerceivedProductAttributeValue(productAttributeConcerned, communicatedValue, systemTime, advertisingImpactFactor);
	}
}