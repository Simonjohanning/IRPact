package IRPact_modellierung.processModel;

import IRPact_modellierung.agents.companyAgents.CompanyAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.agents.posAgents.POSAgent;
import IRPact_modellierung.events.CommunicationEvent;
import IRPact_modellierung.events.EventScheduler;
import IRPact_modellierung.events.NeedEvent;
import IRPact_modellierung.events.PostPurchaseEvaluationEvent;
import IRPact_modellierung.messaging.Message;
import IRPact_modellierung.products.AdoptedProduct;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;

import java.util.*;

/**
 * The KieslingsAdoptionFiveStepModel is a Process Model based on the discrete
 * Rogers Five Step Model, and models the process model in Kiesling.
 * In the knowledge step, ConsumerAgents schedule CommunicationEvents according
 * to their CommunicationScheme, in the persuasion step they schedule NeedEvents
 * based on their NeedDevelopmentScheme and in the confirmation step they
 * schedule PPE Events for all adopted products that are still around.
 * Company agents, POS agents and the policy agent are entirely passive in this process model.
 *
 * @author Simon Johanning
 */
public class KieslingsAdoptionFiveStepModel extends RogersFiveStepModelDiscrete {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    public KieslingsAdoptionFiveStepModel(AdoptionReplacementScheme adoptionReplacementScheme) {
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
        //schedule communication events
        EventScheduler eventScheduler = currentAgent.getAssociatedSimulationContainer().getEventScheduler();
        //Messages are created by the agent message scheme
        Set<Message> messagesToSend = currentAgent.getCorrespondingConsumerAgentGroup().getCommunicationScheme().getCorrespondingMessageScheme().createMessages(currentAgent.getAssociatedSimulationContainer(), currentAgent);
        //schedule these messages
        for(Message messageToSchedule : messagesToSend) {
            eventScheduler.scheduleEvent(new CommunicationEvent(messageToSchedule, simulationTime, currentAgent.getAssociatedSimulationContainer()));
        }
    }

    protected void processPersuasionStep(ConsumerAgent currentAgent, double simulationTime) {
        //schedule need events
        EventScheduler eventScheduler = currentAgent.getAssociatedSimulationContainer().getEventScheduler();
        /*Map<Need, Double> needMap = currentAgent.getCorrespondingConsumerAgentGroup().getNeedMap();
        List<NeedNeedIndicatorMap> scheduledOrder = new LinkedList<NeedNeedIndicatorMap>();
        //for each need check if it arises
        for(Need need : needMap.keySet()){
            //if need arises, schedule a need event
            if(Math.random() < needMap.get(need)){
                scheduledOrder.add(new NeedNeedIndicatorMap(need, needMap.get(need)));
            }
        }
        Collections.sort(scheduledOrder);
        for(NeedNeedIndicatorMap need : scheduledOrder){
            eventScheduler.scheduleEvent(new NeedEvent(simulationTime, currentAgent, need.getNeed(), currentAgent.getAssociatedSimulationContainer()));
        }*/
        List<NeedEvent> needsList = currentAgent.getCorrespondingConsumerAgentGroup().getNeedDevelopmentScheme().createNeedEvents(currentAgent.getAssociatedSimulationContainer(), currentAgent);
        fooLog.debug("needs list for agent {} is {}", currentAgent, needsList);
        for (NeedEvent aNeedsList : needsList) {
            eventScheduler.scheduleEvent(aNeedsList);
        }
    }

    protected void processDecisionStep(ConsumerAgent currentAgent, double simulationTime) {

    }

    protected void processImplementationStep(ConsumerAgent currentAgent, double simulationTime) {

    }

    protected void processConfirmationStep(ConsumerAgent currentAgent, double simulationTime) {
        EventScheduler eventScheduler = currentAgent.getAssociatedSimulationContainer().getEventScheduler();
        for(AdoptedProduct adoptedProduct : currentAgent.getAdoptedProducts()){
            //when product is still around, schedule a PPE event
            if(!currentAgent.getAssociatedSimulationContainer().getHistoricalProducts().contains(adoptedProduct.getCorrespondingProduct())) eventScheduler.scheduleEvent(new PostPurchaseEvaluationEvent(currentAgent.getAssociatedSimulationContainer(), adoptedProduct.getCorrespondingProduct(), currentAgent, simulationTime));
        }
    }
}
