package IRPact_modellierung.helper;

import IRPact_modellierung.agents.consumerAgents.*;
import IRPact_modellierung.decision.ConsumerAgentAdoptionDecisionProcess;
import IRPact_modellierung.distributions.UnivariateDistribution;
import IRPact_modellierung.events.Event;
import IRPact_modellierung.events.MarketIntroductionEvent;
import IRPact_modellierung.events.ScriptedProductEvent;
import IRPact_modellierung.information.Information;
import IRPact_modellierung.information.ProductAttributeInformation;
import IRPact_modellierung.needs.Need;
import IRPact_modellierung.needs.NeedDevelopmentScheme;
import IRPact_modellierung.network.SNEdge;
import IRPact_modellierung.network.SNNode;
import IRPact_modellierung.preference.Preference;
import IRPact_modellierung.preference.PreferenceConfiguration;
import IRPact_modellierung.preference.ProductGroupAttributeValueMapping;
import IRPact_modellierung.preference.Value;
import IRPact_modellierung.products.*;
import IRPact_modellierung.simulation.SimulationContainer;
import antlr.SemanticException;
import org.apache.logging.log4j.LogManager;
//import sun.plugin2.message.SetAppletSizeMessage;
//TODO fix google sets
//import com.google.commons.collect.Sets;

import java.util.*;

/**
 * Class that provides a number of methods that outsources some functionality used in other methods of the simulation.
 * Is intended to keep code in other methods more compact.
 *
 * @author Simon Johanning
 */
public class LazynessHelper {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    /**
     * Method to bundle all product group attributes present in any of the products in the product group set
     *
     * @param productGroups The product groups whose ProductGroupAttributes are to be retrieved
     * @return The set of ProductGroupAttributes present in any of the respective product groups
     * @throws IllegalArgumentException Will be thrown when the set of product groups is empty
     */
    public static Set<ProductGroupAttribute> productGroupAttributesFromProductGroups(Set<ProductGroup> productGroups) throws IllegalArgumentException{
        if(productGroups.isEmpty()) throw new IllegalArgumentException("The set of product groups to extract attributes from is empty!!");
        Set<ProductGroupAttribute> pga = new HashSet<ProductGroupAttribute>();
        for (ProductGroup pg : productGroups) {
            pga.addAll(pg.getProductGroupAttributes());
        }
        return pga;
    }

    /**
     * Method to retrieve all FixedProductDescriptions in the provided product groups
     *
     * @param productGroups Set of product groups whose FixedProductDescriptions are to be extracted
     * @return The FixedProductDescriptions present in any of the respective product groups
     * @throws IllegalArgumentException Will be thrown when the set of product groups is empty
     */
    public static Set<FixedProductDescription> fixedProductDescriptionsFromProductGroups(Set<ProductGroup> productGroups) {
        if(productGroups.isEmpty()) throw new IllegalArgumentException("The set of product groups to extract attributes from is empty!!");
        Set<FixedProductDescription> fpds = new HashSet<FixedProductDescription>();
        for (ProductGroup pg : productGroups) {
            fpds.addAll(pg.getFixedProducts());
        }
        return fpds;
    }

    /**
     * Method to retrieve a random node in the set of SNNodes chosen uniformly distributed
     *
     * @param potentialNodes The set of which one nodes should be selected
     * @return A randomly selected node from the set
     * @throws IllegalArgumentException will be thrown if the set of nodes to chose from is empty (no node can be chosen from
     */
    public static SNNode getRandomSNNode(Set<SNNode> potentialNodes) throws IllegalArgumentException {
        if (potentialNodes.size() > 0) {
            ArrayList<SNNode> nodesAsArrayList = new ArrayList<SNNode>(potentialNodes);
            int targetIndex = (int) (Math.floor(Math.random() * potentialNodes.size()));
            return nodesAsArrayList.get(targetIndex);
        } else
            throw new IllegalArgumentException("ERROR!! There is no node to choose from in getRandomSNNode in LazynessHelper!!!");
    }

    /**
     * Retrieve the consumer agent corresponding to the given SNNode within the simulation container
     *
     * @param simulationContainer The container in which the simulation runs
     * @param correspondingNode   The node for which the ConsumerAgent should be retrieved
     * @return The consumer agent corresponding to the given SNNode within the simulation container
     * @throws IllegalArgumentException Will be thrown if the given SNNode doesn't have an associated consumer agent within the simulation
     */
    public static ConsumerAgent retrieveCorrespondingConsumerAgent(SimulationContainer simulationContainer, SNNode correspondingNode) throws IllegalArgumentException {
        for (ConsumerAgent agentInSN : simulationContainer.getConsumerAgents()) {
            if (agentInSN.getCorrespondingNodeInSN().equals(correspondingNode)) return agentInSN;
        }
        throw new IllegalArgumentException("SNNode " + correspondingNode + " does not have a corresponding consumer agent within the simulation container!!");
    }

    /**
     * Method to retrieve all product attributes present in any of the given products
     *
     * @param productsToExtractFrom The set of products from which the product attributes are to be retrieved
     * @return The set of product attributes present in any of the given products
     * @throws IllegalArgumentException Will be thrown if the set of products whose product attributes are to be retrieved is empty
     */
    public static Set<ProductAttribute> selectProductAttributes(Set<Product> productsToExtractFrom) throws IllegalArgumentException {
        if (productsToExtractFrom.isEmpty())
            throw new IllegalArgumentException("Products from which the product attributes are to be extracted is empty!");
        Set<ProductAttribute> attributes = new HashSet<ProductAttribute>();
        for (Product productToExtractFrom : productsToExtractFrom) {
            attributes.addAll(productToExtractFrom.getProductAttributes());
        }
        return attributes;
    }

    /**
     * Retrieves the edge from sourceNode to targetNode in the given set of edges.
     * Will return null if no such edge is present in the given set.
     * Since the model does not incorporate multigraphs, the SNEdge to be retrieved is unique (if present)
     *
     * @param sourceNode The SNNode from which the edge originates
     * @param targetNode The SNNode to which the edge is directed
     * @param edges      The set of edges from which the edge from sourceNode to targetNode is to be retrieved
     * @return The edge corresponding to the edge from sourceNode to targetNode
     * @throws IllegalArgumentException will be thrown if either of the sourceNode, targetNode or edges is null / empty
     */
    public static SNEdge getCorrespondingEdge(SNNode sourceNode, SNNode targetNode, Set<SNEdge> edges) throws IllegalArgumentException {
        if (sourceNode == null)
            throw new IllegalArgumentException("The source node for the edge to be retrieved is null; Conceptually no such edge exists!!");
        else if (targetNode == null)
            throw new IllegalArgumentException("The target node for the edge to be retrieved is null; Conceptually no such edge exists!!");
        else if (edges.isEmpty())
            throw new IllegalArgumentException("The set of edges from which the edge should be retrieved is empty!!");
        else {
            for (SNEdge currentEdge : edges) {
                if ((currentEdge.getSource() == sourceNode) && (currentEdge.getTarget() == targetNode))
                    return currentEdge;
            }
            return null;
        }
    }

    /**
     * Method to retrieve the product corresponding to the FixedProductDescription from the list of products in the simulation container.
     * If no such product is present in the container, it will return null
     *
     * @param simulationContainer The container in which the simulation is situated
     * @param productConcerned    The FixedProductDescription whose corresponding product is to be retrieved
     * @return The product in the simulationContainer corresponding to the FixedProductDescription if such a product exist; null if not
     * @throws IllegalArgumentException Will be thrown if the simulation container or the given fixed product description is null
     */
    public static Product getCorrespondingProduct(SimulationContainer simulationContainer, FixedProductDescription productConcerned) {
        if (simulationContainer == null)
            throw new IllegalArgumentException("simulationContainer is null!! This should not be the case!!");
        else if (productConcerned == null)
            throw new IllegalArgumentException("The fixed product description to find the respective product for is null; Thus no such product can be retrieved!!");
        for (Product currentProduct : simulationContainer.getProducts()) {
            if (fixedProductCorrespondsToProduct(productConcerned, currentProduct)) return currentProduct;
        }
        fooLog.error("Product corresponding to fixedProductDescription {} was not found!!! Will return null!!", productConcerned.getName());
        return null;
    }

