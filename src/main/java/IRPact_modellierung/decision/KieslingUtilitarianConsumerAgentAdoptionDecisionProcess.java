package IRPact_modellierung.decision;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.helper.FilterHelper;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

//TODO reference to Kiesling dissertation
/**
 * Decision process that models consumer agent product adoption as described in Kiesling.
 * In this process the decision based primarily on the utility function associated with the decision process.
 * To stay true to the modeled described in Kiesling, the utility function should be preference oriented;
 * However, the modeler can choose another utility function to adopt the model in Kiesling.
 * In addition to the sum of the partial utilities of the product attributes,
 * a random deviation of the product utility (within the range of [-rangeEpsilon,rangeEpsilon])
 * is added to represent uncertainty.
 * The consumer agent will adopt the product with the highest total utility.
 *
 * @author Simon Johanning
 */
public class KieslingUtilitarianConsumerAgentAdoptionDecisionProcess extends UtilitarianConsumerAgentAdoptionDecisionProcess {

    private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    private double rangeEpsilon;

    /**
     * Constructor for the utilitarian decision process based on Kiesling
     *
     * @param rangeEpsilon the (maximal) magnitude of the random deviation of calculated product utility
     * @param utilityFunctionUsed the utility function used to calculate the utility of a product for the consumer agent
     */
    public KieslingUtilitarianConsumerAgentAdoptionDecisionProcess(double rangeEpsilon, UtilityFunction utilityFunctionUsed ) {
        super(utilityFunctionUsed);
        this.rangeEpsilon = rangeEpsilon;
    }

    public double getRangeEpsilon() {
        return rangeEpsilon;
    }

    /**
     * Method to determine whether a product is at least as good as all products within a given set.
     * Will indicate whether a better product is situated in the given set
     *
     * @param simulationContainer The container in which the model runs
     * @param consumerAgent The agent who is to evaluate the given products
     * @param productToCompare The product for which a better product shall be found
     * @param potentialProducts The set of product which which productToCompare is compared with
     * @param systemTime The current time of the simulation
     *
     * @return true if potentialProducts contains a product with a higher utility at systemTime for consumerAgent than productToCompare
     */
    public boolean betterProductAvailable(SimulationContainer simulationContainer, ConsumerAgent consumerAgent, Product productToCompare, Set<Product> potentialProducts, double systemTime) {
        //Calculate the utility of the product to be compared to the utility of potentialProducts
        double compareProductUtility = calculateUtility(simulationContainer, productToCompare, consumerAgent, systemTime);
        //See if a better product is found
        for(Product currentProduct : potentialProducts){
            fooLog.debug("compareProductUtility is {}, where as current product utility is {}", compareProductUtility, calculateUtility(simulationContainer, currentProduct, consumerAgent, systemTime));
            //if the current product to be compared has a higher utility, return true (better product is available)
            if(calculateUtility(simulationContainer, currentProduct, consumerAgent, systemTime) > compareProductUtility){
                fooLog.debug("Better product (than {}) found: {}", productToCompare, currentProduct);
                return true;
            }
        }
        return false;
    }

    /**
     * Decision method that decides which product has the highest (randomly pertubated) utility and makes the consumerAgent adopt it
     *
     * @param simulationContainer The container the model runs in
     * @param consumerAgent The agent for with whose preferences and perceptions the utility is calculated and which adopts
     * @param potentialProducts The products entering the decision process
     * @param systemTime The current time of the simulation
     * @throws IllegalArgumentException Will be thrown when no potential products are given
     */
    public Product makeProductAdoptionDecision(SimulationContainer simulationContainer, ConsumerAgent consumerAgent, Set<Product> potentialProducts, double systemTime) {
        //if no product is available, none will be adopted
        if(potentialProducts.size() > 0){
            //variable to hold which product has the highest utility
            Product maxUtilityProduct = null;
            //maximal utility of all products compared yet.
            double maxUtility = 0.0;
            //iterate over all products to see if they have a higher utility than the best product yet found
            for(Product potentialProduct : potentialProducts){
                //calculate utility for the current product
                double currentProductUtility = calculateUtility(simulationContainer, potentialProduct, consumerAgent, systemTime);
                fooLog.debug("Calculated a utility of {} for product {} for consumer agent {}\n", currentProductUtility, potentialProduct.getName(), consumerAgent.getAgentID());
                //update auxiliary variables
                if(currentProductUtility > maxUtility){
                    maxUtility = currentProductUtility;
                    maxUtilityProduct = potentialProduct;
                }
            }
            //adopt product with the highest utility
            return maxUtilityProduct;
        }
        else throw new IllegalArgumentException("No potential product was chosen for the adoption");
    }

