package IRPact_modellierung.time;

import IRPact_modellierung.processModel.ProcessModel;
import IRPact_modellierung.simulation.SimulationContainer;

/**
 * A ContinuousTimeModel describes a temporal model that is organized in a
 * finite number of steps, in which the respective events and processes are executed.
 * After the execution of one step, it will proceed to the next step after reaching the end
 *
 * It is associated with the respective event scheduler and process model,
 * as well as the number of steps it runs, whether its agents run in synchrony and the
 * output scheme used to print information about the state of it.
 *
 * After its execution it will run for the specified number of steps,
 * invoking the step() method, which describes its dynamics.
 *
 * @author Simon Johanning
 */
public class ContinuousTimeModel extends TimeModel {

	private double simulationLength;
	private double currentSimulationTime;

	public ContinuousTimeModel(SimulationContainer simulationContainer, ProcessModel processModel, double simulationLength) {
		super(simulationContainer, processModel);
		this.simulationLength = simulationLength;
		this.currentSimulationTime = 0.0;
	}

	public double getSimulationLength() {
		return this.simulationLength;
	}

	public double getCurrentSimulationTime() {
		return this.currentSimulationTime;
	}

	//TODO implement!!

	public void startSimulation() {

	}
}