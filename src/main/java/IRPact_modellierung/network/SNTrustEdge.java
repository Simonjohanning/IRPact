package IRPact_modellierung.network;

/**
 * A trust edge is an edge within a SocialGraph that formalizes the trust relation between
 * two ConsumerAgents (source nodes trusts target node), and is thus an edge associated with the medium 'TRUST'.
 * The strength of the trust is captured by the edge weight.
 *
 * @author Simon Johanning
 */
public class SNTrustEdge extends SNEdge{

    /**
     * Creates a directed edge from source to target of the TRUST-medium with a given edgeWeight.
     * The edge thus is used for trust-based interactions between the source and target node of intensity edgeWeight.
     *
     * @param source The ConsumerAgent from whom the interaction stems
     * @param target The ConsumerAgent addressed by the interaction
     * @param edgeWeight The strength of the interaction described by the edge
     * @param edgeLabel A human readible label for the interaction
     */
    public SNTrustEdge(SNNode source, SNNode target, double edgeWeight, String edgeLabel) {
        super(source, target, edgeWeight, edgeLabel, SocialGraph.EDGEMEDIUM.TRUST);
    }

    /**
     * Creates a directed edge from source to target of the TRUST-medium with a given edgeWeight.
     * The edge thus is used for trust-based interactions between the source and target node of intensity edgeWeight.
     *
     * @param source The ConsumerAgent from whom the interaction stems
     * @param target The ConsumerAgent addressed by the interaction
     * @param edgeWeight The strength of the interaction described by the edge
     */
    public SNTrustEdge(SNNode source, SNNode target, double edgeWeight) {
        super(source, target, edgeWeight, SocialGraph.EDGEMEDIUM.TRUST);
    }
}
