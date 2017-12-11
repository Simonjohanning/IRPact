package IRPact_modellierung.processModel;

import IRPact_modellierung.agents.companyAgents.CompanyAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.agents.posAgents.POSAgent;
import IRPact_modellierung.events.CommunicationEvent;
import IRPact_modellierung.events.EventScheduler;
import IRPact_modellierung.events.NeedEvent;
import IRPact_modellierung.messaging.CommunicationScheme;
import IRPact_modellierung.messaging.Message;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.Set;

/**
 * Process model to operationalize the processes described in Schwarz'
 * model within the framework of Rogers Five Step Model.
 *
 *
 */
public class SchwarzProcessModel extends RogersFiveStepModelDiscrete{

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    public SchwarzProcessModel(AdoptionReplacementScheme adoptionReplacementScheme) {
        super(adoptionReplacementScheme);
    }


    public void processCompanyAgents(Set<CompanyAgent> agentsToProcess, SimulationContainer simulationContainer, double simulationTime) {

    }


    public void processPolicyAgent(SimulationContainer simulationContainer, double simulationTime) {

    }


    public void processPOSAgents(Set<POSAgent> pOSAgentsToProcess, SimulationContainer simulationContainer, double simulationTime) {

    }

    public void readoptAfterDiscontinuation(ConsumerAgent consumerAgentConcerned, Product productConcerned, double simulationTime){
        super.readoptAfterDiscontinuation(consumerAgentConcerned, productConcerned, simulationTime);
        processMessages(consumerAgentConcerned.getAssociatedSimulationContainer().getEventScheduler(), simulationTime);
    }

    protected void processKnowledgeStep(ConsumerAgent currentAgent, double simulationTime) {
        CommunicationScheme communicationScheme = currentAgent.getCorrespondingConsumerAgentGroup().getCommunicationScheme();
        Set<Message> agentMessages = communicationScheme.getCorrespondingMessageScheme().createMessages(currentAgent.getAssociatedSimulationContainer(), currentAgent);
        Set<CommunicationEvent> agentCommunicationEvents = communicationScheme.createCommunicationEvents(agentMessages, simulationTime, currentAgent.getAssociatedSimulationContainer());
        for(CommunicationEvent currentEvent : agentCommunicationEvents) {
            currentAgent.getAssociatedSimulationContainer().getEventScheduler().scheduleEvent(currentEvent);
        }
    }

    protected void processPersuasionStep(ConsumerAgent currentAgent, double simulationTime) {
        //schedule need events
        EventScheduler eventScheduler = currentAgent.getAssociatedSimulationContainer().getEventScheduler();
        List<NeedEvent> needsList = currentAgent.getCorrespondingConsumerAgentGroup().getNeedDevelopmentScheme().createNeedEvents(currentAgent.getAssociatedSimulationContainer(), currentAgent);
        fooLog.debug("needs list for agent {} is {}", currentAgent, needsList);
        for (NeedEvent needEvent : needsList) {
            eventScheduler.scheduleEvent(needEvent);
        }
    }

    protected void processDecisionStep(ConsumerAgent currentAgent, double simulationTime) {

    }

    protected void processImplementationStep(ConsumerAgent currentAgent, double simulationTime) {

    }

    protected void processConfirmationStep(ConsumerAgent currentAgent, double simulationTime) {
    }
}

