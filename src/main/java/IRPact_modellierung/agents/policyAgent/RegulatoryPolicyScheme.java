package IRPact_modellierung.agents.policyAgent;

import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.Set;

/**
 * regulatory policy scheme describes the way
 products are evaluated towards health, safety and environmental standards.
 It is used to evaluate which products need to be taken of the market, for-
 malizing restrictions on products. The regulatory policy scheme integrates
 legal acts and the corresponding response of company agents in discontinu-
 ing products that don't conform to said standards.
 */
public abstract class RegulatoryPolicyScheme {

    /**
     * Method to formalize the process of formation and evaluation of the regulatory policy.
     * It is used to evaluate which products need to be taken of the market, for-
     * malizing restrictions on products. The regulatory policy scheme integrates
     * legal acts and the corresponding response of company agents in discontinu-
     * ing products that don't conform to said standards.
     *
     * @param simulationContainer The simulation container the policies should be evaluated in
     * @return A set of products not conforming to the regulatory policy specified within this method. These will be removed from the market.
     */
    public abstract Set<Product> evaluateRegulatoryPolicy(SimulationContainer simulationContainer);


    /**
     * Method that removes all 'unsuitable' products from the market.
     * Unsuitable products are determined by the evaluateRegulatoryPolicy method, specifying the process of product discontinuation.
     *
     * @param simulationContainer The simulation container the policies should be evaluated and applied in
     */
    public void implementRegulatoryPolicy(SimulationContainer simulationContainer){
        Set<Product> productsToDiscontinue = evaluateRegulatoryPolicy(simulationContainer);
        for(Product currentProduct : productsToDiscontinue){
            simulationContainer.removeProduct(currentProduct);
        }
    }
}
