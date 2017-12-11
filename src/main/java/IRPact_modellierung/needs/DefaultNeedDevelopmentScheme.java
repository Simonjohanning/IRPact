package IRPact_modellierung.needs;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.events.NeedEvent;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.*;

/**
 * This NeedDevelopmentScheme intends to fulfill all needs not covered by adopted products immediately.
 * Thus the need indicator has no influence and need events are scheduled as soon as a (potential) need arises.
 * This scheme doesn't differentiate between different needs and considers all needs being part of the simulation.
 *
 * @author Simon Johanning
 */
public class DefaultNeedDevelopmentScheme extends NeedDevelopmentScheme{

    public DefaultNeedDevelopmentScheme(Map<Need, Double> needIndicatorMap) {
        super(needIndicatorMap);
    }

    /**
     * Creates all needs not yet covered by adopted products for the consumerAgent.
     * Needs are to be fulfilled immediately.
     *
     * @param simulationContainer The container the consumer agent is part of
     * @param consumerAgent The consumerAgent whos needs are to be created
     * @return A list of all needs of the consumerAgent not yet covered
     */
    public List<NeedEvent> createNeedEvents(SimulationContainer simulationContainer, ConsumerAgent consumerAgent) {
        List<NeedEvent> correspondingNeedEvents = new ArrayList<>();
        for(Need currentNeed : simulationContainer.getNeedsInSimulation()){
            if(!consumerAgent.needAlreadySatisfied(currentNeed)) correspondingNeedEvents.add(new NeedEvent(simulationContainer.getTimeModel().getSimulationTime(), consumerAgent, currentNeed, consumerAgent.getAssociatedSimulationContainer()));
        }
        return correspondingNeedEvents;
    }

    /**
     * Schedules the respective need events regarding needs unfulfilled through the expiration of
     * the expired product immediately
     *
     * @param expiredProduct The product that expired
     * @param associatedConsumer The consumer that formerly adopted the product
     * @param systemTime The current time (time when the product expired
     * @return A list of NeedEvents based on the expiration of said product
     */
    public List<NeedEvent> productExpiration(Product expiredProduct, ConsumerAgent associatedConsumer, double systemTime) {
        List<NeedEvent> needEvents = new ArrayList<NeedEvent>();
        for(Need needToCheck : expiredProduct.getPartOfProductGroup().getNeedsSatisfied()){
            if(!associatedConsumer.needAlreadySatisfied(needToCheck)) needEvents.add(new NeedEvent(associatedConsumer.getAssociatedSimulationContainer().getTimeModel().getSimulationTime(), associatedConsumer, needToCheck, associatedConsumer.getAssociatedSimulationContainer()));
        }
        return needEvents;
    }
}
