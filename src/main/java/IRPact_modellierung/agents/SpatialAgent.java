package IRPact_modellierung.agents;

import IRPact_modellierung.simulation.SimulationContainer;

import java.awt.geom.Point2D;

/**
 * Abstraction of agents that are spatially located within the simulation.
 * SpatialAgents are situated spatially by their spatial coordinates (as 2 dimensional point).
 *
 * @author Simon Johanning
 */
public abstract class SpatialAgent extends Agent {

	private Point2D coordinates;

	/**
	 * Abstraction of agents that are spatially located within the simulation
	 *
	 * @param associatedSimulationContainer The simulation container containing the simulation
	 * @param coordinates The spatial position the spatial agent is initialized with
	 */
	public SpatialAgent(SimulationContainer associatedSimulationContainer, Point2D coordinates) {
		super(associatedSimulationContainer);
		this.coordinates = coordinates;
	}

	public Point2D getCoordinates() {
		return this.coordinates;
	}

	public void setCoordinates(Point2D coordinates) {
		this.coordinates = coordinates;
	}

}