package IRPact_modellierung.helper;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.network.SNNode;
import IRPact_modellierung.preference.Preference;
import IRPact_modellierung.preference.Value;
import IRPact_modellierung.needs.Need;
import IRPact_modellierung.preference.ProductGroupAttributeValueMapping;
import IRPact_modellierung.products.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class to bundle a number of methods selecting a sub data structure from larger data structures.
 *
 * @author Simon Johanning
 */
public class FilterHelper {

    /**
     * Method that selects all products an agent is aware of
     * (all product which have a 'true' value associated with them)
     *
     * @param awarenessMap The awareness map used to determine if an agent is aware of the product
     * @return The products an agent is aware of (that have 'true' associated with them in the awareness map)
     */
    public static Set<Product> selectKnownProducts(Map<Product, Boolean> awarenessMap){
        Set<Product> returnSet = new HashSet<Product>();
        for(Product product : awarenessMap.keySet()){
            if(awarenessMap.get(product)) returnSet.add(product);
        }
        return returnSet;
    }

    /**
     * Method to filter all products from a set of products that fulfil a given need.
     *
     * @param candidateProducts The product to be filtered for need fulfilment
     * @param need The need candidate products need to fulfil
     * @return All products from the candidateProducts set that fulfil the given need
     */
    public static Set<Product> filterProductsForNeed(Set<Product> candidateProducts, Need need) {
        Set<Product> returnSet = new HashSet<Product>();
        for(Product product : candidateProducts){
            if(product.getPartOfProductGroup().getNeedsSatisfied().contains(need)) returnSet.add(product);
        }
        return returnSet;
    }

    /**
     * Method to filter out all products that can't be adopted by the respective agent at this time,
     * operating on the basis of product dependencies (prerequisite products need to be adopted,
     * excluded product groups must not be adopted)
     *
     * @param potentialProducts The product that are to be filtered
     * @param consumerAgentConcerned The respective agent for which the adoptability should be checked
     * @return The set of products which the agent can adopt (that are not excluded by product dependencies)
     */
    public static Set<Product> filterUnadoptableProducts(Set<Product> potentialProducts, ConsumerAgent consumerAgentConcerned) {
        Set<Product> returnSet = new HashSet<Product>();
        for(Product potentialProduct : potentialProducts){
            //if product is not yet adopted
            if(!(consumerAgentConcerned.getAdoptedProducts().contains(potentialProduct))){
                //assume not all prereq. products have been adopted
                boolean productAdoptable = false;
                //if it turns out not to be the case...
                if(LazynessHelper.allPrerequisiteProductsAdopted(potentialProduct, consumerAgentConcerned)) productAdoptable = true;
                //...check whether an adopted product excludes this product
                for(AdoptedProduct adoptedProduct : consumerAgentConcerned.getAdoptedProducts()){
                    if(adoptedProduct.getCorrespondingProduct().getPartOfProductGroup().getExcludeProductGroup().contains(potentialProduct.getPartOfProductGroup())){
                        productAdoptable = false;
                        break;
                    }
                }
                //if still adoptable, add it to the set
                if(productAdoptable) returnSet.add(potentialProduct);
            }
        }
        return returnSet;
    }

    /**
     * A method that filters all products from a set that are not yet introduced into the market
     *
     * @param products The products that are to be checked upon market introduction
     * @return All products from the parameter set that are on the market (isIntroducedToMarket is true)
     */
    public static Set<Product> filterProductsNotOnMarketYet(Set<Product> products) {
        Set<Product> returnSet = new HashSet<Product>();
        for(Product product : products){
            if(product.isIntroducedToMarket()) returnSet.add(product);
        }
        return returnSet;
    }

    //TODO exactly the same as above, decide on which one to keep

    public static Set<Product> filterUnintroducedProducts(Set<Product> potentialProducts) {
        Set<Product> returnSet = new HashSet<Product>(potentialProducts.size());
        for(Product product : potentialProducts){
            if(product.isIntroducedToMarket()) returnSet.add(product);
        }
        return returnSet;
    }

