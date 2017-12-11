package IRPact_modellierung.agents.consumerAgents;

/**
 * Data structure to use a variety of consumer agent attributes not hard coded in the simulation.
 * See callers for semantics of this construct
 *
 * @author Simon Johanning
 */
public class ConsumerAgentAttribute {

	private String attributeName;
	private double attributeValue;

	/**
	 * Creates a single ConsumerAgentAttribute, associating a String to a (double) value
	 *
	 * @param attributeName Name of the ConsumerAgentAttribute
	 * @param attributeValue Value of the ConsumerAgentAttribute
	 */
	public ConsumerAgentAttribute(String attributeName, double attributeValue) {
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public double getAttributeValue() {
		return this.attributeValue;
	}

}