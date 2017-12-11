package IRPact_modellierung.network;

import IRPact_modellierung.simulation.SimulationContainer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

//TODO implement
public class HeterogeneousNewtonianInversePowerlawCommunicationGraph extends HeterogeneousScalefreeCommunicationGraph {

    public HeterogeneousNewtonianInversePowerlawCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes, Set<SNEdge> edges, HashMap<SNNode, Set<SNNode>> neighbours) {
        super(associatedSimulationContainer, sNConfiguration, nodes, edges, neighbours);
    }

    public HeterogeneousNewtonianInversePowerlawCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
        super(associatedSimulationContainer, sNConfiguration, initialNodes);
        Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
        for(SNEdge edge : initialEdges){
            addEdge(edge);
        }
    }

    public HeterogeneousNewtonianInversePowerlawCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) {
        super(associatedSimulationContainer, sNConfiguration, numberOfNodes);
        Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
        for(SNEdge edge : initialEdges){
            addEdge(edge);
        }
    }

    public void addNodes(SNConfiguration sNconfiguration, Set<SNNode> nodesToAdd) {

    }

    public Set<SNEdge> createEdges(SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
        Set<SNEdge> returnSet = new HashSet<>();
        return returnSet;
    }
}