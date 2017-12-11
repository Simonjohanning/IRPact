package IRPact_modellierung.agents.companyAgents;

import IRPact_modellierung.agents.companyAgents.advertisement.AdvertisementScheme;
import IRPact_modellierung.agents.InformationAgent;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.Set;

/**
 * A company agent represents the decision making entities within providers of products and services,
 * not just limited to corporations but e.g. to communal energy providers.
 * It represents these agents as static entities with reference to schemes specifying its behaviors,
 * which thus (conceptually) represent their dynamic interactions with other aspects of the model.
 *
 * @author Simon Johanning
 */
public class CompanyAgent extends InformationAgent {

    private String name;
    private Set<Product> productPortfolio;
    private ProductQualityManipulationScheme productQualityManipulationScheme;
    private ManagementDecisionScheme managementDecisionScheme;
    private AdvertisementScheme advertisementScheme;

    /**
     * A CompanyAgent represents how a company acts in IRPact.
     * As an Agent it is situated in the associatedSimulationContainer and equipped with an informationAuthority.
     *
     * A CompanyAgent manages a number of products (the ones in their productPortfolio), and acts
     * through three schemes, namely the productQualityManipulationScheme, the managementDecisionScheme
     * and the advertisementScheme.
     *
     * @param associatedSimulationContainer The container the simulation runs in
     * @param informationAuthority The credibility information stemming from this agent has
     * @param name The name of the agent
     * @param productPortfolio The set of products managed by the CompanyAgent
     * @param productQualityManipulationScheme The scheme used to change product qualities
     * @param managementDecisionScheme The scheme used to make management decisions
     * @param advertisementScheme The scheme used to advertise their products
     */
    public CompanyAgent(SimulationContainer associatedSimulationContainer, double informationAuthority, String name, Set<Product> productPortfolio, ProductQualityManipulationScheme productQualityManipulationScheme, ManagementDecisionScheme managementDecisionScheme, AdvertisementScheme advertisementScheme) {
        super(associatedSimulationContainer, informationAuthority);
        this.name = name;
        this.productPortfolio = productPortfolio;
        this.productQualityManipulationScheme = productQualityManipulationScheme;
        this.managementDecisionScheme = managementDecisionScheme;
        this.advertisementScheme = advertisementScheme;
    }

    public Set<Product> getProductPortfolio() {
        return productPortfolio;
    }

    public ProductQualityManipulationScheme getProductQualityManipulationScheme() {
        return productQualityManipulationScheme;
    }

    public ManagementDecisionScheme getManagementDecisionScheme() {
        return managementDecisionScheme;
    }

    public AdvertisementScheme getAdvertisementScheme() {
        return advertisementScheme;
    }

    public String getName() {
        return name;
    }
}