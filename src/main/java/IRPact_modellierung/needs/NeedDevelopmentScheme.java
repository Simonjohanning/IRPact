package IRPact_modellierung.needs;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.events.NeedEvent;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A NeedDevelopmentScheme describes how needs develop and what happens when a product expires.
 * Needs are created through two processes, depending on how they came to be,
 * namely whether they stem from a processes connected to surging of needs (often mediated through the process model),
 * or from a more specific process, the lifetime expiration of a product.
 * The processes evaluate the state of the ConsumerAgent (potentially) developing the need and the time in the temporal mode,
 * when the need surges.
 *
 * A NeedDevelopmentScheme gives meaning to a NeedNeedIndicatorMap that is associated with it.
 *
 * @author Simon Johanning
 */
public abstract class NeedDevelopmentScheme {

    Map<Need, Double> needIndicatorMap;

    public NeedDevelopmentScheme(Map<Need, Double> needIndicatorMap){
        this.needIndicatorMap = needIndicatorMap;
    }

    /**
     * This method creates a set of NeedEvents based on the state of the consumerAgent and the simulation.
     * This need creation is generally triggered from the process model, but can be called from other places as well.
     * How this is used depends on the implementation of the model and its implementation.
     *
     * @param simulationContainer The container the simulation runs in
     * @param consumerAgent The ConsumerAgent whom the needs are associated with
     * @return A list of NeedEvents based on the implementation, which order depends on the implementation
     */
    public abstract List<NeedEvent> createNeedEvents(SimulationContainer simulationContainer, ConsumerAgent consumerAgent);

    /**
     * This method is generally called when a products' lifetime expired, and the model needs to
     * 'decide' what NeedEvents are scheduled based on the properties of the product and the consumerAgent whose
     * product expired
     *
     * @param expiredProduct The product whose lifetime exceeded
     * @param associatedConsumer The ConsumerAgent who adopted the product before its expiration
     * @param systemTime The time of the expiration of the product
     * @return A list of NeedEvents triggered by the expiration of the given product
     */
    public abstract List<NeedEvent> productExpiration(Product expiredProduct, ConsumerAgent associatedConsumer, double systemTime);

    /**
     * Method to sort the needIndicatorMap associated with this NeedDevelopmentScheme by its numeric values
     *
     * @return A list of NeedNeedIndicatorMap sorted by their value
     */
    public List<NeedNeedIndicatorMap> sortNeedIndicatorMapByValue(){
        List<NeedNeedIndicatorMap> sortedList = new ArrayList<>();
        for(Need currentNeed : needIndicatorMap.keySet()){
            sortedList.add(new NeedNeedIndicatorMap(currentNeed, needIndicatorMap.get(currentNeed)));
        }
        //sort the needIndicatorMap
        Collections.sort(sortedList);
        return sortedList;
    }

    /**
     * Method to sort the needs in the needIndicatorMap associated with this NeedDevelopmentScheme by its numeric values
     *
     * @return A list of needs sorted by the value of the entry in the needIndicatorMap of the respective need
     */
    public List<Need> sortNeedsByNeedIndicatorValue(){
        List<NeedNeedIndicatorMap> sortedNIM = sortNeedIndicatorMapByValue();
        List<Need> sortedNeeds = new ArrayList<>(sortedNIM.size());
        for(NeedNeedIndicatorMap currentEntry : sortedNIM){
            sortedNeeds.add(currentEntry.getNeed());
        }
        return sortedNeeds;
    }
}
