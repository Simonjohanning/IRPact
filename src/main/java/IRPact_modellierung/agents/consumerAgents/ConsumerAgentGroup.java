package IRPact_modellierung.agents.consumerAgents;

import java.util.Set;
import java.util.Map;

import IRPact_modellierung.messaging.CommunicationScheme;
import IRPact_modellierung.perception.HistogramInitializationScheme;
import IRPact_modellierung.decision.ConsumerAgentAdoptionDecisionProcess;
import IRPact_modellierung.distributions.BooleanDistribution;
import IRPact_modellierung.distributions.UnivariateDistribution;
import IRPact_modellierung.distributions.SpatialDistribution;
import IRPact_modellierung.needs.NeedDevelopmentScheme;
import IRPact_modellierung.perception.PerceptionSchemeConfiguration;
import IRPact_modellierung.preference.Value;
import IRPact_modellierung.products.*;

/**
 * Class to describe the group a set of consumer agents makes up.
 * Will often be a socially or economically more or less coherent group, specifying a range of possible behaviours.
 * A ConsumerAgentGroup is configured in the AgentConfiguration and used for reference and parameterization of ConsumerAgents within the simulation.
 *
 * @author Simon Johanning
 */
public class ConsumerAgentGroup {


	private Set<ConsumerAgentGroupAttribute> consumerAgentGroupAttributes;
	private String groupName;
	private Set<ConsumerAgent> consumerAgentsInGroup;
	private Map<ProductGroupAttribute, PerceptionSchemeConfiguration> productPerceptionSchemes;
	private Map<ProductGroup, BooleanDistribution> productGroupAwarenessDistribution;
	private Map<FixedProductDescription, BooleanDistribution> fixedProductAwarenessDistribution;
	private ConsumerAgentAdoptionDecisionProcess decisionProcessEmployed;
	private Map<Value, UnivariateDistribution> consumerAgentGroupPreferences;
	private SpatialDistribution spatialDistribution;
	private Map<FixedProductDescription, UnivariateDistribution> initialProductConfiguration;
	private NeedDevelopmentScheme needDevelopmentScheme;
	private double informationAuthority;
	private CommunicationScheme communicationScheme;

	//TODO check if importanceAttribute needs to vary for different product groups or if it can be included within consumerAgentGroupAttributes

	/**
	 * As a ConsumerAgentGroup is specified by a range of different aspects, these are explained in the following.
	 * The attributes in a ConsumerAgentGroup generally describe a range of values the ConsumerAgents can (initially) take, and are often specified as distributions
	 *
	 * @param consumerAgentGroupAttributes The set of ConsumerAgentGroupAttributes ConsumerAgents of this group will use
	 * @param groupName The name of the ConsumerAgentGroup
	 * @param consumerAgentsInGroup The ConsumerAgents that are instances of this group (can also be set aposteriori)
	 * @param productGroupAwarenessDistribution A mapping of the fixed products (in the form of FixedProductDescription) with UnivariateDistributions, describing
	 * @param fixedProductAwarenessDistribution Map that describes how the awareness for products specified as fixed products is distributed within the simulation
	 * @param decisionProcessEmployed The (default) DecisionMakingProcess the agent uses
	 * @param consumerAgentGroupPreferences A map of values and UnivariateDistributions used to generate the (strength of) preference of the agents comprising this group
	 * @param spatialDistribution The SpatialDistribution used to initialize the agents in the simulation environment
	 * @param productPerceptionSchemes The perception schemes (and their paramters) used for managing the perceptions of the respective agents
	 * @param initialProductConfiguration The initial product configuration for each fixed product given as a Distribution to be drawn from (to set up the technology mix at the beginning of the simulation)
	 * @param needDevelopmentScheme The NeedDevelopmentScheme used by agents to assess which needs arise when
	 * @param informationAuthority The information authority agents of this group have
	 */
	public ConsumerAgentGroup(Set<ConsumerAgentGroupAttribute> consumerAgentGroupAttributes, String groupName, Set<ConsumerAgent> consumerAgentsInGroup, CommunicationScheme communicationScheme, Map<ProductGroupAttribute, PerceptionSchemeConfiguration> productPerceptionSchemes, Map<ProductGroup, BooleanDistribution> productGroupAwarenessDistribution, Map<FixedProductDescription, BooleanDistribution> fixedProductAwarenessDistribution, ConsumerAgentAdoptionDecisionProcess decisionProcessEmployed, Map<Value, UnivariateDistribution> consumerAgentGroupPreferences, SpatialDistribution spatialDistribution, Map<FixedProductDescription, UnivariateDistribution> initialProductConfiguration, NeedDevelopmentScheme needDevelopmentScheme, double informationAuthority) {
		this.consumerAgentGroupAttributes = consumerAgentGroupAttributes;
		this.groupName = groupName;
		this.consumerAgentsInGroup = consumerAgentsInGroup;
		this.productPerceptionSchemes = productPerceptionSchemes;
		this.productGroupAwarenessDistribution = productGroupAwarenessDistribution;
		this.fixedProductAwarenessDistribution = fixedProductAwarenessDistribution;
		this.decisionProcessEmployed = decisionProcessEmployed;
		this.consumerAgentGroupPreferences = consumerAgentGroupPreferences;
		this.spatialDistribution = spatialDistribution;
		this.initialProductConfiguration = initialProductConfiguration;
		this.needDevelopmentScheme = needDevelopmentScheme;
		this.informationAuthority = informationAuthority;
		this.communicationScheme = communicationScheme;
	}

	public Set<ConsumerAgentGroupAttribute> getConsumerAgentGroupAttributes() {
		return this.consumerAgentGroupAttributes;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public Set<ConsumerAgent> getConsumerAgentsInGroup() {
		return this.consumerAgentsInGroup;
	}

	public void setConsumerAgentsInGroup(Set<ConsumerAgent> consumerAgentsInGroup) {
		this.consumerAgentsInGroup = consumerAgentsInGroup;
	}

	public NeedDevelopmentScheme getNeedDevelopmentScheme() {
		return needDevelopmentScheme;
	}

	public CommunicationScheme getCommunicationScheme() {
		return communicationScheme;
	}

	public Map<ProductGroup, BooleanDistribution> getProductGroupAwarenessDistribution() {
		return productGroupAwarenessDistribution;
	}

	public Map<FixedProductDescription, BooleanDistribution> getFixedProductAwarenessDistribution() {
		return fixedProductAwarenessDistribution;
	}

	public ConsumerAgentAdoptionDecisionProcess getDecisionProcessEmployed() {
		return this.decisionProcessEmployed;
	}

	public Map<Value, UnivariateDistribution> getConsumerAgentGroupPreferences() {
		return consumerAgentGroupPreferences;
	}

	public SpatialDistribution getSpatialDistribution() {
		return spatialDistribution;
	}

	public Map<ProductGroupAttribute, PerceptionSchemeConfiguration> getProductPerceptionSchemes() {
		return productPerceptionSchemes;
	}

	public Map<FixedProductDescription, UnivariateDistribution> getInitialProductConfiguration() {
		return initialProductConfiguration;
	}

	public double getInformationAuthority() {
		return informationAuthority;
	}
}