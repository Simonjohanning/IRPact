package IRPact_modellierung.decision;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.network.CommunicationGraph;
import IRPact_modellierung.preference.Preference;
import IRPact_modellierung.preference.Value;
import IRPact_modellierung.helper.FilterHelper;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.network.SNNode;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.products.ProductGroupAttribute;
import IRPact_modellierung.simulation.SimulationContainer;
import antlr.SemanticException;
import org.apache.logging.log4j.LogManager;

import java.util.*;

//TODO write documentation

public class TakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess extends UtilitarianConsumerAgentAdoptionDecisionProcess {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    public TakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess(UtilityFunction associatedUtilityFunction) {
        super(associatedUtilityFunction);
    }

    public boolean betterProductAvailable(SimulationContainer simulationContainer, ConsumerAgent consumerAgent, Product productToCompare, Set<Product> potentialProducts, double systemTime) throws SemanticException{
        Set<Product> potentiallyBetterProducts = potentialProducts;
        for(Value currentValue : LazynessHelper.orderValuesByPreference(consumerAgent.getPreferences())) {
            double oldProductUtilityForValue = calculateUtility(simulationContainer, productToCompare, consumerAgent, systemTime, currentValue);
            for (Product currentProduct : potentiallyBetterProducts) {
                double referenceProductUtilityForValue = calculateUtility(simulationContainer, currentProduct, consumerAgent, systemTime, currentValue);
                fooLog.debug("oldProdUt for prod {} is {}, whereas new is {} for prod {}", productToCompare.getName(), oldProductUtilityForValue, currentProduct.getName(), referenceProductUtilityForValue);
                if (referenceProductUtilityForValue > oldProductUtilityForValue) return true;
                else if (referenceProductUtilityForValue < oldProductUtilityForValue)
                    potentiallyBetterProducts.remove(currentProduct);
            }
            if (potentiallyBetterProducts.isEmpty()) return false;
        }
        Map<Product, Integer> noAdoptersInNeighbourhood = new HashMap<Product, Integer>(potentiallyBetterProducts.size());
        for(Product currentProduct : potentiallyBetterProducts){
            noAdoptersInNeighbourhood.put(currentProduct, 0);
        }
        //for all neighbours
        for(SNNode neighbouringNode : ((CommunicationGraph) simulationContainer.getSocialNetwork().getSocialGraph()).getCommunicationNeighbours(consumerAgent.getCorrespondingNodeInSN())){
            //count how often which product was adopted
            for(Product currentProduct : potentiallyBetterProducts){
                if(simulationContainer.getsNMap().get(neighbouringNode).getAdoptedProducts().contains(currentProduct)){
                    Integer noAdopters = noAdoptersInNeighbourhood.get(currentProduct);
                    noAdoptersInNeighbourhood.remove(currentProduct);
                    noAdoptersInNeighbourhood.put(currentProduct, noAdopters+1);
                }
            }
        }
        //try to derive most adopted product (null if several products are equally good)
        Product bestProduct;
        try {
            bestProduct = LazynessHelper.findBestProduct(noAdoptersInNeighbourhood);
            //will return whether the bestProduct is the productToCompare
            return bestProduct != productToCompare;
        } catch (SemanticException e) {
            throw e;
        }
    }

