package IRPact_modellierung.network;

import java.util.*;

import IRPact_modellierung.SimulationEntity;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;

/**
 * The SocialGraph describes the structure of the interaction of the ConsumerAgents within the simulation.
 * As a graph it comprises nodes (corresponding to the ConsumerAgents) and edges (standing for their interaction),
 * The edges are associate with a certain EDGEMEDIUM, specifying what interaction the edge stands for.
 *
 * The SocialGraph further manages data structures to derive the neighbours of a node (given a certain medium),
 * an to access all edges stemming from a node (for all or a specific medium).
 *
 * @author Simon Johanning
 */
public abstract class SocialGraph extends SimulationEntity {

	private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	public enum EDGEMEDIUM{
		COMMUNICATION, TRUST
	}

	protected Set<SNNode> nodes;
	protected Set<SNEdge> edges;
	protected EnumMap<EDGEMEDIUM, HashMap<SNNode, Set<SNNode>>> neighbours;
	protected HashMap<SNNode, Set<SNEdge>> outgoingEdges;
	protected EnumMap<EDGEMEDIUM, HashMap<SNNode, Set<SNEdge>>> outgoingEdgesMedium;

	/**
	 * The SocialGraph is a directed graph describing the structure of interactions (corresponding to the edges) of
	 * ConsumerAgents (corresponding to the nodes) with a given semantics (corresponding to the edge mediums).
	 *
	 * In this constructor the graph is already initialized with the set of edges and neighbourhood relations.
	 *
	 * @param associatedSimulationContainer The container the social network is situated in
	 * @param sNConfiguration The configuration object of the corresponding instance of SocialGraph
	 * @param nodes The nodes in the SocialGraph
	 * @param edges The edge in the SocialGraph
	 * @param neighbours The neighbourhood relations of the SocialGraph
	 */
	public SocialGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes, Set<SNEdge> edges, EnumMap<EDGEMEDIUM, HashMap<SNNode, Set<SNNode>>> neighbours) {
		super(associatedSimulationContainer);
		this.nodes = nodes;
		this.edges = edges;
		this.neighbours = neighbours;
		outgoingEdges = new HashMap<SNNode, Set<SNEdge>>();
		boolean outgoingEdgesInitialized = false;
		outgoingEdgesMedium = new EnumMap<EDGEMEDIUM, HashMap<SNNode, Set<SNEdge>>>(EDGEMEDIUM.class);
		//initialize the edge-related data structures
		for(EDGEMEDIUM edgemedium : EDGEMEDIUM.values()){
			HashMap<SNNode, Set<SNEdge>> edgesMediumMap = new HashMap<SNNode, Set<SNEdge>>();
			for(SNNode currentNode : nodes) {
				if(!outgoingEdgesInitialized) outgoingEdges.put(currentNode, new HashSet<SNEdge>());
				edgesMediumMap.put(currentNode, new HashSet<SNEdge>());
			}
			if(!outgoingEdgesInitialized) outgoingEdgesInitialized = true;
			outgoingEdgesMedium.put(edgemedium, edgesMediumMap);
		}
		//fill the initialized edge-related data structures
		for(SNEdge currentEdge : edges){
			outgoingEdges.get(currentEdge.getSource()).add(currentEdge);
			if(currentEdge.getMedium() != null) {
				HashMap<SNNode, Set<SNEdge>> currentInnerMap = outgoingEdgesMedium.get(currentEdge.getMedium());
				currentInnerMap.get(currentEdge.getSource()).add(currentEdge);
				outgoingEdgesMedium.put(currentEdge.getMedium(), currentInnerMap);
			}
		}
	}

	/**
	 * The SocialGraph is a directed graph describing the structure of interactions (corresponding to the edges) of
	 * ConsumerAgents (corresponding to the nodes) with a given semantics (corresponding to the edge mediums).
	 *
	 * This constructor is intended for situations where a partial graph needs to be initialized with a number of initialNodes
	 * (such as in a scale free graph initialization). In this constructor no edges are included in the graph.
	 *
	 * @param associatedSimulationContainer The container the social network is situated in
	 * @param sNConfiguration The configuration object of the corresponding instance of SocialGraph
	 * @param initialNodes The nodes the SocialGraph is based on
	 */
	public SocialGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> initialNodes){
		super(associatedSimulationContainer);
		this.nodes = initialNodes;
		neighbours = new EnumMap<EDGEMEDIUM, HashMap<SNNode, Set<SNNode>>>(EDGEMEDIUM.class);
		outgoingEdges = new HashMap<SNNode, Set<SNEdge>>();
		boolean outgoingEdgesInitialized = false;
		outgoingEdgesMedium = new EnumMap<EDGEMEDIUM, HashMap<SNNode, Set<SNEdge>>>(EDGEMEDIUM.class);
		//initialize neighbours and edge data structures
		for(EDGEMEDIUM edgemedium : EDGEMEDIUM.values()) {
			HashMap<SNNode, Set<SNNode>> innerNeighbourMap = new HashMap<SNNode, Set<SNNode>>();
			HashMap<SNNode, Set<SNEdge>> edgesMediumMap = new HashMap<SNNode, Set<SNEdge>>();
			for (SNNode currentNode : initialNodes) {
				innerNeighbourMap.put(currentNode, new HashSet<SNNode>());
				if(!outgoingEdgesInitialized) outgoingEdges.put(currentNode, new HashSet<SNEdge>());
				edgesMediumMap.put(currentNode, new HashSet<SNEdge>());
			}
			neighbours.put(edgemedium, innerNeighbourMap);
			if(!outgoingEdgesInitialized) outgoingEdgesInitialized = true;
			outgoingEdgesMedium.put(edgemedium, edgesMediumMap);
		}
		this.edges = new HashSet<>();
	}

	/**
	 * The SocialGraph is a directed graph describing the structure of interactions (corresponding to the edges) of
	 * ConsumerAgents (corresponding to the nodes) with a given semantics (corresponding to the edge mediums).
	 *
	 * This constructor is intended for situations where a partial graph needs to be initialized with a number of initial nodes
	 * (such as in a scale free graph initialization). In this constructor no edges are included in the graph.
	 *
	 * @param associatedSimulationContainer The container the social network is situated in
	 * @param sNConfiguration The configuration object of the corresponding instance of SocialGraph
	 * @param numberOfNodes The number of nodes the SocialGraph is to be initialized with
	 * @throws IllegalArgumentException Will be thrown when the number of nodes to add is negative
	 */
	public SocialGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) throws IllegalArgumentException{
		this(associatedSimulationContainer, sNConfiguration, createInitialNodes(numberOfNodes));
		if(numberOfNodes < 0) throw new IllegalArgumentException("The number of nodes to add is negative!! The SocialGraph created in invalid!");
	}

	/**
	 * Method to create a number of nodes for the initialization of the node set of the graph.
	 * Assumes that no nodes exist (if nodes are to be added to an existing graph, please use the
	 * createNodes method).
	 *
	 * Will not modify any of the data structures managed by the SocialGraph, and is not recommended
	 * to be used with an instance of a SocialGraph!
	 *
	 * @param numberOfNodes The number of nodes the graph is to be initialized with
	 * @return The node set the graph is to be initialized with
	 */
	private static Set<SNNode> createInitialNodes(int numberOfNodes){
		HashSet<SNNode> nodes = new HashSet<SNNode>(numberOfNodes);
		try {
			return createNodes(nodes, numberOfNodes);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return nodes;
	}

	/**
	 * Method to create a number of nodes and add them to an existing node set
	 *
	 * Will not modify any of the data structures managed by the SocialGraph, and is not recommended
	 * to be used with an instance of a SocialGraph!
	 *
	 * @param nodeSet The set of nodes the new nodes are to be added to
	 * @param numberOfNodes The number of nodes to add to the existing node set
	 * @return The node set to add to the existing node set
	 * @throws IllegalArgumentException Will be thrown when the number of nodes to add is negative
	 */
	private static Set<SNNode> createNodes(Set<SNNode> nodeSet, int numberOfNodes) throws IllegalArgumentException {
		if(numberOfNodes < 0) throw new IllegalArgumentException("The number of nodes to add is negative!!");
		for(int index=nodeSet.size(); index<numberOfNodes; index++){
			nodeSet.add(new SNNode("Node_no_"+index));
		}
		fooLog.info("{} nodes contructed", numberOfNodes);
		return nodeSet;
	}


	public Set<SNNode> getNodes() {
		return this.nodes;
	}

	public Set<SNEdge> getEdges() {
		return this.edges;
	}

	public EnumMap<EDGEMEDIUM, HashMap<SNNode, Set<SNNode>>> getNeighbours() {
		return this.neighbours;
	}

	public HashMap<SNNode, Set<SNNode>> getNeighbours(EDGEMEDIUM edgemedium) {
		return neighbours.get(edgemedium);
	}

	/**
	 * Adds a node to the SocialGraph without introducing any edges involving this node.
	 * Will add the node to the existing data structures
	 *
	 * @param nodeToAdd The node to add to the graph
	 * @throws IllegalArgumentException Will be thrown when the node to add is already part of the SocialGraph
	 */
	public void addIsolatedNode(SNNode nodeToAdd) throws IllegalArgumentException {
		if (nodes.contains(nodeToAdd))
			throw new IllegalArgumentException("Node to add is already part of the SocialGraph");
		else {
			nodes.add(nodeToAdd);
			outgoingEdges.put(nodeToAdd, new HashSet<SNEdge>());
			for (EDGEMEDIUM edgemedium : EDGEMEDIUM.values()) {
				HashMap<SNNode, Set<SNEdge>> currentOutgoingEdges = outgoingEdgesMedium.get(edgemedium);
				currentOutgoingEdges.put(nodeToAdd, new HashSet<SNEdge>());
				outgoingEdgesMedium.put(edgemedium, currentOutgoingEdges);
				HashMap<SNNode, Set<SNNode>> currentNeighbours = neighbours.get(edgemedium);
				currentNeighbours.put(nodeToAdd, new HashSet<SNNode>());
				neighbours.put(edgemedium, currentNeighbours);
			}
		}
	}

	/**
	 * Method to add an edge to the SocialGraph
	 *
	 * @param edgeToAdd The edge to add to the graph
	 */
	public void addEdge(SNEdge edgeToAdd) throws IllegalArgumentException{
		if(edges.contains(edgeToAdd)) throw new IllegalStateException("Edge "+edgeToAdd+" is part of the graph already");
		else if(neighbours.get(edgeToAdd.getMedium()).get(edgeToAdd.getSource()).contains(edgeToAdd.getTarget())) throw new IllegalStateException("There is already an edge of medium "+edgeToAdd.getMedium()+" from node "+edgeToAdd.getSource()+" to node "+edgeToAdd.getTarget());
		edges.add(edgeToAdd);
		outgoingEdges.get(edgeToAdd.getSource()).add(edgeToAdd);
		if (edgeToAdd.getMedium() != null) {
			//if the edge makes a new node a neighbour, add it to the list of neighbours (this should always occur with the current modeling! (v. 1.0)
			if (!neighbours.get(edgeToAdd.getMedium()).get(edgeToAdd.getSource()).contains(edgeToAdd.getTarget())) neighbours.get(edgeToAdd.getMedium()).get(edgeToAdd.getSource()).add(edgeToAdd.getTarget());
			outgoingEdgesMedium.get(edgeToAdd.getMedium()).get(edgeToAdd.getSource()).add(edgeToAdd);
		}
	}

	/**
	 * Method to get all nodes that are neighbours of a nodeOfInterest through any medium
	 *
	 * @param nodeOfInterest The node whose neighbours are to be derived
	 * @return The neighbours of the node of interest (in any medium)
	 */
	public Set<SNNode> getAllMediaNeighbours(SNNode nodeOfInterest){
		Set<SNNode> neighbourSet = new HashSet<>();
		for(EDGEMEDIUM edgemedium : EDGEMEDIUM.values()){
			neighbourSet.addAll(neighbours.get(edgemedium).get(nodeOfInterest));
		}
		return neighbourSet;
	}


	/**
	 * Removes the given edge from the SocialGraph
	 *
	 * @param edgeToRemove The edge to remove from the graph
	 * @throws IllegalArgumentException Will be thrown when the edge is not part of the graph
	 */
	public void removeEdge(SNEdge edgeToRemove) throws IllegalArgumentException{
		if(!edges.contains(edgeToRemove)) throw new IllegalArgumentException("Trying to remove edge that is not in the network!! Referred is: "+edgeToRemove.getEdgeLabel());
		edges.remove(edgeToRemove);
		if(edgeToRemove.getMedium() != null){
			neighbours.get(edgeToRemove.getMedium()).get(edgeToRemove.getSource()).remove(edgeToRemove.getTarget());
			outgoingEdgesMedium.get(edgeToRemove.getMedium()).get(edgeToRemove.getSource()).remove(edgeToRemove);
		}
		outgoingEdges.get(edgeToRemove.getSource()).remove(edgeToRemove);
	}

	/**
	 * Replaces an edge in the SocialGraph with another edge in the graph.
	 * Will throw an error when the replacing edge is already part of the graph or the edgeToReplace is not.
	 *
	 * @param edgeToReplace The edge in the graph that is replaced
	 * @param replacingEdge The edge in the graph that replaces the respective edge
	 * @throws IllegalArgumentException Will be thrown when either the edgeToReplace is not part of the graph or the replacingEdge already is
	 */
	public void replaceEdge(SNEdge edgeToReplace, SNEdge replacingEdge) throws IllegalArgumentException{
		if(!edges.contains(edgeToReplace)) throw new IllegalArgumentException("The edge that is to be replaced is not part of the graph it is to be replaced within!! \nRespective edge is: "+edgeToReplace);
		else if(edges.contains(replacingEdge)) throw new IllegalArgumentException("The edge that replaces respective edge is already part of the graph it replaces the edge in!! \nRespective edge is: "+replacingEdge);
		removeEdge(edgeToReplace);
		addEdge(replacingEdge);
	}

	/**
	 * Method to change the source node of an edge as long as no edge structurally equivalent to the new edge exists yet
	 * (i.e. from same node to same node via the same medium)
	 *
	 * @param edgeToModify The edge that is to be changed
	 * @param newSource The node the new edge is to be pointing from
	 * @throws IllegalArgumentException Will be thrown when the edge that is changed or the new target node is not part of the social graph, or when the edge has no medium associated to it.
	 */
	public void modifyEdgeSource(SNEdge edgeToModify, SNNode newSource) throws IllegalArgumentException{
		if(!edges.contains(edgeToModify)) throw new IllegalArgumentException("Edge that is to be modified ("+edgeToModify+") is not part of the social graph!");
		else if(!nodes.contains(newSource)) throw new IllegalArgumentException("New source node of the edge that is to be modified ("+newSource+") is not part of the social graph!");
		if(edgeToModify.getMedium() != null){
			if(neighbours.get(edgeToModify.getMedium()).get(newSource).contains(edgeToModify.getTarget())) throw new IllegalArgumentException("Tried to set an edge between "+newSource+" and "+edgeToModify.getTarget()+" of medium "+edgeToModify.getMedium()+" which already exists (in modify edge)!!");
			else{
				try {
					replaceEdge(edgeToModify, SNFactory.createEdge(newSource, edgeToModify.getTarget(), edgeToModify.getEdgeWeight(), edgeToModify.getMedium(), edgeToModify.getEdgeLabel()));
				} catch (IllegalArgumentException e) {
					throw e;
				}
			}
		}else throw new IllegalArgumentException("The edge whose source is to be modified does not have any medium assigned to it!!");
	}

	/**
	 * Method to change the target node of an edge as long as no edge structurally equivalent to the new edge exists yet
	 * (i.e. from same node to same node via the same medium)
	 *
	 * @param edgeToModify The edge that is to be changed
	 * @param newTarget The node the new edge is to be pointing towards
	 * @throws IllegalArgumentException Will be thrown when the edge that is changed or the new target node is not part of the social graph, or when the edge has no medium associated to it.
	 */
	public void modifyEdgeTarget(SNEdge edgeToModify, SNNode newTarget) throws IllegalArgumentException{
		if(!edges.contains(edgeToModify)) throw new IllegalArgumentException("Edge that is to be modified ("+edgeToModify+") is not part of the social graph!");
		else if(!nodes.contains(newTarget)) throw new IllegalArgumentException("New target node of the edge that is to be modified ("+newTarget+") is not part of the social graph!");
		if(edgeToModify.getMedium() != null){
			if(neighbours.get(edgeToModify.getMedium()).get(edgeToModify.getSource()).contains(newTarget)) throw new IllegalArgumentException("Tried to set an edge between "+edgeToModify.getSource()+" and "+newTarget+" of medium "+edgeToModify.getMedium()+" which already exists (in modify edge)!!");
			else{
				try {
					replaceEdge(edgeToModify, SNFactory.createEdge(edgeToModify.getSource(), newTarget, edgeToModify.getEdgeWeight(), edgeToModify.getMedium(), edgeToModify.getEdgeLabel()));
				} catch (IllegalArgumentException e) {
					throw e;
				}
			}
		}else throw new IllegalArgumentException("The edge whose source is to be modified does not have any medium assigned to it!!");
	}

	/**
	 * Method to change the target node of an edge as long as no edge structurally equivalent to the new edge exists yet
	 * (i.e. from same node to same node via the same medium)
	 *
	 * @param edgeToModify The edge that is to be changed
	 * @param newSource The node the new edge is to be pointing from
	 * @param newTarget The node the new edge is to be pointing towards
	 * @throws IllegalArgumentException Will be thrown when the edge that is changed or the new target node is not part of the social graph, or when the edge has no medium associated to it.
	 */
	public void modifyEdgeNodes(SNEdge edgeToModify, SNNode newSource, SNNode newTarget) throws IllegalArgumentException{
		if(!edges.contains(edgeToModify)) throw new IllegalArgumentException("Edge that is to be modified ("+edgeToModify+") is not part of the social graph!");
		else if(!nodes.contains(newTarget)) throw new IllegalArgumentException("New target node of the edge that is to be modified ("+newTarget+") is not part of the social graph!");
		else if(!nodes.contains(newSource)) throw new IllegalArgumentException("New source node of the edge that is to be modified ("+newSource+") is not part of the social graph!");
		if(edgeToModify.getMedium() != null){
			if(neighbours.get(edgeToModify.getMedium()).get(newSource).contains(newTarget)) throw new IllegalArgumentException("Tried to set an edge between "+newSource+" and "+newTarget+" of medium "+edgeToModify.getMedium()+" which already exists (in modify edge)!!");
			else{
				try {
					replaceEdge(edgeToModify, SNFactory.createEdge(newSource, newTarget, edgeToModify.getEdgeWeight(), edgeToModify.getMedium(), edgeToModify.getEdgeLabel()));
				} catch (IllegalArgumentException e) {
					throw e;
				}
			}
		}else throw new IllegalArgumentException("The edge whose source is to be modified does not have any medium assigned to it!!");
	}

	/**
	 * Method to change the target node of an edge as long as no edge structurally equivalent to the new edge exists yet
	 * (i.e. from same node to same node via the same medium)
	 *
	 * @param edgeToModify The edge that is to be changed
	 * @param newMedium The medium the edge should be of
	 * @throws IllegalArgumentException Will be thrown when the edge that is changed or the new target node is not part of the social graph, or when the edge has no medium associated to it.
	 */
	public void modifyEdgeMedium(SNEdge edgeToModify, EDGEMEDIUM newMedium) throws IllegalArgumentException{
		if(!edges.contains(edgeToModify)) throw new IllegalArgumentException("Edge that is to be modified ("+edgeToModify+") is not part of the social graph!");
		if(edgeToModify.getMedium() != null){
			if(neighbours.get(newMedium).get(edgeToModify.getSource()).contains(edgeToModify.getTarget())) throw new IllegalArgumentException("Tried to set an edge between "+edgeToModify.getSource()+" and "+edgeToModify.getTarget()+" of medium "+newMedium+" which already exists (in modify edge)!!");
			else{
				try {
					replaceEdge(edgeToModify, SNFactory.createEdge(edgeToModify.getSource(), edgeToModify.getTarget(), edgeToModify.getEdgeWeight(), newMedium, edgeToModify.getEdgeLabel()));
				} catch (IllegalArgumentException e) {
					throw e;
				}
			}
		}else throw new IllegalArgumentException("The edge whose source is to be modified does not have any medium assigned to it!!");
	}

	/**
	 * Retrieves the edge between the sourceNode and the targetNode of the respectiveMedium.
	 * Edge needs to exist; otherwise an exception will be thrown.
	 *
	 * @param sourceNode The node the edge directs from
	 * @param targetNode The node the edge directs towards
	 * @param respectiveMedium The medium of the edge
	 * @return The edge of the respectiveMedium from the sourceNode to the targetNode
	 * @throws IllegalArgumentException Will be thrown when the respective edge doesn't exist
	 */
	public SNEdge retrieveEdge(SNNode sourceNode, SNNode targetNode, EDGEMEDIUM respectiveMedium) throws IllegalArgumentException{
		Set<SNEdge> potentialEdges = outgoingEdgesMedium.get(respectiveMedium).get(sourceNode);
		for(SNEdge currentEdge : potentialEdges){
			if(currentEdge.getTarget().equals(targetNode)) return currentEdge;
		}
		throw new IllegalArgumentException("There is no edge of medium "+respectiveMedium+" between node "+sourceNode+" and node "+targetNode);
	}

	/**
	 * Method to retrieve all edges (of any medium) between the sourceNode and the targetNode.
	 * Result set will be empty when no edges exist between them
	 *
	 * @param sourceNode The node from which the edges stem from
	 * @param targetNode The node the edges are directed to
	 * @return The set of edges of any medium from the sourceNode to the targetNode
	 */
	public Set<SNEdge> retrieveAllEdges(SNNode sourceNode, SNNode targetNode){
		Set<SNEdge> returnSet = new HashSet<>();
		for(SNEdge currentEdge : outgoingEdges.get(sourceNode)){
			if(currentEdge.getTarget().equals(targetNode)) returnSet.add(currentEdge);
		}
		return returnSet;
	}

	/**
	 * Method deriving all nodes the node of interest has an edge towards by the medium of interest
	 *
	 * @param node The node whose neighbour relation is to be derived
	 * @return An EnumMap with all neighbouring nodes of node by medium
	 */
	public EnumMap<EDGEMEDIUM, Set<SNNode>> getNeighbours(SNNode node) {
		EnumMap<EDGEMEDIUM, Set<SNNode>> nodeNeighbours = new EnumMap<EDGEMEDIUM, Set<SNNode>>(EDGEMEDIUM.class);
		for(EDGEMEDIUM currentMedium : EDGEMEDIUM.values()) {
			nodeNeighbours.put(currentMedium, neighbours.get(currentMedium).get(node));
		}
		return nodeNeighbours;
	}

	/**
	 * Method to derive all nodes the node of interest has an edge towards via the specified medium
	 *
	 * @param node The node whose neighbours to derive
	 * @param medium The medium via which the neighbours are to be derived
	 * @return The set of neighbour nodes via the EDGEMEDIUM relation
	 */
	public Set<SNNode> getNeighbours(SNNode node, EDGEMEDIUM medium){
		return neighbours.get(medium).get(node);
	}

	/**
	 * (Network type specific) method to add a number of nodes (and the edges that go with them) to the social graph
	 *
	 * @param sNconfiguration The configuration the social graph is based on
	 * @param nodesToAdd The nodes to be added to the social graph
	 */
	public void addNodes(SNConfiguration sNconfiguration, Set<SNNode> nodesToAdd) throws IllegalArgumentException, UnsupportedOperationException{
		throw new UnsupportedOperationException("Method is not yet implemented. Please ensure that agents are not added after the social network is initialized. Sorry for the inconvenience.");
	}
	//TODO remove Unsupported when supported and make abstract

	/**
	 * (Network type specific) method to generate edges between a set of initial nodes.
	 * Specifies how edges are (initially) created. Generated edges need to be included
	 * in the neighbourhood relations (method only derives WHICH edges to create).
	 *
	 * @param sNConfiguration The configuration of the social graph of interest
	 * @param initialNodes The nodes the social graph should be initialized with
	 * @return A set of edges of the social graph it is initialized with
	 * @throws IllegalArgumentException Will be thrown when the configuration is errornous
	 */
	protected abstract Set<SNEdge> createEdges(SNConfiguration sNConfiguration, Set<SNNode> initialNodes) throws IllegalArgumentException;
}