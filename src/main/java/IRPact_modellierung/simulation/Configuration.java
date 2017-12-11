package IRPact_modellierung.simulation;

import IRPact_modellierung.decision.DecisionConfiguration;
import IRPact_modellierung.distributions.Distribution;
import IRPact_modellierung.information.InformationScheme;
import IRPact_modellierung.network.SNConfiguration;
import IRPact_modellierung.preference.PreferenceConfiguration;
import IRPact_modellierung.processModel.ProcessModel;
import IRPact_modellierung.time.TemporalConfiguration;
import IRPact_modellierung.products.ProductConfiguration;
import IRPact_modellierung.agents.AgentConfiguration;
import IRPact_modellierung.space.SpatialConfiguration;

import java.util.Map;

public class Configuration {

	private ProductConfiguration productConfiguration;
	private AgentConfiguration agentConfiguration;
	private SpatialConfiguration spatialConfiguration;
	private TemporalConfiguration temporalConfiguration;
	private SNConfiguration sNConfiguration;
	private PreferenceConfiguration preferenceConfiguration;
	private ProcessModel processModel;
	private DecisionConfiguration decisionConfiguration;
	private Map<String, Distribution> distributionMap;
	private InformationScheme informationScheme;

	public Configuration(ProductConfiguration productConfiguration, AgentConfiguration agentConfiguration, SpatialConfiguration spatialConfiguration, TemporalConfiguration temporalConfiguration, SNConfiguration sNConfiguration, PreferenceConfiguration preferenceConfiguration, ProcessModel processModel, DecisionConfiguration decisionConfiguration, Map<String, Distribution> distributionMap, InformationScheme informationScheme) {
		this.productConfiguration = productConfiguration;
		this.agentConfiguration = agentConfiguration;
		this.spatialConfiguration = spatialConfiguration;
		this.temporalConfiguration = temporalConfiguration;
		this.sNConfiguration = sNConfiguration;
		this.preferenceConfiguration = preferenceConfiguration;
		this.processModel = processModel;
		this.decisionConfiguration = decisionConfiguration;
		this.distributionMap = distributionMap;
		this.informationScheme = informationScheme;
	}

	public ProductConfiguration getProductConfiguration() {
		return this.productConfiguration;
	}

	public AgentConfiguration getAgentConfiguration() {
		return this.agentConfiguration;
	}

	public SpatialConfiguration getSpatialConfiguration() {
		return this.spatialConfiguration;
	}

	public TemporalConfiguration getTemporalConfiguration() {
		return this.temporalConfiguration;
	}

	public SNConfiguration getSNConfiguration() {
		return this.sNConfiguration;
	}

	public PreferenceConfiguration getPreferenceConfiguration() {
		return this.preferenceConfiguration;
	}

	public ProcessModel getProcessModel() {
		return processModel;
	}

	public DecisionConfiguration getDecisionConfiguration() {
		return decisionConfiguration;
	}

	public Map<String, Distribution> getDistributionMap() {
		return distributionMap;
	}

	public InformationScheme getInformationScheme() {
		return informationScheme;
	}
}