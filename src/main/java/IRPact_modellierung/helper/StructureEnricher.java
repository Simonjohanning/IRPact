package IRPact_modellierung.helper;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.network.SNNode;
import IRPact_modellierung.preference.Preference;
import IRPact_modellierung.preference.Value;
import IRPact_modellierung.distributions.Distribution;
import IRPact_modellierung.events.MarketIntroductionEvent;
import IRPact_modellierung.events.ProductDiscontinuationEvent;
import IRPact_modellierung.needs.Need;
import IRPact_modellierung.preference.ProductGroupAttributeValueMapping;
import IRPact_modellierung.products.*;

import java.util.*;

/**
 * Helper class to provide methods to enrich the structure of data structures.
 * Will generally return a map with the data entries as values for easier access by handy keys.
 *
 * @author Simon Johanning
 */
public class StructureEnricher {

    /**
     * Method to associate a set of productGroupAttribute with their names and their product group.
     * For all provided product groups, the method will create a map of the name of the productGroupAttribute and the actual productGroupAttribute
     * (productGroupAttributes whose productGroups are not provided in the argument will be ignored)
     *
     * @param productGroupAttributes The productGroupAttributes that are to be associated with their names and product group
     * @param productGroups The productGroups of the productGroupAttributes that should be associated
     * @return A map of product groups and a value consisting of a map of the productGroupAttributes name and the PGA
     * @throws IllegalArgumentException Will be thrown when the set of productGroups or productGroupAttributes is empty, as well as when a productGroupAttribute does not have a name
     */
    public static Map<ProductGroup, Map<String, ProductGroupAttribute>> attachProductGroupAttributeNames(Set<ProductGroupAttribute> productGroupAttributes, Set<ProductGroup> productGroups) throws IllegalArgumentException{
        if(productGroups.isEmpty()) throw new IllegalArgumentException("The set of product groups to attach attribute names is empty");
        else if(productGroupAttributes.isEmpty()) throw new IllegalArgumentException("The set of product group attributes to attach their names to is empty");
        else{
            Map<ProductGroup, Map<String, ProductGroupAttribute>> returnMap = new HashMap<ProductGroup, Map<String, ProductGroupAttribute>>();
            for(ProductGroup productGroup : productGroups){
                Map<String, ProductGroupAttribute> pgaNameMap = new HashMap<String, ProductGroupAttribute>();
                for(ProductGroupAttribute pgaInPG : productGroup.getProductGroupAttributes()){
                    if(productGroupAttributes.contains(pgaInPG)){
                        if(pgaInPG.getName().equals("")) throw new IllegalArgumentException("ProductGroupAttribute "+pgaInPG+" does not have a name");
                        pgaNameMap.put(pgaInPG.getName(), pgaInPG);
                    }
                }
                returnMap.put(productGroup, pgaNameMap);
            }
            return returnMap;
        }
    }

    /**
     * Method to attach the the name of a set of distributions to them.
     *
     * @param distributions The distributions to attach the name to
     * @return A map with the name of the distributions as keys (and the distribution as values)
     * @throws IllegalArgumentException Will be thrown when the set of distributions is empty or a distribution doesn't have a name
     */
    public static Map<String,Distribution> attachDistributionNames(Set<Distribution> distributions) throws IllegalArgumentException{
        if(distributions.isEmpty()) throw new IllegalArgumentException("The set of distributions to attach a name to is empty!!");
        HashMap<String, Distribution> distributionMap = new HashMap<String, Distribution>(distributions.size());
        for(Distribution distribution : distributions){
            if(distribution.getName().equals("")) throw new IllegalArgumentException("Distribution "+distribution+" does not have a name!!");
            distributionMap.put(distribution.getName(), distribution);
        }
        return distributionMap;
    }

