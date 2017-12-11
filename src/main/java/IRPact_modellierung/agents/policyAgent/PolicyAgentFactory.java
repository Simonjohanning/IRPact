package IRPact_modellierung.agents.policyAgent;

import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;

/**
 * The PolicyAgentFactory serves to add the Policy agent to the simulation.
 * It serves to it to the simulation container, and manages whether an agent has already been instatiated,
 * in which case it will throw an error.
 *
 * @author Simon Johanning
 **/
public class PolicyAgentFactory {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    private static PolicyAgent instantiatedAgent = null;

    /**
     * Method to create the agent and add it to the simulation.
     * If the agent already exists, it will throw an error.
     *
     * @param simulationContainer The container the simulation runs in
     * @param informationAuthority The informationAuthority of the PolicyAgent
     * @param regulatoryPolicyScheme The RegulatoryPolicyScheme the Policy agent uses
     * @param productPolicyScheme The ProductPolicyScheme used by the Policy agent
     * @param marketEvaluationScheme The MarketEvaluationScheme used by the Policy agent
     * @param consumerPolicyScheme The ConsumerPolicyScheme used by the Policy agent
     * @return The agent created by the method
     * @throws IllegalStateException Will be thrown when a policy agent has already been instatiated
     */
    public static PolicyAgent createPolicyAgent(SimulationContainer simulationContainer, double informationAuthority, RegulatoryPolicyScheme regulatoryPolicyScheme, ProductPolicyScheme productPolicyScheme, MarketEvaluationScheme marketEvaluationScheme, ConsumerPolicyScheme consumerPolicyScheme) throws IllegalStateException{
        if(instantiatedAgent != null) throw new IllegalStateException("A policy agent has already been instiated! Please use the one that already exists!");
        else{
            PolicyAgent newAgent = new PolicyAgent(simulationContainer, informationAuthority, productPolicyScheme, consumerPolicyScheme, regulatoryPolicyScheme, marketEvaluationScheme);
            simulationContainer.setPolicyAgent(newAgent);
            return newAgent;
        }
    }
    /**
     * The ProductPolicySchemeLoader creates an instance of the ProductPolicyScheme
     * qualified by the productPolicySchemeQualifier, if the corresponding scheme was implemented.
     * If not it will raise the respective IllegalArgumentException.
     *
     * @param productPolicySchemeQualifier A String corresponding to the ProductManipulationScheme to be loaded
     * @return An instance of the ProductManipulationScheme specified by the productPolicySchemeQualifier
     * @throws IllegalArgumentException will be thrown when the productPolicySchemeQualifier refers to an unimplemented scheme
     */
    public static ProductPolicyScheme productPolicySchemeLoader(String productPolicySchemeQualifier) throws IllegalArgumentException{
        switch(productPolicySchemeQualifier) {
            case "DefaultProductPolicyScheme":
                return new DefaultProductPolicyScheme();
            default:
                throw new IllegalArgumentException("ProductManipulationScheme " + productPolicySchemeQualifier + " is not implemented!!\nMake sure to specify an implemented scheme or implement is yourself!");
        }
    }

    /**
     * The marketEvaluationSchemeLoader creates an instance of the MarketEvaluationScheme
     * qualified by the marketEvaluationSchemeQualifier, if the corresponding scheme was implemented.
     * If not it will raise the respective IllegalArgumentException.
     *evaluateMarket
     * @param marketEvaluationSchemeQualifier A String corresponding to the MarketEvaluationScheme to be loaded
     * @return An instance of the MarketEvaluationScheme specified by the marketEvaluationSchemeQualifier
     * @throws IllegalArgumentException will be thrown when the marketEvaluationSchemeQualifier refers to an unimplemented scheme
     */
    public static MarketEvaluationScheme marketEvaluationSchemeLoader(String marketEvaluationSchemeQualifier) throws IllegalArgumentException{
        switch(marketEvaluationSchemeQualifier) {
            case "DefaultMarketEvaluationScheme":
                return new DefaultMarketEvaluationScheme();
            default:
                throw new IllegalArgumentException("MarketEvaluationScheme " + marketEvaluationSchemeQualifier + " is not implemented!!\nMake sure to specify an implemented scheme or implement is yourself!");
        }
    }

    /**
     * The consumerPolicySchemeLoader creates an instance of the ConsumerPolicyScheme
     * qualified by the consumerPolicySchemeQualifier, if the corresponding scheme was implemented.
     * If not it will raise the respective IllegalArgumentException.
     *
     * @param consumerPolicySchemeQualifier A String corresponding to the ConsumerPolicyScheme to be loaded
     * @return An instance of the ConsumerPolicyScheme specified by the consumerPolicySchemeQualifier
     * @throws IllegalArgumentException will be thrown when the consumerPolicySchemeQualifier refers to an unimplemented scheme
     */
    public static ConsumerPolicyScheme consumerPolicySchemeLoader(String consumerPolicySchemeQualifier) throws IllegalArgumentException{
        switch(consumerPolicySchemeQualifier) {
            case "DefaultConsumerPolicyScheme":
                return new DefaultConsumerPolicyScheme();
            default:
                throw new IllegalArgumentException("ConsumerPolicyScheme " + consumerPolicySchemeQualifier + " is not implemented!!\nMake sure to specify an implemented scheme or implement is yourself!");
        }
    }

    /**
     * The regulatoryPolicySchemeLoader creates an instance of the RegulatoryPolicyScheme
     * qualified by the regulatoryPolicySchemeQualifier, if the corresponding scheme was implemented.
     * If not it will raise the respective IllegalArgumentException.
     *
     * @param regulatoryPolicySchemeQualifier A String corresponding to the RegulatoryPolicyScheme to be loaded
     * @return An instance of the RegulatoryPolicyScheme specified by the regulatoryPolicySchemeQualifier
     * @throws IllegalArgumentException will be thrown when the regulatoryPolicySchemeQualifier refers to an unimplemented scheme
     */
    public static RegulatoryPolicyScheme regulatoryPolicySchemeLoader(String regulatoryPolicySchemeQualifier) throws IllegalArgumentException{
        switch(regulatoryPolicySchemeQualifier) {
            case "DefaultRegulatoryPolicyScheme":
                return new DefaultRegulatoryPolicyScheme();
            default:
                throw new IllegalArgumentException("RegulatoryPolicyScheme " + regulatoryPolicySchemeQualifier + " is not implemented!!\nMake sure to specify an implemented scheme or implement is yourself!");
        }
    }

}
