package IRPact_modellierung;

import IRPact_modellierung.simulation.SimulationContainer;

/**
 * The SimulationEntity serves as abstraction for entities within the framework.
 * It basically ensures that every entity has access to the simulation container.
 *
 * @author Simon Johanning
 */
public abstract class SimulationEntity {

	protected SimulationContainer associatedSimulationContainer;

	public SimulationEntity(SimulationContainer associatedSimulationContainer) {
		this.associatedSimulationContainer = associatedSimulationContainer;
	}

	public SimulationContainer getAssociatedSimulationContainer() {
		return this.associatedSimulationContainer;
	}

}