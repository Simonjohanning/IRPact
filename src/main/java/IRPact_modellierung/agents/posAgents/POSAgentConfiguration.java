package IRPact_modellierung.agents.posAgents;

import IRPact_modellierung.distributions.BooleanDistribution;
import IRPact_modellierung.distributions.SpatialDistribution;
import IRPact_modellierung.distributions.UnivariateDistribution;
import IRPact_modellierung.products.ProductGroup;

import java.util.Map;

/**
 * Data structure to describe the configuration of a POS agent
 *
 * @author Simon Johanning
 */
public class POSAgentConfiguration {

    private Map<ProductGroup, BooleanDistribution> productGroupAvailability;
    private Map<ProductGroup, UnivariateDistribution> productGroupPriceFactor;
    private SpatialDistribution spatialDistribution;
    private String name;
    private double informationAuthority;

    /**
     * Data structure to describe the configuration of a POS agent
     *
     * @param productGroupAvailability Distribution to indicate how likely a product of the respective product is to be available at the POS
     * @param informationAuthority The informationAuthority associated with the agent
     * @param productGroupPriceFactor Distribution to indicate what distribution the (relative) price of a product of the respective product follows
     * @param spatialDistribution The spatial distribution from which the initial position of a POS agent is drawn
     * @param name The base name of a POS agent
     */
    public POSAgentConfiguration(Map<ProductGroup, BooleanDistribution> productGroupAvailability, Map<ProductGroup, UnivariateDistribution> productGroupPriceFactor, SpatialDistribution spatialDistribution, String name, double informationAuthority) {
        this.productGroupAvailability = productGroupAvailability;
        this.productGroupPriceFactor = productGroupPriceFactor;
        this.spatialDistribution = spatialDistribution;
        this.name = name;
        this.informationAuthority = informationAuthority;
    }

    public Map<ProductGroup, BooleanDistribution> getProductGroupAvailability() {
        return productGroupAvailability;
    }

    public Map<ProductGroup, UnivariateDistribution> getProductGroupPriceFactor() {
        return productGroupPriceFactor;
    }

    public SpatialDistribution getSpatialDistribution() {
        return spatialDistribution;
    }

    public String getName() {
        return name;
    }

    public double getInformationAuthority() {
        return informationAuthority;
    }
}
