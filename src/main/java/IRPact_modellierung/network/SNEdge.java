package IRPact_modellierung.network;

/**
 * A SNEdge represents an edge within a social network (or more precise in a SocialGraph).
 * It is a directed edge of a certain medium which formalizes the semantics of an interaction between ConsumerAgents
 *
 * @author Simon Johanning
 */
public abstract class SNEdge {

	private SNNode source;
	private SNNode target;
	private double edgeWeight;
	private String edgeLabel;
	private SocialGraph.EDGEMEDIUM medium;

	/**
	 * Creates a directed edge from source to target of the given medium with a given edgeWeight and a human-readible label
	 *
	 * @param source The ConsumerAgent from whom the interaction stems
	 * @param target The ConsumerAgent addressed by the interaction
	 * @param edgeWeight The strength of the interaction described by the edge
	 * @param edgeLabel A human readible label for the interaction
	 * @param medium The medium, as semantic context, of the interaction
	 */
	public SNEdge(SNNode source, SNNode target, double edgeWeight, String edgeLabel, SocialGraph.EDGEMEDIUM medium) {
		this.source = source;
		this.target = target;
		this.edgeWeight = edgeWeight;
		this.edgeLabel = edgeLabel;
		this.medium = medium;
	}

	/**
	 * Creates a directed edge from source to target of the given medium with a given edgeWeight.
	 * The label for this edge is constructed based on the information provided.
	 *
	 * @param source The ConsumerAgent from whom the interaction stems
	 * @param target The ConsumerAgent addressed by the interaction
	 * @param edgeWeight The strength of the interaction described by the edge
	 * @param medium The medium, as semantic context, of the interaction
	 */
	public SNEdge(SNNode source, SNNode target, double edgeWeight, SocialGraph.EDGEMEDIUM medium) {
		this.source = source;
		this.target = target;
		this.edgeWeight = edgeWeight;
		this.edgeLabel = ("Edge from "+source.getNodeLabel()+" to "+target.getNodeLabel()+" of medium "+medium);
		this.medium = medium;
	}

	/**
	 * Creates a directed edge from source to target of the given medium with a given edgeWeight.
	 * With this constructor, the edge is not assigned a medium (i.e. null), and should thus
	 * be used carefully, since its semantics are not clear.
	 *
	 * @param source The ConsumerAgent from whom the interaction stems
	 * @param target The ConsumerAgent addressed by the interaction
	 * @param edgeWeight The strength of the interaction described by the edge
	 * @param edgeLabel A human readible label for the interaction
	 */
	public SNEdge(SNNode source, SNNode target, double edgeWeight, String edgeLabel) {
		this.source = source;
		this.target = target;
		this.edgeWeight = edgeWeight;
		this.edgeLabel = edgeLabel;
		this.medium = null;
	}

	/**
	 * Creates a directed edge from source to target of the given medium with a given edgeWeight.
	 * The label for this edge is constructed based on the information provided.
	 * With this constructor, the edge is not assigned a medium (i.e. null), and should thus
	 * be used carefully, since its semantics are not clear.
	 *
	 * @param source The ConsumerAgent from whom the interaction stems
	 * @param target The ConsumerAgent addressed by the interaction
	 * @param edgeWeight The strength of the interaction described by the edge
	 */
	public SNEdge(SNNode source, SNNode target, double edgeWeight) {
		this.source = source;
		this.target = target;
		this.edgeWeight = edgeWeight;
		this.edgeLabel = ("Edge from "+source.getNodeLabel()+" to "+target.getNodeLabel()+" of unassigned medium");
		this.medium = null;
	}


	public SNNode getSource() {
		return this.source;
	}

	public SNNode getTarget() {
		return this.target;
	}

	public double getEdgeWeight() {
		return this.edgeWeight;
	}

	public void setEdgeWeight(double edgeWeight) {
		this.edgeWeight = edgeWeight;
	}

	public String getEdgeLabel() {
		return this.edgeLabel;
	}

	public SocialGraph.EDGEMEDIUM getMedium() {
		return medium;
	}
}