    /**
     * Method to retrieve the preference corresponding to a value within a set.
     * Will return null no preference correspond to the value, and will throw an exception if several do
     * (since preferences are thought to have a 1-to-1 correspondence to values)
     *
     * @param preferences The preference set from which the preference corresponding to the value is to be selected
     * @param consideredValue The value for which the corresponding reference should be retrieved
     *
     * @return The preference in the preference set corresponding to the value (null if none do)
     *
     * @throws IllegalArgumentException Will be thrown when several preferences refer to the same value (which is thought to be a 1-to-1 mapping)
     */
    public static Preference findCorrespondingPreference(Set<Preference> preferences, Value consideredValue) throws IllegalArgumentException{
        Preference correspondingPreference = null;
        for(Preference currentPreference : preferences){
            if((currentPreference.getValue().equals(consideredValue)) && (correspondingPreference == null)){
                correspondingPreference = currentPreference;
            }else if((currentPreference.getValue().equals(consideredValue)) && (correspondingPreference != null)) throw new IllegalArgumentException("Several preferences in the given set correspond to the referenced value "+consideredValue+";\nThese are (at least) "+correspondingPreference+" and "+currentPreference+"!!\n This is an error, since the correspondence of preference and value (for one agent) should be 1-to-1!!");
        }
        return correspondingPreference;
    }

    /**
     * Method to select a all product group attributes with their respective numerical value corresponding to a given value from a set of ProductGroupAttributeValueMappings.
     * Will retrieve all relevant product group attributes corresponding to the respective value
     *
     * @param productGroupAttributePreferenceMapping The set of ProductGroupAttributeValueMappings to be filtered for the respective value
     * @param consideredValue The value for which the product group attributes should be selected for
     * @return A map of all product group attributes and their respective numerical value (mapping strength) corresponding to the value
     */
    public static Map<ProductGroupAttribute, Double> selectPGAVMappingForValue(Set<ProductGroupAttributeValueMapping> productGroupAttributePreferenceMapping, Value consideredValue) {
        Map<ProductGroupAttribute, Double> returnMap = new HashMap<ProductGroupAttribute, Double>();
        //filter all product attributes whose value corresponds to the given value
        for(ProductGroupAttributeValueMapping currentPGAVMapping : productGroupAttributePreferenceMapping){
            if(currentPGAVMapping.getValue().equals(consideredValue)){
                returnMap.put(currentPGAVMapping.getProductGroupAttribute(), currentPGAVMapping.getMappingStrength());
            }
        }
        return returnMap;
    }

    /**
     * Method to check whether the product to be checked is of the same product group as any product in the reference set
     *
     * @param productToCheck The product whose product group affiliation is to be checked
     * @param adoptedProducts The reference set against which the product group is checked
     * @return true if any product in the reference set is of the same product group as the product to check, false if it doesn't correspond to any
     */
    private static boolean checkForProductGroupMatch(Product productToCheck, Set<AdoptedProduct> adoptedProducts) {
        for(AdoptedProduct currentProduct : adoptedProducts){
            if(currentProduct.getCorrespondingProduct().getPartOfProductGroup() == productToCheck.getPartOfProductGroup()) return true;
        }
        return false;
    }

    /**
     * Helper to filter a set of SNNodes from an Integer, SNNode-Set map.
     * All SNNodes in the nodesToFilter set will be removed from any of the sets associated with the integer
     *
     * @param integerSetHashMap HashMap to filter the SNNode set from
     * @param nodesToFilter Nodes to filter from all values of the HashMap
     * @return A HashMap containing none of the nodesToFilter in the value set
     */
    public static HashMap<Integer, Set<SNNode>> filterIntegerNodeMap(HashMap<Integer, Set<SNNode>> integerSetHashMap, Set<SNNode> nodesToFilter) {
        HashMap<Integer, Set<SNNode>> returnMap = new HashMap<>();
        for(Integer currentKey : integerSetHashMap.keySet()){
            Set<SNNode> filteredSet = new HashSet<>();
            for(SNNode currentNode : integerSetHashMap.get(currentKey)){
                filteredSet.add(currentNode);
            }
            filteredSet.removeAll(nodesToFilter);
            returnMap.put(currentKey, filteredSet);
        }
        return returnMap;
    }
}
