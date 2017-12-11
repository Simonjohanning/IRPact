package IRPact_modellierung.network;

/**
 * The SNCommunicationEdge is an edge in the social graph for the communication medium,
 * i.e. an edge over which communications between ConsumerAgents take place.
 *
 * @author Simon Johanning
 */
public class SNCommunicationEdge extends SNEdge{

    /**
     * Creates a directed edge from source to target of the COMMUNICATION-medium with a given edgeWeight.
     * The edge thus is used for communicative interactions between the source and target node of intensity edgeWeight.
     *
     * @param source The ConsumerAgent from whom the interaction stems
     * @param target The ConsumerAgent addressed by the interaction
     * @param edgeWeight The strength of the interaction described by the edge
     * @param edgeLabel A human readible label for the interaction
     */
    public SNCommunicationEdge(SNNode source, SNNode target, double edgeWeight, String edgeLabel){
        super(source, target, edgeWeight, edgeLabel, SocialGraph.EDGEMEDIUM.COMMUNICATION);
    }

    /**
     * Creates a directed edge from source to target of the COMMUNICATION-medium with a given edgeWeight.
     * The edge thus is used for communicative interactions between the source and target node of intensity edgeWeight.
     *
     * @param source The ConsumerAgent from whom the interaction stems
     * @param target The ConsumerAgent addressed by the interaction
     * @param edgeWeight The strength of the interaction described by the edge
     */
    public SNCommunicationEdge(SNNode source, SNNode target, double edgeWeight) {
        super(source, target, edgeWeight, SocialGraph.EDGEMEDIUM.COMMUNICATION);
    }
}
