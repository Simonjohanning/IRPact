package IRPact_modellierung.products;

import IRPact_modellierung.distributions.UnivariateDistribution;

import java.util.Map;

/**
 * A FixedProductDescription is the 'raw' version of a FixedProduct,
 * used to instantiate the corresponding product correctly.
 *
 * As a product, is equipped with the information about the ProductAttributes,
 * a name, the reference to the ProductGroup (here as a String, since the
 * instantiation of the ProductGroup depends on the FixedProductDescriptions),
 * and the productLifetimeDistribution.
 *
 * @author Simon Johanning
 */
public class FixedProductDescription {

    private Map<ProductGroupAttribute, Double> productAttributeValues;
    private String name;
    private String partOfProductGroup;
    private UnivariateDistribution productLifetimeDistribution;

    /**
     * A FixedProduct(Description) for the product name, exhibiting the productAttributeValues
     * is associated with partOfProductGroup and the productLifetimeDistribution, specifying
     * how long products are adopted.
     *
     * @param productAttributeValues A map of the ProductGroupAttributes and their value the fixed product should be associated with
     * @param name The name of the fixed product
     * @param partOfProductGroup The qualifier of the product group the fixed product is part of
     * @param productLifetimeDistribution The (overwritten) productLifetimeDistribution
     */
    public FixedProductDescription( Map<ProductGroupAttribute, Double> productAttributeValues, String name, String partOfProductGroup, UnivariateDistribution productLifetimeDistribution) {
        this.productAttributeValues = productAttributeValues;
        this.name = name;
        this.partOfProductGroup = partOfProductGroup;
        this.productLifetimeDistribution = productLifetimeDistribution;
    }

    /**
     * A FixedProduct(Description) for the product name, exhibiting the productAttributeValues
     * is associated with partOfProductGroup and the productLifetimeDistribution, specifying
     * how long products are adopted.
     *
     * This constructor specifies no productLifetimeDistribution (set to null);
     * Thus the one assigned to the ProductGroup will be used for a Product instance.
     *
     * @param productAttributeValues A map of the ProductGroupAttributes and their value the fixed product should be associated with
     * @param name The name of the fixed product
     * @param partOfProductGroup The qualifier of the product group the fixed product is part of
     */
    public FixedProductDescription( Map<ProductGroupAttribute, Double> productAttributeValues, String name, String partOfProductGroup) {
        this.productAttributeValues = productAttributeValues;
        this.name = name;
        this.partOfProductGroup = partOfProductGroup;
        this.productLifetimeDistribution = null;
    }

    public Map<ProductGroupAttribute, Double> getProductAttributeValues() {
        return productAttributeValues;
    }

    public String getName() {
        return name;
    }

    public String getPartOfProductGroup() {
        return partOfProductGroup;
    }

    public UnivariateDistribution getProductLifetimeDistribution() {
        return productLifetimeDistribution;
    }
}
