package IRPact_modellierung.network;

import IRPact_modellierung.simulation.SimulationContainer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class HeterogeneousMannaSenYookSN extends HeterogeneousScalefreeCommunicationGraph {

	private double alpha;
	private double beta;

	public HeterogeneousMannaSenYookSN(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes, Set<SNEdge> edges, HashMap<SNNode, Set<SNNode>> neighbours) {
		super(associatedSimulationContainer, sNConfiguration, nodes, edges, neighbours);
	}

	public HeterogeneousMannaSenYookSN(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
		super(associatedSimulationContainer, sNConfiguration, initialNodes);
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}
	}

	public HeterogeneousMannaSenYookSN(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) {
		super(associatedSimulationContainer, sNConfiguration, numberOfNodes);
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}
	}

	public double getAlpha() {
		return this.alpha;
	}

	public double getBeta() {
		return this.beta;
	}


	//TODO implement
	public void addNodes(SNConfiguration sNconfiguration, Set<SNNode> nodesToAdd) {

	}

	public Set<SNEdge> createEdges(SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
		Set<SNEdge> returnSet = new HashSet<>();
		return returnSet;
	}
}