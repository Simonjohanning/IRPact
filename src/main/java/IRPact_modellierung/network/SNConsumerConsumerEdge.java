package IRPact_modellierung.network;

public class SNConsumerConsumerEdge extends SNEdge {

	private SNConsumerNode source;
	private SNConsumerNode target;

	public SNConsumerConsumerEdge(SNNode source, SNNode target, double edgeWeight, String edgeLabel) {
		super(source, target, edgeWeight, edgeLabel);
	}

	public SNConsumerConsumerEdge(SNNode source, SNNode target, double edgeWeight) {
		super(source, target, edgeWeight);
	}

	public SNConsumerNode getSource() {
		return this.source;
	}

	public SNConsumerNode getTarget() {
		return this.target;
	}

}