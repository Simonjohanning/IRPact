package IRPact_modellierung.events;

import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;

/**
 * Models the introduction of a new product to the market within the simulation.
 * Since all products are instantiated at the beginning of the simulation, products are merely activated.
 *
 * @author Simon Johanning
 */
public class MarketIntroductionEvent extends ScriptedProductEvent {

	private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	/**
	 * Represents the event that a product enters the market.
	 * As a scripted product event, a time it is scheduled for is associated with this event.
	 *
	 * @param simulationContainer The container in which the simulation runs
	 * @param productToBeIntroduced The product that is introduced into the market
	 * @param scheduledForTime The time the product enters the market (the event is executed
	 */
	public MarketIntroductionEvent(SimulationContainer simulationContainer, Product productToBeIntroduced, double scheduledForTime) {
		super(simulationContainer, scheduledForTime, productToBeIntroduced);
	}

	/**
	 * Processing a market introduction event merely activates the product
	 * (since products have to be instantiated at the beginning of the simulation).
	 *
	 * @param systemTime The current time of the system for execution
	 */
	public void processEvent(double systemTime) {
		fooLog.info("Introducing product {} to the market (time: {}, scheduled for {})", productConcerned.getName(), systemTime, getScheduledForTime());
		associatedSimulationContainer.activateProduct(productConcerned);
	}

}