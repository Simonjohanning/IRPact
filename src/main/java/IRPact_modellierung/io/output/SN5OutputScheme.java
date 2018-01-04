package IRPact_modellierung.io.output;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.products.AdoptedProduct;
import IRPact_modellierung.products.Product;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Lenovo on 23.08.2016.
 */
public class SN5OutputScheme extends OutputScheme {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    public SN5OutputScheme() {
    }

    public void writeConsumerAgentState(ConsumerAgent consumerAgent, double timeStamp) {

    }

    public void writeConsumerAdoption(Set<ConsumerAgent> consumerAgents, double timeStamp) {
        Map<ConsumerAgentGroup, Map<Product, Integer>> adoptersPerCAGperProduct = countAdopters(consumerAgents, timeStamp);
        for(ConsumerAgentGroup cag : adoptersPerCAGperProduct.keySet()){
            fooLog.info("ConsumerAgentGroup {} adopted the following way: \n", cag.getGroupName());
            for(Product pg : adoptersPerCAGperProduct.get(cag).keySet()){
                fooLog.info("{} instances of product {} have been adopted", adoptersPerCAGperProduct.get(cag).get(pg), pg.getName());
            }
            fooLog.info("\n");
        }
    }

    private int countAwareConsumers(ConsumerAgentGroup cag, Set<ConsumerAgent> consumerAgents, Product pg) {
        int awareConsumers = 0;
        for(ConsumerAgent currentAgent : consumerAgents){
            if(currentAgent.getCorrespondingConsumerAgentGroup().equals(cag)){
                if(currentAgent.isAwareOfProduct(pg)) awareConsumers++;
            }
        }
        return awareConsumers;
    }

    private Map<ConsumerAgentGroup, Map<Product, Integer>> countAdopters(Set<ConsumerAgent> consumerAgents, double timeStamp) {
        Map<ConsumerAgentGroup, Map<Product, Integer>> adoptersPerCAGperProduct = new HashMap<ConsumerAgentGroup, Map<Product, Integer>>();
        Iterator<ConsumerAgent> iter = consumerAgents.iterator();
        ConsumerAgent randomDude = iter.next();
        for (ConsumerAgentGroup cag : randomDude.getAssociatedSimulationContainer().getSimulationConfiguration().getAgentConfiguration().getConsumerAgentGroups()) {
            Map<Product, Integer> productGroupMap = new HashMap<Product, Integer>();
            for (Product product : randomDude.getAssociatedSimulationContainer().getProducts()){
                fooLog.debug("Setting up productGroupMap for product {}", product.getName());
                productGroupMap.put(product, 0);
            }
            adoptersPerCAGperProduct.put(cag, productGroupMap);
        }
        for (ConsumerAgent currentAgent : consumerAgents) {
            Map<Product, Integer> productMap = adoptersPerCAGperProduct.get(currentAgent.getCorrespondingConsumerAgentGroup());
            for (AdoptedProduct adoptedProduct : currentAgent.getAdoptedProducts()) {
                Integer adoptersBefore = productMap.get(adoptedProduct.getCorrespondingProduct());
                adoptersPerCAGperProduct.get(currentAgent.getCorrespondingConsumerAgentGroup()).remove(productMap);
                fooLog.debug("Integer {}, product {}, in market? {}", adoptersBefore, adoptedProduct.getCorrespondingProduct().getName(), adoptedProduct.getCorrespondingProduct().isIntroducedToMarket());
                adoptersPerCAGperProduct.get(currentAgent.getCorrespondingConsumerAgentGroup()).put(adoptedProduct.getCorrespondingProduct(), adoptersBefore + 1);
            }
        }
        return adoptersPerCAGperProduct;
    }
}
