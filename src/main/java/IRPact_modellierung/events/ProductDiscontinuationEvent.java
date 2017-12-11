package IRPact_modellierung.events;

import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class that models the discontinuation of a product.
 * This can mean that the product is simply not available anymore,
 * or that it additionally cease to suffice a need (i.e. isn't adopted anymore).
 * The semantics are specified by the Readoption scheme used.
 * For the product discontinuation event this is managed through leaving this to the simulation container.
 *
 * Technically, as a scripted product event, a product discontinuation event is scheduled at the initialization
 * of the simulation for a certain product and executed at the scheduled time (via the remove product method in the simulation container).
 *
 * @author Simon Johanning
 */
public class ProductDiscontinuationEvent extends ScriptedProductEvent{

	private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	/**
	 * A product discontinuation event removes a certain product from the simulation container at a scheduled time
	 *
	 * @param simulationContainer The container of the simulation the product is to be removed from
	 * @param productToBeDiscontinued The product that is to be removed from the simulation
	 * @param scheduledForTime The simulation time the product is to be removed
	 */
	public ProductDiscontinuationEvent(SimulationContainer simulationContainer, Product productToBeDiscontinued, double scheduledForTime) {
		super(simulationContainer, scheduledForTime, productToBeDiscontinued);
	}

	/**
	 * Processing a product discontinuation event results in the product being removed from the simulation container
	 *
	 * @param systemTime The current time of the system for execution
	 */
	public void processEvent(double systemTime) {
		fooLog.info("In ProductDiscontinuationEvent: Product {} wants to be discontinued at scheduled time {} (system time is {})", productConcerned.getName(), getScheduledForTime(), systemTime);
		associatedSimulationContainer.removeProduct(productConcerned);
	}

}