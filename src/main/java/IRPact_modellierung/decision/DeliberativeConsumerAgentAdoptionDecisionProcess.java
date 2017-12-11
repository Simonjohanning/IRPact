package IRPact_modellierung.decision;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.helper.ConsumerAgentAttributeHelper;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.products.*;
import IRPact_modellierung.simulation.SimulationContainer;
import antlr.SemanticException;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//TODO check out what happens with a tie
//TODO reference to Schwarz dissertation
/**
 * Decision process that models consumer agent adoption based on 'Deliberative Decision' after Schwarz.
 * In this process weighted utilities based on subjective norm, behaviour control and attitude are added to evaluate products.
 * For subjective norm, the adoption pattern of neighbours in the social network of an agent is used,
 * for attitude the preferences of the agent are used
 * and for behaviour control the importance of the products are accounted.
 * The agent will adopt the product with the highest weighted utility
 *
 * @author Simon Johanning
 */
public class DeliberativeConsumerAgentAdoptionDecisionProcess extends UtilitarianConsumerAgentAdoptionDecisionProcess {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");
    private Map<ConsumerAgentGroup, Map<ProductGroup, Double>> importanceAttitudeMap;
    private Map<String, Map<String, Double>> importanceAttitudeProtoMap;

    public DeliberativeConsumerAgentAdoptionDecisionProcess(UtilityFunction associatedUtilityFunction, Map<String, Map<String, Double>> importanceAttitudeProtoMap) {
        super(associatedUtilityFunction);
        this.importanceAttitudeProtoMap = importanceAttitudeProtoMap;
    }

    /**
     * Agent will make a decision to adopt one of the products entering this decision process based on the process of Deliberative Decision if more than one product is available.
     * The product with the highest utility will be adopted.
     *
     * @param simulationContainer The container the simulation runs in
     * @param consumerAgent The consumer agent making the decision
     * @param potentialProducts The products eligible for the adoption of this agent
     * @param systemTime The current time of the simulation
     * @throws IllegalArgumentException Will be thrown when no potential products are given
     */
    public Product makeProductAdoptionDecision(SimulationContainer simulationContainer, ConsumerAgent consumerAgent, Set<Product> potentialProducts, double systemTime) throws IllegalArgumentException {
        if(importanceAttitudeMap == null) setupImportanceAttitudeMap(simulationContainer);
        if(potentialProducts.size() > 0){
            Product maxUtilityProduct = (Product) potentialProducts.toArray()[0];
            double maxUtility = -Double.MAX_VALUE;
            //find the product with the hightest utility
            for(Product potentialProduct : potentialProducts){
                double currentProductUtility = calculateUtility(simulationContainer, potentialProduct, consumerAgent, systemTime);
                if(currentProductUtility > maxUtility){
                    maxUtility = currentProductUtility;
                    maxUtilityProduct = potentialProduct;
                }
            }
            //adopt product with highest utility
            return maxUtilityProduct;
        }
        else throw new IllegalArgumentException("No potential product was chosen for the adoption");
    }

    /**
     * Method to see if any of the potential products have a higher utility for the consumerAgent at systemTime than the productToCompare,
     * based on DeliberativeDecision utilitarian approach
     *
     * @param simulationContainer The container the simulation runs in
     * @param consumerAgent The consumer agent making the decision
     * @param productToCompare The product of interest (that is compared to the other products)
     * @param potentialProducts The products the productToCompare is compared to
     * @param systemTime The current time of the simulation
     *
     * @return true if there is a better product in potentialProducts than productToCompare, false if not
     */
    public boolean betterProductAvailable(SimulationContainer simulationContainer, ConsumerAgent consumerAgent, Product productToCompare, Set<Product> potentialProducts, double systemTime) {
        if(importanceAttitudeMap == null) setupImportanceAttitudeMap(simulationContainer);
        double compareProductUtility = calculateUtility(simulationContainer, productToCompare, consumerAgent, systemTime);
        //check for all potential products if they have a higher utility
        for(Product currentProduct : potentialProducts){
            fooLog.info("compareProductUtility is {}, where as current product utility is {}", compareProductUtility, calculateUtility(simulationContainer, currentProduct, consumerAgent, systemTime));
            //if there is a product with a higher utility than the utility of productToCompare, return true;
            if(calculateUtility(simulationContainer, currentProduct, consumerAgent, systemTime) > compareProductUtility){
                fooLog.info("Better product (than {}) found: {}", productToCompare, currentProduct);
                return true;
            }
        }
        //if no better product was found, return false
        return false;
    }

