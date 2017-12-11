package IRPact_modellierung.network;

/**
 * A SocialNetwork describes the structure of interaction between ConsumerAgents.
 * It is characterized through the SocialGraph, a directed graph with ConsumerAgents as nodes and
 * consumer agent interactions as edges, as well as (implementations) of schemes that describes
 * how the SocialGraph changes over time, namely the EdgeWeightScheme and the TopologyManipulationScheme.
 *
 * @author Simon Johanning
 */
public class SocialNetwork {

    SocialGraph socialGraph;
    EdgeWeightManipulationScheme edgeWeightManipulationScheme;
    TopologyManipulationScheme topologyManipulationScheme;

    /**
     * Constructs the SocialNetwork linking together the SocialGraph and the schemes describing its dynamics
     *
     * @param socialGraph The graph depicting the structure of interaction between the respective ConsumerAgents
     * @param edgeWeightManipulationScheme The scheme governing how the strength of the interaction (i.e. edge weights) change over time
     * @param topologyManipulationScheme The scheme describing how the topology of the graph (i.e. existence of edges between ConsumerAgents) changes over time
     */
    public SocialNetwork(SocialGraph socialGraph, EdgeWeightManipulationScheme edgeWeightManipulationScheme, TopologyManipulationScheme topologyManipulationScheme){
        this.socialGraph = socialGraph;
        this.edgeWeightManipulationScheme = edgeWeightManipulationScheme;
        this.topologyManipulationScheme = topologyManipulationScheme;
    }

    public SocialGraph getSocialGraph() {
        return socialGraph;
    }

    public EdgeWeightManipulationScheme getEdgeWeightManipulationScheme() {
        return edgeWeightManipulationScheme;
    }

    public TopologyManipulationScheme getTopologyManipulationScheme() {
        return topologyManipulationScheme;
    }
}
