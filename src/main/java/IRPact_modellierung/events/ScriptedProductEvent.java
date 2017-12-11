package IRPact_modellierung.events;

import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;

/**
 * Abstraction to represent a scripted event that refers to a product.
 * Will thus require a reference to the product.
 *
 * @author Simon Johanning
 */
public abstract class ScriptedProductEvent extends ScriptedEvent{
    //TODO think about whether the scripted character is necessary or whether it should become just a productEvent (since now CompanyAgents
    protected Product productConcerned;

    public ScriptedProductEvent(SimulationContainer simulationContainer, double scheduledForTime, Product productConcerned) throws IllegalArgumentException{
        super(simulationContainer, scheduledForTime);
        if(!simulationContainer.getProducts().contains(productConcerned)) throw new IllegalArgumentException("Product ("+productConcerned+") doesn't refer to an (active) product in the simulation!!");
        this.productConcerned = productConcerned;
    }

    public Product getProductConcerned() {
        return productConcerned;
    }
}