    /**
     * Method to check whether a product is the instance of a fixedProductDescription.
     * In order for this to be the case, their name, product group and all product attributes need to be equal.
     * For product attributes to be equal, their keys and values have to be equal
     *
     * @param fixedProduct The fixed product to be checked for equality
     * @param product      The product to be checked for equality
     * @return true if name, product group and all product attributes of the product and the fixed product correspond to another; false if any of them are unequal
     * @throws IllegalArgumentException Will be thrown if either the fixed product or the reference product is null
     */
    private static boolean fixedProductCorrespondsToProduct(FixedProductDescription fixedProduct, Product product) throws IllegalArgumentException {
        if (fixedProduct == null) throw new IllegalArgumentException("fixedProduct is null!!");
        else if (product == null) throw new IllegalArgumentException("product is null!!");
        if (!(fixedProduct.getName().equals(product.getName()))) return false;
        if (!(fixedProduct.getPartOfProductGroup().equals(product.getPartOfProductGroup().getGroupName()))) return false;
        for (ProductAttribute productAttribute : product.getProductAttributes()) {
            if (!(fixedProduct.getProductAttributeValues().keySet().contains(productAttribute.getCorrespondingProductGroupAttribute())))
                return false;
            else if (fixedProduct.getProductAttributeValues().get(productAttribute.getCorrespondingProductGroupAttribute()) != productAttribute.getValue())
                return false;
        }
        //everything seems to be equal
        return true;
    }

    //TODO dont return null and check if another product is named the same

    /**
     * Method to retrieve a product within the simulation by its name.
     * Will check current and historical products, so the user needs to check if the product
     * is still on the market if relevant.
     * Will throw an error if no such (multiple) product exists.
     *
     * @param productName         The product name whose corresponding product is to be found
     * @param simulationContainer The container in which the simulation runs
     * @return The product with the respective name
     * @throws IllegalArgumentException Will be thrown when no such product (or multiple such products) exist and thus either product name or the simulation container is illegal
     */
    public static Product getProductByName(String productName, SimulationContainer simulationContainer) throws IllegalArgumentException {
        Product requestedProduct = null;
        boolean singleProductFound = false;
        //check (current) products in the simulation container
        for (Product product : simulationContainer.getProducts()) {
            if (product.getName().equals(productName)) {
                if (singleProductFound)
                    throw new IllegalArgumentException("Multiple products found with name " + productName + "!! Ensure that no two products are named the same!!");
                else {
                    singleProductFound = true;
                    requestedProduct = product;
                }
            }
        }
        //check historical products in the simulation container
        for (Product product : simulationContainer.getHistoricalProducts()) {
            if (product.getName().equals(productName)) {
                if (singleProductFound)
                    throw new IllegalArgumentException("Multiple products found with name " + productName + "!! Ensure that no two products are named the same!!");
                else {
                    singleProductFound = true;
                    requestedProduct = product;
                }
            }
        }
        if (singleProductFound) return requestedProduct;
        else throw new IllegalArgumentException("No product with the name "+productName+" is contained in the simulation!!");
    }

    /**
     * Adds a number of needs characterized through strings to the existing need map.
     * Will manipulate the need map provided as parameter.
     * Needs that are already contained in the need map will be ignored
     *
     * @param needsToAdd Strings corresponding to the needs to add
     * @param needsMap The need map that is to be modified with the needs to add
     */
    public static void addNeedsToNeedMap(Set<String> needsToAdd, Map<String, Need> needsMap){
        for(String needString : needsToAdd){
            if(!(needsMap.keySet().contains(needString))) needsMap.put(needString, new Need(needString));
        }
    }

    /**
     * Method to aggregate all needs the provided product groups satisfy.
     * Will construct the union of all needs satisfied by any of the product groups
     *
     * @param productGroups The product groups whose needs are to be extracted
     * @return The union of all needs satisfied by any of the product groups
     */
    public static Set<Need> aggregateNeeds(Set<ProductGroup> productGroups) {
        Set<Need> aggregatedNeeds = new HashSet<Need>();
        for(ProductGroup pg : productGroups){
            aggregatedNeeds.addAll(pg.getNeedsSatisfied());
        }
        return aggregatedNeeds;
    }

    /**
     * Method to aggregate all needs the product groups corresponding to the provided products satisfy.
     * Will construct the union of all needs satisfied by any of the product groups the products belong to
     *
     * @param products The products whose needs to be satisfied are to be extracted
     * @return The union of all needs satisfied by any of the product groups corresponding to the products
     */
    public static Set<Need> aggregateNeedsFromProducts(Set<Product> products) {
        Set<Need> aggregatedNeeds = new HashSet<Need>();
        for(Product product : products){
            aggregatedNeeds.addAll(product.getPartOfProductGroup().getNeedsSatisfied());
        }
        return aggregatedNeeds;
    }

    /**
     * Method to derive the products a set of adopted products is based on.
     *
     * @param adoptedProducts The set of adopted products the corresponding products are based on
     * @return The products corresponding to the adopted products
     */
    public static Set<Product> getCorrespondingProducts(Set<AdoptedProduct> adoptedProducts){
        Set<Product> correspondingProducts = new HashSet<Product>(adoptedProducts.size());
        for(AdoptedProduct adoptedProduct : adoptedProducts){
            correspondingProducts.add(adoptedProduct.getCorrespondingProduct());
        }
        return correspondingProducts;
    }

    /**
     * Method to derive the products a set of MarketIntroductionEvent corresponds to
     *
     * @param mies The set of MarketIntroductionEvents whose corresponding products should be derived
     * @return The products corresponding to the products the MIEs concern
     */
    public static Set<Product> getConcernedProducts(Set<MarketIntroductionEvent> mies){
        Set<Product> concernedProducts = new HashSet<Product>(mies.size());
        for(MarketIntroductionEvent mie : mies){
            concernedProducts.add(mie.getProductConcerned());
        }
        return concernedProducts;
    }

    /**
     * Method that checks whether all prerequisite products of a given product are adopted.
     * Prerequisite product groups demand that at least one product from each prerequisite product group is adopted.
     * This method checks that all these are satisfied
     *
     * @param potentialProduct The product whose prerequisite products are to be checked
     * @param consumerAgentConcerned The consumer agent whose adoption is to be tested on adoption of prerequisite product groups
     * @return true if all prerequisites are satisfied, false if at least one is violated
     */
    //TODO tidy up
    public static boolean allPrerequisiteProductsAdopted(Product potentialProduct, ConsumerAgent consumerAgentConcerned) {
        Set<ProductGroup> prerequisiteProductGroups = potentialProduct.getPartOfProductGroup().getPrerequisiteProductGroups();
        if(prerequisiteProductGroups.isEmpty()) return true;
        //remove product groups that are prerequisite until empty
        for(AdoptedProduct adoptedProduct : consumerAgentConcerned.getAdoptedProducts()){
            Product product = adoptedProduct.getCorrespondingProduct();
            if(prerequisiteProductGroups.contains(product.getPartOfProductGroup())) prerequisiteProductGroups.remove(product.getPartOfProductGroup());
            if(prerequisiteProductGroups.isEmpty()) return true;
        }
        //if empty, all prerequisites have been met, otherwise not
        return prerequisiteProductGroups.isEmpty();
    }

    /**
     * Method to chose a (uniformly) random product from a set of products
     *
     * @param products The set of products of which one product is to be chosen
     * @return A (uniformly) randomly chosen product of the set to chose from
     * @throws IllegalArgumentException will be thrown if the set to choose from is empty or one of them is null
     */
    public static Product chooseProduct(Set<Product> products) throws IllegalArgumentException{
        if(products.isEmpty()) throw new IllegalArgumentException("List of products to chose from is empty; no product can be chosen from this list, and a (semantic) error must have occurred during the construction of the set of products to chose from");
        else if(products.contains(null)) throw new IllegalArgumentException("One of the products to chose from is null. Products should not be null!");
        else{
            ArrayList<Product> listToChooseFrom = new ArrayList<Product>(products);
            int randomIndex = (int) Math.floor(Math.random()*listToChooseFrom.size());
            return listToChooseFrom.get(randomIndex);
        }
    }

    /**
     * Method to count the number of agents that adopted any product.
     *
     * @param consumerAgents The agents for which to count the number of adoptions
     * @return The number of agents that adopted any product
     */
    public static int countAdoptedAgents(Set<ConsumerAgent> consumerAgents) {
        int adoptedAgents = 0;
        for(ConsumerAgent consumerAgent : consumerAgents){
            if(consumerAgent.getAdoptedProducts().size() > 0) adoptedAgents++;
        }
        return adoptedAgents;
    }

