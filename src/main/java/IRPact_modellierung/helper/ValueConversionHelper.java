package IRPact_modellierung.helper;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.agents.consumerAgents.SynchronousConsumerAgent;
import IRPact_modellierung.preference.Value;
import IRPact_modellierung.needs.Need;
import IRPact_modellierung.products.*;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.geom.Point2D;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Class to provide helper methods to convert values of one data type into another.
 *
 * @author Simon Johanning
 */
public class ValueConversionHelper {

    /**
     * Method to convert a numerical representation of a boolean value into a boolean value.
     * Converts a double to false if it is 0.0 or smaller and true otherwise
     *
     * @param doubleToConvert The (double representation) value to convert to boolean
     * @return true if numerical value > 0.0, false otherwise
     */
    public static boolean signToBoolean(double doubleToConvert){
        return !(doubleToConvert <= 0.0);
    }

    /**
     * Method to convert a set of Double values to an array list.
     * Implies no order, will just arrange them in an order (in which the set is processed).
     *
     * @param setToConvert Set of numbers to convert to a list
     * @return (unsorted) ArrayList of the values within the set
     * @throws IllegalArgumentException Will be thrown when the set is empty
     */
    public static ArrayList<Double> SetToArrayList(Set<Double> setToConvert) throws IllegalArgumentException{
        if(setToConvert.isEmpty()) throw new IllegalArgumentException("Set to convert is empty!!");
        ArrayList<Double> returnList = new ArrayList<Double>(setToConvert.size());
        returnList.addAll(setToConvert);
        return returnList;
    }

    /**
     * Method to convert a 2-tupel ArrayList representation of a point (with Double coordinates)
     * into a Point2D(.Double) format
     *
     * @param twoTuple The two tuple (as an ArrayList of Double values) to convert to a point, with first coordinate as x-Coordinate, and second coordinate as y-Coordinate
     * @return A Point2D.Double representation of the coordenates of the provided array list
     * @throws IllegalArgumentException Will be thrown when the dimension of the parameter is not 2 (not a 2-tuple)
     */
    public static Point2D arrayList2DtoPoint2D(ArrayList<Double> twoTuple) throws IllegalArgumentException{
        if(twoTuple.size() != 2) throw new IllegalArgumentException("Tuple to convert is not a two-tuple, but instead a "+twoTuple.size()+"-tuple!!\n Thus this method is unsuited to deal with it!!");
        else return (new Point2D.Double(twoTuple.get(0), twoTuple.get(1)));
    }

    /**
     * Method to convert an ArrayList of HashMaps (of String-Object pairs) into an Array of HashMaps (of String-Object pairs)
     *
     * @param arrayListToConvert The ArrayList of hash maps that is to be converted to an array of hash maps
     * @return An array consisting of the entries of the provided ArrayList
     * @throws IllegalArgumentException Will be thrown when the array list to convert is empty
     */
    public static HashMap<String, Object>[] arrayListHashMapToArrayHashMap(ArrayList<HashMap<String, Object>> arrayListToConvert) throws IllegalArgumentException{
        if(arrayListToConvert.isEmpty()) throw new IllegalArgumentException("ArrayList to convert to an array is empty!!");
        HashMap<String, Object>[] returnArray = new HashMap[arrayListToConvert.size()];
        //TODO make nicer and more efficient
        for(int index=0;index<arrayListToConvert.size();index++){
            returnArray[index] = arrayListToConvert.get(index);
        }
        return returnArray;
    }

    /**
     * Method to select all needs from a String-Need map as from a set of Strings.
     * Will look up each corresponding Need (for each String) and put them into a result set
     * (provided every String is accounted for; if not an Exception will be thrown)
     *
     * @param needsSatisfied String representation of the needs satisfied whose corresponding needs are to be selected
     * @param needsMap The needs map, mapping the provided strings with their desired needs
     * @return The set of Needs corresponding to the set of Strings provided, based on the String-Need map
     * @throws IllegalArgumentException Will be thrown if the set of Strings is empty or one of them is not a key of the needsMap
     */
    public static Set<Need> getCorrespondingNeeds(Set<String> needsSatisfied, Map<String, Need> needsMap) throws IllegalArgumentException{
        if(needsSatisfied.isEmpty()) throw new IllegalArgumentException("Set of (String representation of) needs to derive the corresponding needs from is empty!!");
        Set<Need> correspondingNeeds = new HashSet<Need>(needsSatisfied.size());
        for(String need : needsSatisfied){
            if(!needsMap.containsKey(need)) throw new IllegalArgumentException("Need "+need+" does not correspond to a key in the provided need map!!");
            else correspondingNeeds.add(needsMap.get(need));
        }
        return correspondingNeeds;
    }

