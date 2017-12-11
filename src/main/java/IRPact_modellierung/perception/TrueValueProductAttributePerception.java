package IRPact_modellierung.perception;

import IRPact_modellierung.products.ProductAttribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The TrueValueProductAttributePerception is a ProductAttributePerceptionScheme
 * that represents the perception of a ProductAttribute as a single numerical value
 * denoting the true value of this ProductAttribute.
 *
 * This scheme is intended for models that assume perfect information of the ConsumerAgents
 * and forbids the values of the perception scheme to change (by throwing an error).
 *
 * @author Simon Johanning
 */
public class TrueValueProductAttributePerception implements ProductAttributePerceptionScheme {

    private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    ProductAttribute associatedProductAttribute;

    /**
     * The TrueValueProductAttributePerception is associated with a product attribute whose value it uses
     * for 'calculating' the 'perception' by using its value.
     *
     * @param associatedProductAttribute The product attribute associated with this perception scheme
     */
    public TrueValueProductAttributePerception(ProductAttribute associatedProductAttribute){
        this.associatedProductAttribute = associatedProductAttribute;
    }

    public ProductAttribute getAssociatedProductAttribute() {
        return associatedProductAttribute;
    }

    /**
     * 'calculates' the product perception at system time (returns the true value of the product attribute)
     *
     * @param systemTime the time point the productAttributePerception is to be calculated for (ignored for this scheme)
     * @return numerical value of the true value of the product attribute
     */
    public double calculateProductAttributePerception(double systemTime) {
        return associatedProductAttribute.getValue();
    }

    /**
     * Since the perception of a product attribute with this scheme shouldn't change
     * without the attribute value changing, this method just ignores all perceptions.
     *
     * @param productAttributeValuePerception The relevant productAttributeValuePerception. It's semantics depend on the implementation, but it's generally intended to be a 'new' perception based on the environment of the actor
     * @param informationWeight Value on how strongly the new information is taken into account
     * @param timestamp The simulation time the product attribute value perception is to take place
     */
    public void modifyValue(double productAttributeValuePerception, double informationWeight, double timestamp) {

    }
}
