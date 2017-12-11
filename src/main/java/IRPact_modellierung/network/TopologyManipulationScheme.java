package IRPact_modellierung.network;

import IRPact_modellierung.simulation.SimulationContainer;

/**
 * A TopologyManipulationScheme describes how the topology of a SocialGraph changes over time.
 * It describes whether edges from a node to itself are allowed (selfReferentialTopology)
 * and might rewire / reassign edges in the graph through the manipulateTopology method.
 *
 * @author Simon Johanning
 */
public abstract class TopologyManipulationScheme{

	protected boolean selfReferentialTopology;

	public TopologyManipulationScheme(boolean selfReferentialTopology) {
		this.selfReferentialTopology = selfReferentialTopology;
	}

	/**
	 * Method to describe how the topology of a SocialGraph changes over time.
	 * Directly manipulates the graph by adding, removing or changing the edges,
	 * depending on the characteristics of the TopologyManipulationScheme.
	 *
	 * @param simulationContainer The container the simulation runs in
	 * @param socialGraph The graph which edges are to be manipulated
	 */
	public abstract void manipulateTopology(SimulationContainer simulationContainer, SocialGraph socialGraph);

	public boolean isSelfReferential() {
		return selfReferentialTopology;
	}
}