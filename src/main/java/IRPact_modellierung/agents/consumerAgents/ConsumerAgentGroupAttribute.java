package IRPact_modellierung.agents.consumerAgents;

import IRPact_modellierung.distributions.UnivariateDistribution;

/**
 * A ConsumerAgentGroupAttribute is the generalization of a ConsumerAgentAttribute.
 * It associates a UnivariateDistribution to a String and has no fixed semantics within this simulation, since the thought behind it is to enable the modeler to flexibly add more semantics to his data.
 *
 * @author Simon Johanning
 */
public class ConsumerAgentGroupAttribute {

	private String name;
	private UnivariateDistribution value;

	/**
	 *
	 * @param name The name of the attribute
	 * @param value The value associated to the attribute (as a UnivariateDistribution out of which instantiations are drawn)
	 */
	public ConsumerAgentGroupAttribute(String name, UnivariateDistribution value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public UnivariateDistribution getValue() {
		return this.value;
	}

}