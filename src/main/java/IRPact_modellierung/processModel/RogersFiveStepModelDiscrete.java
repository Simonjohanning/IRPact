package IRPact_modellierung.processModel;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.events.CommunicationEvent;
import IRPact_modellierung.events.EventScheduler;
import IRPact_modellierung.events.NeedEvent;
import IRPact_modellierung.events.PostPurchaseEvaluationEvent;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

/**
 * The (abstract) RogersFiveStepModelDiscrete process model
 * represents the process models based on Rogers five step model within a discrete temporal model.
 * These steps (knowledge, persuasion, decision, implementation and confirmation) are given as
 * abstract methods that implementations of this process model have to specify.
 * These phases are processed after one another, with each consumer agent being processed after one another.
 *
 * After each step, all events scheduled for this time are processed, and the model proceeds to the next step.
 *
 * @author Simon Johanning
 */
public abstract class RogersFiveStepModelDiscrete extends ProcessModel {

    private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    public RogersFiveStepModelDiscrete(AdoptionReplacementScheme adoptionReplacementScheme) {
        super(adoptionReplacementScheme);
    }

    /**
     * Consumer agents are processed by running through the individual steps of Rogers five step process model.
     * After each step, outstanding events are processed.
     *
     * @param consumerAgentsToProcess The consumer agents to process
     * @param simulationContainer The container the simulation runs in
     * @param simulationTime The current system time
     */
    public void processConsumerAgents(Set<ConsumerAgent> consumerAgentsToProcess, SimulationContainer simulationContainer, double simulationTime) {
        checkForProductExpiration(simulationContainer);
        processMessages(simulationContainer.getEventScheduler(), simulationTime);
        //fooLog.info("\nBefore knowledge step in step {}\n-------------------", simulationTime);
        //process knowledge step for all agents
        for(ConsumerAgent currentAgent : consumerAgentsToProcess){
            processKnowledgeStep(currentAgent, simulationTime);
        }
        //fooLog.info("\nBefore processing communication messages in step {}\n-------------------", simulationTime);
        //process all messages from knowledge step
        processMessages(simulationContainer.getEventScheduler(), simulationTime);
       // fooLog.info("\nBefore persuasion step in step {}\n-------------------", simulationTime);
        //process persuasion step for all agents
        for(ConsumerAgent currentAgent : consumerAgentsToProcess){
            processPersuasionStep(currentAgent, simulationTime);
        }
        //fooLog.info("\nBefore processing need events step in step {}\n-------------------", simulationTime);
        //process all messages from persuasion step
        processMessages(simulationContainer.getEventScheduler(), simulationTime);
       // fooLog.info("\nBefore empty substeps\n-------------------\n------------------------\n", simulationTime);
        //process decision step for all agents
        for(ConsumerAgent currentAgent : consumerAgentsToProcess){
            processDecisionStep(currentAgent, simulationTime);
        }
        processMessages(simulationContainer.getEventScheduler(), simulationTime);
        //process implementation step for all agents
        for(ConsumerAgent currentAgent : consumerAgentsToProcess){
            processImplementationStep(currentAgent, simulationTime);
        }
        processMessages(simulationContainer.getEventScheduler(), simulationTime);
        //process confirmation step for all agents
        for(ConsumerAgent currentAgent : consumerAgentsToProcess){
            processConfirmationStep(currentAgent, simulationTime);
        }
        processMessages(simulationContainer.getEventScheduler(), simulationTime);
    }

    /**
     * Method to process the knowledge step of a ConsumerAgent
     * within Rogers Five Step Model
     *
     * @param currentAgent The agent in the knowledge step
     * @param simulationTime The current time of the simulation
     */
    protected abstract void processKnowledgeStep(ConsumerAgent currentAgent, double simulationTime);

    /**
     * Method to process the persuasion step of a ConsumerAgent
     * within Rogers Five Step Model
     *
     * @param currentAgent The agent in the knowledge step
     * @param simulationTime The current time of the simulation
     */
    protected abstract void processPersuasionStep(ConsumerAgent currentAgent, double simulationTime);

    /**
     * Method to process the decision step of a ConsumerAgent
     * within Rogers Five Step Model
     *
     * @param currentAgent The agent in the knowledge step
     * @param simulationTime The current time of the simulation
     */
    protected abstract void processDecisionStep(ConsumerAgent currentAgent, double simulationTime);

    /**
     * Method to process the implementation step of a ConsumerAgent
     * within Rogers Five Step Model
     *
     * @param currentAgent The agent in the knowledge step
     * @param simulationTime The current time of the simulation
     */
    protected abstract void processImplementationStep(ConsumerAgent currentAgent, double simulationTime);

    /**
     * Method to process the confirmation step of a ConsumerAgent
     * within Rogers Five Step Model
     *
     * @param currentAgent The agent in the knowledge step
     * @param simulationTime The current time of the simulation
     */
    protected abstract void processConfirmationStep(ConsumerAgent currentAgent, double simulationTime);

    /**
     * Method to process all current events within the event scheduler.
     * Will process events according to their order within the event scheduler
     * as long as events for this time step exist.
     *
     * @param eventScheduler The event scheduler to process elements from
     * @param simulationTime The current time of the simulation
     */
    protected void processMessages(EventScheduler eventScheduler, double simulationTime){
        boolean validElementTop = eventScheduler.existsNextEvent();
        if(validElementTop && (eventScheduler.topNextEvent().getScheduledForTime() > simulationTime)) validElementTop = false;
        while(validElementTop){
            fooLog.debug("About to process element {}", eventScheduler.topNextEvent());
            eventScheduler.popNextEvent().processEvent(simulationTime);
            if(!eventScheduler.existsNextEvent()) validElementTop = false;
            if(validElementTop && (eventScheduler.topNextEvent().getScheduledForTime() > simulationTime)) validElementTop = false;
        }
    }
}
