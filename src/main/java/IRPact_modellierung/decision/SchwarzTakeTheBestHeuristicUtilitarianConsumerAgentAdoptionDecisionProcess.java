package IRPact_modellierung.decision;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.network.CommunicationGraph;
import IRPact_modellierung.preference.Preference;
import IRPact_modellierung.preference.Value;
import IRPact_modellierung.helper.FilterHelper;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.helper.MapUtil;
import IRPact_modellierung.helper.ValueConversionHelper;
import IRPact_modellierung.network.SNNode;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.products.ProductGroup;
import IRPact_modellierung.products.ProductGroupAttribute;
import IRPact_modellierung.simulation.SimulationContainer;
import antlr.SemanticException;
import org.apache.logging.log4j.LogManager;

import java.util.*;

//TODO add reference Schwarz

/**
 * Decision process based on the model described in Schwarz.
 * This decision process is set up analogously to TakeTheBestHeuristic,
 * however is adapted to the water technology diffusion model in Schwarz.
 * For the model mechanisms, the reader is referred to the more general TakeTheBestHeuristic as well as the userDocumentation.
 * For the concrete model to be compatible with the modeling framework,
 * decision variables based on behavioural control are modeled as (dummy) values based on the products importance.
 *
 * @author Simon Johanning
 */
public class SchwarzTakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess extends UtilitarianConsumerAgentAdoptionDecisionProcess {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    public SchwarzTakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess(UtilityFunction associatedUtilityFunction) {
        super(associatedUtilityFunction);
    }

    //TODO rework to incorporate changes!!!

    /**
     * Method to test whether a better product is available.
     *
     * @param simulationContainer
     * @param consumerAgent
     * @param productToCompare
     * @param potentialProducts
     * @param systemTime
     * @return
     */
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
        for(SNNode neighbouringNode : ((CommunicationGraph) simulationContainer.getSocialNetwork().getSocialGraph()).getCommunicationNeighbours().get(consumerAgent)){
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
        try {
            Product bestProduct = LazynessHelper.findBestProduct(noAdoptersInNeighbourhood);
            return bestProduct != productToCompare;
        } catch (SemanticException e) {
            throw e;
        }
    }

    //TODO improve documentation (including error handling)

