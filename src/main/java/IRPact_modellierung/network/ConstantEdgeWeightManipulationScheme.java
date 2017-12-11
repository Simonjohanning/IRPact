package IRPact_modellierung.network;

/**
 * EdgeWeightManipulationScheme that assigns a constant weight to an edge.
 * The edge weight is the same for all edges and static throughout the simulation.
 *
 * @author Simon Johanning
 */
public class ConstantEdgeWeightManipulationScheme extends EdgeWeightManipulationScheme {

	private double edgeWeight;

	/**
	 * The ConstantEdgeWeightManipulationScheme assigns a constant weight to every edge.
	 *
	 * @param edgeWeight The weight that is to be assigned to every edge
	 */
	public ConstantEdgeWeightManipulationScheme(double edgeWeight) {
		super();
		this.edgeWeight = edgeWeight;
	}

	public double getEdgeWeight() {
		return this.edgeWeight;
	}

	public double weighEdge(SNNode source, SNNode target, SocialGraph.EDGEMEDIUM edgemedium) {
		return edgeWeight;
	}

	public double reweighEdge(SNEdge edgeToReweigh, double simulationTime) {
		return edgeWeight;
	}
}