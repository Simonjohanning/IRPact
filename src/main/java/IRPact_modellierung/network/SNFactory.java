package IRPact_modellierung.network;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.HashMap;
import java.util.Set;

/**
 * Class to bundle all process relevant for the initialization of the SocialNetwork base on the configuration of the concrete social network
 *
 * @author Simon Johanning
 */
public class SNFactory {

	/**
	 * Method to initialize the SocialGraph given by the configuration
	 *
	 * @param simulationContainer The container the SocialGraph is to be included in
	 * @param sNConfiguration The configuration object of the concrete social network of interest
	 * @param initialNodes The nodes the SocialGraph is initialized with
	 * @return The initialized SocialGraph object
	 */
	public static SocialGraph createSocialGraph(SimulationContainer simulationContainer, SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
		switch (sNConfiguration.getSocialGraph()) {
			case "GilbertCommunicationGraph":
				return new GilbertCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "ErdosRenyiCommunicationGraph":
				return new ErdosRenyiCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "RegularCommunicationGraph":
				return new RegularCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "HeterogeneousRegularCommunicationGraph":
				return new HeterogeneousRegularCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "HeterogeneousSmallWorldCommunicationGraph":
				return new HeterogeneousSmallWorldCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "NewtonianInversePowerlawCommunicationGraph" :
				return new NewtonianInversePowerlawCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "ExponentialDecayCommunicationGraph" :
				return new ExponentialDecayCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "MannaSenYookCommunicationGraph" :
				return new MannaSenYookCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "MannaSenYookPseudoCountCommunicationGraph" :
				return new MannaSenYookPseudoCountCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			default:
				throw new IllegalArgumentException("Error!!! Social Network " + sNConfiguration.getSocialGraph() + " not implemented!!!");
		}
	}
		/*switch(sNConfiguration.getSocialGraphEmployed()){
			case "GilbertCommunicationGraph":
				return new GilbertCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "ErdosRenyiCommunicationGraph":
				return new ErdosRenyiCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
		*/	/*case "SmallWorldCommunicationGraph":
				return new SmallWorldCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "RegularCommunicationGraph":
				return new RegularCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "ScalefreeCommunicationGraph":
				return new ScalefreeCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "HeterogeneousSmallWorldCommunicationGraph":
				return new HeterogeneousSmallWorldCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "HeterogeneousRegularCommunicationGraph":
				return new HeterogeneousRegularCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "HeterogeneousScalefreeCommunicationGraph":
				return new HeterogeneousScalefreeCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "SpatialSmallWorldSN":
				return new SpatialSmallWorldSN(simulationContainer, sNConfiguration, initialNodes);
			case "SpatialRegularSN":
				return new SpatialRegularSN(simulationContainer, sNConfiguration, initialNodes);
			case "SpatialHeterogeneousSmallWorldSN":
				return new SpatialHeterogeneousSmallWorldSN(simulationContainer, sNConfiguration, initialNodes);
			case "SpatialHeterogeneousRegularSN":
				return new SpatialHeterogeneousRegularSN(simulationContainer, sNConfiguration, initialNodes);
			case "MannaSenYookCommunicationGraph":
				return new MannaSenYookCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "HeterogeneousMannaSenYookSN":
				return new HeterogeneousMannaSenYookSN(simulationContainer, sNConfiguration, initialNodes);
			case "NewtonianInversePowerlawCommunicationGraph":
				return new NewtonianInversePowerlawCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "HeterogeneousNewtonianInversePowerlawCommunicationGraph":
				return new HeterogeneousNewtonianInversePowerlawCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "ExponentialDecayCommunicationGraph":
				return new ExponentialDecayCommunicationGraph(simulationContainer, sNConfiguration, initialNodes);
			case "HeterogeneousExponentialDecaySN":
				return new HeterogeneousExponentialDecaySN(simulationContainer, sNConfiguration, initialNodes);*/
			/*default:
				throw new IllegalStateException("Error!!! Social Network "+sNConfiguration.getSocialGraphEmployed()+" not implemented!!!");*/