    /**
     * Method to count the number of agents that adopted the respective product.
     *
     * @param consumerAgents The agents for which to count the number of adoptions
     * @param productToCheckAdoptionFor The product for which the number of adopters is checked
     * @return The number of agents that adopted the respective product
     * @throws IllegalArgumentException Will be thrown if the product is null
     */
    public static int countAdoptedAgents(Set<ConsumerAgent> consumerAgents, Product productToCheckAdoptionFor) throws IllegalArgumentException{
        if(productToCheckAdoptionFor == null) throw new IllegalArgumentException("The product to check adoption for is null!!");
        int adoptedAgents = 0;
        for(ConsumerAgent consumerAgent : consumerAgents){
            if(consumerAgent.getAdoptedProducts().contains(productToCheckAdoptionFor)) adoptedAgents++;
        }
        return adoptedAgents;
    }

    /**
     * Method to count the number of agents that adopted a prodcut from the respective product group.
     *
     * @param consumerAgents The agents for which to count the number of adoptions
     * @param productGroupToCheckAdoptionFor The product group for which the number of adopters is checked
     * @return The number of agents that adopted any product of the respective productGroup
     * @throws IllegalArgumentException Will be thrown if the productGroup is null
     */
    public static int countAdoptedAgents(Set<ConsumerAgent> consumerAgents, ProductGroup productGroupToCheckAdoptionFor) throws IllegalArgumentException{
        if(productGroupToCheckAdoptionFor == null) throw new IllegalArgumentException("The product group to check adoption for is null!!");
        int adoptedAgents = 0;
        for(ConsumerAgent consumerAgent : consumerAgents){
            for(Product currentProduct : productGroupToCheckAdoptionFor.getDerivedProducts()) {
                if (consumerAgent.getAdoptedProducts().contains(currentProduct)) {
                    adoptedAgents++;
                    //dont count consumers twice
                    break;
                }
            }
        }
        return adoptedAgents;
    }

    /**
     * Method to compile a list of how many agent adopted a certain number of products.
     * Every entry in that list (of index i) is associated with the number of agents from the provided set that adopted i products
     *
     * @param consumerAgents The consumer agents for which to check the number of adopted products
     * @return An array list with keys: number of products adopted and values: number of agents that adopted these many products. Will be an empty list if the set of consumer agents is empty
     */
    public static ArrayList<Integer> countNoProductsAdoptedPerAgent(Set<ConsumerAgent> consumerAgents) {
        ArrayList<Integer> returnList = new ArrayList<Integer>();
        if(consumerAgents.isEmpty()) return returnList;
        int maxProductsAdopted = 0;
        //count the maximal number of adopted products (to make an estimate on how long to make the list
        for(ConsumerAgent agent : consumerAgents){
            if(agent.getAdoptedProducts().size() > maxProductsAdopted) maxProductsAdopted = agent.getAdoptedProducts().size();
        }
        //prepare the array list
        for(int index=0; index<maxProductsAdopted+1;index++){
            returnList.add(0);
        }
        //For every consumer agent, increase the index corresponding to the number of products agents adopted by 1
        for(ConsumerAgent agent : consumerAgents){
            returnList.set(agent.getAdoptedProducts().size(), returnList.get(agent.getAdoptedProducts().size())+1);
        }
        return returnList;
    }

    //chooses a random consumerAgentGroup according to the probabilities given by affinities

    /**
     * Method to chose a consumerAgentGroup based on the affinity between consumer agents groups.
     * Will choose a consumerAgent group with a probability proportional to the affinity the consumerAgentGroup has with each other CAG
     *
     * @param affinities The affinities between consumer agent groups
     * @param sourceConsumerAgentGroup The consumerAgentGroup from which a target group is to be found
     * @return The target consumer agent group chosen for the provided consumer group, proportional to the affinity between these two CAGs
     * @throws IllegalArgumentException will be thrown when the consumer agent group affinities don't contain data for the source CAG or one of the affinities is negative
     */
    public static ConsumerAgentGroup chooseTargetCAG(ConsumerAgentGroupAffinities affinities, ConsumerAgentGroup sourceConsumerAgentGroup) throws IllegalArgumentException{
        if(!affinities.getAffinities().containsKey(sourceConsumerAgentGroup)) throw new IllegalArgumentException("The affinities provided to the method to chose a target CAG don't have an entry for the CAG to find a target consumer agent group for!!");
        double affinitySum = 0.0;
        //create a map of cumulated affinities towards target consumer agent groups
        Map<ConsumerAgentGroup, Double> targetAffinities = affinities.getAffinities(sourceConsumerAgentGroup);
        SortedMap<Double, ConsumerAgentGroup> cumulatedAffinities = new TreeMap<Double, ConsumerAgentGroup>();
        for(ConsumerAgentGroup currentCag : targetAffinities.keySet()){
            affinitySum += targetAffinities.get(currentCag);
            if(targetAffinities.get(currentCag) > 0.0){
                cumulatedAffinities.put(affinitySum, currentCag);
            }else if(targetAffinities.get(currentCag) < 0) throw new IllegalArgumentException("One of the affinities (between "+sourceConsumerAgentGroup+" and "+currentCag+" is negative!! \n This should not be allowed, since affinities are supposed to be non-negative!!");
        }
        double diceRoll = Math.random() * affinitySum;
//        System.out.println("Dice roll is "+diceRoll+", with affinity sum being "+affinitySum);
        for(Double cumulatedAffinity : cumulatedAffinities.keySet()){
//            System.out.println("cumulated affinity is "+cumulatedAffinity+", which is for "+cumulatedAffinities.get(cumulatedAffinity).getGroupName());
            if(diceRoll < cumulatedAffinity) return cumulatedAffinities.get(cumulatedAffinity);
        }
        throw new IllegalStateException("ERROR!! No consumerAgentGroup was found for affinity map "+affinities+" and consumer group "+sourceConsumerAgentGroup);
    }

    /**
     * Method to find the least (or any of the least) connected node within a list of nodes,
     * potentially excluding the node to connect
     *
     * @param nodesWithDegrees Map to link the degree of connectivity of a set of nodes to the nodes with that connectivity
     * @param selfLoopRegular Flag to indicate whether self-loops are allowed. If so the source node is allowed to be a return value
     * @param sourceNode The node from which the connection is made (will be a candidate if selfLoopRegular (and not if not))
     * @return A random SNNode in the candidate set (nodesWithDegrees) that is among the least connected nodes
     * @throws IllegalArgumentException will be thrown if there is no legal node to choose (either the node set is empty or only contains the source node and the network is not selfLoopRegular)
     */
    public static SNNode getLeastConnectedNode(HashMap<Integer, Set<SNNode>> nodesWithDegrees, boolean selfLoopRegular, SNNode sourceNode) throws IllegalArgumentException{
        ArrayList<Integer> sortedKeys = new ArrayList<Integer>(nodesWithDegrees.keySet());
        Collections.sort(sortedKeys);
        for(Integer nodeDegree : sortedKeys){
            //find the first set within the nodesWithDegree that is not empty and chose a node from these
            if(!nodesWithDegrees.get(nodeDegree).isEmpty()){
                fooLog.debug("least connected node found a node of degree {}", nodeDegree);
                fooLog.debug("nodes with degree {}: {}", nodeDegree, nodesWithDegrees.get(nodeDegree));
                //only choose a node from the list if it doesnt contain an illegal node (i.e. the node itself when the network is not selfLoopRegular
                if(!(nodesWithDegrees.get(nodeDegree).contains(sourceNode)) || selfLoopRegular){
                    return chooseRandomNode(nodesWithDegrees.get(nodeDegree));
                }else{
                    //choose another node
                    Set<SNNode> nodesWithoutSourceNode = new HashSet<SNNode>();
                    nodesWithoutSourceNode.addAll(nodesWithDegrees.get(nodeDegree));
                    nodesWithoutSourceNode.remove(sourceNode);
                    if(nodesWithoutSourceNode.size() > 0) return chooseRandomNode(nodesWithoutSourceNode);
                }
            }else{
                fooLog.debug("no nodes of degree {}, see? {}", nodeDegree, nodesWithDegrees.get(nodeDegree));
            }
        }
        throw new IllegalArgumentException("Only node to choose from was the source node of the edge, which is prohibited in this case!!");
    }

