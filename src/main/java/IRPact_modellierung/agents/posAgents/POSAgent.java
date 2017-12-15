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

	private Map<Product, Boolean> productAvailability;
	private Map<Product, Double> productPriceFactor;
	private PurchaseProcess correspondingPurchaseProcess;

	/**
	 * Initializes a point-of-sales agent
	 *
	 * @param simulationContainer The container of the simulation the POS agent shall be part of
	 * @param coordinates The position the POSagent is initialized with
	 * @param informationAuthority The authority information stemming from this agent has
	 * @param productAvailability A map describing which products are available at the POS agent at initialization time
	 * @param productPriceFactor A map describing the (relative) price of the respective products
	 * @param purchaseProcessIdentifier A string refering to the purchase process the POS agent models
	 */
	public POSAgent(SimulationContainer simulationContainer, Point2D coordinates, double informationAuthority, Map<Product, Boolean> productAvailability, Map<Product, Double> productPriceFactor, String purchaseProcessIdentifier) {
		super(simulationContainer, coordinates, informationAuthority);
		this.productAvailability = productAvailability;
		this.productPriceFactor = productPriceFactor;
		this.correspondingPurchaseProcess = POSAgentFactory.createPurchaseProcess(this, purchaseProcessIdentifier);
	}

	/**
	 * Method to check whether a product is available at this POS
	 *
	 * @return true if the product is available (i.e. in the productAvailability map), false if not
	 */
	public boolean productAvailable(Product product){
		return productAvailability.containsKey(product);
	}

	/**
	 * Method to determine the price of a Product at the respective POS.
	 * Requires the respective product to have a product attribute called 'price',
	 * and will thrown an error if it doesn't
	 *
	 * @param product The product whose price is to be calculated (if no product attribute exists with the name 'price', an error will be thrown)
	 * @throws IllegalAccessError Will be thrown when the product doesn't have a productAttribute named 'price'
	 */
	public double giveProductPrice(Product product) throws IllegalAccessError{
		if(!productPriceFactor.containsKey(product)) throw new IllegalAccessError("No product price factor is set for product "+product+"!!!");
		for(ProductAttribute productAttribute : product.getProductAttributes()){
			if(productAttribute.getCorrespondingProductGroupAttribute().getName().equals("price")) return productAttribute.getValue()*productPriceFactor.get(product);
		}
		throw new IllegalAccessError("Product "+product.getName()+" has no product attribute called 'price'!!\nPlease make sure the model is set up accordingly!!");
	}

	public PurchaseProcess getCorrespondingPurchaseProcess() {
		return correspondingPurchaseProcess;
	}
}