	/**
	 * Method to initialize the SocialGraph given by the configuration
	 *
	 * @param simulationContainer The container the SocialGraph is to be included in
	 * @param sNConfiguration The configuration object of the concrete social network of interest
	 * @param numberOfNodes The number of nodes the SocialGraph is initialized with
	 * @return The initialized SocialGraph object
	 */
	public static SocialGraph createSocialGraph(SimulationContainer simulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) {
		switch (sNConfiguration.getSocialGraph()) {
			case "GilbertCommunicationGraph":
				return new GilbertCommunicationGraph(simulationContainer, sNConfiguration, numberOfNodes);
			case "ErdosRenyiCommunicationGraph":
				return new ErdosRenyiCommunicationGraph(simulationContainer, sNConfiguration, numberOfNodes);
			case "RegularCommunicationGraph":
				return new RegularCommunicationGraph(simulationContainer, sNConfiguration, numberOfNodes);
			case "HeterogeneousRegularCommunicationGraph":
				return new HeterogeneousRegularCommunicationGraph(simulationContainer, sNConfiguration, numberOfNodes);
			case "HeterogeneousSmallWorldCommunicationGraph":
				return new HeterogeneousSmallWorldCommunicationGraph(simulationContainer, sNConfiguration, numberOfNodes);
			default:
				throw new IllegalArgumentException("Error!!! Social Network " + sNConfiguration.getSocialGraph() + " not implemented!!!");
		}
	}
	//TODO complete with other networks and several methods with RegularCommunicationGraph, HeterogeneousRegularCommunicationGraph and HeterogeneousSmallWorldCommunicationGraph
		/*switch(sNConfiguration.getSocialGraphEmployed()){
			case "GilbertCommunicationGraph":
				return new GilbertCommunicationGraph(simulationContainer, sNConfiguration, numberOfNodes);
			case "ErdosRenyiCommunicationGraph":
				return new ErdosRenyiCommunicationGraph(simulationContainer, sNConfiguration, numberOfNodes);
		*/	/*case "SmallWorldCommunicationGraph":
				return new SmallWorldCommunicationGraph(simulationContainer, sNConfiguration, numberOfNodes);
			case "RegularCommunicationGraph":
				return new RegularCommunicationGraph(simulationContainer, sNConfiguration, numberOfNodes);
			case "ScalefreeCommunicationGraph":
				return new ScalefreeCommunicationGraph(simulationContainer, sNConfiguration, numberOfNodes);
			case "HeterogeneousSmallWorldCommunicationGraph":
				return new HeterogeneousSmallWorldCommunicationGraph(simulationContainer, sNConfiguration, numberOfNodes);
			case "HeterogeneousRegularCommunicationGraph":
				return new HeterogeneousRegularCommunicationGraph(simulationContainer, sNConfiguration, numberOfNodes);
			case "HeterogeneousScalefreeCommunicationGraph":
				return new HeterogeneousScalefreeCommunicationGraph(simulationContainer, sNConfiguration, numberOfNodes);
			case "SpatialSmallWorldSN":
				return new SpatialSmallWorldSN(simulationContainer, sNConfiguration, numberOfNodes);
			case "SpatialRegularSN":
				return new SpatialRegularSN(simulationContainer, sNConfiguration, numberOfNodes);
			case "SpatialHeterogeneousSmallWorldSN":
				return new SpatialHeterogeneousSmallWorldSN(simulationContainer, sNConfiguration, numberOfNodes);
			case "SpatialHeterogeneousRegularSN":
				return new SpatialHeterogeneousRegularSN(simulationContainer, sNConfiguration, numberOfNodes);
			case "MannaSenYookCommunicationGraph":
				return new MannaSenYookCommunicationGraph(simulationContainer, sNConfiguration, numberOfNodes);
			case "HeterogeneousMannaSenYookSN":
				return new HeterogeneousMannaSenYookSN(simulationContainer, sNConfiguration, numberOfNodes);
			case "NewtonianInversePowerlawCommunicationGraph":
				return new NewtonianInversePowerlawCommunicationGraph(simulationContainer, sNConfiguration, numberOfNodes);
			case "HeterogeneousNewtonianInversePowerlawCommunicationGraph":
				return new HeterogeneousNewtonianInversePowerlawCommunicationGraph(simulationContainer, sNConfiguration, numberOfNodes);
			case "ExponentialDecayCommunicationGraph":
				return new ExponentialDecayCommunicationGraph(simulationContainer, sNConfiguration, numberOfNodes);
			case "HeterogeneousExponentialDecaySN":
				return new HeterogeneousExponentialDecaySN(simulationContainer, sNConfiguration, numberOfNodes);*/
			/*default:
				throw new IllegalStateException("Error!!! Social Network "+sNConfiguration.getSocialGraphEmployed()+" not implemented!!!");*/