    /**
     * Unsafe method to find the least (or any of the least) connected node within a list of nodes,
     * potentially excluding the node to connect. If no suitable node is found, it will return null
     *
     * @param nodesWithDegrees Map to link the degree of connectivity of a set of nodes to the nodes with that connectivity
     * @param selfLoopRegular Flag to indicate whether self-loops are allowed. If so the source node is allowed to be a return value
     * @param sourceNode The node from which the connection is made (will be a candidate if selfLoopRegular (and not if not))
     * @return A random SNNode in the candidate set (nodesWithDegrees) that is among the least connected nodes
     * @throws IllegalArgumentException will be thrown if there is no legal node to choose (either the node set is empty or only contains the source node and the network is not selfLoopRegular)
     */
    public static SNNode getLeastConnectedNodeUnsafe(HashMap<Integer, Set<SNNode>> nodesWithDegrees, boolean selfLoopRegular, SNNode sourceNode) throws IllegalArgumentException{
        ArrayList<Integer> sortedKeys = new ArrayList<Integer>(nodesWithDegrees.keySet());
        Collections.sort(sortedKeys);
        for(Integer nodeDegree : sortedKeys){
            //find the first set within the nodesWithDegree that is not empty and chose a node from these
            if(!nodesWithDegrees.get(nodeDegree).isEmpty()){
                fooLog.debug("least connected node found a node of degree {}", nodeDegree);
                fooLog.debug("nodes with degree {}: {}", nodeDegree, nodesWithDegrees.get(nodeDegree));
                //only choose a node from the list if it doesnt contain an illegal node (i.e. the node itself when the network is not selfLoopRegular
                if(!(nodesWithDegrees.get(nodeDegree).contains(sourceNode)) || selfLoopRegular){
                    return chooseRandomNode(nodesWithDegrees.get(nodeDegree));
                }else{
                    //choose another node
                    Set<SNNode> nodesWithoutSourceNode = new HashSet<SNNode>();
                    nodesWithoutSourceNode.addAll(nodesWithDegrees.get(nodeDegree));
                    nodesWithoutSourceNode.remove(sourceNode);
                    if(nodesWithoutSourceNode.size() > 0) return chooseRandomNode(nodesWithoutSourceNode);
                }
            }else{
                fooLog.debug("no nodes of degree {}, see? {}", nodeDegree, nodesWithDegrees.get(nodeDegree));
            }
        }
        return null;
    }

    /**
     * Chooses a random node from a set of nodes uniformly
     *
     * @param potentialNodes The set of nodes from which the resulting node can be chosen
     * @return A (uniformly) randomly chosen node from the set of potential nodes
     * @throws IllegalArgumentException Will be thrown if the set of nodes to choose from is empty
     */
    public static SNNode chooseRandomNode(Set<SNNode> potentialNodes) throws IllegalArgumentException{
        ArrayList<SNNode> potentialNodeArray = new ArrayList<SNNode>(potentialNodes);
        int targetIndex = (int) Math.floor(Math.random()*potentialNodeArray.size());
        return potentialNodeArray.get(targetIndex);
    }

    /**
     * Method to derive a ProductGroupAttribute Preference map from a preference configuration.
     * Will return the preferences in 'raw' form as Value-Double map.
     *
     * @param preferenceConfiguration The preference configuration which the map should be extracted from.
     * @return A map of ProductGroupAttributes and their preferences in 'raw' form as Value-Double map.
     */
    public static Map<ProductGroupAttribute, Map<Value, Double>> derivePGAPreferenceMap(PreferenceConfiguration preferenceConfiguration) {
        Map<ProductGroupAttribute, Map<Value, Double>> returnMap = new HashMap<ProductGroupAttribute, Map<Value, Double>>();
        //loop through the ProductGroupAttributeValueMappings within the preference configuration
        for(ProductGroupAttributeValueMapping pgavMapping : preferenceConfiguration.getProductGroupAttributePreferenceMapping()){
            //create the respective entry if the PGA is not yet present in the return map
            if(!(returnMap.keySet().contains(pgavMapping.getProductGroupAttribute()))) returnMap.put(pgavMapping.getProductGroupAttribute(), new HashMap<Value, Double>());
            returnMap.get(pgavMapping.getProductGroupAttribute()).put(pgavMapping.getValue(), pgavMapping.getMappingStrength());
        }
        return returnMap;
    }

    /**
     * Method to calculate the fraction of adopters of the product of interest within the social network of the consumer agent of interest
     *
     * @param consumerAgent The consumer agent of interest
     * @param productConcerned The product for which the fraction of adopters is to be counted
     * @param simulationContainer The container the simulation runs in
     * @return The fraction of neighbours of consumer agent that adopted productConcerned (#adopter/#neighbours)
     * @throws IllegalArgumentException Will be thrown when the product or the consumer agent are null or not part of the simulation container
     * @throws antlr.SemanticException Will be thrown when the agent of interest has no neighbours within the social network (fraction is undefined)
     */
    public static double calculateFractionAdoptersInSN(ConsumerAgent consumerAgent, Product productConcerned, SimulationContainer simulationContainer) throws IllegalArgumentException, SemanticException{
        if(consumerAgent == null) throw new IllegalArgumentException("ConsumerAgent of interest is null!! Must be defined!");
        else if(productConcerned == null) throw new IllegalArgumentException("Product of interest is null!! Must be defined!");
        else if(!simulationContainer.getConsumerAgents().contains(consumerAgent)) throw new IllegalArgumentException("ConsumerAgent of interest is not part of the simulation container!! Please make sure to use only valid agents in the simulation!!");
        else if(!simulationContainer.getProducts().contains(productConcerned)) throw new IllegalArgumentException("Product of interest is not part of the simulation container!! Please make sure to use only valid products in the simulation!!");
        else if(simulationContainer.getSocialNetwork().getSocialGraph().getNeighbours().size() < 1) throw new SemanticException("The ConsumerAgent of interest doesn't have any neighbours! \n As such, the number of adopters in his social network is undefined!!");
        else{
            int noAdoptersNeighbours = 0;
            for(SNNode neighbourNode : simulationContainer.getSocialNetwork().getSocialGraph().getAllMediaNeighbours(consumerAgent.getCorrespondingNodeInSN())){
                if(simulationContainer.getsNMap().get(neighbourNode).getAdoptedProducts().contains(productConcerned)) noAdoptersNeighbours++;
            }
            return (double) noAdoptersNeighbours/simulationContainer.getSocialNetwork().getSocialGraph().getNeighbours().size();
        }
    }

    /**
     * Method to convert a set of preferences to a map of Value-Double pairs
     *
     * @param preferences The preferences to extract the map from
     * @return A map of Value-Double pairs corresponding to the preferences of interest
     * @throws IllegalArgumentException Will be thrown if the preference set to be converted is empty
     */
    public static Map<Value, Double> preferenceToValueQuantityMap(Set<Preference> preferences) throws IllegalArgumentException{
        if(preferences.isEmpty()) throw new IllegalArgumentException("Preference set to convert is empty!");
        Map<Value, Double> returnMap = new HashMap<Value, Double>(preferences.size());
        for(Preference currentPreference : preferences){
            returnMap.put(currentPreference.getValue(), currentPreference.getStrength());
        }
        return returnMap;
    }

    /**
     * Method to find the best products in a map of products and integer values.
     * The map is assumed to assign a quality to each product, and the method will find the products
     * that have the maximum value of it
     *
     * @param productQualityMap Map that assigns a quality to each product
     * @return The set of products that are optimal with respect to the provided data
     * @throws IllegalArgumentException Will be thrown when the productQualityMap is empty
     */
    public static Set<Product> findBestProducts(Map<Product, Integer> productQualityMap) throws IllegalArgumentException{
        if(productQualityMap.isEmpty()) throw new IllegalArgumentException("productQualityMap to be used to find the best products is empty! Like this no best products can be found!!");
        Set<Product> bestProducts = new HashSet<Product>(productQualityMap.keySet().size());
        //find maximal value
        Integer maxValue = 0;
        for(Product currentProduct : productQualityMap.keySet()){
            if(productQualityMap.get(currentProduct) > maxValue) maxValue = productQualityMap.get(currentProduct);
        }
        //add all products of this value to the return set
        for(Product currentProduct : productQualityMap.keySet()){
            if(productQualityMap.get(currentProduct).equals(maxValue)){
              bestProducts.add(currentProduct);
            }
        }
        return bestProducts;
    }


