package IRPact_modellierung.IO.Output;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.products.AdoptedProduct;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created by Lenovo on 15.08.2016.
 */
public class N2OutputScheme extends OutputScheme {

    private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    public void writeConsumerAgentState(ConsumerAgent consumerAgent, double timeStamp) {
       /* for(ProductAttribute pa : consumerAgent.getPerceivedProductAttributeValues().keySet()){
            fooLog.info("Consumer agent {} has a perceived value of {} for product attribute {}", consumerAgent.getAgentID(), consumerAgent.getPerceivedProductAttributeValues().get(pa).calculateProductPerception(timeStamp), pa.getCorrespondingProductGroupAttribute().getName());
        }*/
    }

    public void writeConsumerAdoption(Set<ConsumerAgent> consumerAgents, double timeStamp) {
        Map<ConsumerAgentGroup, Map<ProductGroup, Integer>> adoptersPerCAGperPG = countAdopters(consumerAgents, timeStamp);
        for(ConsumerAgentGroup cag : adoptersPerCAGperPG.keySet()){
            fooLog.info("ConsumerAgentGroup {} adopted the following way: ", cag.getGroupName());
            for(ProductGroup pg : adoptersPerCAGperPG.get(cag).keySet()){
                fooLog.info("{} products of product group {} adopted", adoptersPerCAGperPG.get(cag).get(pg), pg.getGroupName());
            }
        }
    }

    private Map<ConsumerAgentGroup, Map<ProductGroup, Integer>> countAdopters(Set<ConsumerAgent> consumerAgents, double timeStamp) {
        Map<ConsumerAgentGroup, Map<ProductGroup, Integer>> adoptersPerCAGperPG = new HashMap<ConsumerAgentGroup, Map<ProductGroup, Integer>>();
        Iterator<ConsumerAgent> iter = consumerAgents.iterator();
        ConsumerAgent randomDude = iter.next();
        for (ConsumerAgentGroup cag : randomDude.getAssociatedSimulationContainer().getSimulationConfiguration().getAgentConfiguration().getConsumerAgentGroups()) {
            Map<ProductGroup, Integer> productGroupMap = new HashMap<ProductGroup, Integer>();
            for (ProductGroup pg : randomDude.getAssociatedSimulationContainer().getSimulationConfiguration().getProductConfiguration().getProductGroups()) {
                productGroupMap.put(pg, 0);
            }
            adoptersPerCAGperPG.put(cag, productGroupMap);
        }
        for (ConsumerAgent currentAgent : consumerAgents) {
            Map<ProductGroup, Integer> productGroupMap = adoptersPerCAGperPG.get(currentAgent.getCorrespondingConsumerAgentGroup());
            for (AdoptedProduct adoptedProduct : currentAgent.getAdoptedProducts()) {
                productGroupMap.put(adoptedProduct.getCorrespondingProduct().getPartOfProductGroup(), productGroupMap.get(adoptedProduct.getCorrespondingProduct().getPartOfProductGroup())+1);
            }
            adoptersPerCAGperPG.put(currentAgent.getCorrespondingConsumerAgentGroup(), productGroupMap);
        }
        return adoptersPerCAGperPG;
    }
}
