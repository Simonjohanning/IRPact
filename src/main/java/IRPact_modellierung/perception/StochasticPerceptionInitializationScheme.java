package IRPact_modellierung.perception;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.distributions.BoundedUnivariateDistribution;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.Configuration;

/**
 * PerceptionInitializationScheme containing a BoundedUnivariateDistribution,
 * which determines the initial value (independent of any other circumstances)
 * according to the distribution associated with it.
 *
 * @author Simon Johanning
 */
public class StochasticPerceptionInitializationScheme extends PerceptionInitializationScheme{

    private BoundedUnivariateDistribution initializationDistribution;

    /**
     * Initializes the StochasticPerceptionInitializationScheme with the distribution
     * that all initial values of this StochasticPerceptionInitializationScheme are derived from.
     *
     * @param initializationDistribution The BoundedUnivariateDistribution the derived values are taken from
     */
    public StochasticPerceptionInitializationScheme(BoundedUnivariateDistribution initializationDistribution) {
        this.initializationDistribution = initializationDistribution;
    }

    public BoundedUnivariateDistribution getInitializationDistribution() {
        return initializationDistribution;
    }

    /**
     * The initial values are determined by drawing from the associated distribution.
     *
     * @param correspondingProductAttribute the ProductAttribute the perception relates to
     * @param associatedAgentGroup the ConsumerAgentGroup of the agents that perceive the product
     * @param configuration the Configuration of the simulation
     * @return
     */
    public double determineInitialValue(ProductAttribute correspondingProductAttribute, ConsumerAgentGroup associatedAgentGroup, Configuration configuration){
        return initializationDistribution.draw();
    }
}
