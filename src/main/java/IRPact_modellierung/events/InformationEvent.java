package IRPact_modellierung.events;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.information.Information;
import IRPact_modellierung.simulation.SimulationContainer;

/**
 * An InformationEvent links the information a ConsumerAgent requested in an information scheme
 * to the processing of the respective information within the simulation.
 *
 * The information is provided at the scheduled time as an InformationEvent,
 * and will be evaluated when the InformatioEvent is processed.
 *
 * Processing of this event depends on the ConsumerAgent requesting the information,
 * and is specified in their respective method.
 *
 * @author Simon Johanning
 */
public class InformationEvent extends Event{

    private Information respectiveInformation;
    private ConsumerAgent processingAgent;

    /**
     * An InformationEvent links a given information and the ConsumerAgent being informed to the
     * temporal frame. Since it is an event it is scheduled for a given time (and linked to the
     * container the simulation runs in), and contains the description of its processing in itself.
     *
     * @param simulationContainer The container the simulation runs in
     * @param scheduledForTime The time the event is scheduled for
     * @param respectiveInformation The information that is to be received by the processingAgent
     * @param processingAgent The ConsumerAgent processing the information
     * @throws IllegalArgumentException Will be thrown when the processingAgent does not belong to the simulation (is not in the simulationContainer)
     */
    public InformationEvent(SimulationContainer simulationContainer, double scheduledForTime, Information respectiveInformation, ConsumerAgent processingAgent) throws IllegalArgumentException {
        super(simulationContainer, scheduledForTime);
        if(!simulationContainer.getConsumerAgents().contains(processingAgent)) throw new IllegalArgumentException("ConsumerAgent "+processingAgent+" associated with the InformationEvent is not part of the simulation!!");
        this.respectiveInformation = respectiveInformation;
        this.processingAgent = processingAgent;
    }

    /**
     * Processing an InformationEvent depends on how the ConsumerAgent responds to information.
     * Its execution is thus done within the ConsumerAgent by its processInformation method.
     *
     * @param systemTime The current time of the system for execution
     */
    public void processEvent(double systemTime) {
        processingAgent.processInformation(respectiveInformation, systemTime);
    }
}
