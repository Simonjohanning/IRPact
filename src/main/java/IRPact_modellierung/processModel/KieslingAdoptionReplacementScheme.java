package IRPact_modellierung.processModel;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.events.NeedEvent;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.needs.Need;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * AdoptionReplacementScheme that represents the behavior of ConsumerAgents
 * to adopt a product when a product is discontinued
 *
 * @author Simon Johanning
 */
public class KieslingAdoptionReplacementScheme extends AdoptionReplacementScheme {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    public KieslingAdoptionReplacementScheme() {
        super();
    }

    /**
     * Readopting a product means to find out all needs (now) unsatisfied
     * (all that could be satisfied by the product lost), and to schedule
     * a number of need events straight away for these needs.
     * These NeedEvents are ordered by their strength (NeedNeedIndicator map
     * of the corresponding NeedDevelopmentScheme).
     *
     * @param consumerAgentConcerned The ConsumerAgent readopting
     * @param productConcerned The Product being replaced, causing the readoption
     * @param simulationTime The current time of the simulation
     */
    public void readopt(ConsumerAgent consumerAgentConcerned, Product productConcerned, double simulationTime) {
        //get all (potentially) unsatisfied needs of the agent
        Set<Need> unsatisfiedNeeds = LazynessHelper.getUnsatisfiedNeeds(consumerAgentConcerned);
        //select the needs related to the product (that the readoption of the product affects)
        Set<Need> needsToSatisfy = new HashSet<>();
        for (Need lackedNeed : productConcerned.getPartOfProductGroup().getNeedsSatisfied()) {
            if (unsatisfiedNeeds.contains(lackedNeed)) needsToSatisfy.add(lackedNeed);
        }
        List<Need> sortedNeeds = LazynessHelper.sortNeeds(needsToSatisfy, consumerAgentConcerned.getCorrespondingConsumerAgentGroup().getNeedDevelopmentScheme());
        fooLog.debug("CA {} readopting. Needs to satisfy the needs {}", consumerAgentConcerned, sortedNeeds);
        Iterator<Need> needListIterator = sortedNeeds.listIterator();
        while(needListIterator.hasNext()){
            consumerAgentConcerned.getAssociatedSimulationContainer().getEventScheduler().scheduleEvent(new NeedEvent(simulationTime, consumerAgentConcerned, needListIterator.next(), consumerAgentConcerned.getAssociatedSimulationContainer()));
        }
    }

    /**
     * Removing the product from the agents means that each agent forgets about the product.
     * If they adopted it, they will unadopt, and create needevents via the readopt method.
     *
     * @param simulationContainer The container the simulation runs in
     * @param productToBeRemoved The product that gets removed from the simulation
     */
    public void removeProductFromAgents(SimulationContainer simulationContainer, Product productToBeRemoved) {
        for(ConsumerAgent consumerAgent : simulationContainer.getConsumerAgents()){
            if(LazynessHelper.anyAdoptedProductRefersToProduct(consumerAgent.getAdoptedProducts(), productToBeRemoved)) {
                fooLog.debug("Product {} wants to be removed at simulation time {}", productToBeRemoved, simulationContainer.getTimeModel().getSimulationTime());
                consumerAgent.wipeProduct(productToBeRemoved);
                readopt(consumerAgent, productToBeRemoved, simulationContainer.getTimeModel().getSimulationTime());
            }else consumerAgent.forgetProduct(productToBeRemoved);
        }
    }
}
