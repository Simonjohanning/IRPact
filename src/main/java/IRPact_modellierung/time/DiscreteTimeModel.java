package IRPact_modellierung.time;

import IRPact_modellierung.IO.Output.DefaultOutputScheme;
import IRPact_modellierung.IO.Output.OutputScheme;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.events.DiscreteEventScheduler;
import IRPact_modellierung.processModel.ProcessModel;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A DiscreteTimeModel describes a temporal model that is organized in a
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
public class DiscreteTimeModel extends TimeModel {

	private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	private int numberTotalSteps;
	private boolean synchronousTimeRegime;
	private DiscreteEventScheduler eventScheduler;
	//TODO regard synchronousity

	/**
	 * A DiscreteTimeModel governs the temporal aspects of a simulation in the simulation container
	 * and the process model in it for a number of steps specified, with agent
	 * behavior synchronicity specified
	 *
	 * @param simulationContainer The container the simulation runs in
	 * @param processModel The process model the simulation is based on
	 * @param numberTotalSteps The number of steps the simulation is to run
	 * @param synchronousTimeRegime Whether agents should (be forced to) act in synchrony or not
	 */
	public DiscreteTimeModel(SimulationContainer simulationContainer, ProcessModel processModel, int numberTotalSteps, boolean synchronousTimeRegime) {
		super(simulationContainer, processModel);
		this.numberTotalSteps = numberTotalSteps;
		simulationTime = 0.0;
		this.synchronousTimeRegime = synchronousTimeRegime;
	}

	public int getNumberTotalSteps() {
		return this.numberTotalSteps;
	}

	public boolean isSynchronousTimeRegime() {
		return this.synchronousTimeRegime;
	}

	/**
	 * This method starts the simulation, and calls the step()
	 * method the number of times specified.
	 *
	 * It thus describes the discrete temporal dynamics.
	 */
	public void startSimulation() {
		//instantiate (and schedule initial events)
		eventScheduler = (DiscreteEventScheduler) associatedSimulationContainer.getEventScheduler();
		fooLog.info("Before first step the state is:\n");
		//outputScheme.writeConsumerAdoption(associatedSimulationContainer.getConsumerAgents(), simulationTime);
		for(int step=0;step<=numberTotalSteps;step++){
			simulationTime = (double) step;
			fooLog.info("\n\nSimulation time is {}\n\n",simulationTime);
			step();
		}
	}

	/**
	 * Each step in the discrete time model is exectuted by
	 * 1. printing out the state of the agents according to the output scheme
	 * 2. Executing all events around for this step
	 * 3. Invoking the process model
	 * 4. Printing adoption status of the agents according to the output scheme
	 *
	 */
	private void step(){
		double timeBefore = System.currentTimeMillis();
		for(ConsumerAgent currentConsumer : associatedSimulationContainer.getConsumerAgents()) {
			outputScheme.writeConsumerAgentState(currentConsumer, simulationTime);
		}
		//process scheduled events
		boolean eventScheduledForThisStep = eventScheduler.existsNextEvent();
		while(eventScheduledForThisStep){
			if(eventScheduler.existsNextEvent()){
				if((int) eventScheduler.topNextEvent().getScheduledForTime() == (int) Math.floor(simulationTime)){
					fooLog.debug("Scheduled event {} is scheduled for time {}, simulation time is {}", eventScheduler.topNextEvent(),  eventScheduler.topNextEvent().getScheduledForTime(), simulationTime);
					eventScheduler.popNextEvent().processEvent(simulationTime);
				}
				else eventScheduledForThisStep = false;
			}else eventScheduledForThisStep = false;
		}
		processModel.processConsumerAgents(associatedSimulationContainer.getConsumerAgents(), associatedSimulationContainer, simulationTime);
		outputScheme.writeConsumerAdoption(associatedSimulationContainer.getConsumerAgents(), simulationTime);
		fooLog.info("Step {} took {}s to execute\n\n", simulationTime, (System.currentTimeMillis()-timeBefore)/1000);
	}
}