package IRPact_modellierung.perception;

/**
 * The ProductAttributePerceptionScheme is an abstraction of the model mechanism of
 * how the value of a product (attribute) is perceived by a ConsumerAgent.
 * It implements methods to calculate the current perception of the product attribute
 * (based on the simulation time), and offers a functionality to modify the value
 * of the perception of the product attribute.
 * Being on the (concrete) level of ConsumerAgents and ProductAttributes,
 * ProductAttributePerceptionSchemes describe the state of and mechanics of changing
 * the concrete perception of a ProductAttribute by a ConsumerAgent.
 *
 * A ProductAttributePerceptionScheme is specified as an interface since classes
 * exist that need to extend abstract classes, and the functionality of it is central.
 *
 * @author Simon Johanning
 */
public interface ProductAttributePerceptionScheme {

    /**
     * Method to calculate the perception of a product attribute based on the simulation time.
     * This method thus represents (the state of) the perception of the associated ProductAttribute.
     *
     * @param systemTime the time point the productAttributePerception is to be calculated for
     * @return numerical value of the current perception of the product attribute
     */
    double calculateProductAttributePerception(double systemTime);

    /**
     * Method to change the value of the current product attribute perception.
     * This method thus describes the mechanics of how the perception of an agent for a product attribute
     * changes given information or communication about relevant aspects of the product.
     *
     * @param productAttributeValuePerception The relevant productAttributeValuePerception. It's semantics depend on the implementation, but it's generally intended to be a 'new' perception based on the envionment of the actor
     * @param informationWeight Value on how strongly the new information is taken into account
     * @param timestamp The simulation time the product attribute value perception is to take place
     */
    void modifyValue(double productAttributeValuePerception, double informationWeight, double timestamp);
}
