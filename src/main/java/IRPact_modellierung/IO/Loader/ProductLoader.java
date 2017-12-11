package IRPact_modellierung.IO.Loader;

import IRPact_modellierung.decision.ConsumerAgentAdoptionDecisionProcess;
import IRPact_modellierung.decision.DecisionConfiguration;
import IRPact_modellierung.distributions.BoundedUnivariateDistribution;
import IRPact_modellierung.distributions.Distribution;
import IRPact_modellierung.distributions.UnivariateDistribution;
import IRPact_modellierung.events.*;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.helper.StructureEnricher;
import IRPact_modellierung.helper.ValueConversionHelper;
import IRPact_modellierung.needs.Need;
import IRPact_modellierung.products.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Class to load the product configuration of the simulation.
 * Depends on the distributions and decision configuration already being loaded.
 *
 * @author Simon Johanning
 */
public class ProductLoader {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    /**
     * Method to load the product configuration of the model
     *
     * @param configPath The path in which the product configuration files are to be found
     * @param distributions A map of the distributions used within the product configuration and their respective names
     * @param decisionConfiguration An instance of the decision configuration containing at least the decision processes overwritten by product groups
     * @return The configuration object for the products used in the simulation
     * @throws IllegalArgumentException Will be thrown when any of the helper methods throws an IllegalArgumentException
     * @throws IOException Will be thrown when an error occurs handling the file storing the configuration of the referred product configuration
     * @throws JsonParseException Will be thrown upon a parse error for the json file corresponding to the file handler
     * @throws JsonMappingException Will be thrown upon a mapping error for the json file corresponding to the file handler
     */
    public static ProductConfiguration loadProductConfiguration(String configPath, Map<String, Distribution> distributions, DecisionConfiguration decisionConfiguration) throws IllegalArgumentException, IOException, JsonMappingException, JsonParseException{
        try {
            Set<ProductGroup> productGroups = loadProductGroups(configPath, distributions, decisionConfiguration);
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> productConfigurationJSON = (HashMap<String, Object>) mapper.readValue(new File(configPath+"ProductConfiguration.json"), HashMap.class);
            HashMap<String, Integer> noProductsPerGroupString;
            if(!productConfigurationJSON.containsKey("noProductsPerGroup")) throw new IllegalArgumentException("ProductConfiguration file does not have the noProductsPerGroup set!!\nPlease make sure the configuration is valid!!");
            else{
                try{
                    noProductsPerGroupString = (HashMap<String, Integer>) productConfigurationJSON.get("noProductsPerGroup");
                }catch (ClassCastException cce){
                    throw new IllegalArgumentException("The noProductsPerGroup in the products configuration can't be parsed to a HashMap of String-Integer values!!\n"+cce);
                }
            }
            Map<String, ProductGroup> productGroupHashSet = StructureEnricher.attachProductGroupNames(productGroups);
            HashMap<ProductGroup, Integer> noProductsPerGroup = new HashMap<ProductGroup, Integer>(noProductsPerGroupString.size());
            for(String key : noProductsPerGroupString.keySet()){
                fooLog.debug("For string {}, match {} and {}", key, productGroupHashSet.get(key), noProductsPerGroupString.get(key));
                noProductsPerGroup.put(productGroupHashSet.get(key), noProductsPerGroupString.get(key));
            }
            ArrayList<HashMap<String, Object>> mieObject;
            if(!productConfigurationJSON.containsKey("marketIntroductionEvents")) throw new IllegalArgumentException("ProductConfiguration file does not have the marketIntroductionEvents set!!\nPlease make sure the configuration is valid!!");
            else{
                try{
                    mieObject = (ArrayList<HashMap<String, Object>>) productConfigurationJSON.get("marketIntroductionEvents");
                } catch (ClassCastException cce){
                    throw new IllegalArgumentException("The marketIntroductionEvents in the products configuration can't be parsed to an ArrayList of HashMaps of String-Object values!!\n"+cce);
                }
            }
            List<MarketIntroductionEventDescription> marketIntroductionEventDescriptions = loadMarketIntroductionEvents(mieObject, LazynessHelper.productGroupAttributesFromProductGroups(productGroups), productGroups, distributions);
            ArrayList<HashMap<String, Object>> pdeObject;
            if(!productConfigurationJSON.containsKey("productDiscontinuationEvents")) throw new IllegalArgumentException("ProductConfiguration file does not have the productDiscontinuationEvents set!!\nPlease make sure the configuration is valid!!");
            else{
                try{
                    pdeObject = (ArrayList<HashMap<String, Object>>) productConfigurationJSON.get("productDiscontinuationEvents");
                } catch (ClassCastException cce){
                    throw new IllegalArgumentException("The productDiscontinuationEvents in the products configuration can't be parsed to an ArrayList of HashMaps of String-Object values!!\n"+cce);
                }
            }
            List<ProductDiscontinuationEventDescription> productDiscontinuationEventDescriptions = loadProductDiscontinuationEvents(pdeObject);
            fooLog.debug("{} PDE descriptions created.",productDiscontinuationEventDescriptions.size());
            return new ProductConfiguration(productGroups, marketIntroductionEventDescriptions, productDiscontinuationEventDescriptions, noProductsPerGroup);
        } catch (JsonParseException jpe){
            throw jpe;
        } catch (JsonMappingException jme){
            throw jme;
        } catch (IOException e) {
            throw e;
        } catch (IllegalArgumentException iae){
            throw iae;
        }
    }