    /**
     * Method to calculate the number of adopters of a set of products of interest within the social network of the consumer agent of interest
     *
     * @param consideredProducts The set of products for which the number of adopters is to be counted
     * @param consumerAgent The consumer agent whose neighbours adoption patterns are to be counted
     * @param simulationContainer The container the simulation runs in
     * @return A map indicating how many neighbours of consumerAgent have adopted a certain product for each prodcut
     * @throws IllegalArgumentException Will be thrown when the products or the consumer agent are null (or empty) or not part of the simulation container
     * @throws antlr.SemanticException Will be thrown when the agent of interest has no neighbours within the social network (fraction is undefined)
     */
    public static Map<Product, Integer> countNoAdoptersInNeighbourhood(Set<Product> consideredProducts, ConsumerAgent consumerAgent, SimulationContainer simulationContainer) throws IllegalArgumentException, SemanticException{
            if(consumerAgent == null) throw new IllegalArgumentException("ConsumerAgent of interest is null!! Must be defined!");
            else if(consideredProducts == null) throw new IllegalArgumentException("Products of interest are null!! Must be defined!");
            else if(consideredProducts.isEmpty()) throw new IllegalArgumentException("Products of interest is empty!! Must be defined, otherwise no counting can take place!");
            else if(!simulationContainer.getConsumerAgents().contains(consumerAgent)) throw new IllegalArgumentException("ConsumerAgent of interest is not part of the simulation container!! Please make sure to use only valid agents in the simulation!!");
            else if(simulationContainer.getSocialNetwork().getSocialGraph().getNeighbours().size() < 1) throw new SemanticException("The ConsumerAgent of interest doesn't have any neighbours! \n As such, the number of adopters in his social network is undefined!!");
            else {
                Map<Product, Integer> noAdoptersInNeighbourhood = new HashMap<Product, Integer>(consideredProducts.size());
                for (Product currentProduct : consideredProducts) {
                    if (!simulationContainer.getProducts().contains(currentProduct))
                        throw new IllegalArgumentException("One of the products of interest (" + currentProduct + ") is not part of the simulation container!! Please make sure to use only valid products in the simulation!!");
                    noAdoptersInNeighbourhood.put(currentProduct, 0);
                }
                fooLog.debug("social network is {}", simulationContainer.getSocialNetwork().getSocialGraph());
                fooLog.debug("neighbours are {}", simulationContainer.getSocialNetwork().getSocialGraph().getNeighbours());
                fooLog.debug("neighbours of ca {} are {}", consumerAgent, simulationContainer.getSocialNetwork().getSocialGraph().getNeighbours().get(consumerAgent.getCorrespondingNodeInSN()));
                //for all neighbours
                for (SNNode neighbouringNode : simulationContainer.getSocialNetwork().getSocialGraph().getAllMediaNeighbours(consumerAgent.getCorrespondingNodeInSN())) {
                    //count how often which product was adopted
                    for (Product currentProduct : consideredProducts) {
                        if (simulationContainer.getsNMap().get(neighbouringNode).getAdoptedProducts().contains(currentProduct)) {
                            Integer noAdopters = noAdoptersInNeighbourhood.get(currentProduct);
                            noAdoptersInNeighbourhood.remove(currentProduct);
                            noAdoptersInNeighbourhood.put(currentProduct, noAdopters + 1);
                        }
                    }
                }
                for (Product currentProduct : consideredProducts) {
                    fooLog.debug("product {} was adopted {} times already by neighbours", currentProduct, noAdoptersInNeighbourhood.get(currentProduct));
                }
                return noAdoptersInNeighbourhood;
            }
    }

    //TODO check whether it can be removed
    public static Product findBestProduct(Map<Product, Integer> productImportanceMap) throws SemanticException{
        Set<Product> bestProducts = findBestProducts(productImportanceMap);
        if(bestProducts.size() == 1) return (Product) bestProducts.toArray()[0];
        else throw new SemanticException("Several products have the same quality; A single best product is undefined.\n Please use the findBestProducts method located in this class");
   /*     fooLog.debug("productImportanceMap is {}", productImportanceMap);
        Integer maxIndex = 0;
        boolean bestProductUnique = true;
        boolean bestProductFound = false;
        Product bestProduct = null;
        for(Product currentProduct : productImportanceMap.keySet()){
            if(productImportanceMap.get(currentProduct) > maxIndex) maxIndex = productImportanceMap.get(currentProduct);
        }
        for(Product currentProduct : productImportanceMap.keySet()){
            if((productImportanceMap.get(currentProduct) == maxIndex) && !bestProductFound){
                bestProductFound = true;
                bestProduct = currentProduct;
                fooLog.debug("best product of maxIndex = {} is {}", maxIndex, bestProduct.getName());
            }
            else if((productImportanceMap.get(currentProduct) == maxIndex) && bestProductFound) bestProductUnique = false;
        }
        if(!bestProductFound) fooLog.warn("No best product was found even though it was found in the first round! This should never happen!!!!");
        if(bestProductUnique){
            fooLog.debug("best product is {}", bestProduct.getName());
            return bestProduct;
        }
        else return null;*/
    }

/*    *//**
     * Method to find a overwrite decision making process for a set of product.
     * If all products share the same overwrite decision process, it will be returned;
     * if not, will return null
     *
     * @param productsQualifyingForDecisison Products to be checked for a common overwrite decision process
     * @return The decision process common to all products if there is one; null otherwise
     *//*
    public static ConsumerAgentAdoptionDecisionProcess findCommonOverwriteDecisionMakingProcessProducts(Set<Product> productsQualifyingForDecisison) {
        //make set of product groups to check
        Set<ProductGroup> productGroupsInvolved = deriveProductGroups(productsQualifyingForDecisison);
        ConsumerAgentAdoptionDecisionProcess commonProcess = null;
        for(ProductGroup currentPG : productGroupsInvolved){
            if((commonProcess == null) && (currentPG.getOverwriteDecisionProcess() != null)){
                commonProcess = currentPG.getOverwriteDecisionProcess();
            }else if((commonProcess != null) && (commonProcess != currentPG.getOverwriteDecisionProcess())) return null;
        }
        return commonProcess;
    }*/

     /**
     * Method to determine if there is a overwrite decision making process common to a set of product.
     * If all products share the same overwrite decision process, it will return true, if not false.
     *
     * @param productsQualifyingForDecisison Products to be checked for a common overwrite decision process
     * @return true if all products share the same decision overwrite process, false if otherwise
     */
    public static boolean findCommonOverwriteDecisionMakingProcessProducts(Set<Product> productsQualifyingForDecisison) {
        if(productsQualifyingForDecisison.isEmpty()) throw new IllegalArgumentException("No products are to be checked for a common decision process!!");
        //make set of product groups to check
        Set<ProductGroup> productGroupsInvolved = deriveProductGroups(productsQualifyingForDecisison);
        //use the first process to reference other processes
        ConsumerAgentAdoptionDecisionProcess commonProcess = ((ProductGroup) productGroupsInvolved.toArray()[0]).getOverwriteDecisionProcess();
        for (ProductGroup currentPG : productGroupsInvolved) {
            //if any process differs from the first, they all differ (since its an equivalent relation)
            if (commonProcess != currentPG.getOverwriteDecisionProcess()) return false;
        }
        if(commonProcess == null) return false;
        else return true;
    }

    /**
     * Method to derive the standard product of a set of products.
     * Will throw an error if products are not all part of the same product group
     * and otherwise return the (instantiation of the) standard product of the product group
     * (if the product group has any)
     *
     * @param consideredProducts The products whose standard product is to be derived
     * @param simulationContainer The container the simulation runs in
     * @return The instantiation of the standard product of the shared product group of the consideredProducts (if not unique it will throw an error)
     * @throws IllegalArgumentException Will be thrown if products fall in several product groups, are not part of the simulation, or the container is null
     */
    public static Product determineStandardProduct(Set<Product> consideredProducts, SimulationContainer simulationContainer) throws IllegalArgumentException{
        if(simulationContainer == null) throw new IllegalArgumentException("Simulation container is null!! Please instatiate a simulation!!");
        else if(consideredProducts.isEmpty()) throw new IllegalArgumentException("Product set to derive the standard products from is empty!! No product can be determined!!");
        //check if all products are in the simulation
        //TODO uncomment when check out google sets!!
            //else if(!(Sets.difference(consideredProducts, Sets.union(simulationContainer.getProducts(), simulationContainer.getHistoricalProducts()))).isEmpty()) throw new IllegalArgumentException("Some of the products to find a standard product are not contained in the simulation!!");
        else{
            Set<ProductGroup> productGroupsInvolved = deriveProductGroups(consideredProducts);
            if(productGroupsInvolved.size() != 1) throw new IllegalArgumentException("Products to derive standard products from are contained in different product groups!! Semantics are ambiguous and can't be resolved!!");
            else{
                return  simulationContainer.getFixedProductMap().get(((ProductGroup) productGroupsInvolved.toArray()[0]).getStandardProduct());
            }
        }
    }

