package IRPact_modellierung.products;

import IRPact_modellierung.SimulationEntity;
import IRPact_modellierung.distributions.UnivariateDistribution;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.Set;

/**
 * Products in IRPact are represented by encapsulating a number of aspects into a common structure,
 * the Product.
 *
 * It bundles the productAttributes describing its qualities, keeps a record of whether it is introduced
 * into the market, which ProductGroup it is derived from, what the lifetime distribution is
 * (i.e. how long its products can be 'actively' adopted), and its name as a qualifier.
 *
 * @author Simon Johanning
 */
public class Product extends SimulationEntity {

	private Set<ProductAttribute> productAttributes;
	private boolean introducedToMarket;
	private ProductGroup partOfProductGroup;
	private String name;
	private UnivariateDistribution productLifetimeDistribution;

	/**
	 * This method constructs a product of qualifier name, with the qualities described by the productAttributes,
	 * of partOfProductGroup and market introduction status introducedToMarket, with its adopted products
	 * lasting according to the specification of the productLifetimeDistribution.
	 *
	 * @param associatedSimulationContainer The simulation container it is located in
	 * @param productAttributes The ProductAttributes it exhibits
	 * @param introducedToMarket whether the product is 'active' in the market
	 * @param partOfProductGroup The ProductGroup it is derived from
	 * @param name The qualifier of the product
	 * @param productLifetimeDistribution A distribution indicating how long its AdoptedProducts 'last'
	 */
	public Product(SimulationContainer associatedSimulationContainer, Set<ProductAttribute> productAttributes, boolean introducedToMarket, ProductGroup partOfProductGroup, String name, UnivariateDistribution productLifetimeDistribution) {
		super(associatedSimulationContainer);
		this.productAttributes = productAttributes;
		this.introducedToMarket = introducedToMarket;
		this.partOfProductGroup = partOfProductGroup;
		this.name = name;
		this.productLifetimeDistribution = productLifetimeDistribution;
	}

	public Set<ProductAttribute> getProductAttributes() {
		return productAttributes;
	}

	public boolean isIntroducedToMarket() {
		return this.introducedToMarket;
	}

	public void setIntroducedToMarket(boolean introducedToMarket) {
		this.introducedToMarket = introducedToMarket;
	}

	public ProductGroup getPartOfProductGroup() {
		return partOfProductGroup;
	}

	public String getName() {
		return name;
	}

	public UnivariateDistribution getProductLifetimeDistribution() {
		return productLifetimeDistribution;
	}
}