    public Product makeProductAdoptionDecision(SimulationContainer simulationContainer, ConsumerAgent consumerAgent, Set<Product> potentialProducts, double systemTime) {
        ArrayList<Value> orderedFactors = LazynessHelper.orderValuesByPreference(consumerAgent.getPreferences());
        //fooLog.info("ordered factors for agent {}: {}", consumerAgent.getAgentID(), deriveValueStrings(orderedFactors));
        Integer factorIndex = 0;
        Set<Product> consideredProducts = new HashSet<Product>(potentialProducts.size());
        if(potentialProducts.contains(null)) fooLog.info("considering products {}", potentialProducts);
        while(factorIndex < orderedFactors.size()){
            double maxUtility = calculateMaxUtility(simulationContainer, potentialProducts, consumerAgent, systemTime, orderedFactors.get(factorIndex));
            fooLog.debug("consideredProducts {}",consideredProducts);
            fooLog.debug("orderedFactor of index {} is {}",factorIndex, orderedFactors.get(factorIndex));
            for(Product consideredProduct : potentialProducts){
                fooLog.debug("currentProduct {}",consideredProduct);
                //don't use "==" for numerical reasons
                if(Math.abs(calculateUtility(simulationContainer, consideredProduct, consumerAgent, systemTime, orderedFactors.get(factorIndex)) - maxUtility) < 10E-8){
                    consideredProducts.add(consideredProduct);
                }else{
                    fooLog.debug("utility for product {} is {} with maxUtility being {}", consideredProduct, calculateUtility(simulationContainer, consideredProduct, consumerAgent, systemTime, orderedFactors.get(factorIndex)), maxUtility);
                }
            }
            if(consideredProducts.size() == 1){
                fooLog.debug("Adopting product {} since its the only one available (psize {}, csize {})", ((Product) consideredProducts.toArray()[0]).getName(), potentialProducts.size(), consideredProducts.size());
                return (Product) consideredProducts.toArray()[0];
            }
            factorIndex++;
            potentialProducts = consideredProducts;
            consideredProducts = new HashSet<Product>(potentialProducts.size());
        }
        consideredProducts = potentialProducts;
        //if no best choice found, imitate

        Map<Product, Integer> noAdoptersInNeighbourhood = new HashMap<Product, Integer>(consideredProducts.size());
        for(Product currentProduct : consideredProducts){
            noAdoptersInNeighbourhood.put(currentProduct, 0);
        }
        //for all neighbours
        fooLog.debug("social network is {}", simulationContainer.getSocialNetwork().getSocialGraph());
        fooLog.debug("neighbours are {}", simulationContainer.getSocialNetwork().getSocialGraph().getNeighbours());
        fooLog.debug("neighbours of ca {} are {}", consumerAgent, simulationContainer.getSocialNetwork().getSocialGraph().getNeighbours().get(consumerAgent.getCorrespondingNodeInSN()));
        for(SNNode neighbouringNode : ((CommunicationGraph) simulationContainer.getSocialNetwork().getSocialGraph()).getCommunicationNeighbours(consumerAgent.getCorrespondingNodeInSN())){
            //count how often which product was adopted
            for(Product currentProduct : consideredProducts){
                if(simulationContainer.getsNMap().get(neighbouringNode).getAdoptedProducts().contains(currentProduct)){
                    Integer noAdopters = noAdoptersInNeighbourhood.get(currentProduct);
                    noAdoptersInNeighbourhood.remove(currentProduct);
                    noAdoptersInNeighbourhood.put(currentProduct, noAdopters+1);
                }
            }
        }
        for(Product currentProduct : consideredProducts){
            fooLog.debug("product {} was adopted {} times already by neighbours", currentProduct, noAdoptersInNeighbourhood.get(currentProduct));
        }
        //try to derive most adopted product (null if several products are equally good)
        //TODO check if this is the right way
        Product bestProduct = null;
        try {
            bestProduct = LazynessHelper.findBestProduct(noAdoptersInNeighbourhood);
        } catch (SemanticException e) {
            e.printStackTrace();
        }
        if(bestProduct != null){
            return bestProduct;
        }
        else{
            fooLog.info("no best product found!!");
            //if one of them is the standard product (if not it will be null), adopt this, otherwise adopt a random product
            Product standardProduct = LazynessHelper.determineStandardProduct(consideredProducts, simulationContainer);
            if(standardProduct != null) return standardProduct;
            else{
                return LazynessHelper.chooseProduct(consideredProducts);
            }
        }
    }