	/**
	 * Method to instantiate the TopologyManipulationScheme for the social network based on the given parameters
	 *
	 * @param scheme The qualifier of the TopologyManipulationScheme that is to be instantiated
	 * @param parameters The parameters the TopologyManipulationScheme is to be initialized with
	 * @return The generated TopologyManipulationScheme
	 */
	public static TopologyManipulationScheme generateSNTopologyManipulationScheme(String scheme, HashMap<String, Object> parameters) {
		switch (scheme) {
			case "RandomRewireTMS":
				return new RandomRewireTMS((Double) parameters.get("rewireProbability"), (Boolean) parameters.get("selfReferentialTopology"));
			case "RandomInDelTMS":
				return new RandomInDelTMS((Double) parameters.get("edgeDeletionProbability"), (Double) parameters.get("edgeInsertionProbability"), (Boolean) parameters.get("selfReferentialTopology"));
			default:
				throw new IllegalArgumentException("ERROR!!! This TopologyManipulationScheme (" + scheme + ") is not implemented!!!");
		}
	}

	/**
	 * Method to instantiate the EdgeWeightManipulationScheme for the social network based on the given parameters
	 *
	 * @param scheme The qualifier of the EdgeWeightManipulationScheme that is to be instantiated
	 * @param parameters The parameters the EdgeWeightManipulationScheme is to be initialized with
	 * @return The generated EdgeWeightManipulationScheme
	 */
	public static EdgeWeightManipulationScheme generateSNEdgeWeightMapping(String scheme, HashMap<String, Object> parameters){
		switch(scheme){
			case "ConstantEdgeWeightManipulationScheme":
				return new ConstantEdgeWeightManipulationScheme((Double) parameters.get("edgeWeight"));
			default:
				throw new IllegalArgumentException("ERROR!!! This EdgeWeightManipulationScheme ("+scheme+") is not implemented!!!");
		}
	}

	/**
	 * Method to create an edge of a given medium type of a given weight between two nodes in the graph.
	 * Will not check whether the source or target nodes are valid nodes within the graph!!
	 *
	 * @param source The node the edge is directed from
	 * @param target The node the edge is directed to
	 * @param weight The weight of the edge
	 * @param edgemedium The medium that is to be instantiated
	 * @return An edge of the respective medium and given weight from source to target
	 * @throws IllegalArgumentException Will be thrown when the medium is not implemented
	 */
	public static SNEdge createEdge(SNNode source, SNNode target, double weight, SocialGraph.EDGEMEDIUM edgemedium) throws IllegalArgumentException{
		switch (edgemedium){
			case TRUST: return new SNTrustEdge(source, target, weight);
			case COMMUNICATION: return new SNCommunicationEdge(source, target, weight);
			default: throw new IllegalArgumentException("Medium "+edgemedium+" doesn't exist / is not implemented!!");
		}
	}

	/**
	 * Method to create an edge of a given medium type of a given weight between two nodes in the graph.
	 * Will not check whether the source or target nodes are valid nodes within the graph!!
	 *
	 * @param source The node the edge is directed from
	 * @param target The node the edge is directed to
	 * @param weight The weight of the edge
	 * @param edgemedium The medium that is to be instantiated
	 * @param label The label the new edge will get
	 * @return An edge of the respective medium and given weight from source to target
	 * @throws IllegalArgumentException Will be thrown when the medium is not implemented
	 */
	public static SNEdge createEdge(SNNode source, SNNode target, double weight, SocialGraph.EDGEMEDIUM edgemedium, String label) throws IllegalArgumentException{
		switch (edgemedium){
			case TRUST: return new SNTrustEdge(source, target, weight, label);
			case COMMUNICATION: return new SNCommunicationEdge(source, target, weight, label);
			default: throw new IllegalArgumentException("Medium "+edgemedium+" doesn't exist / is not implemented!!");
		}
	}
}