    /**
     * Method to load the product groups used in the simulation
     *
     * @param configPath The relative path the configuration files for the product groups are located at
     * @param distributions A map of distributions and their names. Needs to include at least the distributions used within the configuration of the products
     * @param decisionConfiguration The decision configuration of the simulation. Needs to include at least the decision processes used for decision process overwrite of the products in the productGroups
     * @return A set of the product groups configured in the configPath
     * @throws IOException Will be thrown when errors occur handling the files the configuration is stored in
     * @throws IllegalArgumentException Will be thrown when any of the invoked helper methods generates an IllegalArgumentException
     */
    //TODO check whether protoProductGroups is used or can be deleted
    private static Set<ProductGroup> loadProductGroups(String configPath, Map<String, Distribution> distributions, DecisionConfiguration decisionConfiguration) throws IOException, IllegalArgumentException{
        try{
            Set<ProtoProductGroup> protoProductGroups = new HashSet<ProtoProductGroup>();
            Set<ProductGroup> preliminaryProductGroupSet = new HashSet<ProductGroup>();
            Map<ProductGroup, ProtoProductGroup> pgMap = new HashMap<ProductGroup, ProtoProductGroup>();
            Map<String, Need> needsMap  = new HashMap<String, Need>();
            File folder = new File(configPath+"productGroups");
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                //TODO find a smart way to deal with ProductGroup restrictions and their dependencies. If done like below think whether ProtoProductGroups are really needed.
                if (file.isFile()) {
                    ProtoProductGroup currentPPG = loadProtoProductGroup(configPath, file.getName(), distributions, decisionConfiguration);
                    LazynessHelper.addNeedsToNeedMap(currentPPG.getNeedsSatisfied(), needsMap);
                    protoProductGroups.add(currentPPG);
                    ProductGroup correspondingProductGroup = new ProductGroup(currentPPG.getProductGroupAttributes(), currentPPG.getGroupName(), new HashSet<ProductGroup>(currentPPG.getPrerequisiteProductGroupStrings().size()), new HashSet<ProductGroup>(currentPPG.getExcludeProductGroupStrings().size()), currentPPG.getFixedProducts(), ValueConversionHelper.getCorrespondingNeeds(currentPPG.getNeedsSatisfied(), needsMap), currentPPG.getStandardProduct(), currentPPG.getOverwriteDecisionProcess(), currentPPG.getProductLifetimeDistribution());
                    preliminaryProductGroupSet.add(correspondingProductGroup);
                    pgMap.put(correspondingProductGroup, currentPPG);
                }
            }
            Map<String, ProductGroup> productGroupMap = StructureEnricher.attachProductGroupNames(preliminaryProductGroupSet);
            //add the prerequisite and exclude product groups
            for(ProductGroup currentProductGroup : preliminaryProductGroupSet){
                for(String currentPrerequisiteString : pgMap.get(currentProductGroup).getPrerequisiteProductGroupStrings()){
                    currentProductGroup.addPrerequisiteProductGroup(productGroupMap.get(currentPrerequisiteString));
                }
                for(String currentExcludeString : pgMap.get(currentProductGroup).getExcludeProductGroupStrings()){
                    currentProductGroup.addExcludeProductGroup(productGroupMap.get(currentExcludeString));
                }
            }
            return preliminaryProductGroupSet;
        }catch (IOException ioe){
            throw ioe;
        } catch (IllegalArgumentException iae){
            throw iae;
        }
    }

    /**
     * Method to load the market introduction events (MIEs) of the simulation
     *
     * @param scheduledMarketIntroductionEventEntries Representation of the MIEs as a configuration object. Is an array list with one entry for each MIE represented as a HashMap of attribute-value key-value pairs
     * @param productGroupAttributes A set of productGroupAttributes to associate the product attributes of the products represented by the MIE with. Needs to contain at least the productGroupAttributes referred to by the MIE attribute list
     * @param productGroups A list of productGroups within the simulation to associate the MIEs with. Needs to contain at least the ProductGroups the MIEs refer to
     * @return A list of MarketIntroductionEventDescriptions to be used to instantiate the corresponding MarketIntroductionEvents.
     * @throws IllegalArgumentException Will be thrown when the productGroups are erroneous, have no product attributes associated with them or can't be cast to their internal structure (ArrayList of HashMaps), productAttributeValues or -Importance are erroneous, a productGroupAttribute is not associated with a value or lacks the scheduled time, name or an associated product group
     */
    //TODO check implementation (again)
    private static List<MarketIntroductionEventDescription> loadMarketIntroductionEvents(ArrayList<HashMap<String, Object>> scheduledMarketIntroductionEventEntries, Set<ProductGroupAttribute> productGroupAttributes, Set<ProductGroup> productGroups, Map<String, Distribution> distributions) throws IllegalArgumentException{
        List<MarketIntroductionEventDescription> scheduledMarketIntroductionEvents = new LinkedList<MarketIntroductionEventDescription>();
        Map<String, ProductGroup> productGroupMap;
        Map<ProductGroup, Map<String, ProductGroupAttribute>> pgas = StructureEnricher.attachProductGroupAttributeNames(productGroupAttributes, productGroups);
        try {
            productGroupMap = StructureEnricher.attachProductGroupNames(productGroups);
        } catch (IllegalArgumentException e) {
            throw e;
        }
        for(int index=0;index<scheduledMarketIntroductionEventEntries.size();index++) {
            if (!scheduledMarketIntroductionEventEntries.get(index).containsKey("partOfProductGroup"))
                throw new IllegalArgumentException("Error! No product group has been associated with market introduction event " + index + "!!");
            else if (!scheduledMarketIntroductionEventEntries.get(index).containsKey("name"))
                throw new IllegalArgumentException("market introduction event " + index + " does not have a name associated with it!!\nPlease check the configuration or the parameters passed to this function");
            else if (!scheduledMarketIntroductionEventEntries.get(index).containsKey("scheduledForTime"))
                throw new IllegalArgumentException("market introduction event " + index + " does not have a scheduled time associated with it!!\nPlease check the configuration or the parameters passed to this function");
            else if (!scheduledMarketIntroductionEventEntries.get(index).containsKey("productAttributes"))
                throw new IllegalArgumentException("Market introduction event does not have any product attributes associated with it! \nPlease make sure that no 'empty' products are configured");
            else {
                if (!productGroupMap.containsKey(scheduledMarketIntroductionEventEntries.get(index).get("partOfProductGroup")))
                    throw new IllegalArgumentException("MarketIntroductionEvent " + scheduledMarketIntroductionEventEntries.get(index).get("name") + " refers to a product that doesn't exist!!");
                ProductGroup correspondingProductGroup = productGroupMap.get(scheduledMarketIntroductionEventEntries.get(index).get("partOfProductGroup"));
                Map<ProductGroupAttribute, Double> pgaValueMap = new HashMap<>();
                //load productAttributes
                ArrayList<HashMap<String, Object>> productAttributesJSON = (ArrayList<HashMap<String, Object>>) scheduledMarketIntroductionEventEntries.get(index).get("productAttributes");
                Iterator<HashMap<String, Object>> productAttributeIterator = productAttributesJSON.iterator();
                while (productAttributeIterator.hasNext()) {
                    HashMap<String, Object> productGroupAttributeMap = productAttributeIterator.next();
                    if (!productGroupAttributeMap.containsKey("value"))
                        throw new IllegalArgumentException("Product attribute has no value!!");
                    else if (!productGroupAttributeMap.containsKey("correspondingProductGroupAttribute"))
                        throw new IllegalArgumentException("Product attribute has no product attribute!");
                    else if (!pgas.get(correspondingProductGroup).containsKey(productGroupAttributeMap.get("correspondingProductGroupAttribute"))) {
                        throw new IllegalArgumentException("ProductAttribute refers to a non-existing productGroupAttribute!");
                    }else {
                        ProductGroupAttribute correspondingPGA = pgas.get(correspondingProductGroup).get((String) productGroupAttributeMap.get("correspondingProductGroupAttribute"));
                        pgaValueMap.put(correspondingPGA, (Double) productGroupAttributeMap.get("value"));
                    }
                }
                if (scheduledMarketIntroductionEventEntries.get(index).containsKey("productLifetimeDistribution")) {
                    FixedProductDescription correspondingFixedProductDescription = new FixedProductDescription(pgaValueMap, (String) scheduledMarketIntroductionEventEntries.get(index).get("name"), (String) scheduledMarketIntroductionEventEntries.get(index).get("partOfProductGroup"), (UnivariateDistribution) distributions.get(scheduledMarketIntroductionEventEntries.get(index).get("productLifetimeDistribution")));
                    scheduledMarketIntroductionEvents.add(new MarketIntroductionEventDescription(correspondingFixedProductDescription, (Double) scheduledMarketIntroductionEventEntries.get(index).get("scheduledForTime")));
                } else {
                    FixedProductDescription correspondingFixedProductDescription = new FixedProductDescription(pgaValueMap, (String) scheduledMarketIntroductionEventEntries.get(index).get("name"), (String) scheduledMarketIntroductionEventEntries.get(index).get("partOfProductGroup"));
                    scheduledMarketIntroductionEvents.add(new MarketIntroductionEventDescription(correspondingFixedProductDescription, (Double) scheduledMarketIntroductionEventEntries.get(index).get("scheduledForTime")));
                }
            }
        }
        return scheduledMarketIntroductionEvents;
    }

    //TODO link to products here (? all products introduced now)
    /**
     * Method to extract the ProductDiscontinuationEventDescriptions from the configuration object scheduledProductDiscontinuationEventEntries
     *
     * @param scheduledProductDiscontinuationEventEntries Configuration object containing a (array) list of ProductDiscontinuationEventDescription in the form of HashMaps containing at least the name of the respective product and the scheduled time of its discontinuation
     * @return A list of ProductDiscontinuationEventDescriptions for the simulation representing the ProductDiscontinuationEvents
     * @throws IllegalArgumentException Will be thrown when a product discontinuation event is not associated with a name or a scheduled time
     */
    private static List<ProductDiscontinuationEventDescription> loadProductDiscontinuationEvents(ArrayList<HashMap<String, Object>> scheduledProductDiscontinuationEventEntries) throws IllegalArgumentException{
        List<ProductDiscontinuationEventDescription> scheduledProductDiscontinuationEventDescriptions = new LinkedList<ProductDiscontinuationEventDescription>();
        for(int index=0;index<scheduledProductDiscontinuationEventEntries.size();index++){
            if(!scheduledProductDiscontinuationEventEntries.get(index).containsKey("name")) throw new IllegalArgumentException("Product discontinuation event "+index+" does not have a name associated with it!! Please check the configuration or the parameters passed to this function");
            else if(!scheduledProductDiscontinuationEventEntries.get(index).containsKey("scheduledForTime")) throw new IllegalArgumentException("Product discontinuation event "+index+" does not have a scheduled time associated with it!! Please check the configuration or the parameters passed to this function");
            else scheduledProductDiscontinuationEventDescriptions.add(new ProductDiscontinuationEventDescription((String) scheduledProductDiscontinuationEventEntries.get(index).get("name"), (Double) scheduledProductDiscontinuationEventEntries.get(index).get("scheduledForTime")));
        }
        return scheduledProductDiscontinuationEventDescriptions;
    }

    /**
     * Method to load a ProtoProductGroup from a json file.
     * A ProtoProductGroup represents a product group with properties being linked to string references for objects not yet instantiated
     *
     * @param configPath The (relative) path at which the configuration files for the product groups is located
     * @param fileName The name of the file in which the configuration of the corresponding product group is located
     * @param distributions A map of distributions and their corresponding name. Needs to include at least the distributions referenced in the configuration
     * @param decisionConfiguration The configuration object of the decision methods. Needs to include at least the decision methods referenced in the corresponding configuration file
     * @return A ProtoProductGroup object corresponding to the configuration of the respective ProductGroup
     * @throws IllegalArgumentException Will be thrown when any of the entries is lacking, can't be parsed, a reference is invalid or any of the methods used in deriving the data throws a IllegalArgumentException
     * @throws IOException Will be thrown when an error occurs in handling the configuration file of the respective product group
     * @throws JsonParseException Will be thrown when an error occurs parsing the json-file corresponding to the respective product group
     * @throws JsonMappingException Will be thrown when an error occurs mapping the json-file corresponding to the respective product group
     */
    private static ProtoProductGroup loadProtoProductGroup(String configPath, String fileName, Map<String, Distribution> distributions, DecisionConfiguration decisionConfiguration) throws IllegalArgumentException, IOException, JsonMappingException, JsonParseException{
        ObjectMapper mapper = new ObjectMapper();
        try {
            //read JSON file
            HashMap<String, Object> productGroupJSON = mapper.readValue(new File(configPath+"productGroups/"+fileName), HashMap.class);
            //retrieve productGroupAttributes
            ArrayList<HashMap<String, Object>> productAttributesJSON;
            if(!productGroupJSON.containsKey("productGroupAttributes")) throw new IllegalArgumentException("Product group configured in "+configPath+"/"+fileName+" does not configure any productGroupAttributes!! Make sure the configuration of the productGroups is valid!!");
            else{
                try{
                    productAttributesJSON = (ArrayList<HashMap<String, Object>>) productGroupJSON.get("productGroupAttributes");
                } catch (ClassCastException cce){
                    throw new IllegalArgumentException("productGroupAttributes of file "+fileName+" can't be parsed to an array list of hashmaps!!\nMake sure to provide a valid configuration!\n"+cce);
                }
            }
            Set<ProductGroupAttribute> productGroupAttributes;
            try {
                productGroupAttributes = loadProductGroupAttributes(productAttributesJSON, distributions);
            } catch (IllegalArgumentException iae){
                throw iae;
            }
            //retrieve prerequisite product list
            ArrayList<String> prerequisiteProductsJSON;
            Set<String> prerequisiteProductGroups;
            if(!productGroupJSON.containsKey("prerequisiteProductGroups")) throw new IllegalArgumentException("ProtoProductGroup located in "+fileName+" does not contain prerequisite product group!\nPlease make sure this entry exists, even if it is empty!!");
            try {
                prerequisiteProductsJSON = (ArrayList<String>) productGroupJSON.get("prerequisiteProductGroups");
                 prerequisiteProductGroups = new HashSet<String>(prerequisiteProductsJSON.size());
                //System.out.println(prerequisiteProductsJSON.toString());
                prerequisiteProductGroups.addAll(prerequisiteProductsJSON);
            } catch (ClassCastException cce){
                throw new IllegalArgumentException("prerequisiteProductGroups of file "+fileName+" can't be parsed to an array list of hashmaps!!\nMake sure to provide a valid configuration!\n"+cce);
            }
            //retrieve excludes product list
            ArrayList<String> excludesProductGroupsJSON;
            Set<String> excludedProductGroups;
            if(!productGroupJSON.containsKey("excludedProductGroups")) throw new IllegalArgumentException("ProtoProductGroup located in "+fileName+" does not contain excludedProductGroups product group!\nPlease make sure this entry exists, even if it is empty!!");
            try {
                excludesProductGroupsJSON = (ArrayList<String>) productGroupJSON.get("excludedProductGroups");
                excludedProductGroups = new HashSet<String>(excludesProductGroupsJSON.size());
                //System.out.println(prerequisiteProductsJSON.toString());
                excludedProductGroups.addAll(excludesProductGroupsJSON);
            } catch (ClassCastException cce){
                throw new IllegalArgumentException("excludedProductGroups of file "+fileName+" can't be parsed to an array list of hashmaps!!\nMake sure to provide a valid configuration!\n"+cce);
            }
            //strip .json from name
            String[] fileParts = fileName.split("\\.");
            //Load the respective product lifetime distribution
            UnivariateDistribution productLifetimeDistribution;
            if(productGroupJSON.containsKey("productLifetimeDistribution")) productLifetimeDistribution = (UnivariateDistribution) distributions.get((String) productGroupJSON.get("productLifetimeDistribution"));
            else throw new IllegalStateException("product "+fileParts[0]+" has no valid productLifetimeDistribution associated with it!");
            //load fixed products
            ArrayList<HashMap<String, Object>> fixedProductsJSON;
            Set<FixedProductDescription> fixedProducts;
            if(!productGroupJSON.containsKey("fixedProducts")) throw new IllegalArgumentException("ProtoProductGroup located in "+fileName+" does not contain fixedProducts product group!\nPlease make sure this entry exists, even if it is empty!!");
            try {
                fixedProductsJSON = (ArrayList<HashMap<String, Object>>) productGroupJSON.get("fixedProducts");
                fixedProducts = loadFixedProducts(fixedProductsJSON, productGroupAttributes, distributions, productLifetimeDistribution, fileParts[0]);
            } catch (ClassCastException cce){
                throw new IllegalArgumentException("fixedProducts of file "+fileName+" can't be parsed to an array list of hashMaps!!\nMake sure to provide a valid configuration!\n"+cce);
            } catch (IllegalArgumentException iae){
                throw iae;
            }
            //retrieve the needs satisfied
            ArrayList<String> needStrings;
            Set<String> needSet;
            if(!productGroupJSON.containsKey("satisfiesNeeds")) throw new IllegalArgumentException("ProtoProductGroup located in "+fileName+" does not contain satisfiesNeeds product group!\nPlease make sure this entry exists, even if it is empty!!");
            try {
                needStrings = (ArrayList<String>) productGroupJSON.get("satisfiesNeeds");
                needSet = ValueConversionHelper.arrayListToSet(needStrings);
            } catch (ClassCastException cce){
                throw new IllegalArgumentException("satisfiesNeeds of file "+fileName+" can't be parsed to an array list of strings!!\nMake sure to provide a valid configuration!\n"+cce);
            } catch (IllegalArgumentException iae){
                throw iae;
            }
            //check if there is a decision making process that overwrites
            ConsumerAgentAdoptionDecisionProcess overwriteDecisionProcess = null;
            if(productGroupJSON.containsKey("overwriteDecisionProcess")){
                //overwriteDecisionProcess = DecisionMakingProcessFactory.createDecisionMakingProcess((String) productGroupJSON.get("overwriteDecisionProcess"), decisionConfiguration);
                overwriteDecisionProcess = decisionConfiguration.getDecisionMakingProcess((String) productGroupJSON.get("overwriteDecisionProcess"));
            }
            String standardProductString = "";
            if(productGroupJSON.containsKey("standardProduct")) standardProductString = (String) productGroupJSON.get("standardProduct");
            //create the preliminary product group
            return new ProtoProductGroup(productGroupAttributes, fileParts[0], prerequisiteProductGroups, excludedProductGroups, fixedProducts, needSet, overwriteDecisionProcess, productLifetimeDistribution, standardProductString);
        } catch (JsonMappingException jme){
            throw jme;
        } catch (JsonParseException jpe){
            throw jpe;
        } catch (IOException ioe) {
           throw ioe;
        }
    }

    /**
     * Method to load the productGroupAttributes from a configuration object consisting of an arrayList of HashMaps
     *
     * @param pgaMap The configuration object as an arraylist containing hashMaps for each ProductGroupAttribute as entries. These entries correspond to the name, value, importance and visibility of a productGroupAttribte
     * @param distributions A map of distributions and their names. Needs to contain at least the distributions referenced in the values of the respective productGroupAttributes
     * @return The set of ProductGroupAttributes belonging to the ProductGroup of interest, as configured by the pgaMap
     * @throws IllegalArgumentException Will be thrown when a productGroupAttribute lacks an entry for name, value, importance or mutability or any of these can't be casted to their respective value. Will also be thrown when value refers to an invalid distribution (one not provided in the parameters of this method).
     */
    private static Set<ProductGroupAttribute> loadProductGroupAttributes(ArrayList<HashMap<String, Object>> pgaMap, Map<String, Distribution> distributions) throws IllegalArgumentException{
        Set<ProductGroupAttribute> returnSet = new HashSet<ProductGroupAttribute>();
        for(int index=0; index<pgaMap.size(); index++) {
            HashMap<String, Object> currentEntry = pgaMap.get(index);
            //load the name
            String name;
            if(!currentEntry.containsKey("name")) throw new IllegalArgumentException(index+"th productGroupAttribute is not associated with name!! \nMake sure to write a valid configuration and pass correct arguments to the loader!!");
            else name = (String) currentEntry.get("name");
            //load the value distribution
            BoundedUnivariateDistribution valueDistribution;
            if(!currentEntry.containsKey("value")) throw new IllegalArgumentException(index+"th productGroupAttribute is not associated with value!! \nMake sure to write a valid configuration and pass correct arguments to the loader!!");
            else if(!distributions.containsKey(currentEntry.get("value"))) throw new IllegalArgumentException("The value of the "+index+"th productGroupAttribute is associated with an unknown distribution ("+currentEntry.get("value")+"!! \nMake sure to write a valid configuration and pass correct arguments to the loader!!");
            else{
                try{
                    valueDistribution = (BoundedUnivariateDistribution) distributions.get(currentEntry.get("value"));
                }catch(ClassCastException cce){
                    throw new IllegalArgumentException("The distribution associated the "+index+"th productGroupAttribute ("+currentEntry.get("value")+") is not of type BoundedUnivariateDistribution!! \nMake sure to write a valid configuration and pass correct arguments to the loader\n!!"+cce);
                }
            }
            boolean mutability;
            if(!currentEntry.containsKey("mutability")) throw new IllegalArgumentException(index+"th productGroupAttribute is not associated with a mutability!! \nMake sure to write a valid configuration and pass correct arguments to the loader!!");
            else{
                try{
                    mutability = (Boolean) currentEntry.get("mutability");
                } catch (ClassCastException cce){
                    throw new IllegalArgumentException("The mutability associated the "+index+"th productGroupAttribute ("+currentEntry.get("mutability")+") is not a boolean!! \nMake sure to write a valid configuration and pass correct arguments to the loader!!\n"+cce);
                }
            }
            double observability;
            if(!currentEntry.containsKey("observability")) throw new IllegalArgumentException(index+"th productGroupAttribute is not associated with an observability!! \nMake sure to write a valid configuration and pass correct arguments to the loader!!");
            else{
                try{
                    observability = (Double) currentEntry.get("observability");
                } catch (ClassCastException cce){
                    throw new IllegalArgumentException("The observability associated the "+index+"th productGroupAttribute ("+currentEntry.get("observability")+") is not a double!! \nMake sure to write a valid configuration and pass correct arguments to the loader!!\n"+cce);
                }
            }
            ProductGroupAttribute fooPga = new ProductGroupAttribute(name, valueDistribution, mutability, observability);
            returnSet.add(fooPga);
        }
        return returnSet;
    }

    /**
     * Method to load fixed products from a configuration object
     *
     * @param fixedProductsEntries The configuration object representing the fixedProducts (as array list of hashmaps characterizing each fixed product with values that need to parse to an array list of hashMaps for the product attributes)
     * @param productGroupAttributes A set of productGroupAttributes used to link the fixedProducts to productGroupAttributes in the simulation (needs to contain at least the PGAs refered to in the fixedProducts list
     * @param distributions A map of the distributions used in the simulation. Needs to at least contain the productLifetimeDistribution (if set) and will otherwise throw an exception.
     * @param pGProductLifetimeDistribution the productLifetimeDistribution of the product group the fixed products will be part of
     * @param productGroupQualifier The name of the ProductGroup the FixedProduct is to be part of
     * @return A set of FixedProductDescriptions as described by the configuration object fixedProductsEntries
     * @throws IllegalArgumentException Will be thrown when the product attributes of the fixed products cant be parsed, the attribute values or importance can't be derived or a product is not associated with a product name
     */
    private static Set<FixedProductDescription> loadFixedProducts(ArrayList<HashMap<String, Object>> fixedProductsEntries, Set<ProductGroupAttribute> productGroupAttributes, Map<String, Distribution> distributions, UnivariateDistribution pGProductLifetimeDistribution, String productGroupQualifier) throws IllegalArgumentException{
        Set<FixedProductDescription> fixedProducts = new HashSet<FixedProductDescription>(fixedProductsEntries.size());
        for(int index=0;index<fixedProductsEntries.size();index++){
            ArrayList<HashMap<String, Object>> productAttributesList;
            try {
                productAttributesList = (ArrayList<HashMap<String, Object>>) fixedProductsEntries.get(index).get("productAttributes");
            }catch (ClassCastException cce){
                throw new IllegalArgumentException("Can't parse the "+index+"th entry of the fixed products list to an array list of product attributes!!\n"+cce);
            }
            Map<ProductGroupAttribute, Double> productAttributeValueMapping;
            try {
                productAttributeValueMapping= loadProductAttributeValues(productAttributesList, StructureEnricher.attachProductGroupAttributeNamesForProductGroup(productGroupAttributes));
            } catch (IllegalArgumentException e) {
                throw e;
            }
            UnivariateDistribution productLifetimeDistribution;
            if(fixedProductsEntries.get(index).containsKey("productLifetimeDistribution")) productLifetimeDistribution = (UnivariateDistribution) distributions.get((String) fixedProductsEntries.get(index).get("productLifetimeDistribution"));
            else productLifetimeDistribution = pGProductLifetimeDistribution;
            if(!fixedProductsEntries.get(index).containsKey("name")) throw new IllegalArgumentException(index+"th fixed product is not associated with a product name!!\nPlease fix configuration or ensure correct arguments are passed to this method!!");
            else fixedProducts.add(new FixedProductDescription(productAttributeValueMapping, (String) fixedProductsEntries.get(index).get("name"), productGroupQualifier, productLifetimeDistribution));
        }
        return fixedProducts;
    }

    /**
     * Method to load the values for the productGroupAttributes from a configuration object.
     *
     * @param productAttributeMap The productAttributeMap containing the product attribute for the market introduction events, containing the value of the attributes as entries in the hashmaps (entries of the array list)
     * @param productGroupAttributes A map of the product attributes and their names that needs to contain at least the product attributes referred to in the productAttributeMap from which to extract the value
     * @return A map of the productGroupAttributes listed in the productAttributeMap and their corresponding value
     * @throws IllegalArgumentException will be thrown when a product attribute is not associated with a value or the referenced product attribute does not correspond to a product attribute in the simulation
     */
    private static Map<ProductGroupAttribute, Double> loadProductAttributeValues(ArrayList<HashMap<String, Object>> productAttributeMap, Map<String, ProductGroupAttribute> productGroupAttributes) throws IllegalArgumentException{
        Map<ProductGroupAttribute, Double> productAttributes = new HashMap<ProductGroupAttribute, Double>(productAttributeMap.size());
        for (HashMap<String, Object> aProductAttributeMap : productAttributeMap) {
            if (!productGroupAttributes.containsKey(aProductAttributeMap.get("correspondingProductGroupAttribute")))
                throw new IllegalArgumentException("No productGroupAttribute in the passed list corresponds to the referenced product attribute " + aProductAttributeMap.get("correspondingProductGroupAttribute"));
            else if (!aProductAttributeMap.containsKey("value"))
                throw new IllegalArgumentException("No value is associated with the productGroupAttribute " + aProductAttributeMap);
            productAttributes.put(productGroupAttributes.get((String) aProductAttributeMap.get("correspondingProductGroupAttribute")), (Double) aProductAttributeMap.get("value"));
        }
        return productAttributes;
    }

}