    private ArrayList<String> deriveValueStrings(ArrayList<Value> orderedFactors) {
        ArrayList<String> returnedValueStrings = new ArrayList<String>();
        for(Value blubb : orderedFactors){
            returnedValueStrings.add(blubb.getName());
        }
        return returnedValueStrings;
    }

    private double calculateMaxUtility(SimulationContainer simulationContainer, Set<Product> consideredProducts, ConsumerAgent consumerAgent, double systemTime, Value value) {
        double maxUtility = 0.0;
        fooLog.debug("considered products in calMaxUt is {}", consideredProducts);
        for(Product currentProduct : consideredProducts){
            double productUtility = calculateUtility(simulationContainer, currentProduct, consumerAgent, systemTime, value);
            fooLog.debug("in calMaxUtil in TTB for product {}: utility is {}", currentProduct, productUtility);
            if(productUtility > maxUtility) maxUtility = productUtility;
        }
        return maxUtility;
    }

    //TODO inherit from something else (so value can be used)
    protected double calculateUtility(SimulationContainer simulationContainer, Product product, ConsumerAgent consumerAgent, double systemTime) {
        return 0;
    }

    private double calculateUtility(SimulationContainer simulationContainer, Product product, ConsumerAgent consumerAgent, double systemTime, Value consideredValue){
        if(product == null) throw new IllegalArgumentException("Error!! Trying to calculate the utility of a non-existing product!!");
        else if(product.getProductAttributes() == null) throw new IllegalArgumentException("Error!! Trying to calculate the utility of a product with empty attributes!!");
        //identify corresponding preference (null if several or none are found)
        Preference correspondingPreference = FilterHelper.findCorrespondingPreference(consumerAgent.getPreferences(), consideredValue);
        if(correspondingPreference == null){
            fooLog.error("Error!! Found multiple preferences for value {} for consumer agent {}!!!. \n This shouldn't occur and the product {} will yield a negative utility!!", consideredValue, consumerAgent, consideredValue);
            return -Double.MAX_VALUE;
        }else{
            double cumulatedUtility = 0.0;
            Map<ProductGroupAttribute, Double> pgavMapping = FilterHelper.selectPGAVMappingForValue(simulationContainer.getSimulationConfiguration().getPreferenceConfiguration().getProductGroupAttributePreferenceMapping(), consideredValue);
            for(ProductAttribute currentAttribute : product.getProductAttributes()){
                /*fooLog.info("annoyance 1: {}", correspondingPreference.getStrength());
                fooLog.info("annoyance 2: {}", pgavMapping.get(currentAttribute.getCorrespondingProductGroupAttribute()));
                fooLog.info("annoyance 2.3: {}", consumerAgent.getPerceivedProductAttributeValues());
                fooLog.info("annoyance 2.4: {}", currentAttribute.getCorrespondingProductGroupAttribute());
                fooLog.info("annoyance 2.5: {}", consumerAgent.getPerceivedProductAttributeValues().get(currentAttribute));
                fooLog.info("annoyance 3: {}", consumerAgent.getPerceivedProductAttributeValues().get(currentAttribute.getCorrespondingProductGroupAttribute()).calculateProductAttributePerception(systemTime));
                fooLog.info("Calculating partial utility for pa {} ({}*{}*{})", currentAttribute,correspondingPreference.getStrength(),pgavMapping.get(currentAttribute.getCorrespondingProductGroupAttribute()),consumerAgent.getPerceivedProductAttributeValues().get(currentAttribute.getCorrespondingProductGroupAttribute()).calculateProductAttributePerception(systemTime));
                */cumulatedUtility += correspondingPreference.getStrength() * pgavMapping.get(currentAttribute.getCorrespondingProductGroupAttribute()) * consumerAgent.getPerceivedProductAttributeValues().get(currentAttribute).calculateProductAttributePerception(systemTime);
            }
            return cumulatedUtility;
        }
    }
}