    /**
     * Method to derive the product groups a set of products correspond to.
     * Will return the union of the product groups of all products
     *
     * @param products The products whose product groups are to be derived
     * @return The corresponding product groups of the products in the parameter
     * @throws IllegalArgumentException Will be thrown when set of products are empty
     */
    public static Set<ProductGroup> deriveProductGroups(Set<Product> products) throws IllegalArgumentException{
        if(products.isEmpty()) throw new IllegalArgumentException("The set of products for which the product groups are to derived is empty!!");
        else {
            Set<ProductGroup> productGroupsInvolved = new HashSet<ProductGroup>(products.size());
            for (Product currentProduct : products) {
                if (!(productGroupsInvolved.contains(currentProduct.getPartOfProductGroup())))
                    productGroupsInvolved.add(currentProduct.getPartOfProductGroup());
            }
            return productGroupsInvolved;
        }
    }

    /**
     * Method to order the Values corresponding to preferences in the parameter set
     * by their value strength.
     * Will order the corresponding values by order descending in preference strength
     *
     * @param preferences The preferences to be ordered
     * @return An ordered list (in descending strength) of the Values corresponding to the provided preferences
     * @throws IllegalArgumentException Will be thrown when the preference set is empty
     */
    public static ArrayList<Value> orderValuesByPreference(Set<Preference> preferences) throws IllegalArgumentException{
        if(preferences.isEmpty()) throw new IllegalArgumentException("Set of preferences to extract values from is empty!!");
        ArrayList<Preference> preferenceList = new ArrayList<Preference>(preferences);
        Collections.sort(preferenceList);
        ArrayList<Value> returnList = new ArrayList<Value>();
        for(Integer index=0;index<preferenceList.size();index++){
            returnList.add(index, preferenceList.get(index).getValue());
        }
        Collections.reverse(returnList);
        return returnList;
    }

    /**
     * Method to select the initially adopted products for each agent / distribute the initially adopted products among the agents.
     * Will assign a number of products (as FixedProductDescriptions) to a set of integers (representing agent indices)
     * proportionally to each distribution associated with the fixed product description.
     *
     *
     * @param numberOfConsumers The number of consumers to assign fixed products to
     * @param initialProductConfiguration A map of fixedProductDescriptions (representing products) and their distribution (representing how likely a consumer agent is to have the product adopted initially)
     * @return A map of indices (representing consumer agents) and a set of fixedProductDescriptions representing the products the corresponding agent has adopted at the beginning of the simulation
     * @throws IllegalArgumentException Will be thrown when the number of consumers is non-positive
     */
    public static Map<Integer,Set<FixedProductDescription>> determineInitiallyAdoptedProducts(int numberOfConsumers, Map<FixedProductDescription, UnivariateDistribution> initialProductConfiguration) throws IllegalArgumentException{
        if(numberOfConsumers < 1) throw new IllegalArgumentException("An illegal number of consumers ("+numberOfConsumers+") was used to determine the initial adoptiion of products!!");
        Map<Integer, Set<FixedProductDescription>> initiallyAdoptedProductsByAgentIndex = new HashMap<Integer, Set<FixedProductDescription>>(numberOfConsumers);
        //Map to determine how many agents adopt a certain product (these will be distributed among the agents)
        Map<FixedProductDescription, Integer> numInitialAdoptersPerProduct = new HashMap<FixedProductDescription, Integer>(initialProductConfiguration.keySet().size());
        for(Integer index=0;index<numberOfConsumers;index++){
            initiallyAdoptedProductsByAgentIndex.put(index, new HashSet<FixedProductDescription>());
        }
        //determine the number of (and index of) initial adopter agents for the respective product
        for(FixedProductDescription fpd : initialProductConfiguration.keySet()){
            fooLog.debug("fpd is {}, numCons is {} and inProdConf is {}", fpd, numberOfConsumers, initialProductConfiguration.get(fpd));
            //determine the number of initial adopters for the product
            numInitialAdoptersPerProduct.put(fpd, (int) Math.floor(numberOfConsumers* initialProductConfiguration.get(fpd).draw()));
            //see which direction to go (find adopters (if <= 50%) or find non-adopters (if > 50%)) in order to improve performance and acurracy
            if(numInitialAdoptersPerProduct.get(fpd) > (Math.floor(((double) numberOfConsumers)/ 2.0))){
                Set<Integer> nonAdopterIndices = deriveNonAdopters(numInitialAdoptersPerProduct.get(fpd), numberOfConsumers);
                //check for every customer if it is not a non-adopter
                for(Integer currentIndex = 0; currentIndex < numberOfConsumers; currentIndex++){
                    if(!(nonAdopterIndices.contains(currentIndex))) initiallyAdoptedProductsByAgentIndex.get(currentIndex).add(fpd);
                }
            }else{
                Set<Integer> adopterIndices = deriveAdopters(numInitialAdoptersPerProduct.get(fpd), numberOfConsumers);
                //check for every customer if it is an adopter
                for(Integer currentIndex : adopterIndices){
                    initiallyAdoptedProductsByAgentIndex.get(currentIndex).add(fpd);
                }
            }
        }
        return initiallyAdoptedProductsByAgentIndex;
    }

    //TODO move to a random helper
    //TODO make more efficient by using a list to draw from (so not a lot of misses occur)
    /**
     * Helper method to draw a number of integers without repetition uniformly
     *
     * @param numberIndicesToFind number of numbers to draw within [0:numberOfConsumers)
     * @param numberOfConsumers upper bound of set of numbers to draw from
     * @return A set of uniformly numberIndicesToFind drawn integers between 0 and numberOfConsumers without repetition
     * @throws IllegalArgumentException Will be thrown if any of the arguments is non-positive or the number of numbers to draw exceeds the total number of numbers to draw from
     */
    private static Set<Integer> deriveAdopters(int numberIndicesToFind, int numberOfConsumers){
        if(numberIndicesToFind > numberOfConsumers) throw new IllegalArgumentException("Numbers to draw without repetition is larger than the set of numbers to draw from!! This would result in an infinite loop!!");
        else if(numberIndicesToFind < 1) throw new IllegalArgumentException("number of integers to find is negative; doesn't make sense!!");
        else if(numberOfConsumers < 1) throw new IllegalArgumentException("Numbers to draw from is non-positive; doesn't make sense!!");
        else{
            Set<Integer> derivedIndices = new HashSet<Integer>(numberIndicesToFind);
            for(int currentIndexPosition = 0; currentIndexPosition < numberIndicesToFind; currentIndexPosition++){
                boolean indexFound = false;
                while(!indexFound){
                    Integer preliminaryIndex = (int) Math.floor(Math.random()*((double) numberOfConsumers));
                    if(!(derivedIndices.contains(preliminaryIndex))){
                        derivedIndices.add(preliminaryIndex);
                        indexFound = true;
                    }
                }
            }
            return derivedIndices;
        }
        /*//determine adopterIndices for products
        for(int fpdAgentIndex=0; fpdAgentIndex < numInitialAdoptersPerProduct.get(fpd); fpdAgentIndex++){
            boolean validAdopterFound = false;
            //failSafeCheckIndex tests every n^2 times whether the configuration can be resolved. If not it will output this to the user, so an indefinite loop will be detected
            int failSafeCheckIndex = (int) Math.floor(Math.pow(numberOfConsumers, 2));
            int loopIndex = 0;
            while(!validAdopterFound){
                if(loopIndex == failSafeCheckIndex){
                    if(!initialConfigurationResolvable(numInitialAdoptersPerProduct, numberOfConsumers)) fooLog.error("Configuration errornous! Program will not terminate. Initial product distribution can not be resolved!!!!");
                }
                int preliminaryAgentIndex = (int) Math.floor(Math.random() * numberOfConsumers);
                //fooLog.info("inConfConf {}",initialConfigurationConflict(initiallyAdoptedProductsByAgentIndex.get(preliminaryAgentIndex), fpd));
                if(!initialConfigurationConflict(initiallyAdoptedProductsByAgentIndex.get(preliminaryAgentIndex), fpd)){
                    validAdopterFound = true;
                    initiallyAdoptedProductsByAgentIndex.get(preliminaryAgentIndex).add(fpd);
                }
                loopIndex++;
            }
        }*/
    }

