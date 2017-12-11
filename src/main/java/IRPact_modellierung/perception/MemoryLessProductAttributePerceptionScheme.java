package IRPact_modellierung.perception;

import IRPact_modellierung.products.ProductAttribute;

/**
 * The MemoryLessProductAttributePerceptionScheme is a ProductAttributePerceptionScheme
 * that doesn't model a ConsumerAgents memory for ProductAttribute perceptions.
 * The perception for the associated ProductAttribute is modeled through a single value
 * and is overwritten directly (ignoring the previous perceptions of the consumer for this attribute)
 *
 * @author Simon Johanning
 */
public class MemoryLessProductAttributePerceptionScheme implements ProductAttributePerceptionScheme{

    private ProductAttribute associatedProductAttribute;
    private double currentPerception;

    /**
     * The perception of this scheme is initialized with the ProductAttribute it is to describe
     * and the perception it starts out with.
     *
     * @param associatedProductAttribute The PA associated with this scheme
     * @param initialPerception The perception of the respective agent at the beginning of the simulation
     */
    public MemoryLessProductAttributePerceptionScheme(ProductAttribute associatedProductAttribute, double initialPerception) {
        this.associatedProductAttribute = associatedProductAttribute;
        this.currentPerception = initialPerception;
    }

    /**
     * Since the perception described through this scheme is implemented by a single value,
     * ignoring the history of perceptions of the agent,
     * the calculation of the value is trivial (and is just that value).
     *
     * @param systemTime the time point the productAttributePerception is to be calculated for
     * @return The current perception of the agent
     */
    public double calculateProductAttributePerception(double systemTime) {
        return currentPerception;
    }

    public ProductAttribute getAssociatedProductAttribute() {
        return associatedProductAttribute;
    }

    /**
     * Since in this scheme, no history is taken into account, the modification of the perception
     * consists through replacing the old perception described through this scheme with the new perception,
     * irrespective of the information weight and the time of the new perception.
     *
     * @param productAttributeValuePerception The relevant productAttributeValuePerception. It's semantics depend on the implementation, but it's generally intended to be a 'new' perception based on the envionment of the actor
     * @param informationWeight Value on how strongly the new information is taken into account
     * @param timestamp The simulation time the product attribute value perception is to take place
     */
    public void modifyValue(double productAttributeValuePerception, double informationWeight, double timestamp) {
        currentPerception = productAttributeValuePerception;
    }
}
