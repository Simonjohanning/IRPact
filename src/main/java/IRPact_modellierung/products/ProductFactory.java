package IRPact_modellierung.products;

import IRPact_modellierung.events.MarketIntroductionEvent;
import IRPact_modellierung.events.MarketIntroductionEventDescription;
import IRPact_modellierung.events.ProductDiscontinuationEvent;
import IRPact_modellierung.events.ProductDiscontinuationEventDescription;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.helper.StructureEnricher;
import IRPact_modellierung.simulation.Configuration;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;

import java.util.*;

/**
 * The ProductFactory serves to provides methods that instantiate product-related instances within the simulation.
 *
 * @author Simon Johanning
 */
public class ProductFactory {


	private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	/**
	 * This method instantiates the products belonging to the productGroup based on the configuration and the simulation container.\
	 * The products are not added to the simulationContainer (for this the responsibility of other parts of the framework,
	 * currently the SimulationFactory).
	 *
	 * @param productGroup The ProductGroup the products to instantiate are part of
	 * @param configuration The configuration of the simulation
	 * @param simulationContainer The container the products are to be associated with
	 * @return The product instances belonging to the respective productGroup
	 * @throws IllegalArgumentException Will be thrown when the product groups to create products of has no lifetime distribution set
	 */
	public static Set<Product> createProducts(ProductGroup productGroup, Configuration configuration, SimulationContainer simulationContainer) throws IllegalArgumentException{
		HashSet<Product> products = new HashSet<Product>();
		//instantiate stochastic products
		for(int index=0; index<configuration.getProductConfiguration().getNoProductsPerGroup().get(productGroup); index++){
			//instantiate product group attributes
			HashSet<ProductAttribute> productAttributes = new HashSet<ProductAttribute>();
			for(ProductGroupAttribute pga : productGroup.getProductGroupAttributes()){
				productAttributes.add(new ProductAttribute(pga.getValue().draw(), pga));
			}
			String productName = productGroup.getGroupName()+"_"+index;
			if(productGroup.getProductLifetimeDistribution() == null) throw new IllegalArgumentException("Product group "+productGroup.getGroupName()+" has no product lifetime distribution associated to it!!");
			products.add(new Product(simulationContainer, productAttributes, true, productGroup, productName, productGroup.getProductLifetimeDistribution()));
		}
		fooLog.debug("Attempting to create fixed products for product group {}", productGroup.getGroupName());
		//add fixed products
		for(FixedProductDescription fixedProduct : productGroup.getFixedProducts()){
			HashSet<ProductAttribute> productAttributes = new HashSet<ProductAttribute>();
			for(ProductGroupAttribute pga : fixedProduct.getProductAttributeValues().keySet()){
				productAttributes.add(new ProductAttribute(fixedProduct.getProductAttributeValues().get(pga),pga));
			}
			fooLog.debug("FixedProduct with name {}",fixedProduct.getName());
			Product createdProduct = new Product(simulationContainer, productAttributes, true, productGroup, fixedProduct.getName(), fixedProduct.getProductLifetimeDistribution());
			products.add(createdProduct);
			simulationContainer.addFixedProductMapEntry(fixedProduct, createdProduct);
		}
		return products;
	}

	/**
	 * Method to create the MarketIntroductionEvents within the simulation container.
	 * Events are not yet scheduled; This has to be done by the calling function!
	 *
	 * @param productConfiguration The configuration for product aspect within the simulation
	 * @param simulationContainer The container the simulation runs in
	 * @return The MarketIntroductionEvents specified in the productConfiguration
	 */
	public static Set<MarketIntroductionEvent> createMarketIntroductionEvents(ProductConfiguration productConfiguration, SimulationContainer simulationContainer){
		HashSet<MarketIntroductionEvent> mies = new HashSet<MarketIntroductionEvent>();
		//add products introduced at a later time
		for(MarketIntroductionEventDescription mied : productConfiguration.getMarketIntroductionEvents()){
			//create the product the MIE should target
			Product productConcerned;
			if(mied.getCorrespondingFixedProduct().getProductLifetimeDistribution() != null) productConcerned =  new Product(simulationContainer, LazynessHelper.makeProductAttributesFromProductGroupAttributeValueMap(mied.getCorrespondingFixedProduct().getProductAttributeValues()), false, StructureEnricher.attachProductGroupNames(productConfiguration.getProductGroups()).get(mied.getCorrespondingFixedProduct().getPartOfProductGroup()), mied.getCorrespondingFixedProduct().getName(), mied.getCorrespondingFixedProduct().getProductLifetimeDistribution());
			else productConcerned =  new Product(simulationContainer, LazynessHelper.makeProductAttributesFromProductGroupAttributeValueMap(mied.getCorrespondingFixedProduct().getProductAttributeValues()), false, StructureEnricher.attachProductGroupNames(productConfiguration.getProductGroups()).get(mied.getCorrespondingFixedProduct().getPartOfProductGroup()), mied.getCorrespondingFixedProduct().getName(), StructureEnricher.attachProductGroupNames(LazynessHelper.deriveProductGroups(simulationContainer.getProducts())).get(mied.getCorrespondingFixedProduct().getPartOfProductGroup()).getProductLifetimeDistribution());
			simulationContainer.addProduct(productConcerned);
			//add new product to derived products
			productConcerned.getPartOfProductGroup().addDerivedProduct(productConcerned);
			//create a new MIE within the simulation container
			mies.add(new MarketIntroductionEvent(simulationContainer, productConcerned, mied.getScheduledForTime()));
		}
		return mies;
	}

	/**
	 * Method to create the ProductDiscontinuationEvents for the simulation
	 * from the respective configuration.
	 *
	 * @param simulationContainer The container the simulation runs in
	 * @param productDiscontinuationEvents The configuration of the PDEs
	 * @return A set of all PDEs within the simulation
	 */
	public static Set<ProductDiscontinuationEvent> createProductDiscontinuationEvents(SimulationContainer simulationContainer, List<ProductDiscontinuationEventDescription> productDiscontinuationEvents) {
		Set<ProductDiscontinuationEvent> pdes = new HashSet<>();
		Map<String, Product> productMap = StructureEnricher.attachProductNamesProducts(simulationContainer.getProducts());
		Iterator<ProductDiscontinuationEventDescription> pdeIterator = productDiscontinuationEvents.iterator();
		while(pdeIterator.hasNext()){
			ProductDiscontinuationEventDescription currentPded = pdeIterator.next();
			pdes.add(new ProductDiscontinuationEvent(simulationContainer, productMap.get(currentPded.getProductName()), currentPded.getScheduledForTime()));
		}
		return pdes;
	}
}