    /**
     * Method to attach the the name of a set of productGroups to them.
     *
     * @param productGroups The productGroups to attach the name to
     * @return A map with the name of the productGroups as keys (and the distribution as values)
     * @throws IllegalArgumentException Will be thrown when the set of productGroups is empty or a productGroup doesn't have a name
     */

    public static Map<String, ProductGroup> attachProductGroupNames(Set<ProductGroup> productGroups) throws IllegalArgumentException{
        if(productGroups.isEmpty()) throw new IllegalArgumentException("The set of productGroups to attach a name to is empty!!");
        HashMap<String, ProductGroup> productGroupMap = new HashMap<String, ProductGroup>(productGroups.size());
        for(ProductGroup productGroup : productGroups){
            if(productGroup.getGroupName().equals("")) throw new IllegalArgumentException("productGroup "+productGroup+" does not have a name!!");
            productGroupMap.put(productGroup.getGroupName(), productGroup);
        }
        return productGroupMap;
    }

    /**
     * Method to convert a set of MarketIntroductionEvents to a sorted list of MarketIntroductionEvent in ascending order.
     * Will throw an error if the set is empty
     *
     * @param marketIntroductionEvents The MarketIntroductionEvents to be sorted
     * @return A sorted List of MarketIntroductionEvents (in ascending (in time) order)
     * @throws IllegalArgumentException Will be thrown if the set of MarketIntroductionEvents is empty
     */
    public static List<MarketIntroductionEvent> sortMarketIntroductionEventSetToListAscending(Set<MarketIntroductionEvent> marketIntroductionEvents) throws IllegalArgumentException{
        if(marketIntroductionEvents.isEmpty()) throw new IllegalArgumentException("The set of MarketIntroductionEvents to be sorted is empty!!");
        List<MarketIntroductionEvent> returnList = new LinkedList<MarketIntroductionEvent>();
        returnList.addAll(marketIntroductionEvents);
        Collections.sort(returnList);
        return returnList;
    }

    /**
     * Method to convert a set of ProductDiscontinuationEvents to a sorted list of ProductDiscontinuationEvents in ascending order.
     * Will throw an error if the set is empty
     *
     * @param productDiscontinuationEvents The ProductDiscontinuationEvents to be sorted
     * @return A sorted List of ProductDiscontinuationEvents (in ascending (in time) order)
     * @throws IllegalArgumentException Will be thrown if the set of ProductDiscontinuationEvents is empty
     */
    public static List<ProductDiscontinuationEvent> sortProductDiscontinuationEventSetToListAscending(Set<ProductDiscontinuationEvent> productDiscontinuationEvents) throws IllegalArgumentException{
        if(productDiscontinuationEvents.isEmpty()) throw new IllegalArgumentException("The set of ProductDiscontinuationEvent to be sorted is empty!!");
        List<ProductDiscontinuationEvent> returnList = new LinkedList<ProductDiscontinuationEvent>();
        returnList.addAll(productDiscontinuationEvents);
        Collections.sort(returnList);
        return returnList;
    }

    /**
     * Method to attach the name of fixedProductDescriptions to a set of fixedProductDescriptions.
     * Will return a map with their names as keys and the corresponding fixedProductDescriptions as value for easier access.
     * Will throw an exception if the set of fixedProductDescriptions is empty
     *
     * @param fixedProductDescriptions The fixedProductDescriptions whose names should be attached
     * @return A map of fixedProductDescriptions as values and their respective names as keys
     * @throws IllegalArgumentException Will be thrown if the set of fixedProductDescriptions is empty or one of them doesnt have a name
     */
    public static HashMap<String, FixedProductDescription> attachFixedProductDescriptionNames(Set<FixedProductDescription> fixedProductDescriptions) throws IllegalArgumentException{
        if(fixedProductDescriptions.isEmpty()) throw new IllegalArgumentException("The set of fixedProductDescriptions to attach their names to is empty!!");
        HashMap<String, FixedProductDescription> fpdMap = new HashMap<String, FixedProductDescription>(fixedProductDescriptions.size());
        for(FixedProductDescription fpd : fixedProductDescriptions){
            if(fpd.getName().equals("")) throw new IllegalArgumentException("The name of FixedProductDescription "+fpd+" is empty!! FixedProductDescriptions should always be given a name!!");
            fpdMap.put(fpd.getName(), fpd);
        }
        return fpdMap;
    }

