package IRPact_modellierung.products;

import java.util.*;

import IRPact_modellierung.events.*;
import org.apache.logging.log4j.LogManager;

/**
 * The ProductConfiguration encapsulates all information relevant for the instantiation of the product groups.
 *
 * @author Simon Johanning
 */
public class ProductConfiguration {

	private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	private Set<ProductGroup> productGroups;
	private List<MarketIntroductionEventDescription> marketIntroductionEvents;
	private List<ProductDiscontinuationEventDescription> productDiscontinuationEvents;
	private Map<ProductGroup, Integer> noProductsPerGroup;

	/**
	 * The configuration consists of the productGroups used in the simulation,
	 * the events describing when products enter (marketIntroductionEvents) or (productDiscontinuationEvents)
	 * leave the market, and the noProductsPerGroup that are stochastically instantiated.
	 *
	 * @param productGroups The ProductGroups that are used in the simulation
	 * @param marketIntroductionEvents The events describing the introduction of products to the market
	 * @param productDiscontinuationEvents The events describing the discontinuation of products to the market
	 * @param noProductsPerGroup The number of products that are stochastically instantiated in every product group
	 */
	public ProductConfiguration(Set<ProductGroup> productGroups, List<MarketIntroductionEventDescription> marketIntroductionEvents, List<ProductDiscontinuationEventDescription> productDiscontinuationEvents, Map<ProductGroup, Integer> noProductsPerGroup) {
		this.productGroups = productGroups;
		this.marketIntroductionEvents = marketIntroductionEvents;
		this.productDiscontinuationEvents = productDiscontinuationEvents;
		this.noProductsPerGroup = noProductsPerGroup;
	}

	public Map<ProductGroup, Integer> getNoProductsPerGroup() {
		return noProductsPerGroup;
	}

	public Set<ProductGroup> getProductGroups() {
		return this.productGroups;
	}

	public List<MarketIntroductionEventDescription> getMarketIntroductionEvents() {
		return marketIntroductionEvents;
	}

	public List<ProductDiscontinuationEventDescription> getProductDiscontinuationEvents() {
		return productDiscontinuationEvents;
	}

}