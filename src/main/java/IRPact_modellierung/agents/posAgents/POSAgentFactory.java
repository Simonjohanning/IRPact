package IRPact_modellierung.agents.posAgents;

import IRPact_modellierung.helper.ValueConversionHelper;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;

import java.awt.geom.Point2D;
import java.util.*;

//TODO implement

/**
 * A factory to create POS agents within the simulation based on their configuration
 *
 * @author Simon Johanning
 */
public class POSAgentFactory {

	private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	/**
	 * Creates a POS agent with default values independent of the configuration.
	 * Agent will be situated at point (0,0), and have all products in the simulation available with a priceFactor of 1
	 *
	 * @param simulationContainer The container the simulation the POS agent is to be part of takes place in
	 * @return a POS agent with default settings described above
	 */
	public static POSAgent createDefaultPOSAgent(SimulationContainer simulationContainer) {
		Set<Product> simulationProducts = simulationContainer.getProducts();
		HashMap<Product, Boolean> productAvailability = new HashMap<Product, Boolean>(simulationProducts.size());
		HashMap<Product, Double> productPriceFactor = new HashMap<Product, Double>(simulationProducts.size());
		for(Product product : simulationProducts) {
			productAvailability.put(product, true);
			productPriceFactor.put(product, 1.0);
		}
		return new POSAgent(simulationContainer, new Point2D.Double(0.0, 0.0), 1.0, productAvailability, productPriceFactor, "TrivialPurchaseProcessScheme");
	}

	/**
	 * Method to create a number of POS agents with their respective POSAgentConfiguration within the simulation.
	 *
	 * @param simulationContainer The container the simulation the POS agent is to be part of takes place in
	 * @param simulationProducts The products the POS agent is to be initialized with
	 * @param configuration The configuration of the POS agents to be created
	 * @return A set of POSAgents within the simulationContainer based on the configuration of these agents
	 */
	public static Set<POSAgent> createPOSAgents(SimulationContainer simulationContainer, Set<Product> simulationProducts, Set<POSAgentConfiguration> configuration){
		Set<POSAgent> posAgents = new HashSet<POSAgent>(configuration.size());
		for(POSAgentConfiguration agentConfiguration : configuration){
			Map<Product, Boolean> productAvailability = new HashMap<Product, Boolean>(simulationProducts.size());
			Map<Product, Double> productPriceFactor = new HashMap<Product, Double>(simulationProducts.size());
			//fooLog.info("attempting to create products {} ({} in total) for {}", simulationProducts, simulationProducts.size(), agentConfiguration.getName());
			for(Product product : simulationProducts){
				fooLog.debug("product {} is part of product group {}", product, product.getPartOfProductGroup());
				fooLog.debug("creating product {} for pos {} with availability {}",product.getName(), agentConfiguration.getName(), agentConfiguration.getProductGroupAvailability().get(product.getPartOfProductGroup()));
				productAvailability.put(product, ValueConversionHelper.signToBoolean(agentConfiguration.getProductGroupAvailability().get(product.getPartOfProductGroup()).draw()));
				productPriceFactor.put(product, agentConfiguration.getProductGroupPriceFactor().get(product.getPartOfProductGroup()).draw());
			}
			posAgents.add(new POSAgent(simulationContainer, agentConfiguration.getSpatialDistribution().draw(simulationContainer.getSpatialModel()), agentConfiguration.getInformationAuthority(), productAvailability, productPriceFactor, agentConfiguration.getPurchaseProcessSchemeIdentifier()));
		}
		return posAgents;
	}

	/**
	 * Method to create a number of POS agents with their respective POSAgentConfiguration within the simulation.
	 * Will use all products within the simulation to create the POS agents
	 *
	 * @param simulationContainer The container the simulation the POS agent is to be part of takes place in
	 * @param configuration The configuration of the POS agents to be created
	 * @return A set of POSAgents within the simulationContainer based on the configuration of these agents
	 */
	public static Set<POSAgent> createPOSAgents(SimulationContainer simulationContainer, Set<POSAgentConfiguration> configuration) {
		return createPOSAgents(simulationContainer, simulationContainer.getProducts(), configuration);
	}

	/**
	 * Method to create the purchase process for the POSAgents.
	 * Returns the corresponding purchase process based on the identifier,
	 * with the POSAgent bound to the process
	 *
	 * @param correspondingPOSAgent The agent linked with the purchase process
	 * @param PurchaseProcessSchemeIdentifier An identifier for the purchase process to be initialized
	 * @throws IllegalArgumentException Will be thrown when the purchase process identifier refers to an unimplemented purchase process
	 */
	public static PurchaseProcessScheme createPurchaseProcessScheme(POSAgent correspondingPOSAgent, String PurchaseProcessSchemeIdentifier) throws IllegalArgumentException{
		switch(PurchaseProcessSchemeIdentifier){
			case "TrivialPurchaseProcessScheme": return new TrivialPurchaseProcessScheme(correspondingPOSAgent);
			default: throw new IllegalArgumentException("Purchase process "+PurchaseProcessSchemeIdentifier+" is not implemented!!\nPlease provide a valid one!");
		}
	}
}