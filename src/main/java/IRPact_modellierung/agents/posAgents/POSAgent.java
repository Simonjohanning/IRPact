package IRPact_modellierung.agents.posAgents;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Map;

import IRPact_modellierung.agents.SpatialInformationAgent;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.Set;

//TODO split POSAgent in spatialPOSAgent and non-spatial POS agent for services that are accessed / purchased in a non-local fashion (such as non-physical products or mail order products)?

/**
 * POS agents represent actors that are encountered / manage a point-of-sale.
 * They are intermediates between CompanyAgents and ConsumerAgents that serve to provide products to ConsumerAgents.
 *
 * @author Simon Johanning
 */
public class POSAgent extends SpatialInformationAgent {
//TODO think about non-spatial POS agent
	//maps to describe what products are available at the POS and how expensive they are (relatively)
	private Map<Product, Boolean> productAvailability;
	private Map<Product, Double> productPriceFactor;

	/**
	 * Initializes a point-of-sales agent
	 *
	 * @param simulationContainer The container of the simulation the POS agent shall be part of
	 * @param coordinates The position the POSagent is initialized with
	 * @param informationAuthority The authority information stemming from this agent has
	 * @param productAvailability A map describing which products are available at the POS agent at initialization time
	 * @param productPriceFactor A map describing the (relative) price of the respective products
	 */
	public POSAgent(SimulationContainer simulationContainer, Point2D coordinates, double informationAuthority, Map<Product, Boolean> productAvailability, Map<Product, Double> productPriceFactor) {
		super(simulationContainer, coordinates, informationAuthority);
		this.productAvailability = productAvailability;
		this.productPriceFactor = productPriceFactor;
	}

	public Set<Product> showAvailableProducts() {
		HashSet<Product> availableProducts = new HashSet<Product>();
		for(Product productToCheck : productAvailability.keySet()){
			if(productAvailability.get(productToCheck)) availableProducts.add(productToCheck);
		}
		return availableProducts;
	}

	/**
	 * 
	 * @param product
	 */
	//TODO how to signify price?
	public double giveProductPrice(Product product) {
		for(ProductAttribute productAttribute : product.getProductAttributes()){
			if(productAttribute.getCorrespondingProductGroupAttribute().getName().equals("price")) return productAttribute.getValue()*productPriceFactor.get(product);
		}
		//TODO make a nice failcheck
		return -1.0;
	}

}