    /**
     * Method to make adoption decision for consumerAgent, adopting the product with the highest utility of potentialProducts at systemTime
     *
     * @param simulationContainer The container the simulation runs in
     * @param consumerAgent The consumer agent to adopt a product
     * @param potentialProducts The set of products out of which a consumer will adopt
     * @param systemTime The current time of the simulation
     */
    public Product makeProductAdoptionDecision(SimulationContainer simulationContainer, ConsumerAgent consumerAgent, Set<Product> potentialProducts, double systemTime) throws SemanticException{

        //fooLog.info("ordered factors for agent {}: {}", consumerAgent.getAgentID(), deriveValueStrings(orderedFactors));
        Map<Value, Double> valueImportanceMap = LazynessHelper.preferenceToValueQuantityMap(consumerAgent.getPreferences());
        //include factors that driven by product importance
        Set<String> includedDummyValues = new HashSet<String>();
//TODO implement attitude importance into the decision scheme and take it from there
//        for(Product currentProduct : potentialProducts){
//            for(ProductAttribute currentAttribute : currentProduct.getProductAttributes()){
//                if(currentAttribute.getImportance() > 0.0){
//                    if(!includedDummyValues.contains("dummyValue_"+currentAttribute.getCorrespondingProductGroupAttribute().getName())) valueImportanceMap.put(new Value("dummyValue_"+currentAttribute.getCorrespondingProductGroupAttribute().getName()), currentAttribute.getImportance());
//                    includedDummyValues.add("dummyValue_"+currentAttribute.getCorrespondingProductGroupAttribute().getName());
//                }
//            }
//        }
        LinkedHashMap<Value, Double> orderedFactorMap = (LinkedHashMap) MapUtil.sortByValue(valueImportanceMap);
        ArrayList<Value> orderedFactors = ValueConversionHelper.linkedHashMapToArrayList(orderedFactorMap);
        /*fooLog.info("orderedFactors is:");
        for(Value currentValue : orderedFactors){
            fooLog.info(currentValue);
        }*/
        ProductGroup consideredProductGroup = LazynessHelper.findCommonProductGroup(potentialProducts);
        Integer factorIndex = 0;
        Set<Product> consideredProducts = new HashSet<Product>(potentialProducts.size());
        if(potentialProducts.contains(null)) fooLog.info("considering products {}", potentialProducts);
        //cycles through the products until a (or none) products is found
        //TODO revise if it was rewritten correctly
        while(factorIndex < orderedFactors.size()){
            if(orderedFactors.get(factorIndex).getName().equals("socialNorm")){
                fooLog.debug("using social norm as a factor");
                try {
                    consideredProducts = LazynessHelper.findBestProducts(LazynessHelper.countNoAdoptersInNeighbourhood(consideredProducts, consumerAgent, simulationContainer));
                } catch (SemanticException e) {
                    throw e;
                }
            }
            else {
                double maxUtility = calculateMaxUtility(simulationContainer, potentialProducts, consumerAgent, systemTime, orderedFactors.get(factorIndex));
                fooLog.debug("consideredProducts {}", consideredProducts);
                fooLog.debug("orderedFactor of index {} is {}", factorIndex, orderedFactors.get(factorIndex));
                for (Product consideredProduct : potentialProducts) {
                    fooLog.debug("currentProduct {}", consideredProduct);
                    //don't use "==" for numerical reasons
                    if (Math.abs(calculateUtility(simulationContainer, consideredProduct, consumerAgent, systemTime, orderedFactors.get(factorIndex)) - maxUtility) < 10E-8) {
                        consideredProducts.add(consideredProduct);
                    } else {
                        fooLog.debug("utility for product {} is {} with maxUtility being {}", consideredProduct, calculateUtility(simulationContainer, consideredProduct, consumerAgent, systemTime, orderedFactors.get(factorIndex)), maxUtility);
                    }
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
        try {
            Map<Product, Integer> noAdoptersInNeighbourhood = LazynessHelper.countNoAdoptersInNeighbourhood(consideredProducts, consumerAgent, simulationContainer);
            //try to derive most adopted product (null if several products are equally good)
            Product bestProduct = LazynessHelper.findBestProduct(noAdoptersInNeighbourhood);
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
        } catch (SemanticException e) {
            throw e;
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

    private double calculateUtility(SimulationContainer simulationContainer, Product product, ConsumerAgent consumerAgent, double systemTime, Value consideredValue) throws IllegalArgumentException{
        if(product == null) throw new IllegalArgumentException("Error!! Trying to calculate the utility of a non-existing product!!");
        //identify corresponding preference (null if several or none are found)
        Preference correspondingPreference = FilterHelper.findCorrespondingPreference(consumerAgent.getPreferences(), consideredValue);
//TODO implement attitude importance into the decision scheme and take it from there
//        if(correspondingPreference == null){
//            if(consideredValue.getName().contains("dummyValue_investmentCost")){
//                ProductAttribute investmentCostPA = LazynessHelper.getInvestmentCostPA(product);
//                return investmentCostPA.getImportance()*investmentCostPA.getValue();
//            }else if(consideredValue.getName().contains("dummyValue_compatibility")){
//                ProductAttribute compatibilityPA = LazynessHelper.getCompatibilityPA(product);
//                return compatibilityPA.getImportance()*compatibilityPA.getValue();
//            }else {
//                fooLog.error("Error!! Found multiple preferences for value {} for consumer agent {}!!!. \n This shouldn't occur and the product {} will yield a negative utility!!", consideredValue, consumerAgent, consideredValue);
//                return -Double.MAX_VALUE;
//            }
//        }else{
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