    /**
     * Method to attach the name of Values to a set of Values.
     * Will return a map with their names as keys and the corresponding Values as value for easier access.
     * Will throw an exception if the set of Values is empty
     *
     * @param values The Values whose names should be attached
     * @return A map of Values as values and their respective names as keys
     * @throws IllegalArgumentException Will be thrown if the set of Values is empty or one of them doesnt have a name
     */
    public static HashMap<String, Value> attachValueNames(Set<Value> values) {
        if(values.isEmpty()) throw new IllegalArgumentException("The set of Values to attach their names to is empty!!");
        HashMap<String, Value> returnMap = new HashMap<String, Value>(values.size());
        for(Value value : values){
            if(value.getName().equals("")) throw new IllegalArgumentException("The name of Values "+value+" is empty!! Value should always be given a name!!");
            returnMap.put(value.getName(), value);
        }
        return returnMap;
    }

    /**
     * Method to attach the name of ConsumerAgentGroups to a set of ConsumerAgentGroups.
     * Will return a map with their names as keys and the corresponding ConsumerAgentGroups as value for easier access.
     * Will throw an exception if the set of ConsumerAgentGroups is empty
     *
     * @param consumerAgentGroups The ConsumerAgentGroups whose names should be attached
     * @return A map of ConsumerAgentGroups as values and their respective names as keys
     * @throws IllegalArgumentException Will be thrown if the set of ConsumerAgentGroups is empty or one of them doesnt have a name
     */
    public static HashMap<String, ConsumerAgentGroup> attachConsumerAgentGroupNames(Set<ConsumerAgentGroup> consumerAgentGroups) throws IllegalArgumentException{
        if(consumerAgentGroups.isEmpty()) throw new IllegalArgumentException("The set of ConsumerAgentGroups to attach their names to is empty!!");
        HashMap<String, ConsumerAgentGroup> returnMap = new HashMap<String, ConsumerAgentGroup>(consumerAgentGroups.size());
        for(ConsumerAgentGroup cag : consumerAgentGroups){
            if(cag.getGroupName().equals("")) throw new IllegalArgumentException("The name of ConsumerAgentGroup "+cag+" is empty!! ConsumerAgentGroups should always be given a name!!");
            returnMap.put(cag.getGroupName(), cag);
        }
        return returnMap;
    }

    /**
     * Method to attach the value of Preferences to a set of Preferences.
     * Will return a map with their values as keys and the corresponding Preference as value for easier access.
     * Will throw an exception if the set of Preferences is empty
     *
     * @param preferences The Preferences whose values should be attached
     * @return A map of Preferences as values and their respective values as keys
     * @throws IllegalArgumentException Will be thrown if the set of Preferences is empty or one of them doesnt have a value
     */
    public static HashMap<Value, Preference> attachPreferenceValues(Set<Preference> preferences) throws IllegalArgumentException{
        if(preferences.isEmpty()) throw new IllegalArgumentException("The set of Preferences to attach their values to is empty!!");
        HashMap<Value, Preference> returnMap = new HashMap<Value, Preference>(preferences.size());
        for(Preference preference : preferences){
            if(preference.getValue() == null) throw new IllegalArgumentException("The value of Preference "+preference+" is null!! Preferences should always be associated a value with!!");
            returnMap.put(preference.getValue(), preference);
        }
        return returnMap;
    }

