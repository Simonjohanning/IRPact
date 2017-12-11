package IRPact_modellierung.events;

import IRPact_modellierung.simulation.SimulationContainer;

/**
 * Abstraction to represent an event scripted for a specific time.
 * Will generally be instantiated from the configuration of the model,
 * but can be created by a simulation entity as well.
 *
 * @author Simon Johaning
 */
public abstract class ScriptedEvent extends Event{

    public ScriptedEvent(SimulationContainer simulationContainer, double scheduledForTime) throws IllegalArgumentException{
        super(simulationContainer, scheduledForTime);
    }
}