    /**
     * Method to copy the strings contained in an Array list into a set.
     * Will throw an exception if the array list is empty or an entry appear twice
     *
     * @param stringsToConvert The (Arraylist of) strings to convert into a set
     * @return A set consisting of the strings contained in the provided array list
     * @throws IllegalArgumentException Will be thrown if the array list is empty or contains duplicate entries
     */
    public static Set<String> arrayListToSet(ArrayList<String> stringsToConvert) throws IllegalArgumentException{
        if(stringsToConvert.isEmpty()) throw new IllegalArgumentException("ArrayList to convert is empty!!");
        Set<String> returnSet = new HashSet<String>(stringsToConvert.size());
        for (String aStringsToConvert : stringsToConvert) {
            if (returnSet.contains(aStringsToConvert))
                throw new IllegalArgumentException("String " + aStringsToConvert + " is a duplicate entry within the array list to convert!!");
            else returnSet.add(aStringsToConvert);
        }
        return returnSet;
    }

    /**
     * Method to convert a set of SynchronousConsumerAgents to a set of ConsumerAgents
     *
     * @param agentsInCAG SynchronousConsumerAgents to be converted
     * @return A set of the same SynchronousConsumerAgent as ConsumerAgents
     * @throws IllegalArgumentException will be thrown if the set to convert is empty
     */
    public static Set<ConsumerAgent> synchronousConsumerAgentsToConsumerAgents(Set<SynchronousConsumerAgent> agentsInCAG) throws IllegalArgumentException{
        if(agentsInCAG.isEmpty()) throw new IllegalArgumentException("Set of SynchronousConsumerAgents to be converted is empty!!");
        Set<ConsumerAgent> returnSet = new HashSet<ConsumerAgent>();
        returnSet.addAll(agentsInCAG);
        return returnSet;
    }

    /**
     * Method to convert a Collection of productGroups to a set of these
     *
     * @param productGroups The Collection to convert to a set
     * @return The product groups to be converted as a set
     * @throws IllegalArgumentException Will be thrown when collection is empty
     */
    public static Set<ProductGroup> ProductGroupCollectionToSet(Collection<ProductGroup> productGroups) throws IllegalArgumentException{
        if(productGroups.isEmpty()) throw new IllegalArgumentException("Collection to be converted is empty!!");
        Set<ProductGroup> productGroupSet = new HashSet<ProductGroup>();
        for (ProductGroup productGroup : productGroups) {
            productGroupSet.add(productGroup);
        }
        return productGroupSet;
    }

    /**
     * Method to convert a map of Integer keys and sets of FixedProductDescription to a map
     * of the same integer keys with set of products corresponding to the instances of the FixedProductDescriptions as values
     *
     * @param initiallyAdoptedFPDs The map which values are to be converted to sets
     * @param simulationContainer The container the simulation runs in
     * @return A map of the same keys as the initiallyAdoptedFPDs with their values converted to sets of instances of the corresponding products
     * @throws IllegalArgumentException Will be throw if the map is empty, the container is null, or a FixedProductDescription doesn't not have a corresponding product in the simulation
     */
    public static Map<Integer, Set<Product>> IntegerFPDSetMapToIntegerProductSetMap(Map<Integer, Set<FixedProductDescription>> initiallyAdoptedFPDs, SimulationContainer simulationContainer) throws IllegalArgumentException{
        if(simulationContainer == null) throw new IllegalArgumentException("Simulation container is null; A valid container is needed!!");
        else if(initiallyAdoptedFPDs.isEmpty()) throw new IllegalArgumentException("Map to convert is empty; Need to provide a valid map to be converted!!");
        else{
            Map<Integer, Set<Product>> returnSet = new HashMap<Integer, Set<Product>>(initiallyAdoptedFPDs.size());
            for(Integer currentIndex : initiallyAdoptedFPDs.keySet()){
                returnSet.put(currentIndex, new HashSet<Product>());
                for(FixedProductDescription currentFPD : initiallyAdoptedFPDs.get(currentIndex)){
                    if(!simulationContainer.getFixedProductMap().containsKey(currentFPD)) throw new IllegalArgumentException("FixedProductDescription "+currentFPD+" does not have a corresponding product within this simulation container!!");
                    returnSet.get(currentIndex).add(simulationContainer.getFixedProductMap().get(currentFPD));
                }
            }
            return returnSet;
        }
    }

    //TODO check if it really preserves the order!
    /**
     * Method to convert an ordered LinkedHashMap of Value-Double pairs to an array list of the same order
     *
     * @param orderedFactorMap The (ordered) map to be converted
     * @return An array list with the same order as the provided argument hash map
     * @throws IllegalArgumentException Gets thrown when the argument is an empty map
     */
    public static ArrayList<Value> linkedHashMapToArrayList(LinkedHashMap<Value, Double> orderedFactorMap) throws IllegalArgumentException{
        if(orderedFactorMap.isEmpty()) throw new IllegalArgumentException("orderedFactorMap provided is empty!!");
        ArrayList<Value> returnList = new ArrayList<Value>();
        for(Value currentValue : orderedFactorMap.keySet()){
            returnList.add(currentValue);
        }
        return returnList;
    }

