package IRPact_modellierung.agents;

import java.util.Map;
import java.util.Set;

import IRPact_modellierung.agents.companyAgents.CompanyAgentConfiguration;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroupAffinities;
import IRPact_modellierung.agents.policyAgent.PolicyAgentConfiguration;
import IRPact_modellierung.agents.posAgents.POSAgentConfiguration;
import IRPact_modellierung.messaging.CompanyAgentMessageScheme;
import IRPact_modellierung.messaging.ConsumerAgentMessageScheme;

/**
 * A data structure to describe the configuration of the agents used in the simulation
 *
 * @author Simon Johanning
 */
public class AgentConfiguration {

	private Set<ConsumerAgentGroup> consumerAgentGroups;
	private Map<ConsumerAgentGroup, Integer> noAgentsPerGroup;
	private Set<POSAgentConfiguration> posAgentConfiguration;
	private ConsumerAgentGroupAffinities affinities;
	private Set<CompanyAgentConfiguration> companyAgentsConfiguration;
	private PolicyAgentConfiguration policyAgentConfiguration;

	/**
	 * Constructor to bind relevant parameterizations of aspects of the simulation together
	 *
	 * @param consumerAgentGroups The ConsumerAgentGroups used in the simulation
	 * @param posAgentConfiguration The configuration of the POSAgents used in the simulation
	 * @param noAgentsPerGroup A map of how many consumer agents of each ConsumerAgentGroup are initialized
	 * @param affinities The affinity between different ConsumerAgentGroups within the simulation
	 * @param companyAgentsConfiguration The configuration of the company agents to use in the simulation
	 * @param policyAgentConfiguration The configuration the policy agent used in the simulation is based on
	 */
	public AgentConfiguration(Set<ConsumerAgentGroup> consumerAgentGroups, Set<POSAgentConfiguration> posAgentConfiguration, Map<ConsumerAgentGroup, Integer> noAgentsPerGroup, ConsumerAgentGroupAffinities affinities, Set<CompanyAgentConfiguration> companyAgentsConfiguration, PolicyAgentConfiguration policyAgentConfiguration) {
		this.consumerAgentGroups = consumerAgentGroups;
		this.posAgentConfiguration = posAgentConfiguration;
		this.noAgentsPerGroup = noAgentsPerGroup;
		this.affinities = affinities;
		this.companyAgentsConfiguration = companyAgentsConfiguration;
		this.policyAgentConfiguration = policyAgentConfiguration;
	}

	public Set<ConsumerAgentGroup> getConsumerAgentGroups() {
		return this.consumerAgentGroups;
	}

	public Set<POSAgentConfiguration> getPosAgentConfiguration() {
		return this.posAgentConfiguration;
	}

	public Map<ConsumerAgentGroup, Integer> getNoAgentsPerGroup() {
		return noAgentsPerGroup;
	}

	public ConsumerAgentGroupAffinities getAffinities() {
		return affinities;
	}

	public Set<CompanyAgentConfiguration> getCompanyAgentsConfiguration() {
		return companyAgentsConfiguration;
	}

	public PolicyAgentConfiguration getPolicyAgentConfiguration() {
		return policyAgentConfiguration;
	}
}