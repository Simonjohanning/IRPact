package IRPact_modellierung.network;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NewtonianInversePowerlawCommunicationGraph extends ScalefreeCommunicationGraph {

    public NewtonianInversePowerlawCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes, Set<SNEdge> edges, HashMap<SNNode, Set<SNNode>> neighbours) {
        super(associatedSimulationContainer, sNConfiguration, nodes, edges, neighbours);
    }

    public NewtonianInversePowerlawCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
        super(associatedSimulationContainer, sNConfiguration, initialNodes);
        Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
        for(SNEdge edge : initialEdges){
            addEdge(edge);
        }
    }

    public NewtonianInversePowerlawCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) {
        super(associatedSimulationContainer, sNConfiguration, numberOfNodes);
        Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
        for(SNEdge edge : initialEdges){
            addEdge(edge);
        }
    }

    //TODO implement
    public void addNodes(SNConfiguration sNconfiguration, Set<SNNode> nodesToAdd) {

    }

    protected double calculateProbabilityConnected(SNNode sourceNode, SNNode targetNode, Map<SNNode, Integer> nodeDegreeMap){
        ConsumerAgent sourceAgent = associatedSimulationContainer.getsNMap().get(sourceNode);
        ConsumerAgent targetAgent = associatedSimulationContainer.getsNMap().get(targetNode);
        double distance = associatedSimulationContainer.getSpatialModel().measureDistance(sourceAgent.getCoordinates(), targetAgent.getCoordinates());
        return 1 / Math.pow(distance, 2);
    }

}