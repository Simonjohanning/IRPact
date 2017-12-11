package IRPact_modellierung.network;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.simulation.SimulationContainer;

public class SpatialHeterogeneousSmallWorldSN extends CommunicationGraph {

	private Map<ConsumerAgentGroup, Integer> consumerGroupZMapping;
	private Map<ConsumerAgentGroup, Double> consumerGroupBetaMapping;


	public SpatialHeterogeneousSmallWorldSN(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes, Set<SNEdge> edges, HashMap<SNNode, Set<SNNode>> neighbours) {
		super(associatedSimulationContainer, sNConfiguration, nodes, edges, neighbours);
	}

	public SpatialHeterogeneousSmallWorldSN(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
		super(associatedSimulationContainer, sNConfiguration, initialNodes);
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}
	}

	public SpatialHeterogeneousSmallWorldSN(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) {
		super(associatedSimulationContainer, sNConfiguration, numberOfNodes);
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}
	}

	public Map<ConsumerAgentGroup, Integer> getConsumerGroupZMapping() {
		return this.consumerGroupZMapping;
	}

	public Map<ConsumerAgentGroup, Double> getConsumerGroupBetaMapping() {
		return this.consumerGroupBetaMapping;
	}

	//TODO implement
	public void addNodes(SNConfiguration sNconfiguration, Set<SNNode> nodesToAdd) {

	}

	public Set<SNEdge> createEdges(SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
		Set<SNEdge> returnSet = new HashSet<>();
		return returnSet;
	}
}