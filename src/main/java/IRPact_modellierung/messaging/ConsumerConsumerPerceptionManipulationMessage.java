package IRPact_modellierung.messaging;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductAttribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A ConsumerConsumerPerceptionManipulationMessage is a message from a ConsumerAgent to a ConsumerAgent
 * that changes the perception of the receiving ConsumerAgent given the perception of the sending
 * ConsumerAgent.
 *
 * It is a message about a given ProductAttribute, whose perception (on evaluation of the message)
 * gets changed by adding the perception of the ProductAttribute by the sending agent to the perception
 * of the receiving one (i.e. by invoking addPerceivedProductAttributeValue by the current perception of the sending agent,
 * via the modifyValue of the respective perception scheme).
 *
 * The strength of the connection of the agents is given through the edgeWeightConsumerEdge attribute associated with this message.
 *
 * If the receiving agent is not aware of the product the perception is about, they are made aware of it through this message.
 *
 * @author Simon Johanning
 */
public class ConsumerConsumerPerceptionManipulationMessage extends Message {

	private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	private ProductAttribute productAttributeConcerned;
	private double edgeWeightConsumerEdge;
	private Product productConcerned;

	/**
	 * A ConsumerConsumerPerceptionManipulationMessage changes (manipulates) the perception
	 * of a ProductAttribute of a receiving consumer by the perception
	 * of it by the sending consumer, with the strength of the message given by the edgeWeightConsumerEdge.
	 *
	 * @param sender The ConsumerAgent sending the message
	 * @param receiver The ConsumerAgent receiving the message
	 * @param productAttributeConcerned The ProductAttribute the message is about
	 * @param productConcerned The product the product attribute belongs to
	 * @param edgeWeightConsumerEdge The strength of the transmitted perception
	 */
	public ConsumerConsumerPerceptionManipulationMessage(ConsumerAgent sender, ConsumerAgent receiver, ProductAttribute productAttributeConcerned, Product productConcerned, double edgeWeightConsumerEdge) {
		super(sender, receiver);
		this.productAttributeConcerned = productAttributeConcerned;
		this.edgeWeightConsumerEdge = edgeWeightConsumerEdge;
		this.productConcerned = productConcerned;
		fooLog.debug("Message from {} to {} about product attribute {} created", sender, receiver, productAttributeConcerned);
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
	 * The effect of the message is that a perception of a ProductAttribute is added to the the
	 * perception of the receiving agent. The perception added is based on the perception of the sending agent.
	 * Also, if unaware, the receiving agent is made aware of the product the ProductAttribute describes a quality of.
	 *
	 * @param systemTime The time at which the message is processed / evaluated
	 */
	public void processMessage(double systemTime) {
		fooLog.debug("Evaluating message from {} to {} about {}",sender, receiver, productConcerned);
		if(sender.getAssociatedSimulationContainer().getActiveProducts().contains(productConcerned)) {
			ConsumerAgent agentConcerned = (ConsumerAgent) getReceiver();
			ConsumerAgent sendingAgent = (ConsumerAgent) sender;
			if (!(agentConcerned.isAwareOfProduct(productConcerned))){
				fooLog.debug("{} is made aware of {}", agentConcerned, productConcerned);
				agentConcerned.makeAwareOfProduct(productConcerned);
			}
			agentConcerned.addPerceivedProductAttributeValue(productAttributeConcerned, ((ConsumerAgent) sender).getPerceivedProductAttributeValues().get(productAttributeConcerned).calculateProductAttributePerception(systemTime), systemTime, edgeWeightConsumerEdge);
		}//if product doesn't exist anymore, no communication about it is needed
	}
}