    /**
     * Method to transform a String-Integer map to a ConsumerAgentGroup-Integer map
     * with the ConsumerAgentGroup corresponding to the keys of the String-Integer map.
     * If a String doesn't refer to the name of a CAG given, it will throw an Exception
     *
     * @param consumerAgentGroups The potential keys for the CAG-Integer map
     * @param consumerGroupIntegerMapping The String-Integer map with the keys referring to existing CAGs
     * @param context A String describing the context the method was called from (for a more verbose Exception)
     * @return A CAG-Integer map with the keys corresponding to the referred CAGs and the values being the respective values of the String-Integer map
     * @throws IllegalArgumentException Will be thrown when a key of the String-Integer map doesn't refer to an agent group given as argument
     */
    public static Map<ConsumerAgentGroup, Integer> deriveCAGIntegerMap(Set<ConsumerAgentGroup> consumerAgentGroups, HashMap<String, Integer> consumerGroupIntegerMapping, String context) throws IllegalArgumentException{
        Map<ConsumerAgentGroup, Integer> returnMap = new HashMap<>(consumerGroupIntegerMapping.keySet().size());
        Map<String, ConsumerAgentGroup> cagMap = StructureEnricher.attachConsumerAgentGroupNames(consumerAgentGroups);
        for(String cagName : consumerGroupIntegerMapping.keySet()){
            if(!cagMap.keySet().contains(cagName)) throw new IllegalArgumentException("ConsumerAgentGroup "+cagName+" mentioned in "+context+" does not exist in the simulation!");
            returnMap.put(cagMap.get(cagName), consumerGroupIntegerMapping.get(cagName));
        }
        return returnMap;
    }

    /**
     * Helper method to cast an ArrayList of doubles to an array of double primitives
     * (via the primitive type).
     *
     * @param meansList The ArrayList to cast to the array
     * @return A double array with the entries of the array list
     */
    public static double[] arrayListToArray(ArrayList<Double> meansList) {
        Double[] meansPrimitiveTypeArray = new Double[meansList.size()];
        meansList.toArray(meansPrimitiveTypeArray);
        return ArrayUtils.toPrimitive(meansPrimitiveTypeArray);
    }

    /**
     * Helper method to cast an ArrayList of ArrayLists of doubles to a (2-dim) array of double primitives
     * (via the primitive type, and the one-dimensional version of this method).
     *
     * @param meansList The ArrayList to cast to the array
     * @return A double array with the entries of the array list
     */
    public static double[][] arrayListToArray2D(ArrayList<ArrayList<Double>> meansList) {
        double[][] returnArray = new double[meansList.size()][];
        Iterator<ArrayList<Double>> outerListIterator = meansList.iterator();
        int currentIndex = 0;
        while(outerListIterator.hasNext()){
            returnArray[currentIndex] = arrayListToArray(outerListIterator.next());
//            System.out.println(Arrays.toString(returnArray[currentIndex]));
            currentIndex++;
        }
//        System.out.println(Arrays.toString(returnArray));
        return returnArray;
    }

    /**
     * Method to extract an ArrayList of Doubles within an ArrayList and put them together
     *
     * @param outerList An array list containing Objects that can be cast to Double lists themselves
     * @return An Array List containing the ArrayLists (of Doubles) contained in the Objects of the argument Array List
     */
    public static ArrayList<ArrayList<Double>> extractInnerDoubleList(ArrayList<Object> outerList) {
        ArrayList<ArrayList<Double>> returnList = new ArrayList<>();
        Iterator<Object> listIterator = outerList.listIterator();
        while(listIterator.hasNext()){
            returnList.add((ArrayList<Double>) listIterator.next());
        }
        return returnList;
    }

    /*public static Product marketIntroductionEventToProduct(MarketIntroductionEvent event, SimulationContainer simulationContainer){
        FixedProductDescription correspondingFixedProductDescription = event.getProductConcerned();
        Set<ProductAttribute> productAttributes = new HashSet<ProductAttribute>();
        for(ProductGroupAttribute currentProductAttribute : correspondingFixedProductDescription.getProductAttributes().keySet()){
            productAttributes.add(new ProductAttribute(correspondingFixedProductDescription.getProductAttributes().get(currentProductAttribute), currentProductAttribute));
        }
        return new Product(simulationContainer, productAttributes, true, correspondingFixedProductDescription.getPartOfProductGroup(), correspondingFixedProductDescription.getName());
    }*/
}
