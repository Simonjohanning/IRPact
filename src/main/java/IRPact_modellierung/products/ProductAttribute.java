package IRPact_modellierung.products;

/**
 * A ProductAttribute describes a specific quality of a Product,
 * based on the ProductGroupAttribute it corresponds to.
 * The degree to which it adhers to the quality is given by its value.
 *
 * @author Simon Johanning
 */
public class ProductAttribute {

	private double value;
	private ProductGroupAttribute correspondingProductGroupAttribute;

	/**
	 * A ProductAttribute represents the value a product has in respect to the correspondingProductGroupAttribute
	 *
	 * @param value The degree to which the respective quality is fulfilled.
	 * @param correspondingProductGroupAttribute The ProductGroupAttribute this ProductAttribute is based on
	 */
	public ProductAttribute(double value, ProductGroupAttribute correspondingProductGroupAttribute) {
		this.value = value;
		this.correspondingProductGroupAttribute = correspondingProductGroupAttribute;
	}

	public double getValue() {
		return this.value;
	}

	public ProductGroupAttribute getCorrespondingProductGroupAttribute() {
		return this.correspondingProductGroupAttribute;
	}

}