    //TODO move to a random helper
    //TODO make more efficient by using a list to draw from (so not a lot of misses occur)
    /**
     * Helper method to draw a number of integers without repetition uniformly,
     * using the complementary number of indices to find
     *
     * @param complementaryNumberIndicesToFind complement of the number of numbers to draw within [0:numberOfConsumers)
     * @param numberOfConsumers upper bound of set of numbers to draw from
     * @return A set of (numberOfConsumers - complementaryNumberIndicesToFind) uniformly drawn integers between 0 and numberOfConsumers without repetition
     * @throws IllegalArgumentException Will be thrown if any of the arguments is non-positive or the number of numbers to draw exceeds the total number of numbers to draw from
     */
    private static Set<Integer> deriveNonAdopters(int complementaryNumberIndicesToFind, int numberOfConsumers) {
        if(complementaryNumberIndicesToFind > numberOfConsumers) throw new IllegalArgumentException("Numbers to draw without repetition is larger than the set of numbers to draw from!! This would result in an infinite loop!!");
        else if(complementaryNumberIndicesToFind < 1) throw new IllegalArgumentException("number of integers to find is negative; doesn't make sense!!");
        else if(numberOfConsumers < 1) throw new IllegalArgumentException("Numbers to draw from is non-positive; doesn't make sense!!");
        else{
            int numberIndicesToFind = numberOfConsumers - complementaryNumberIndicesToFind;
            Set<Integer> derivedIndices = new HashSet<Integer>(numberIndicesToFind);
            for (int currentIndexPosition = 0; currentIndexPosition < numberIndicesToFind; currentIndexPosition++) {
                boolean indexFound = false;
                while (!indexFound) {
                    Integer preliminaryIndex = (int) Math.floor(Math.random() * ((double) numberOfConsumers));
                    if (!(derivedIndices.contains(preliminaryIndex))) {
                        derivedIndices.add(preliminaryIndex);
                        indexFound = true;
                    }
                }
            }
            return derivedIndices;
        }
    }

    /*//checks whether there is a conflict between fpd and fixedProductDescriptions
    private static boolean initialConfigurationConflict(Set<FixedProductDescription> fixedProductDescriptions, FixedProductDescription fpd) {
        for(FixedProductDescription fpdAdopted : fixedProductDescriptions){
            //if the product group is the same and only one product is allowed in this group, return false
            if(fpd.getPartOfProductGroup() == fpdAdopted.getPartOfProductGroup()){
                if(fpd.getPartOfProductGroup().getSingleAdoption()){
                    return true;
                }
            }
            else{
                //check if product group excludes fpd to check
                if(fpdAdopted.getPartOfProductGroup().getExcludeProductGroup().contains(fpd.getPartOfProductGroup())) return true;
            }
        }
        //so far no conflict has been detected for single adoption or exclusion; check prerequisite
        for(ProductGroup pg : fpd.getPartOfProductGroup().getPrerequisiteProductGroups()){
            boolean preRequisiteGroupAdopted = false;
            for(FixedProductDescription fpdAdopted : fixedProductDescriptions){
                if(fpdAdopted.getPartOfProductGroup() == pg){
                    preRequisiteGroupAdopted = true;
                    break;
                }
            }
            if(!preRequisiteGroupAdopted){
                return true;
            }
        }
        //if this test is passed as well, no conflict is present
        return false;
    }*/

    /**
     * Aggregates the total number of nodes from a list of nodes by degree.
     * Doesn't discriminate between nodes of different degree.
     *
     * @param nodesByDegree HashMap of the nodes to count and their degree
     * @return The total number of nodes in the nodesByDegree set. Does not discriminate between the
     */
    public static int countNodes(HashMap<Integer, Set<SNNode>> nodesByDegree) {
        int totalNodes = 0;
        for(Integer nodeDegree : nodesByDegree.keySet()){
            fooLog.debug("counting nodes: {} nodes with degree {}", nodesByDegree.get(nodeDegree).size(), nodeDegree);
            totalNodes += nodesByDegree.get(nodeDegree).size();
        }
        return totalNodes;
    }

    /**
     * Method to derive the product group a set of products correspond to.
     * Will throw an error if the products dont share a common product group.
     *
     * @param potentialProducts The products whose product groups are to be derived
     * @return The corresponding product group of the products in the parameter
     * @throws IllegalArgumentException Will be thrown when set of products are empty or don't share the same product group
     */
    public static ProductGroup findCommonProductGroup(Set<Product> potentialProducts) throws IllegalArgumentException{
        if(potentialProducts.isEmpty()) throw new IllegalArgumentException("The set of products to derive a common product group from is empty!!");
        Set<ProductGroup> productGroups = deriveProductGroups(potentialProducts);
        if(productGroups.size() == 1) return (ProductGroup) (productGroups.toArray()[0]);
        else throw new IllegalArgumentException("The products don't share the same product group!!!");
    }

    /**
     * Method to extract the investmentCost product attribute of a product (if present).
     * Will throw an exception if not present
     *
     * @param product The product from which investmentCost is extracted
     * @return The product attribute of the product with the name investmentCost
     * @throws IllegalArgumentException Gets thrown when the product is null or doesnt containt the product attribute investmentCost
     */
    public static ProductAttribute getInvestmentCostPA(Product product) throws IllegalArgumentException{
        if(product == null) throw new IllegalArgumentException("The product of interest in getInvestmentCostPA is null!!");
        for(ProductAttribute productAttribute : product.getProductAttributes()){
            if(productAttribute.getCorrespondingProductGroupAttribute().getName().equals("investmentCost")) return productAttribute;
        }
        throw new IllegalArgumentException("The product does not have a product attribute corresponding to investmentCost");
    }

    /**
     * Method to extract the compatibility product attribute of a product (if present).
     * Will throw an exception if not present
     *
     * @param product The product from which compatibility is extracted
     * @return The product attribute of the product with the name compatibility
     * @throws IllegalArgumentException Gets thrown when the product is null or doesnt containt the product attribute compatibility
     */
    public static ProductAttribute getCompatibilityPA(Product product) throws IllegalArgumentException{
        if(product == null) throw new IllegalArgumentException("The product "+product+" is null!!");
        for(ProductAttribute productAttribute : product.getProductAttributes()){
            if(productAttribute.getCorrespondingProductGroupAttribute().getName().equals("compatibility")) return productAttribute;
        }
        throw new IllegalArgumentException("The product does not have a product attribute corresponding to compatibility");
    }

    /**
     * Method to transform a map of ProductGroupAttributes and the values along them into ProductAttributes
     *
     * @param productGroupAttributeValueMap A map of PGAs and the values the resulting ProductAttributes should exhibit
     * @return A set of the newly created ProductAttributes
     */
    public static Set<ProductAttribute> makeProductAttributesFromProductGroupAttributeValueMap(Map<ProductGroupAttribute, Double> productGroupAttributeValueMap) {
        Set<ProductAttribute> returnSet = new HashSet<>(productGroupAttributeValueMap.keySet().size());
        for(ProductGroupAttribute currentPGA : productGroupAttributeValueMap.keySet()){
            returnSet.add(new ProductAttribute(productGroupAttributeValueMap.get(currentPGA), currentPGA));
        }
        return returnSet;
    }

    /**
     * Method to see if a product overwrite a decision process / which one would be used for the product adoption by a ConsumerAgent
     * Will give back the products decision overwrite process (if not null) or else the decision process of the consumer actor
     *
     * @param consumerAgent agent to whose decision process this method defaults
     * @param product product that potentially overwrites the decision process
     * @return The decision process used
     */
    public static ConsumerAgentAdoptionDecisionProcess getEmployedDecisionProcess(ConsumerAgent consumerAgent, Product product) {
        if(product.getPartOfProductGroup().getOverwriteDecisionProcess() == null) return consumerAgent.getCorrespondingConsumerAgentGroup().getDecisionProcessEmployed();
        else return product.getPartOfProductGroup().getOverwriteDecisionProcess();
    }

    /**
     * Method to cast all entries of type ScriptedProductEvent in a list into an ArrayList of Events (by one-by-one casting them)
     *
     * @param scheduledProductEvents The ScriptedProductEvents to cast to Events
     * @return An ArrayList of type Event based on the scheduledProductEvents
     */
    public static List<Event> ScriptedEventToEvent(List<ScriptedProductEvent> scheduledProductEvents) {
        ArrayList<Event> eventList = new ArrayList<Event>();
        eventList.addAll(scheduledProductEvents);
        return eventList;
    }

    /**
     * Method to find the adoptedProduct in a set of AdoptedProducts that corresponds to given product
     *
     * @param adoptedProducts The set of AdoptedProducts to find the product in
     * @param respectiveProduct The product the AdoptedProduct to find refers to
     * @return The AdoptedProduct in adoptedProducts that corresponds to respectiveProduct
     * @throws IllegalArgumentException Will be thrown when the set of adoptedProducts doesn't contain any that corresponds to the respectiveProduct
     */
    public static AdoptedProduct getCorrespondingAdoptedProduct(Set<AdoptedProduct> adoptedProducts, Product respectiveProduct) throws IllegalArgumentException{
        for(AdoptedProduct currentAP : adoptedProducts){
            if(currentAP.getCorrespondingProduct().equals(respectiveProduct)) return currentAP;
        }
        throw new IllegalArgumentException("Set of adoptedProducts does not contain any refering to product "+respectiveProduct);
    }

