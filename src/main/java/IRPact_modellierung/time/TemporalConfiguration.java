package IRPact_modellierung.time;

/**
 * Object to configure the TimeModel of the simulation.
 * Contains all relevant information to start the simulation
 *
 * @author Simon Johanning
 */
public class TemporalConfiguration {

	private double simulationLength;
	private TimeModel.TEMPORALMODE timeModel;
	private boolean synchronousity;

	/**
	 * The TimeModel is configured by the length of the simulation,
	 * its mode and whether agents act in synchrony or not.
	 *
	 * @param simulationLength The duration of the simulation
	 * @param timeModel Which temporal model is applied (discrete or continuous)
	 * @param synchronousity Whether agents act in synchrony or not
	 */
	public TemporalConfiguration(double simulationLength, TimeModel.TEMPORALMODE timeModel, boolean synchronousity) {
		this.simulationLength = simulationLength;
		this.timeModel = timeModel;
		this.synchronousity = synchronousity;
	}

	public double getSimulationLength() {
		return this.simulationLength;
	}

	public TimeModel.TEMPORALMODE getTimeModel() {
		return this.timeModel;
	}

	public boolean isSynchronous() {
		return this.synchronousity;
	}

}