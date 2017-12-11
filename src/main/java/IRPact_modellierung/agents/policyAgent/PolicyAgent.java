package IRPact_modellierung.agents.policyAgent;

import IRPact_modellierung.agents.Agent;
import IRPact_modellierung.agents.InformationAgent;
import IRPact_modellierung.simulation.SimulationContainer;

/**
 * A policy agent represents the policy sphere of the simulation.
 * Although the policy sphere consists of many stakeholders and complex decision making processes,
 * within the simulation (from an external perspective) these processes are all joined together by a single agent.
 * That agents behavior is formalized through the schemes associated with the agent,
 * and the modeler should carefully select the appropriate schemes in order to get the desired behavior.
 *
 * @author Simon Johanning
 */
public class PolicyAgent extends InformationAgent{

    ProductPolicyScheme productPolicyScheme;
    ConsumerPolicyScheme consumerPolicyScheme;
    RegulatoryPolicyScheme regulatoryPolicyScheme;
    MarketEvaluationScheme marketEvaluationScheme;

    /**
     * A PolicyAgent represents the policy sphere of the simulation.
     * As an agent it is situated in a simulation container and equipped with an informationAuthority.
     * It acts through the productPolicyScheme, the consumerPolicyScheme, the regulatoryPolicyScheme and the marketEvaluationScheme.
     *
     * @param associatedSimulationContainer The container the simulation runs in
     * @param informationAuthority The credibility information stemming from this agent has within the simulation
     * @param productPolicyScheme The scheme by which product policies are implemented
     * @param consumerPolicyScheme The scheme by which consumer policies are implemented
     * @param regulatoryPolicyScheme The scheme by which regulatory policies are implemented
     * @param marketEvaluationScheme The scheme by which the market is evaluated
     */
    public PolicyAgent(SimulationContainer associatedSimulationContainer, double informationAuthority, ProductPolicyScheme productPolicyScheme, ConsumerPolicyScheme consumerPolicyScheme, RegulatoryPolicyScheme regulatoryPolicyScheme, MarketEvaluationScheme marketEvaluationScheme) {
        super(associatedSimulationContainer, informationAuthority);
        this.productPolicyScheme = productPolicyScheme;
        this.consumerPolicyScheme = consumerPolicyScheme;
        this.regulatoryPolicyScheme = regulatoryPolicyScheme;
        this.marketEvaluationScheme = marketEvaluationScheme;
    }

    public ProductPolicyScheme getProductPolicyScheme() {
        return productPolicyScheme;
    }

    public ConsumerPolicyScheme getConsumerPolicyScheme() {
        return consumerPolicyScheme;
    }

    public RegulatoryPolicyScheme getRegulatoryPolicyScheme() {
        return regulatoryPolicyScheme;
    }

    public MarketEvaluationScheme getMarketEvaluationScheme() {
        return marketEvaluationScheme;
    }
}
