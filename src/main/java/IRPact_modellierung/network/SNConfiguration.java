package IRPact_modellierung.network;

import java.util.HashMap;

/**
 * Class to represent the configuration of the social network.
 * This encapsules the qualifiers and parameters of the components relevant for the SocialNetwork,
 * i.e. the SocialGraph, the TopologyManipulationScheme and the EdgeWeightMappingScheme
 *
 * @author Simon Johanning
 */
public class SNConfiguration {

	//General network parameters
	private String socialGraph;
	private HashMap<String, Object> socialGraphParameters;
	private TopologyManipulationScheme topologyManipulationScheme;
	private EdgeWeightManipulationScheme edgeWeightMappingScheme;

	/**
	 * An SNConfiguration contains the information relevant for the configuration of the social network in the simulation.
	 * It is based on information in the social network configuration file, as extracted by the SNLoader
	 *
 	 * @param socialGraph The qualifier for the socialGraph to use
	 * @param socialGraphParameters The parameters relevant for the socialGraph to use
	 * @param sNTopologyManipulationScheme The qualifier for the TopologyManipulationScheme to use
	 * @param sNTopologyManipulationSchemeParameters The parameters relevant for the TopologyManipulationScheme to use
	 * @param sNEdgeWeightMappingScheme The qualifier for the EdgeWeightMappingScheme to use
	 * @param sNEdgeWeightMappingSchemeParameters The parameters for the EdgeWeightMappingScheme to use
	 */
	public SNConfiguration(String socialGraph, HashMap<String, Object> socialGraphParameters, String sNTopologyManipulationScheme, HashMap<String, Object> sNTopologyManipulationSchemeParameters, String sNEdgeWeightMappingScheme, HashMap<String, Object> sNEdgeWeightMappingSchemeParameters) {
		this.socialGraph = socialGraph;
		this.socialGraphParameters = socialGraphParameters;
		topologyManipulationScheme = SNFactory.generateSNTopologyManipulationScheme(sNTopologyManipulationScheme, sNTopologyManipulationSchemeParameters);
		edgeWeightMappingScheme = SNFactory.generateSNEdgeWeightMapping(sNEdgeWeightMappingScheme, sNEdgeWeightMappingSchemeParameters);
	}

	public String getSocialGraph() {
		return socialGraph;
	}

	public HashMap<String, Object> getSocialGraphParameters() {
		return socialGraphParameters;
	}

	public TopologyManipulationScheme getTopologyManipulationScheme() {
		return topologyManipulationScheme;
	}

	public EdgeWeightManipulationScheme getEdgeWeightMappingScheme() {
		return edgeWeightMappingScheme;
	}
}