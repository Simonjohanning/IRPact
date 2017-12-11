package IRPact_modellierung.network;

import IRPact_modellierung.simulation.SimulationContainer;

import java.util.*;

/**
 * A RegularCommunicationGraph is a graph where each node is connected to a number of 'adjacent' nodes (via communicative edges),
 * given by the parameter z. The order chosen for this is the ID of the nodes by which they are ordered.
 *
 * The number of adjacent nodes a node is connected to is given by the parameter 'z'
 *
 * @author Simon Johanning
 */
public class RegularCommunicationGraph extends CommunicationGraph {

	private int z;

	public RegularCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes, Set<SNEdge> edges, HashMap<SNNode, Set<SNNode>> neighbours) {
		super(associatedSimulationContainer, sNConfiguration, nodes, edges, neighbours);
		z = (int) sNConfiguration.getSocialGraphParameters().get("z");
	}

	public RegularCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
		super(associatedSimulationContainer, sNConfiguration, initialNodes);
		z = (int) sNConfiguration.getSocialGraphParameters().get("z");
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}
	}

	public RegularCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) {
		super(associatedSimulationContainer, sNConfiguration, numberOfNodes);
		z = (int) sNConfiguration.getSocialGraphParameters().get("z");
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}
	}

	public int getZ() {
		return this.z;
	}


	//TODO implement and specify 5. & 6. more thoroughly
	/**
	 * A (directed) regular graph is characterized by the property that every node has the same in- as out-degree
	 * as every other node, and that these are equal.
	 * Adding nodes to a regular graph needs to retain this property.
	 * In graph initialization, this is achieved by assigning an order to the nodes,
	 * and connecting each node to the 'next nodes' (by modulo).
	 *
	 * Adding nodes to the graph through this method is done with the following algorithm:
	 * For all nodes to add:
	 * 1. Find any node (called the anchor) node which will become the node sharing no neighbours with the new node, but have the node to add as neighbour.
	 * 2. (After adding the new node to the graph), this node will be unique, since all other nodes having the new node as neighbour share at least one other neighbour with the new node.
	 * 3. Construct an ordered list of all neighbours of the anchor node (the neighbourList), ordered by the number of neighbours in common with the anchor node. There will be exactly one node for all k \in [1:z]. These are the nodes whose neighbour list needs to change.
	 * 4. Identify the node whose in-arcs need to be set to the the node to add, called the nodeToReplace. This node is given by the property that it is the common neighbour of all nodes in the neighbourList.
	 * 5. Change the targetNode of the arcs from nodes in the neighbourList to the nodeToReplace with the nodeToAdd, resetting the weight and label where necessary
	 * 6. Change the targetNodes of the nodes not shared by all neighbours
	 * 7. Create the arcs from the nodeToAdd to all neighbouring nodes of the nodeToReplace except for the one not in the union of the nodes in the neighbourList, as well to the nodeToReplace.
	 *
	 * @param sNconfiguration The configuration the social graph is based on
	 * @param nodesToAdd The nodes to be added to the social graph
	 * @throws IllegalArgumentException
	 */
	public void addNodes(SNConfiguration sNconfiguration, Set<SNNode> nodesToAdd) throws IllegalArgumentException, UnsupportedOperationException{
//		for(SNNode currentNode : nodesToAdd){
//			addIsolatedNode(currentNode);
//			SNNode anchorNode = LazynessHelper.chooseRandomNode(nodes);
//			//all nodes after the anchor nodes need to be manipulated
//			ArrayList<SNNode> variantNodes = (ArrayList<SNNode>) getCommunicationNeighbours().get(anchorNode);
//			//sort these nodes by the size of the intersection of their neighbour set and the neighbour set of the anchor node in order to get the right order for changing the target nodes
//			//....
//		}
		throw new UnsupportedOperationException("Adding nodes to this network is currently not implemented");
	}

	public Set<SNEdge> createEdges(SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
		if(initialNodes.size() < z) throw new IllegalArgumentException("The number of nodes to link node to in this RegularCommunicationGraph is smaller than the number of links!! ("+initialNodes.size()+" of "+z+")");
		else if((initialNodes.size() == z) && !sNConfiguration.getTopologyManipulationScheme().isSelfReferential()) throw new IllegalArgumentException("The number of nodes to link node to in this RegularCommunicationGraph is smaller than the number of links!! ("+initialNodes.size()+" of "+z+"), and the scheme is not self-referential!");
		return createStaticEdges(z, initialNodes, sNConfiguration.getEdgeWeightMappingScheme());
	}

	public static Set<SNEdge> createStaticEdges(int z, Set<SNNode> initialNodes, EdgeWeightManipulationScheme edgeWeightMappingScheme) {
		Set<SNEdge> edgesToCreate = new HashSet<>();
		//initialize the nodeIDMap
		Map<Integer, SNNode> nodeIDMap = new HashMap<>();
		int nodeIndex = 0;
		for(SNNode currentNode : initialNodes){
			nodeIDMap.put(nodeIndex, currentNode);
			nodeIndex++;
		}
		nodeIndex = 0;
//		System.out.println("Creating edges for nodes "+initialNodes);
		//create the edges based on the nodeID
		for(SNNode currentNode : initialNodes){
			for(int ordinalNeighbourIndex=1; ordinalNeighbourIndex<=z;ordinalNeighbourIndex++){
				edgesToCreate.add(new SNCommunicationEdge(currentNode, nodeIDMap.get((nodeIndex+ordinalNeighbourIndex)%(initialNodes.size())), edgeWeightMappingScheme.weighEdge(currentNode,nodeIDMap.get(nodeIndex+ordinalNeighbourIndex%initialNodes.size()), EDGEMEDIUM.COMMUNICATION)));
//				System.out.println("Edge created from node "+nodeIndex+" to node "+(nodeIndex+ordinalNeighbourIndex)%(initialNodes.size()));
			}
			nodeIndex++;

		}
		return edgesToCreate;
	}
}