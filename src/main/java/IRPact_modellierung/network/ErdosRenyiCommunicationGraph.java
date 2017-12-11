package IRPact_modellierung.network;

import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.*;

/**
 * The ErdosRenyiCommunicationGraph is a graph whose edges are all communicative edges and whose topology
 * is initialized after the Erdös-Rényi random model, i.e. a random graph for a fixed number of edges.
 *
 * The number of edges is given by the paramter 'k'
 *
 * @author Simon Johanning
 */
public class ErdosRenyiCommunicationGraph extends CommunicationGraph {

	private int k;

	/**
	 * Constructor giving the parameter for the ErdosRenyiCommunicationGraph and the edges,
	 * as well as neighbourhood relations explicitly
	 *
	 * @param associatedSimulationContainer The container the social network is embedded in
	 * @param sNConfiguration The configuration object of the social network
	 * @param nodes The nodes the graph contains
	 * @param edges The edges the graph contains
	 * @param neighbours The neighbourhood relationships of the graph
	 */
	public ErdosRenyiCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes, Set<SNEdge> edges, HashMap<SNNode, Set<SNNode>> neighbours) {
		super(associatedSimulationContainer, sNConfiguration, nodes, edges, neighbours);
		this.k = edges.size();
	}

	/**
	 * Constructor for the ErdosRenyiCommunicationGraph giving the nodes in the social graph explicitly
	 *
	 * @param associatedSimulationContainer The container the social network is embedded in
	 * @param sNConfiguration The configuration object of the social network with the socialGraph parameter containing the parameter 'k'
	 * @param nodes The nodes the socialGraph is based upon
	 */
	public ErdosRenyiCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes) {
		super(associatedSimulationContainer, sNConfiguration, nodes);
		this.k = (int) sNConfiguration.getSocialGraphParameters().get("k");
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}
	}

	/**
	 * Constructor for the ErdosRenyiCommunicationGraph giving the number of nodes in the social graph
	 *
	 * @param associatedSimulationContainer The container the social network is embedded in
	 * @param sNConfiguration The configuration object of the social network with the socialGraph parameter containing the parameter 'k'
	 * @param numberOfNodes The nodes the socialGraph is based upon
	 */
	public ErdosRenyiCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) {
		super(associatedSimulationContainer, sNConfiguration, numberOfNodes);
		this.k = (int) sNConfiguration.getSocialGraphParameters().get("k");
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}
	}

	public void addNodes(SNConfiguration sNconfiguration, Set<SNNode> nodesToAdd) throws IllegalArgumentException {
		if(nodesToAdd.size() > 0) throw new IllegalStateException("In this model no new nodes can be added, since the parameter for the number of edges is unclear!");
	}


	protected Set<SNEdge> createEdges(SNConfiguration sNConfiguration, Set<SNNode> initialNodes) throws IllegalArgumentException{
		Set<SNEdge> edgesToCreate = new HashSet<>(k);
		//List to make sure that no two edges (of different weight / label) will be created between any two nodes
		Map<SNNode, Set<SNNode>> protoEdgeList = new HashMap<>(initialNodes.size());
		for(SNNode currentNode : initialNodes){
			protoEdgeList.put(currentNode, new HashSet<>());
		}
		//create k edges between any nodes with equal probability
		for(int index=0;index<k;index++){
			boolean validEdgeFound = false;
			//iterate until a valid edge (i.e. one that is not already a proto edge or would be selfreferential if that is forbidden) is found
			while(!validEdgeFound){
				//find random nodes
				SNNode sourceNode = LazynessHelper.chooseRandomNode(initialNodes);
				SNNode targetNode = LazynessHelper.chooseRandomNode(initialNodes);
				//check that edge does not exist yet
				if(!protoEdgeList.get(sourceNode).contains(targetNode)){
					//check that nodes are distinct or self-referential loops are allowed
					if(!sourceNode.equals(targetNode) || sNConfiguration.getTopologyManipulationScheme().isSelfReferential()){
						//valid node is found -> add to proto nodes
						protoEdgeList.get(sourceNode).add(targetNode);
						validEdgeFound = true;
					}
				}
			}
		}
		//make edges out of these proto edges
		for(SNNode sourceNode : protoEdgeList.keySet()){
			for(SNNode targetNode : protoEdgeList.get(sourceNode)){
				edgesToCreate.add(new SNCommunicationEdge(sourceNode, targetNode, sNConfiguration.getEdgeWeightMappingScheme().weighEdge(sourceNode, targetNode, EDGEMEDIUM.COMMUNICATION)));
			}
		}
		return edgesToCreate;
	}

	public int getK() {
		return this.k;
	}
}