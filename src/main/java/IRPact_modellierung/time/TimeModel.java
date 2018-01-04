package IRPact_modellierung.time;

import IRPact_modellierung.io.output.DefaultOutputScheme;
import IRPact_modellierung.io.output.OutputScheme;
import IRPact_modellierung.SimulationEntity;
import IRPact_modellierung.processModel.ProcessModel;
import IRPact_modellierung.simulation.SimulationContainer;

/**
 * The TimeModel governs the model dynamics within IRPact.
 * It is superordinate to the process model and the component
 * starting (and continuing) the simulation.
 *
 * Temporal models can be coarsely classified in discrete and continuous
 * models, depending on the temporal mode.
 *
 * It governs the temporal order and triggers the process model
 * based on the respective implementation.
 *
 * @author Simon Johanning
 */
public abstract class TimeModel extends SimulationEntity {

	protected ProcessModel processModel;
	protected double simulationTime;
	protected OutputScheme outputScheme;

	public enum TEMPORALMODE {
		DISCRETE, CONTINUOUS
	}

	/**
	 * The TimeModel governs the temporal dynamics of the simulation running
	 * in the simulationContainer, as well as the execution of the processModel
	 * associated with it.
	 *
	 * @param simulationContainer The container in which the simulation governed by this temporal model runs
	 * @param processModel The process model governed by this temporal model
	 */
	public TimeModel(SimulationContainer simulationContainer, ProcessModel processModel) {
		super(simulationContainer);
		this.processModel = processModel;
		simulationTime = 0.0;
		outputScheme = new DefaultOutputScheme();
	}

	public ProcessModel getProcessModel() {
		return this.processModel;
	}

	public double getSimulationTime() {
		return simulationTime;
	}

	public void setOutputScheme(OutputScheme outputScheme) {
		this.outputScheme = outputScheme;
	}

	/**
	 * Method that starts the simulation.
	 * Will set off the model dynamics.
	 *
	 */
	public abstract void startSimulation();
}