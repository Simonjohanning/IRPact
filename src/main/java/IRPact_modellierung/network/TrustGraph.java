package IRPact_modellierung.network;

import IRPact_modellierung.simulation.SimulationContainer;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Set;

/**
 * A TrustGraph is a SocialGraph which formalizes the semantics for trust between ConsumerAgents.
 * Every edge represents the trust of one ConsumerAgent (source) to another (target), with the
 * strength of trust given by the edge weight.
 * It is thus subset of the social graph, where every edge is of medium 'TRUST'.
 *
 * @author Simon Johanning
 */
public abstract class TrustGraph extends SocialGraph{

    public TrustGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes, Set<SNEdge> edges, HashMap<SNNode, Set<SNNode>> neighbours) {
        super(associatedSimulationContainer, sNConfiguration, nodes, edges, embedNeighbourMap(neighbours));
    }

    private static EnumMap<EDGEMEDIUM, HashMap<SNNode, Set<SNNode>>> embedNeighbourMap(HashMap<SNNode, Set<SNNode>> neighbours) {
        EnumMap<EDGEMEDIUM, HashMap<SNNode, Set<SNNode>>> neighbourMap = new EnumMap<EDGEMEDIUM, HashMap<SNNode, Set<SNNode>>>(EDGEMEDIUM.class);
        neighbourMap.put(EDGEMEDIUM.TRUST, neighbours);
        return neighbourMap;
    }

    public TrustGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
        super(associatedSimulationContainer, sNConfiguration, initialNodes);
    }

    public TrustGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) throws IllegalArgumentException {
        super(associatedSimulationContainer, sNConfiguration, numberOfNodes);
    }

    public HashMap<SNNode, Set<SNNode>> getTrustNeighbours() {
        return getNeighbours(EDGEMEDIUM.TRUST);
    }

    public Set<SNNode> getTrustNeighbours(SNNode nodeOfInterest) {
        return getNeighbours(nodeOfInterest, EDGEMEDIUM.TRUST);
    }
}
