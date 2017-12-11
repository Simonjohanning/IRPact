package IRPact_modellierung.network;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.simulation.SimulationContainer;

public abstract class SpatialHeterogeneousScalefreeSN extends ScalefreeCommunicationGraph {

	private Map<ConsumerAgentGroup, Integer> consumerGroupNoConnectedEdgesMapping;
	private int noInitSeeds;

	public SpatialHeterogeneousScalefreeSN(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes, Set<SNEdge> edges, HashMap<SNNode, Set<SNNode>> neighbours) {
		super(associatedSimulationContainer, sNConfiguration, nodes, edges, neighbours);
	}

	public SpatialHeterogeneousScalefreeSN(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
		super(associatedSimulationContainer, sNConfiguration, initialNodes);
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}
	}

	public SpatialHeterogeneousScalefreeSN(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) {
		super(associatedSimulationContainer, sNConfiguration, numberOfNodes);
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}
	}

	public Map<ConsumerAgentGroup, Integer> getConsumerGroupNoConnectedEdgesMapping() {
		return this.consumerGroupNoConnectedEdgesMapping;
	}

	public int getNoInitSeeds() {
		return this.noInitSeeds;
	}

}