    /**
     * Method to check whether any of the AdoptedProducts corresponds to a given product
     *
     * @param adoptedProducts The set of AdoptedProducts to check (corresponding) membership in
     * @param correspondingProduct The product the set membership of the corresponding AdoptedProduct is to be found
     * @return Whether one of the adopted products corresponds to the given product
     */
    public static boolean anyAdoptedProductRefersToProduct(Set<AdoptedProduct> adoptedProducts, Product correspondingProduct) {
        for(AdoptedProduct currentAP : adoptedProducts){
            if(currentAP.getCorrespondingProduct().equals(correspondingProduct)) return true;
        }
        return false;
    }

    /**
     * Method to choose a random piece of Information from a set of information.
     *
     * @param informationSet The set of information to choose a piece from
     * @return A randomly chosen Information out of the set of provided information
     */
    public static Information chooseRandomInformation(Set<Information> informationSet) {
        ArrayList<Information> informationToChooseFrom = new ArrayList<>(informationSet);
        int randomIndex = (int) Math.floor(Math.random()*informationToChooseFrom.size());
        return informationToChooseFrom.get(randomIndex);
    }

    /**
     * Method to choose a random piece of ProductAttributeInformation from a set of ProductAttributeInformation.
     *
     * @param informationSet The set of ProductAttributeInformation to choose a piece from
     * @return A randomly chosen ProductAttributeInformation out of the set of provided information
     */
    public static ProductAttributeInformation chooseRandomProductAttributeInformation(Set<ProductAttributeInformation> informationSet) {
        ArrayList<ProductAttributeInformation> informationToChooseFrom = new ArrayList<>(informationSet);
        int randomIndex = (int) Math.floor(Math.random()*informationToChooseFrom.size());
        return informationToChooseFrom.get(randomIndex);
    }

    /**
     * Method to derive all needs of the consumerAgentConcerned not currently satisfied by any
     * of the adopted products
     *
     * @param consumerAgentConcerned The ConsumerAgent to derive the unsatisfied needs from
     * @return All needs not currently satisfied by adopted products
     */
    public static Set<Need> getUnsatisfiedNeeds(ConsumerAgent consumerAgentConcerned) {
        Set<Need> unsatisfiedNeeds = consumerAgentConcerned.getAssociatedSimulationContainer().getNeedsInSimulation();
        for(AdoptedProduct adoptedProduct : consumerAgentConcerned.getAdoptedProducts()){
            unsatisfiedNeeds.removeAll(adoptedProduct.getCorrespondingProduct().getPartOfProductGroup().getNeedsSatisfied());
        }
        return unsatisfiedNeeds;
    }

    /**
     * Method to sort the needs in a set based on their order in the needDevelopmentScheme.
     * Filters the needs of the NeedsByNeedIndicatorValue of the NeedDevelopmentScheme
     * by whether they are part of the needsToSatisfy set
     *
     * @param needsToSatisfy The needs to be included in the return list
     * @param needDevelopmentScheme The NeedDevelopmentScheme within which the Needs are to be sorted
     * @return An ordered list of the needsToSatisfy by their order in the needDevelopmentScheme
     */
    public static List<Need> sortNeeds(Set<Need> needsToSatisfy, NeedDevelopmentScheme needDevelopmentScheme) {
        List<Need> sortedNeeds = needDevelopmentScheme.sortNeedsByNeedIndicatorValue();
        List<Need> returnList = new ArrayList<>();
        Iterator<Need> needIterator = sortedNeeds.iterator();
        while(needIterator.hasNext()){
            Need currentNeed = needIterator.next();
            if(needsToSatisfy.contains(currentNeed)) returnList.add(currentNeed);
        }
        return returnList;
    }

    public static double getMinimum(Set<Double> doubles) {
        double minimum = Double.MAX_VALUE;
        for(Double currentValue : doubles){
            if(currentValue < minimum) minimum = currentValue;
        }
        return minimum;
    }

    public static double getMaximum(Set<Double> doubles) {
        double maximum = -Double.MAX_VALUE;
        for(Double currentValue : doubles){
            if(currentValue > maximum) maximum = currentValue;
        }
        return maximum;
    }

    public static void detectDoubleEdges(Set<SNEdge> preliminaryEdgeSet, Set<SNNode> nodeSet) {
        Map<SNNode, Set<SNNode>> neighbouringNodes = new HashMap<>();
        for(SNNode currentNode : nodeSet){
            neighbouringNodes.put(currentNode, new HashSet<>());
        }
        for(SNEdge currentEdge : preliminaryEdgeSet){
            if(neighbouringNodes.get(currentEdge.getSource()).contains(currentEdge.getTarget())) System.out.println("Double edge detected");
            neighbouringNodes.get(currentEdge.getSource()).add(currentEdge.getTarget());
        }
    }

    /**
     * Method to randomly choose noNodesToPick nodes from a set of nodes
     *
     * @param nodesToPick The nodes to choose from
     * @param noNodesToPick The number of nodes to chose
     * @return A subset of nodes of nodesToPick with the size of the set corresponding to noNodesToPick
     * @throws IllegalArgumentException Will be thrown when the number of nodes to pick exceeds the number of nodes in the set to pick from
     */
    public static Set<SNNode> pickNNodes(Set<SNNode> nodesToPick, int noNodesToPick) throws IllegalArgumentException{
        if(noNodesToPick > nodesToPick.size()) throw new IllegalArgumentException("Cant pick "+noNodesToPick+" from "+nodesToPick.size()+" nodes!!");
        else if(nodesToPick.size() == noNodesToPick) return nodesToPick;
        else{
            Set<SNNode> returnSet = new HashSet<>();
            Set<SNNode> remainingNodes = new HashSet<>();
            for(SNNode currentNode : nodesToPick){
                remainingNodes.add(currentNode);
            }
            for(int i=0;i<noNodesToPick;i++){
                SNNode pickedNode = chooseRandomNode(remainingNodes);
                remainingNodes.remove(pickedNode);
                returnSet.add(pickedNode);
            }
            return returnSet;
        }
    }

    /**
     * Method to choose an Integer value from the keyset of the argument
     * based on its probability density specified as values.
     * Each value of a pair in the map thus specifies how 'likely' it is that the respective key is returned
     *
     * @param integerProbabilityMap A map of values to choose and their probability to be chosen
     * @return A random key from the integerProbabilityMap, proportional to the respective value
     */
    public static Integer chooseIntegerByDistribution(Map<Integer, Double> integerProbabilityMap) {
        double cumulatedProbability = 0.0;
        //Map to hold the cumulative probability that the current or a former value would be chosen
        //(whereas the former would have already been returned)
        SortedMap<Double, Integer> indexByCumulatedProbability = new TreeMap<>();
        //Set up the respective sorted map and calculate the cumulative probability of all respective events
        for(Integer index : integerProbabilityMap.keySet()){
            if(integerProbabilityMap.get(index) > 0.0) {
                indexByCumulatedProbability.put(cumulatedProbability + integerProbabilityMap.get(index), index);
                cumulatedProbability += integerProbabilityMap.get(index);
            }
        }
        if(cumulatedProbability == 0.0) throw new IllegalStateException("Probability map to choose from has no mass!!");
        //choose a random entry within the map based on a randomly chosen number
        double diceRoll = Math.random()*cumulatedProbability;
        for(Map.Entry<Double,Integer> currentEntry : indexByCumulatedProbability.entrySet()){
            if(diceRoll < currentEntry.getKey()) return currentEntry.getValue();
        }
        throw new IllegalStateException("No entry found in "+indexByCumulatedProbability+" that is smaller than "+diceRoll+"!!\nThis shouldn't happen and is most certainly a bug!!");
    }

    /**
     * Method to check whether an edge between two nodes already exists in a given set.
     *
     * @param edgeSet The edges to check whether such an edge is contained already
     * @param sourceNode The source of the edge to check
     * @param targetNode The target of the edge to check
     * @return true if there is an edge from sourceNode to targetNode in the edge set, false otherwise
     */
    public static boolean existsEdge(Set<SNEdge> edgeSet, SNNode sourceNode, SNNode targetNode) {
        for(SNEdge currentEdge : edgeSet){
            if((currentEdge.getSource() == sourceNode) && (currentEdge.getTarget() == targetNode)) return true;
        }
        return false;
    }
}
