package IRPact_modellierung.network;

import IRPact_modellierung.simulation.SimulationContainer;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Set;

/**
 * A CommunicationGraph is a SocialGraph which formalizes the semantics for communicative activities between ConsumerAgents.
 * Every edge represents a relationship between one ConsumerAgent (source) to another (target) in which
 * the source ConsumerAgent transmits messages to the target ConsumerAgent, with the
 * strength of the communicative link given by the edge weight.
 * It is thus subset of the social graph, where every edge is of medium 'COMMUNICATION'.
 *
 * @author Simon Johanning
 *
 */
public abstract class CommunicationGraph extends SocialGraph{

    public CommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes, Set<SNEdge> edges, HashMap<SNNode, Set<SNNode>> neighbours) {
        super(associatedSimulationContainer, sNConfiguration, nodes, edges, embedNeighbourMap(neighbours));
    }

    private static EnumMap<EDGEMEDIUM, HashMap<SNNode, Set<SNNode>>> embedNeighbourMap(HashMap<SNNode, Set<SNNode>> neighbours) {
        EnumMap<EDGEMEDIUM, HashMap<SNNode, Set<SNNode>>> neighbourMap = new EnumMap<EDGEMEDIUM, HashMap<SNNode, Set<SNNode>>>(EDGEMEDIUM.class);
        neighbourMap.put(EDGEMEDIUM.COMMUNICATION, neighbours);
        return neighbourMap;
    }

    public CommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
        super(associatedSimulationContainer, sNConfiguration, initialNodes);
    }

    public CommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) throws IllegalArgumentException {
        super(associatedSimulationContainer, sNConfiguration, numberOfNodes);
    }

    /**
     * Method to derive the neighbour map in a network where all edges are of communication medium
     *
     * @return The neighbour map
     */
    public HashMap<SNNode, Set<SNNode>> getCommunicationNeighbours() {
        return getNeighbours(EDGEMEDIUM.COMMUNICATION);
    }

    /**
     * Method to derive the neighbours of a given node in a network where all edges are of communication medium
     *
     * @param nodeOfInterest The node whose neighbours are to be determined
     * @return The neighbour map
     */
    public Set<SNNode> getCommunicationNeighbours(SNNode nodeOfInterest) {
        return getNeighbours(nodeOfInterest, EDGEMEDIUM.COMMUNICATION);
    }
}