    /**
     * Method to derive the values that a group of consumer agent groups use.
     * Will return all values any of the consumerAgentGroups has, and throw an error if the set of consumerAgentGroups is empty.
     *
     * @param consumerAgentGroups The consumerAgentGroups to derive the Values from
     * @return The set of values employed by any of the consumerAgentGroups
     * @throws IllegalArgumentException Will be thrown if the set of consumerAgentGroups is empty
     */
    public static Set<Value> getValuesUsed(Set<ConsumerAgentGroup> consumerAgentGroups) throws IllegalArgumentException{
        if(consumerAgentGroups.isEmpty()) throw new IllegalArgumentException("The set of consumerAgentGroups to extract values from is empty!!");
        Set<Value> valuesUsed = new HashSet<Value>();
        for(ConsumerAgentGroup agentGroup : consumerAgentGroups){
            valuesUsed.addAll(agentGroup.getConsumerAgentGroupPreferences().keySet());
        }
        return valuesUsed;
    }

    /**
     * Method to attach the name of Needs to a set of Needs.
     * Will return a map with their names as keys and the corresponding Needs as value for easier access.
     * Will throw an exception if the set of Needs is empty
     *
     * @param needs The Needs whose names should be attached
     * @return A map of Needs as values and their respective names as keys
     * @throws IllegalArgumentException Will be thrown if the set of Needs is empty or one of them doesnt have a name
     */
    public static Map<String, Need> attachNeedNames(Set<Need> needs) throws IllegalArgumentException{
        if(needs.isEmpty()) throw new IllegalArgumentException("The set of Needs to attach their names to is empty!!");
        Map<String, Need> returnMap = new HashMap<String, Need>(needs.size());
        for(Need need : needs){
            if(need.getName().equals("")) throw new IllegalArgumentException("The name of Need "+need+" is empty!! Needs should always be given a name!!");
            returnMap.put(need.getName(), need);
        }
        return returnMap;
    }

    /**
     * Method to attach the name of ProductGroupAttribute to a set of ProductGroupAttributes.
     * Will return a map with their names as keys and the corresponding ProductGroupAttributes as value for easier access.
     * Will throw an exception if the set of ProductGroupAttributes is empty or two ProductGroupAttributes in the provided set share the same name (and thus can't be unambiguously be resolved).
     *
     * @param productGroupAttributes The ProductGroupAttributes whose names should be attached
     * @return A map of ProductGroupAttributes as values and their respective names as keys
     * @throws IllegalArgumentException Will be thrown if the set of ProductGroupAttributes is empty, one of them doesnt have a name or two ProductGroupAttributes in the provided set share the same name (and thus can't be unambiguously be resolved).
     */
    public static Map<String, ProductGroupAttribute> attachProductGroupAttributeNamesForProductGroup(Set<ProductGroupAttribute> productGroupAttributes) throws IllegalArgumentException{
        if(productGroupAttributes.isEmpty()) throw new IllegalArgumentException("The set of ProductGroupAttributes to attach their names to is empty!!");
        Map<String, ProductGroupAttribute> returnSet = new HashMap<String, ProductGroupAttribute>(productGroupAttributes.size());
        for(ProductGroupAttribute pga : productGroupAttributes){
            if(pga.getName().equals("")) throw new IllegalArgumentException("The name of ProductGroupAttribute "+pga+" is empty!! ProductGroupAttributes should always be given a name!!");
            else if(returnSet.containsKey(pga.getName())) throw new IllegalArgumentException("productGroupAttribute "+pga+" has the same name as "+returnSet.get(pga.getName())+" and thus can't be unambigously resolved!!");
            else{
                returnSet.put(pga.getName(), pga);
            }
        }
        return returnSet;
    }

