package IRPact_modellierung.IO.Output;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.products.AdoptedProduct;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductAttribute;
import org.apache.logging.log4j.LogManager;

import java.util.*;

/**
 * Created by Lenovo on 23.08.2016.
 */
public class SN3OutputScheme extends OutputScheme {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    public SN3OutputScheme() {
    }

    public void writeConsumerAgentState(ConsumerAgent consumerAgent, double timeStamp) {

    }

    public void writeConsumerAdoption(Set<ConsumerAgent> consumerAgents, double timeStamp) {
        Map<ConsumerAgentGroup, Map<ProductAttribute, Double>> perceptionPerCAGperProduct = calculateAveragePerception(consumerAgents, timeStamp);
        for(ConsumerAgentGroup cag : perceptionPerCAGperProduct.keySet()){
            fooLog.info("ConsumerAgentGroup {} perceives the following way: \n", cag.getGroupName());
            for(ProductAttribute pa : perceptionPerCAGperProduct.get(cag).keySet()){
                fooLog.info("Average perception of {} is {}",pa.getCorrespondingProductGroupAttribute().getName(), perceptionPerCAGperProduct.get(cag).get(pa));
            }
            fooLog.info("\n");
        }
    }

    /**
     * Very inefficient function to calculate the average perception of product attributes for product groups
     *
     * @param consumerAgents
     * @param timeStamp
     * @return
     */
    private Map<ConsumerAgentGroup, Map<ProductAttribute, Double>> calculateAveragePerception(Set<ConsumerAgent> consumerAgents, double timeStamp) {
        Set<Product> relevantProducts = new HashSet<>();
        Map<ConsumerAgentGroup, Set<ConsumerAgent>> consumerAgentsPerGroup = new HashMap<>();
        for(ConsumerAgentGroup cag : consumerAgents.iterator().next().getAssociatedSimulationContainer().getSimulationConfiguration().getAgentConfiguration().getConsumerAgentGroups()){
            consumerAgentsPerGroup.put(cag, new HashSet<>());
        }
        //associates the consumer agents with their respective groups and fills the map of all products in the simulation
        for(ConsumerAgent ca : consumerAgents){
            consumerAgentsPerGroup.get(ca.getCorrespondingConsumerAgentGroup()).add(ca);
            for(AdoptedProduct ap : ca.getAdoptedProducts()){
                if(!relevantProducts.contains(ap.getCorrespondingProduct())) relevantProducts.add(ap.getCorrespondingProduct());
            }
        }
        Map<ConsumerAgentGroup, Map<ProductAttribute, Double>> returnMap = new HashMap<>();
        //calculate all average perceptions
        for (ConsumerAgentGroup cag : consumerAgentsPerGroup.keySet()) {
            Map<ProductAttribute, Double> innerMap = new HashMap<>();
            for (Product currentProduct : relevantProducts) {
                for (ProductAttribute pa : currentProduct.getProductAttributes()) {
                    double cumulatedPerceptions = 0.0;
                    int noObservations = 0;
                    for (ConsumerAgent ca : consumerAgentsPerGroup.get(cag)) {
                        for (AdoptedProduct ap : ca.getAdoptedProducts()) {
                            if (ap.getCorrespondingProduct() == currentProduct) {
                                cumulatedPerceptions += ca.getPerceivedProductAttributeValues().get(pa).calculateProductAttributePerception(timeStamp);
                                noObservations++;
                            }
                        }
                    }
                    innerMap.put(pa, cumulatedPerceptions / noObservations);
                }
            }
            returnMap.put(cag, innerMap);
        }
        return returnMap;
    }
}
