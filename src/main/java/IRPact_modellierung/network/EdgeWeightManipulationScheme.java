package IRPact_modellierung.network;

/**
 * Calculates an edge weight for a SNEdge based on agent group, distance and other factors.
 * This scheme determines the temporal dynamics of the edgeWeightFunction and is used
 * to (re-)evaluate the strength of interaction between different actors
 *
 * @author Simon Johanning
 */
public abstract class EdgeWeightManipulationScheme{

	/**
	 * An EdgeWeightManipulationScheme describes how the weight of an edge in the SocialGraph is determined.
	 *
	 */
	public EdgeWeightManipulationScheme() {
	}

	/**
	 * Method to describe how an edge of a given medium, that is to be established between two nodes is to be weighed
	 *
	 * @param source The node the corresponding edge stems from
	 * @param target The node the corresponding edge points towards
	 * @param edgemedium The medium the edge is established via
	 * @return The weight that should be assigned to the edge of interest
	 */
	public abstract double weighEdge(SNNode source, SNNode target, SocialGraph.EDGEMEDIUM edgemedium);

	/**
	 * Method to re-evaluate the interaction strength associated with an edge at a given time
	 *
	 * @param edgeToReweigh The edge that is to be re-evaluated
	 * @param simulationTime The current system time
	 * @return The weight that should be (re-)assigned to the edge of interest
	 */
	public abstract double reweighEdge(SNEdge edgeToReweigh, double simulationTime);

}