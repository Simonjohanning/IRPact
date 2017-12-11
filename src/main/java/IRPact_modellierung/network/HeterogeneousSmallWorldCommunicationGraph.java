package IRPact_modellierung.network;

import java.util.*;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.helper.StructureEnricher;
import IRPact_modellierung.simulation.SimulationContainer;

public class HeterogeneousSmallWorldCommunicationGraph extends CommunicationGraph {

	private Map<ConsumerAgentGroup, Integer> consumerGroupZMapping;
	private Map<ConsumerAgentGroup, Double> consumerGroupBetaMapping;

	public HeterogeneousSmallWorldCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes, Set<SNEdge> edges, HashMap<SNNode, Set<SNNode>> neighbours) throws IllegalArgumentException, ClassCastException {
		super(associatedSimulationContainer, sNConfiguration, nodes, edges, neighbours);
		try {
			if(!sNConfiguration.getSocialGraphParameters().containsKey("consumerGroupZMapping")) throw new IllegalArgumentException("No consumerGroupZMapping set for the CommunicationGraph!!");
            else if(!sNConfiguration.getSocialGraphParameters().containsKey("consumerGroupBetaMapping")) throw new IllegalArgumentException("No consumerGroupBetaMapping set for the CommunicationGraph!!");
            else{
                Map<String, ConsumerAgentGroup> cags = StructureEnricher.attachConsumerAgentGroupNames(associatedSimulationContainer.getSimulationConfiguration().getAgentConfiguration().getConsumerAgentGroups());
                consumerGroupZMapping = new HashMap<>();
                consumerGroupBetaMapping = new HashMap<>();
                Map<String, Object> zMap = (Map<String, Object>) sNConfiguration.getSocialGraphParameters().get("consumerGroupZMapping");
                Map<String, Object> betaMap = (Map<String, Object>) sNConfiguration.getSocialGraphParameters().get("consumerGroupBetaMapping");
                for(String cagString : cags.keySet()){
                    if(!zMap.containsKey(cagString)) throw new IllegalArgumentException("No z map provided for ConsumerAgentGroup "+cagString);
                    else if(!betaMap.containsKey(cagString)) throw new IllegalArgumentException("No beta map provided for ConsumerAgentGroup "+cagString);
                    else{
                        consumerGroupZMapping.put(cags.get(cagString), (Integer) zMap.get(cagString));
                        consumerGroupBetaMapping.put(cags.get(cagString), (Double) zMap.get(cagString));
                    }
                }
            }
		} catch (IllegalArgumentException iae) {
			throw iae;
		} catch (ClassCastException cce){
			throw cce;
		}
	}

	public HeterogeneousSmallWorldCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
		super(associatedSimulationContainer, sNConfiguration, initialNodes);
		try {
			if(!sNConfiguration.getSocialGraphParameters().containsKey("consumerGroupZMapping")) throw new IllegalArgumentException("No consumerGroupZMapping set for the CommunicationGraph!!");
			else if(!sNConfiguration.getSocialGraphParameters().containsKey("consumerGroupBetaMapping")) throw new IllegalArgumentException("No consumerGroupBetaMapping set for the CommunicationGraph!!");
			else{
				Map<String, ConsumerAgentGroup> cags = StructureEnricher.attachConsumerAgentGroupNames(associatedSimulationContainer.getSimulationConfiguration().getAgentConfiguration().getConsumerAgentGroups());
				consumerGroupZMapping = new HashMap<>();
				consumerGroupBetaMapping = new HashMap<>();
				Map<String, Object> zMap = (Map<String, Object>) sNConfiguration.getSocialGraphParameters().get("consumerGroupZMapping");
				Map<String, Object> betaMap = (Map<String, Object>) sNConfiguration.getSocialGraphParameters().get("consumerGroupBetaMapping");
				for(String cagString : cags.keySet()){
					if(!zMap.containsKey(cagString)) throw new IllegalArgumentException("No z map provided for ConsumerAgentGroup "+cagString);
					else if(!betaMap.containsKey(cagString)) throw new IllegalArgumentException("No beta map provided for ConsumerAgentGroup "+cagString);
					else{
						consumerGroupZMapping.put(cags.get(cagString), (Integer) zMap.get(cagString));
						consumerGroupBetaMapping.put(cags.get(cagString), (Double) betaMap.get(cagString));
					}
				}
			}
			Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
			for(SNEdge edge : initialEdges){
				addEdge(edge);
			}
		} catch (IllegalArgumentException iae) {
			throw iae;
		} catch (ClassCastException cce){
			throw cce;
		}
	}

	public HeterogeneousSmallWorldCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) {
		super(associatedSimulationContainer, sNConfiguration, numberOfNodes);
		try {
			if(!sNConfiguration.getSocialGraphParameters().containsKey("consumerGroupZMapping")) throw new IllegalArgumentException("No consumerGroupZMapping set for the CommunicationGraph!!");
			else if(!sNConfiguration.getSocialGraphParameters().containsKey("consumerGroupBetaMapping")) throw new IllegalArgumentException("No consumerGroupBetaMapping set for the CommunicationGraph!!");
			else{
				Map<String, ConsumerAgentGroup> cags = StructureEnricher.attachConsumerAgentGroupNames(associatedSimulationContainer.getSimulationConfiguration().getAgentConfiguration().getConsumerAgentGroups());
				consumerGroupZMapping = new HashMap<>();
				consumerGroupBetaMapping = new HashMap<>();
				Map<String, Object> zMap = (Map<String, Object>) sNConfiguration.getSocialGraphParameters().get("consumerGroupZMapping");
				Map<String, Object> betaMap = (Map<String, Object>) sNConfiguration.getSocialGraphParameters().get("consumerGroupBetaMapping");
				for(String cagString : cags.keySet()){
					if(!zMap.containsKey(cagString)) throw new IllegalArgumentException("No z map provided for ConsumerAgentGroup "+cagString);
					else if(!betaMap.containsKey(cagString)) throw new IllegalArgumentException("No beta map provided for ConsumerAgentGroup "+cagString);
					else{
						consumerGroupZMapping.put(cags.get(cagString), (Integer) zMap.get(cagString));
						consumerGroupBetaMapping.put(cags.get(cagString), (Double) zMap.get(cagString));
					}
				}
			}
			Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
			for(SNEdge edge : initialEdges){
				addEdge(edge);
			}
		} catch (IllegalArgumentException iae) {
			throw iae;
		} catch (ClassCastException cce){
			throw cce;
		}
	}

	public Map<ConsumerAgentGroup, Integer> getConsumerGroupZMapping() {
		return this.consumerGroupZMapping;
	}

	public Map<ConsumerAgentGroup, Double> getconsumerGroupBetaMapping() {
		return this.consumerGroupBetaMapping;
	}

	//TODO implement
	public void addNodes(SNConfiguration sNconfiguration, Set<SNNode> nodesToAdd) {

	}

	/**
	 * Edges in a HeterogeneousSmallWorldCommunicationGraph are created in a two step process.
	 * First, edges are creates as in a HeterogeneousRegularCommunicationGraph.
	 * Then, each edge created in the first step will be randomly rewired with a probability depending
	 * on the ConsumerAgentGroup of the agent belonging to the source node of the edge.
	 * To determine the new target of the edge, a random node corresponding to an agent in this group
	 * is chosen unless it is already connected to the source node of the respective edge.
	 * If no such node exists, a new target agent group is searched until such a node is found.
	 *
	 * @param sNConfiguration The configuration of the social graph of interest
	 * @param initialNodes The nodes the social graph should be initialized with
	 * @return The edges of a heterogeneous small world communication graph
	 */
	public Set<SNEdge> createEdges(SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
		Set<SNEdge> returnSet = new HashSet<>();
		Set<SNEdge> preliminaryEdgeSet = HeterogeneousRegularCommunicationGraph.createHeterogeneousRegularCommunicationEdges(associatedSimulationContainer, consumerGroupZMapping, sNConfiguration, initialNodes);
		LazynessHelper.detectDoubleEdges(preliminaryEdgeSet, initialNodes);
		returnSet.addAll(preliminaryEdgeSet);
		Map<SNNode, ConsumerAgent> correspondingConsumerAgent = StructureEnricher.consumerAgentByNode(associatedSimulationContainer.getConsumerAgents());
		Map<ConsumerAgentGroup, Set<SNNode>> sNNodesByCAG = StructureEnricher.deriveNodeByCAG(consumerGroupBetaMapping.keySet(), correspondingConsumerAgent);
		Map<SNNode, Set<SNNode>> preliminaryNeighbours = new HashMap<>();
		for(SNNode currentNode : nodes){
			preliminaryNeighbours.put(currentNode, new HashSet<>());
		}
		for(SNEdge currentEdge : preliminaryEdgeSet){
			preliminaryNeighbours.get(currentEdge.getSource()).add(currentEdge.getTarget());
		}
		//rewire edges
		for (SNEdge currentEdge : preliminaryEdgeSet) {
			ConsumerAgentGroup currentCAG = correspondingConsumerAgent.get(currentEdge.getSource()).getCorrespondingConsumerAgentGroup();
			//stochastically determine if the edge needs to be rewired
			if (Math.random() < consumerGroupBetaMapping.get(currentCAG)) {
				boolean validTargetNodeFound = false;
				SNNode targetNode = null;
				//search for a new target node for the edge that is not yet connected to the source node, proportional to the consumer agent group affinity
				while(!validTargetNodeFound) {
					ConsumerAgentGroup targetGroup = LazynessHelper.chooseTargetCAG(associatedSimulationContainer.getSimulationConfiguration().getAgentConfiguration().getAffinities(), currentCAG);
					Set<SNNode> potentialTargetNodes = new HashSet<>();
					for(SNNode currentNode : sNNodesByCAG.get(targetGroup)){
						potentialTargetNodes.add(currentNode);
					}
					//don't establish edges for node where an edge already exists
					potentialTargetNodes.removeAll(preliminaryNeighbours.get(currentEdge.getSource()));
					if((currentEdge.getSource() == currentEdge.getTarget()) && !sNConfiguration.getTopologyManipulationScheme().isSelfReferential()) potentialTargetNodes.remove(currentEdge.getSource());
					if(!potentialTargetNodes.isEmpty()){
						validTargetNodeFound = true;
						targetNode = LazynessHelper.chooseRandomNode(potentialTargetNodes);
					}
				}
				returnSet.remove(currentEdge);
				preliminaryNeighbours.get(currentEdge.getSource()).remove(currentEdge.getTarget());
				returnSet.add(new SNCommunicationEdge(currentEdge.getSource(), targetNode, currentEdge.getEdgeWeight()));
				preliminaryNeighbours.get(currentEdge.getSource()).add(targetNode);
			}else{
				preliminaryNeighbours.get(currentEdge.getSource()).add(currentEdge.getTarget());
			}
		}
		return returnSet;
	}
}