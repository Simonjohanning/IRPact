package IRPact_modellierung.agents.companyAgents;

/**
 * A ProductQualityManipulationScheme describes how a company agent changes the quality of the managed products within the simulation.
 * It should allow a company agent to evaluate their portfolio and encapsulate measures that change the qualities of the products.
 * How these propagate is left to the advertisement processes linked with the company agent.
 *
 * @author Simon Johanning
 */
public abstract class ProductQualityManipulationScheme {

    /**
     * This method describes the process of the quality evaluation and measures of quality manipulation.
     * It bundles all relevant processes within the company the company agent represents, is (generally) triggered by the process model and should select the relevant products from the product portfolio of the company agent.
     *
     * @param correspondingAgent The agent managing the product qualities
     */
    public abstract void manageProductQualities(CompanyAgent correspondingAgent);
}
