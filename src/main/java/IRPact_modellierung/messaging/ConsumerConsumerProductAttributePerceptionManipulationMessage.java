package IRPact_modellierung.messaging;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductAttribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A ConsumerConsumerProductAttributePerceptionManipulationMessage is a message that
 * a ConsumerAgent directs at another ConsumerAgent in order to change their perception
 * about a product attribute, towards their perception of this attribute.
 *
 * It is thus primarily used to model how perceptions are communicated and how the
 * perceptions of other agents are incorporated in ones' own perceptions.
 *
 * @author Simon Johanning
 */
public class ConsumerConsumerProductAttributePerceptionManipulationMessage extends Message {

	private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	private ProductAttribute productAttributeConcerned;
	private double edgeWeightConsumerEdge;
	private Product productConcerned;

	/**
	 * A ConsumerConsumerProductAttributePerceptionManipulationMessage is a message
	 * from the sending agent to the receiving agent about communicating their
	 * perception of the productAttributeConcerned of the productConcerned,
	 * with the strength of the message effect depending on the edgeWeightConsumerEdge
	 * at the time of message creation.
	 * It further has the effect to make the receiver aware of the product if they don't know it yet.
	 *
	 * @param sender The agent sending the respective message
	 * @param receiver The agent receiving the message
	 * @param productAttributeConcerned The productAttribute the message is about
	 * @param productConcerned The product the message communicates about
	 * @param edgeWeightConsumerEdge The strength of the connection between the sender and the receiver
	 */
	public ConsumerConsumerProductAttributePerceptionManipulationMessage(ConsumerAgent sender, ConsumerAgent receiver, ProductAttribute productAttributeConcerned, Product productConcerned, double edgeWeightConsumerEdge) {
		super(sender, receiver);
		this.productAttributeConcerned = productAttributeConcerned;
		this.edgeWeightConsumerEdge = edgeWeightConsumerEdge;
		this.productConcerned = productConcerned;
	}

	public ProductAttribute getProductAttributeConcerned() {
		return this.productAttributeConcerned;
	}

	public double getEdgeWeightConsumerEdge() {
		return edgeWeightConsumerEdge;
	}

	public Product getProductConcerned() {
		return productConcerned;
	}

	/**
	 * Processing this message leads to making the receiver of the message aware of the product
	 * (if they are not already) and to add the perception of the sending agent at the current time
	 * with the strength of the connection between the two agents
	 *
	 * @param systemTime The time at which the message is processed / evaluated
	 */
	public void processMessage(double systemTime) {
		if(sender.getAssociatedSimulationContainer().getActiveProducts().contains(productConcerned)) {
			ConsumerAgent agentConcerned = (ConsumerAgent) getReceiver();
			if (!(agentConcerned.isAwareOfProduct(productConcerned)))
				agentConcerned.makeAwareOfProduct(productConcerned);
			agentConcerned.addPerceivedProductAttributeValue(productAttributeConcerned, ((ConsumerAgent) sender).getPerceivedProductAttributeValues().get(productAttributeConcerned).calculateProductAttributePerception(systemTime), systemTime, edgeWeightConsumerEdge);
		}//if product doesn't exist anymore, no communication about it is needed
	}
}