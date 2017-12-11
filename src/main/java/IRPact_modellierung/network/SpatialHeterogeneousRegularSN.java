package IRPact_modellierung.network;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.simulation.SimulationContainer;

public class SpatialHeterogeneousRegularSN extends CommunicationGraph {

	private Map<ConsumerAgentGroup, Integer> consumerGroupZMapping;

	public SpatialHeterogeneousRegularSN(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes, Set<SNEdge> edges, HashMap<SNNode, Set<SNNode>> neighbours) {
		super(associatedSimulationContainer, sNConfiguration, nodes, edges, neighbours);
	}

	public SpatialHeterogeneousRegularSN(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
		super(associatedSimulationContainer, sNConfiguration, initialNodes);
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}
	}

	public SpatialHeterogeneousRegularSN(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) {
		super(associatedSimulationContainer, sNConfiguration, numberOfNodes);
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}
	}

	public Map<ConsumerAgentGroup, Integer> getConsumerGroupZMapping() {
		return this.consumerGroupZMapping;
	}


	//TODO implement
	public void addNodes(SNConfiguration sNconfiguration, Set<SNNode> nodesToAdd) {

	}

	public Set<SNEdge> createEdges(SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
		Set<SNEdge> returnSet = new HashSet<>();
		return returnSet;
	}
}