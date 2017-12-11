package IRPact_modellierung.network;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExponentialDecayCommunicationGraph extends ScalefreeCommunicationGraph {

	private double b;

	public ExponentialDecayCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes, Set<SNEdge> edges, HashMap<SNNode, Set<SNNode>> neighbours) {
		super(associatedSimulationContainer, sNConfiguration, nodes, edges, neighbours);
		if(!sNConfiguration.getSocialGraphParameters().containsKey("b")) throw new IllegalArgumentException("A scalefree communication graph was chosen, but no parameter noInitSeeds given!!");
		else{
			b = (Double) sNConfiguration.getSocialGraphParameters().get("b");
		}
	}

	public ExponentialDecayCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
		super(associatedSimulationContainer, sNConfiguration, initialNodes);
		if(!sNConfiguration.getSocialGraphParameters().containsKey("b")) throw new IllegalArgumentException("A scalefree communication graph was chosen, but no parameter noInitSeeds given!!");
		else{
			b = (Double) sNConfiguration.getSocialGraphParameters().get("b");
		}
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}
	}

	public ExponentialDecayCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) {
		super(associatedSimulationContainer, sNConfiguration, numberOfNodes);
		if(!sNConfiguration.getSocialGraphParameters().containsKey("b")) throw new IllegalArgumentException("A scalefree communication graph was chosen, but no parameter noInitSeeds given!!");
		else{
			b = (Double) sNConfiguration.getSocialGraphParameters().get("b");
		}
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}
	}

	public double getB() {
		return this.b;
	}

	//TODO implement
	public void addNodes(SNConfiguration sNconfiguration, Set<SNNode> nodesToAdd) {

	}

	protected double calculateProbabilityConnected(SNNode sourceNode, SNNode targetNode, Map<SNNode, Integer> nodeDegree){
		ConsumerAgent sourceAgent = associatedSimulationContainer.getsNMap().get(sourceNode);
		ConsumerAgent targetAgent = associatedSimulationContainer.getsNMap().get(targetNode);
		double distance = associatedSimulationContainer.getSpatialModel().measureDistance(sourceAgent.getCoordinates(), targetAgent.getCoordinates());
		return Math.pow(Math.E, -b * distance);
	}

}