    /**
     * Method to calculate the utility of the given product for the consumerAgent at systemTime using the DeliberativeDecision utility function.
     * The total utility is a weighted sum of partial utilities representing
     * subjective norm, behaviour control and attitude of the agent.
     * The weighting is done by the subjective norm (fraction of adopters in the social network of the agent)
     * and its complement in the unit interval of the weighted sum of behaviour control and attitude.
     * The latter weighted sum is weighted by the importanceAttitude for the agent (and its complement for behaviourControl),
     * where importance attitude is based on the agents preferences and product attributes,
     * whereas behaviour control is based on the product attributes importance and product attributes.
     * For more details see userDocumentation or Schwarz
     *
     * @param simulationContainer The container the simulation is contained in
     * @param product The product whose utility is to be calculated
     * @param consumerAgent The consumer agent for which the utility is calculated
     * @param systemTime The time at which the utility is calculated
     *
     * @return A double value representing the utility of product for consumerAgent at systemTime
     */
    protected double calculateUtility(SimulationContainer simulationContainer, Product product, ConsumerAgent consumerAgent, double systemTime) throws IllegalArgumentException{
        if(product == null) throw new IllegalArgumentException("Error!! Trying to calculate the utility of a non-existing product!!");
        //the weighting of subjective norm
        double importanceSubjectiveNorm = ConsumerAgentAttributeHelper.loadSubjectiveNorm(consumerAgent);
        //the weighting of behaviour control
        double importanceBehaviourControl = (1-importanceAttitudeMap.get(consumerAgent.getCorrespondingConsumerAgentGroup()).get(product.getPartOfProductGroup()));
        //the weighting of attitude
        double importanceAttitude = importanceAttitudeMap.get(consumerAgent.getCorrespondingConsumerAgentGroup()).get(product.getPartOfProductGroup());
        //TODO do proper
        double fractionAdopters = 0;
        try {
            fractionAdopters = LazynessHelper.calculateFractionAdoptersInSN(consumerAgent, product, simulationContainer);
        } catch (SemanticException e) {
            e.printStackTrace();
        }
        //partial utility of subjective norm is the sum of adopters in the social network of the agent (same as n*average value)
        double partialUtilitySubjectiveNorm = simulationContainer.getSocialNetwork().getSocialGraph().getNeighbours(consumerAgent.getCorrespondingNodeInSN()).size() * fractionAdopters;
        double partialUtilityBehaviourControl = 0.0;
        double partialUtilityAttitude = 0.0;
        //Preference should already be situated within the utility function
        /*Map<ProductGroupAttribute, Set<ProductGroupAttributeValueMapping>> pgaPgavMapping = StructureEnricher.attachProductGroupAttributesToProductGroupAttributeValueMappings(simulationContainer.getSimulationConfiguration().getPreferenceConfiguration().getProductGroupAttributePreferenceMapping());
        Map<Value, Double> consumerAgentPreference = LazynessHelper.preferenceToValueQuantityMap(consumerAgent.getPreferences());*/
        //Sum utilities for each product attribute
        //TODO add product importance to decision scheme and take it from there
        /*for(ProductAttribute pa : product.getProductAttributes()){
            partialUtilityBehaviourControl += pa.getImportance()*consumerAgent.getPerceivedProductAttributeValues().get(pa).calculateProductAttributePerception(systemTime);
            //TODO ensure that the utility function is preference oriented
            partialUtilityAttitude += associatedUtilityFunction.calculatePartialUtility(consumerAgent, pa, systemTime);
        }*/
        //partial utility of factors not being subjective norm is weighted by the importance of attitude and behaviour control
        double objectivePartialUtility = partialUtilityAttitude * importanceAttitude + partialUtilityBehaviourControl * importanceBehaviourControl;
        //total utility is the weighted sum of subjective and objective partial utility, weighted witht the importance of subjective norm for that agent
        return (objectivePartialUtility * (1-importanceSubjectiveNorm) + partialUtilitySubjectiveNorm * importanceSubjectiveNorm);
    }

    /**
     * Helper method to set up the importance attitude map from the configured proto map
     *
     * @param simulationContainer The container the simulation runs in
     * @throws IllegalStateException Will be thrown when the importanceAttitudeProtoMap lacks relevant entries
     */
    private void setupImportanceAttitudeMap(SimulationContainer simulationContainer) throws IllegalStateException{
        for(ConsumerAgentGroup cag : simulationContainer.getSimulationConfiguration().getAgentConfiguration().getConsumerAgentGroups()){
            if(!importanceAttitudeProtoMap.containsKey(cag.getGroupName())) throw new IllegalStateException("No importanceAttitude was configured for ConsumerAgentGroup "+cag.getGroupName()+" in the configuration of the Deliberate Decision process!!");
            Map<String, Double> importanceProtoMap = importanceAttitudeProtoMap.get(cag.getGroupName());
            Map<ProductGroup, Double> importanceMap = new HashMap<>();
            for(ProductGroup pg : simulationContainer.getSimulationConfiguration().getProductConfiguration().getProductGroups()){
                if(!importanceProtoMap.containsKey(pg.getGroupName())) throw new IllegalStateException("No importanceAttitude was configured for ProductGroup "+pg.getGroupName()+" for ConsumerAgentGroup "+cag.getGroupName()+" in the configuration of the Deliberate Decision process!!");
                importanceMap.put(pg,importanceProtoMap.get(pg.getGroupName()));
            }
            importanceAttitudeMap.put(cag, importanceMap);
        }
    }

}