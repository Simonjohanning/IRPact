package IRPact_modellierung.needs;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.events.NeedEvent;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The StochasticNeedDevelopmentScheme is a NeedDevelopmentScheme,
 * which develops needs proportionally to their needIndicator.
 * This will however be determined stochastically, so that the
 * developed needs are proportional to the agent groups' needIndicator
 * only in the expected value.
 * Scheduled NeedEvents are set up for immediate evaluation (no need development delay).
 *
 * @author Simon Johanning
 */
public class StochasticNeedDevelopmentScheme extends NeedDevelopmentScheme{

    public StochasticNeedDevelopmentScheme(Map<Need, Double> needIndicatorMap) {
        super(needIndicatorMap);
    }

    /**
     * NeedEvents are created proportionally to the needIndicatorMap provided in the constructor.
     * However, only needs that are not yet satisfied will be considered for this stochastic process.
     * The respective NeedEvents are scheduled to take place immediately.
     *
     * @param simulationContainer The container the simulation runs in
     * @param consumerAgent The ConsumerAgent whom the needs are associated with
     * @return A list of (immediately scheduled) NeedEvents with the Needs being proportional to the needIndicatorMap
     */
    public List<NeedEvent> createNeedEvents(SimulationContainer simulationContainer, ConsumerAgent consumerAgent) {
        List<NeedEvent> scheduledEvents = new ArrayList<>();
        for(Need currentNeed : simulationContainer.getNeedsInSimulation()){
            if(!consumerAgent.needAlreadySatisfied(currentNeed)){
                if(Math.random() < needIndicatorMap.get(currentNeed)) {
                    scheduledEvents.add(new NeedEvent(simulationContainer.getTimeModel().getSimulationTime(), consumerAgent, currentNeed, consumerAgent.getAssociatedSimulationContainer()));
                }
            }
        }
        return scheduledEvents;
    }

    /**
     * Expired products trigger needs stochastically, as long as these aren't covered.
     * These needs are scheduled for immediate evaluation and the probability that
     * a NeedEvent is scheduled is proportional to the needIndicator.
     *
     * @param expiredProduct The product whose lifetime exceeded
     * @param associatedConsumer The ConsumerAgent who adopted the product before its expiration
     * @param systemTime The time of the expiration of the product
     * @return A list of (immediately scheduled) NeedEvents with the Needs being proportional to the needIndicatorMap
     */
    public List<NeedEvent> productExpiration(Product expiredProduct, ConsumerAgent associatedConsumer, double systemTime) {
        List<NeedEvent> needEvents = new ArrayList<NeedEvent>();
        for(Need needToCheck : expiredProduct.getPartOfProductGroup().getNeedsSatisfied()){
            if(!associatedConsumer.needAlreadySatisfied(needToCheck)){
                if(Math.random() < needIndicatorMap.get(needToCheck)) {
                    needEvents.add(new NeedEvent(associatedConsumer.getAssociatedSimulationContainer().getTimeModel().getSimulationTime(), associatedConsumer, needToCheck, associatedConsumer.getAssociatedSimulationContainer()));
                }

            }
        }
        return needEvents;
    }
}
