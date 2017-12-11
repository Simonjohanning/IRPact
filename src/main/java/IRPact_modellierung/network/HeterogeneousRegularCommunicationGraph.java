package IRPact_modellierung.network;

import java.util.*;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.helper.FilterHelper;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.helper.ValueConversionHelper;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;

/**
 * A HeterogeneousRegularCommunicationGraph is a communication graph, whose nodes correspond to
 * agents of different agent groups, which tries to be as regular as possible.
 * Since agent groups may differ by their out-degree, and the agent group a node establishes a communication link to
 * is chosen stochastically, the in-degree of the corresponding agent groups differs as well.
 * Within an agent group, the out-degree however is homogeneous, and the in-degree differs by at most one (is balance as well as possible).
 *
 * @author Simon Johanning
 */
public class HeterogeneousRegularCommunicationGraph extends CommunicationGraph {

	private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	private Map<ConsumerAgentGroup, Integer> consumerGroupZMapping;

	public HeterogeneousRegularCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes, Set<SNEdge> edges, HashMap<SNNode, Set<SNNode>> neighbours) {
		super(associatedSimulationContainer, sNConfiguration, nodes, edges, neighbours);
		consumerGroupZMapping = ValueConversionHelper.deriveCAGIntegerMap(getAssociatedSimulationContainer().getSimulationConfiguration().getAgentConfiguration().getConsumerAgentGroups(), (HashMap<String, Integer>) sNConfiguration.getSocialGraphParameters().get("consumerGroupZMapping"), "the out-degree map of the social graph");
	}

	public HeterogeneousRegularCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
		super(associatedSimulationContainer, sNConfiguration, initialNodes);
		consumerGroupZMapping = ValueConversionHelper.deriveCAGIntegerMap(getAssociatedSimulationContainer().getSimulationConfiguration().getAgentConfiguration().getConsumerAgentGroups(), (HashMap<String, Integer>) sNConfiguration.getSocialGraphParameters().get("consumerGroupZMapping"), "the out-degree map of the social graph");
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}
	}

	public HeterogeneousRegularCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) {
		super(associatedSimulationContainer, sNConfiguration, numberOfNodes);
		consumerGroupZMapping = ValueConversionHelper.deriveCAGIntegerMap(getAssociatedSimulationContainer().getSimulationConfiguration().getAgentConfiguration().getConsumerAgentGroups(), (HashMap<String, Integer>) sNConfiguration.getSocialGraphParameters().get("consumerGroupZMapping"), "the out-degree map of the social graph");
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}
	}

	public Map<ConsumerAgentGroup, Integer> getConsumerGroupZMapping() {
		return this.consumerGroupZMapping;
	}


	/**
	 * Method to create the edges of a newly instantiated graph.
	 * For each ConsumerAgentGroup this will create edges corresponding to the number of neighbours of that ConsumerAgentGroup,
	 * as described by the ConsumerGroupZMapping.
	 * Edges will be directed to a least connected node in a (stochastically chosen, proportional to the respective ConsumerAgentGroup affinity)
	 * target ConsumerAgentGroup.
	 * Through this homogeneity of degree is achieved (as much as possible), while the degrees between the groups might be quite heterogeneous.
	 *
	 * @param sNConfiguration The configuration of the social graph of interest
	 * @param initialNodes The nodes the social graph should be initialized with
	 * @return The set of edges according to a heterogeneous regular graph
	 */
	public Set<SNEdge> createEdges(SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
		return createHeterogeneousRegularCommunicationEdges(associatedSimulationContainer, consumerGroupZMapping, sNConfiguration, initialNodes);
	}

	/**
	 * Helper method to provide the functionality to create the respective edges for a heterogeneous regular network
	 * to other methods by making the method static.
	 *
	 * @param associatedSimulationContainer The container the graph is used in
	 * @param consumerGroupZMapping The map of how many neighbours to create for each ConsumerAgentGroup
	 * @param sNConfiguration The configuration of the social graph of interest
	 * @param initialNodes The nodes the social graph should be initialized with
	 * @return The set of edges according to a heterogeneous regular graph
	 */
	public static Set<SNEdge> createHeterogeneousRegularCommunicationEdges(SimulationContainer associatedSimulationContainer, Map<ConsumerAgentGroup, Integer> consumerGroupZMapping, SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
		Set<SNEdge> returnSet = new HashSet<>();
		Map<SNNode, Set<SNNode>> neighbours = new HashMap<>();
		for(SNNode currentNode : initialNodes){
			neighbours.put(currentNode, new HashSet<>());
		}
		//data structures to access the node of interest
		HashMap<ConsumerAgentGroup, HashMap<Integer, Set<SNNode>>> nodeInDegree = new HashMap<ConsumerAgentGroup, HashMap<Integer, Set<SNNode>>>(associatedSimulationContainer.getSimulationConfiguration().getAgentConfiguration().getConsumerAgentGroups().size());
		HashMap<ConsumerAgentGroup, Set<SNNode>> sNNodesByCAG = new HashMap<ConsumerAgentGroup, Set<SNNode>>();
		//Set up nodeInDegree to determine the node to connect to (the one with the smallest degree)
		for(ConsumerAgentGroup cag : associatedSimulationContainer.getSimulationConfiguration().getAgentConfiguration().getConsumerAgentGroups()){
			HashSet<SNNode> sNNodesOfCAG = new HashSet<SNNode>();
			sNNodesByCAG.put(cag, sNNodesOfCAG);
			HashMap<Integer, Set<SNNode>> nodesByInDegree = new HashMap<Integer, Set<SNNode>>();
			nodesByInDegree.put(0, new HashSet<SNNode>());
			nodeInDegree.put(cag, nodesByInDegree);
		}
		//assign the nodes to the data structures
		for(ConsumerAgent ca : associatedSimulationContainer.getConsumerAgents()){
			sNNodesByCAG.get(ca.getCorrespondingConsumerAgentGroup()).add(ca.getCorrespondingNodeInSN());
			Set<SNNode> zeroNodes = nodeInDegree.get(ca.getCorrespondingConsumerAgentGroup()).get(0);
			zeroNodes.add(ca.getCorrespondingNodeInSN());
			fooLog.debug("Adding node {} to nodeInDegreeZeroMap {}", ca.getCorrespondingNodeInSN(), zeroNodes);
			nodeInDegree.get(ca.getCorrespondingConsumerAgentGroup()).put(0, zeroNodes);
		}
		//add nodes evenly for all CAGs and the nodes corresponding to their agents
		for(ConsumerAgentGroup currentCag : sNNodesByCAG.keySet()) {
			for (SNNode currentNode : sNNodesByCAG.get(currentCag)) {
				//add z edges for the current node to a node of the target cag trying to even out the out-degrees as much as possible
				for (int edgeIndex = 0; edgeIndex < consumerGroupZMapping.get(currentCag); edgeIndex++) {
					boolean validNodeFound = false;
					boolean outputted = false;
					SNNode targetNode = null;
					ConsumerAgentGroup targetGroup = null;
					while(!validNodeFound) {
						//the CAG the target node belongs to is chosen stochastically based on the agent group affinities
						targetGroup = LazynessHelper.chooseTargetCAG(associatedSimulationContainer.getSimulationConfiguration().getAgentConfiguration().getAffinities(), currentCag);
						if(!outputted) fooLog.debug("Chose targetGroup {} with nodeInDegreeMap {}", targetGroup.getGroupName(), nodeInDegree.get(targetGroup));
						fooLog.debug("Nodes of the group are {}", sNNodesByCAG.get(targetGroup));
						if(nodeInDegree.get(targetGroup).keySet().isEmpty()) fooLog.info("nodeInDegree keyset is empty!!");
						HashMap<Integer, Set<SNNode>> potentialTargetNodes = FilterHelper.filterIntegerNodeMap(nodeInDegree.get(targetGroup), neighbours.get(currentNode));
						if(!outputted) fooLog.debug("After filtering the neighbours {}, the potentialTargetNodes are {}", neighbours.get(currentNode), potentialTargetNodes);
						//and is the least connected node within the group unless it is oneself and self-loops are prohibited
						targetNode = LazynessHelper.getLeastConnectedNodeUnsafe(potentialTargetNodes, sNConfiguration.getTopologyManipulationScheme().isSelfReferential(), currentNode);
						validNodeFound = (targetNode != null);
						outputted = true;
					}
					if((targetGroup == null) || (targetNode == null)) throw new IllegalArgumentException("No target group or target node found for the node "+currentNode);
					//move node to group with a degree higher by 1
					HashMap<Integer, Set<SNNode>> nodeDegreeMap = nodeInDegree.get(targetGroup);
					nodeInDegree.put(targetGroup, increaseNodeDegree(nodeDegreeMap, targetNode));
					fooLog.debug("edge: {}, {}, {}\n\n", currentNode, targetNode, sNConfiguration.getEdgeWeightMappingScheme().weighEdge(currentNode, targetNode, EDGEMEDIUM.COMMUNICATION));
					neighbours.get(currentNode).add(targetNode);
					returnSet.add(new SNCommunicationEdge(currentNode, targetNode, sNConfiguration.getEdgeWeightMappingScheme().weighEdge(currentNode, targetNode, EDGEMEDIUM.COMMUNICATION)));
				}
			}
		}
		fooLog.debug("nodeInDegreeMap is {}.\n In summary this is as follows:\n",nodeInDegree);
//		printNodeInDegreeMap(nodeInDegree);
		return returnSet;
	}

	private static void printNodeInDegreeMap(HashMap<ConsumerAgentGroup, HashMap<Integer, Set<SNNode>>> nodeInDegree) {
		for(ConsumerAgentGroup cag : nodeInDegree.keySet()){
			System.out.println("cag "+cag.getGroupName()+" is as follows:");
			for(Integer nodeDegree : nodeInDegree.get(cag).keySet()){
				System.out.println(nodeInDegree.get(cag).get(nodeDegree).size()+" nodes of degree "+nodeDegree);
			}
		}
	}

	/**
	 * Helper method to process the degree bookkeeping method used in createEdges.
	 * Will remove the targetNode from the set corresponding to its current degree
	 * and add it to the set corresponding to the new degree (old+1).
	 * Will add the respective key if not yet present.
	 *
	 * @param originalNodeDegreeMap The current map of node degrees and nodes of that degree
	 * @param targetNode The node to be moved 'one key up'
	 * @return The nodeDegreeMap with target node in the value corresponding to the augmented one.
	 */
	private static HashMap<Integer, Set<SNNode>> increaseNodeDegree(HashMap<Integer, Set<SNNode>> originalNodeDegreeMap, SNNode targetNode) {
		HashMap<Integer, Set<SNNode>> nodeDegreeMap = new HashMap<>();
		for(Integer currentInt : originalNodeDegreeMap.keySet()){
			nodeDegreeMap.put(currentInt, new HashSet<>());
			for(SNNode currentNode : originalNodeDegreeMap.get(currentInt)){
				nodeDegreeMap.get(currentInt).add(currentNode);
			}
		}
		Set<SNNode> allNodes = new HashSet<>();
		ArrayList<Integer> sortedKeys = new ArrayList<Integer>(nodeDegreeMap.keySet());
		fooLog.debug("keyset is {} and sortedKeys is {}",nodeDegreeMap.keySet(), sortedKeys);
		Collections.sort(sortedKeys);
		ListIterator<Integer> listIterator = sortedKeys.listIterator();
//		fooLog.info("Old nodeDegreeMap size {}",nodeDegreeMap.size());
		while (listIterator.hasNext()) {
			int degree = listIterator.next();
//			allNodes.addAll(nodeDegreeMap.get(degree));
//			fooLog.info("degree is {}",degree);
			if (nodeDegreeMap.get(degree).contains(targetNode)) {
				nodeDegreeMap.get(degree).remove(targetNode);
				if(nodeDegreeMap.keySet().contains(degree+1)){
//					fooLog.info("Former nodeIndex+1 set is {}",nodeDegreeMap.get(degree+1));
//					nodeDegreeMap.get(degree+1).add(targetNode);
//					fooLog.info("Current nodeIndex+1 set is {}",nodeDegreeMap.get(degree+1));
					Set<SNNode> newNodeSet = new HashSet<>();
					for(SNNode currentNode : nodeDegreeMap.get(degree+1)){
						newNodeSet.add(currentNode);
					}
					newNodeSet.add(targetNode);
					nodeDegreeMap.put(degree+1, newNodeSet);
				}
				else {
					Set<SNNode> newDegreeSet = new HashSet<>();
					newDegreeSet.add(targetNode);
					nodeDegreeMap.put(degree+1, newDegreeSet);
					fooLog.debug("Increasing the nodeDegreeMap");
				}
				break;
			}
		}
//		if(nodeIndex == -1) fooLog.info("nodeIndex negative");
//		if(!allNodes.contains(targetNode)) throw new IllegalArgumentException("Target node is in none of the degree sets!");
//		returnMap.get(nodeIndex).remove(targetNode);
//		fooLog.info("returnMap keyset is {} nodeIndex+1 is {}",returnMap.keySet(), nodeIndex+1);

		return nodeDegreeMap;
	}
}