    /**
     * Method to calculate the normalized utility of the given product for consumerAgent at systemTime
     * by summing over the partial utilities of each product attribute and distorting them by
     * a (uniformly) random value within [-rangeEpsilon, rangeEpsilon]
     *
     * @param simulationContainer The container the model runs in
     * @param consumerAgent The agent for with whose preferences and perceptions the utility is calculated and which adopts
     * @param product The product for which the utility is calculated
     * @param systemTime The current time of the simulation

     * @return returns the normalized (within [0,1]) utility of the product
     */
    protected double calculateUtility(SimulationContainer simulationContainer, Product product, ConsumerAgent consumerAgent, double systemTime) {
        //calculate the maximal (maximally pertubated) utility for all products for this agent at this time (as upper bound for the utility of product)
        double maxUtility = calculateMaxUtility(simulationContainer,consumerAgent, systemTime);
        //initialize partial utility sum
        double utility = 0.0;
        //sum over all partial utilities
        for(ProductAttribute productAttribute : product.getProductAttributes()){
            //calculate partial utility using the associated utility function
            utility += associatedUtilityFunction.calculatePartialUtility(consumerAgent, productAttribute, systemTime);
            //debug stuff
            fooLog.debug("contribution of pa {} to the utility of product {} is {}", productAttribute.getCorrespondingProductGroupAttribute().getName(), product.getName(), associatedUtilityFunction.calculatePartialUtility(consumerAgent, productAttribute, systemTime));
            if(product.getName().contains("takeOver")) fooLog.debug("perceived productAttribute {} for {} is {}", productAttribute, product.getName(), consumerAgent.getPerceivedProductAttributeValues().get(productAttribute).calculateProductAttributePerception(systemTime));
        }
        //pertubate the utility by a value within [-rangeEpsilon, rangeEpsilon]
        utility += (Math.random() * 2.0 - 1.0) * rangeEpsilon;
        fooLog.debug("maxUtility in calcultateUtility is {}",maxUtility);
        //normalize utility
        double normalizedUtility = utility / maxUtility;
        return Math.max(0, normalizedUtility);
    }

    /**
     * Method to calculate the maximal (pertubated) utility a product can obtain for consumerAgent at systemTime
     *
     * @param simulationContainer The container the model runs in
     * @param consumerAgent The agent for with whose preferences and perceptions the utility is calculated and which adopts
     * @param systemTime The current time of the simulation

     * @return maximal (pertubated) utility a product can obtain for consumerAgent at systemTime
     */
    protected double calculateMaxUtility(SimulationContainer simulationContainer, ConsumerAgent consumerAgent, double systemTime) {
        double maxUtility = 0.0;
        for (Product potentialProduct : FilterHelper.selectKnownProducts(consumerAgent.getProductAwarenessMap())) {
            double productUtility = 0.0;
            //calculate utility of the product
            for (ProductAttribute productAttribute : potentialProduct.getProductAttributes()) {
                productUtility += associatedUtilityFunction.calculatePartialUtility(consumerAgent, productAttribute, systemTime);
            }
            //pertubate perception as much as possible
            productUtility += rangeEpsilon;
            fooLog.debug("In calculate max utility, productUtility of prod {} is {}, max utility is {}", potentialProduct.getName(), productUtility, maxUtility);
            if (productUtility > maxUtility) maxUtility = productUtility;
        }
        return maxUtility;
    }


}