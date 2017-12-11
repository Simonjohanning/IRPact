package IRPact_modellierung.preference;

import IRPact_modellierung.products.ProductGroupAttribute;

/**
 * A ProductGroupAttributeValueMapping represents the correlation between a ProductGroupAttribute and Value.
 * Its is generally used in the evaluation of the utility of a product, in deriving the importance of
 * a product attribute given the preferences (i.e. strength of values) of a ConsumerAgent.
 *
 * This is done by assigning a numerical weight (the mapping strength) to a ProductGroupAttribute and a Value
 *
 * @author Simon Johanning
 */
public class ProductGroupAttributeValueMapping {

	private ProductGroupAttribute productGroupAttribute;
	private Value value;
	private double mappingStrength;

	/**
	 * A ProductGroupAttributeValueMapping assigns a numerical value, the mappingStrength
	 * to the correlation of a ProductGroupAttribute and a Value
	 *
	 * @param productGroupAttribute The ProductGroupAttribute to be mapped to a Preference
	 * @param value The Value the Preference to link is based upon
	 * @param mappingStrength The strength of the mapping between the ProductGroupAttribute and the respective Value
	 */
	public ProductGroupAttributeValueMapping(ProductGroupAttribute productGroupAttribute, Value value, double mappingStrength) {
		this.productGroupAttribute = productGroupAttribute;
		this.value = value;
		this.mappingStrength = mappingStrength;
	}

	public ProductGroupAttribute getProductGroupAttribute() {
		return this.productGroupAttribute;
	}

	public Value getValue() {
		return this.value;
	}

	public double getMappingStrength() {
		return this.mappingStrength;
	}

	public String toString(){
		return ("The mapping strength between "+productGroupAttribute.getName()+" and "+value.getName()+" is "+mappingStrength);
	}

}