package IRPact_modellierung.events;

import IRPact_modellierung.simulation.SimulationContainer;

/**
 * This abstraction represents an event scheduler that is used within a continuous timing scheme.
 * Since continuous temporal models can be very distinct, little functionality is abstracted into this class.
 *
 * @author Simon Johanning
 */
public abstract class ContinuousEventScheduler extends EventScheduler {

    public ContinuousEventScheduler(SimulationContainer associatedSimulationContainer) {
        super(associatedSimulationContainer);
    }
}