    /**
     * Method to attach the corresponding ProductGroupAttribute of ProductGroupAttributeValueMappings to a set of ProductGroupAttributeValueMappings.
     * Will return a map with their ProductGroupAttribute as keys and the corresponding ProductGroupAttributeValueMappings as value for easier access.
     * Will throw an exception if the set of ProductGroupAttributeValueMappings is empty
     *
     * @param productGroupAttributePreferenceMapping The ProductGroupAttributeValueMappings whose ProductGroupAttribute should be attached
     * @return A map of ProductGroupAttributeValueMappings as values and their respective ProductGroupAttributes as keys
     * @throws IllegalArgumentException Will be thrown if the set of ProductGroupAttributeValueMappings is empty or one of them doesnt have a ProductGroupAttribute
     */
    public static Map<ProductGroupAttribute, Set<ProductGroupAttributeValueMapping>> attachProductGroupAttributesToProductGroupAttributeValueMappings(Set<ProductGroupAttributeValueMapping> productGroupAttributePreferenceMapping) throws IllegalArgumentException{
        if(productGroupAttributePreferenceMapping.isEmpty()) throw new IllegalArgumentException("The set of ProductGroupAttributeValueMapping to attach their names to is empty!!");
        Map<ProductGroupAttribute, Set<ProductGroupAttributeValueMapping>> returnMap = new HashMap<ProductGroupAttribute, Set<ProductGroupAttributeValueMapping>>();
        for(ProductGroupAttributeValueMapping pgavMapping : productGroupAttributePreferenceMapping){
            if(pgavMapping.getProductGroupAttribute() == null) throw new IllegalArgumentException("ProductGroupAttributeValueMapping "+pgavMapping+" doesn't have a ProductGroupAttribute associated with it!!");
            if(!(returnMap.keySet().contains(pgavMapping.getProductGroupAttribute()))){
                returnMap.put(pgavMapping.getProductGroupAttribute(), new HashSet<ProductGroupAttributeValueMapping>());
            }
            returnMap.get(pgavMapping.getProductGroupAttribute()).add(pgavMapping);
        }
        return returnMap;
    }

    /**
     * Method to make consumer agents accessible by node by adding them to a map
     *
     * @param consumerAgents The agents to append to their respective node
     * @return A Map of the agents as values and their nodes as keys
     */
    public static Map<SNNode, ConsumerAgent> consumerAgentByNode(Set<ConsumerAgent> consumerAgents) {
        Map<SNNode, ConsumerAgent> returnMap = new HashMap<>();
        for(ConsumerAgent consumerAgent : consumerAgents){
            returnMap.put(consumerAgent.getCorrespondingNodeInSN(), consumerAgent);
        }
        return returnMap;
    }

    /**
     * Method to order nodes by the given ConsumerAgentGroup the respective ConsumerAgent belongs to
     *
     * @param consumerAgentGroups The CAGs used
     * @param nodeConsumerAgentMap The association of the nodes and their consumer agent
     * @return A Map of the CAGs and all the nodes corresponding to agents within the CAG
     */
    public static Map<ConsumerAgentGroup, Set<SNNode>> deriveNodeByCAG(Set<ConsumerAgentGroup> consumerAgentGroups, Map<SNNode, ConsumerAgent> nodeConsumerAgentMap) {
        Map<ConsumerAgentGroup, Set<SNNode>> returnMap = new HashMap<>();
        for(ConsumerAgentGroup cag : consumerAgentGroups){
            returnMap.put(cag, new HashSet<SNNode>());
        }
        for(SNNode currentNode : nodeConsumerAgentMap.keySet()){
            returnMap.get(nodeConsumerAgentMap.get(currentNode).getCorrespondingConsumerAgentGroup()).add(currentNode);
        }
        return returnMap;
    }

    /**
     * Method to attach the name of the products to a given set of products
     *
     * @param products The products to attach names to
     * @return A map of the names and respective products
     */
    public static Map<String, Product> attachProductNamesProducts(Set<Product> products) {
        Map<String, Product> returnMap = new HashMap<>();
        for(Product currentProduct : products){
            returnMap.put(currentProduct.getName(), currentProduct);
        }
        return returnMap;
    }
}
