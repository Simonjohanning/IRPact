package IRPact_modellierung.information;

import IRPact_modellierung.agents.Agent;
import IRPact_modellierung.agents.InformationAgent;
import IRPact_modellierung.products.ProductAttribute;

/**
 * ProductAttributeInformation is Information that describes the value of a quality of a product
 * (i.e. productAttributeInformationValue of a ProductAttribute) with a numerical value.
 *
 * This value doesn't necessarily have to be the value the ProductAttribute holds, but is
 * given as a piece of (subjective, by the originator) knowledge, consciously or unconsciously
 * chosen from this subjective persective.
 *
 * @author Simon Johanning
 */
public class ProductAttributeInformation extends Information{

    private double productAttributeInformationValue;
    private ProductAttribute correspondingProductAttribute;

    /**
     * ProductAttributeInformation is Information about the value of a ProductAttribute.
     *
     * It is the productAttributeInformationValue that the originator transports as a piece of information
     * about the correspondingProductAttribute
     *
     * @param originator The agent from which the information stems
     * @param productAttributeInformationValue The value the originator wants to transport about the ProductAttribute
     * @param correspondingProductAttribute The ProductAttribute the value of the information is to be associated with
     */
    public ProductAttributeInformation(InformationAgent originator, double productAttributeInformationValue, ProductAttribute correspondingProductAttribute) {
        super(originator);
        this.productAttributeInformationValue = productAttributeInformationValue;
        this.correspondingProductAttribute = correspondingProductAttribute;
    }

    public double getProductAttributeInformationValue() {
        return productAttributeInformationValue;
    }

    public ProductAttribute getCorrespondingProductAttribute() {
        return correspondingProductAttribute;
    }
}
