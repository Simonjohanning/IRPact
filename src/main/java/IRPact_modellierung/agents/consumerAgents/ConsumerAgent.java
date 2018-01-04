package IRPact_modellierung.agents.consumerAgents;

import IRPact_modellierung.agents.SpatialInformationAgent;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.information.Information;
import IRPact_modellierung.information.ProductAttributeInformation;
import IRPact_modellierung.perception.PerceptionSchemeFactory;
import IRPact_modellierung.perception.ProductAttributePerceptionScheme;
import IRPact_modellierung.decision.ConsumerAgentAdoptionDecisionProcess;
import IRPact_modellierung.needs.Need;
import IRPact_modellierung.network.SNConsumerNode;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Map;
import IRPact_modellierung.agents.SpatialAgent;
import java.util.Set;

import IRPact_modellierung.preference.Preference;
import IRPact_modellierung.preference.Value;
import IRPact_modellierung.products.AdoptedProduct;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Actor that adopts technology, communicates and evaluates technology.
 * Refers to many other concepts explained in their rightful place and in the documentation.
 *
 * @author Simon Johanning
 */
public abstract class ConsumerAgent extends SpatialInformationAgent {

	private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	private Set<ConsumerAgentAttribute> attributes;
	private Set<Preference> preferences;
	private SNConsumerNode correspondingNodeInSN;
	private Set<AdoptedProduct> adoptedProducts;
	private Map<ProductAttribute, ProductAttributePerceptionScheme> perceivedProductAttributeValues;
	private Map<Product, Boolean> productAwarenessMap;
	private ConsumerAgentGroup correspondingConsumerAgentGroup;
	private ConsumerAgentAdoptionDecisionProcess decisionProcessEmployed;
	private String agentID;

	/**
	 * Since ConsumerAgents intend to cover numerous, heterogeneous aspects of the simulation, a variety of concepts if found in their attributes.
	 * The attributes will be elaborated in the following
	 *
	 * @param simulationContainer The SimulationContainer the ConsumerAgent is to be part of (as a way to reference to other objects in the simulation)
	 * @param attributes ConsumerAgentAttributes the ConsumerAgent is to be initialized with
	 * @param preferences A set of preferences of the actor
	 * @param correspondingNodeInSN SNConsumerNode the actor corresponds to in the social network; used in order to dereference the social environment and position of the actor
	 * @param adoptedProducts Set of (initially) adopted Products of the ConsumerAgent
	 * @param perceivedProductAttributeValues A map to map each ProductAttribute to the perception the actor has of this product attribute
	 * @param productAwarenessMap A map for each product to indicate whether the actor is aware of this product or not
	 * @param correspondingConsumerAgentGroup The ConsumerAgentGroup the actor is part of
	 * @param decisionProcessEmployed The (default) decision process the actor uses in order to make decisions
	 * @param spatialPosition The initial position of the actor in simulation space
	 * @param agentID The ID to be refered to (String)
	 */
	public ConsumerAgent(SimulationContainer simulationContainer, Set<ConsumerAgentAttribute> attributes, Set<Preference> preferences, SNConsumerNode correspondingNodeInSN, Set<Product> adoptedProducts, Map<ProductAttribute, ProductAttributePerceptionScheme> perceivedProductAttributeValues, Map<Product, Boolean> productAwarenessMap, ConsumerAgentGroup correspondingConsumerAgentGroup, ConsumerAgentAdoptionDecisionProcess decisionProcessEmployed, Point2D spatialPosition, String agentID) {
		super(simulationContainer, spatialPosition, correspondingConsumerAgentGroup.getInformationAuthority());
		this.attributes = attributes;
		this.preferences = preferences;
		this.correspondingNodeInSN = correspondingNodeInSN;
		this.perceivedProductAttributeValues = perceivedProductAttributeValues;
		this.productAwarenessMap = productAwarenessMap;
		this.correspondingConsumerAgentGroup = correspondingConsumerAgentGroup;
		this.decisionProcessEmployed = decisionProcessEmployed;
		this.agentID = agentID;
		this.adoptedProducts = new HashSet<>();
		for(Product currentProduct : adoptedProducts){
			adoptProduct(currentProduct);
		}
	}

	public Set<ConsumerAgentAttribute> getAttributes() {
		return this.attributes;
	}

	public Set<Preference> getPreferences() {
		return this.preferences;
	}

	public SNConsumerNode getCorrespondingNodeInSN() {
		return this.correspondingNodeInSN;
	}

	public Set<AdoptedProduct> getAdoptedProducts() {
		return this.adoptedProducts;
	}

	public Map<ProductAttribute, ProductAttributePerceptionScheme> getPerceivedProductAttributeValues() {
		return this.perceivedProductAttributeValues;
	}

	public Map<Product, Boolean> getProductAwarenessMap() {
		return this.productAwarenessMap;
	}

	public ConsumerAgentGroup getCorrespondingConsumerAgentGroup() {
		return this.correspondingConsumerAgentGroup;
	}

	public ConsumerAgentAdoptionDecisionProcess getDecisionProcessEmployed() {
		return this.decisionProcessEmployed;
	}

	public String getAgentID() {
		return agentID;
	}

	/**
	 * Changes the preference of the agent for the given value by an absolute value given by amount
	 *
	 * @param valueConcerned the value corresponding to the preference to be changed
	 * @param amount The amount by which the preference is to be increased/decreased (depending on the sign of amount)
	 */
	public void manipulatePreferenceAbsolute(Value valueConcerned, double amount) {
		for(Preference correspondingPreference : getPreferences()){
			if(correspondingPreference.getValue() == valueConcerned){
				manipulatePreferenceAbsolute(correspondingPreference, amount);
				break;
			}
		}
	}

    /**
     * Changes the preference of the agent by an absolute value given by amount
     *
     * @param preferenceConcerned the preference to be changed
     * @param amount The amount by which the preference is to be increased/decreased (depending on the sign of amount)
     */
	public void manipulatePreferenceAbsolute(Preference preferenceConcerned, double amount) {
		if(preferenceConcerned.getStrength() < amount) fooLog.warn("Preference "+preferenceConcerned+" will be decreased below 0 with this preference manipulation. \nPlease ensure this is intended in the model.");
		preferenceConcerned.setStrength(preferenceConcerned.getStrength()+amount);
	}

    /**
     * Changes the preference of the agent for the given value by an a fraction of the value given by amount
     *
     * @param valueConcerned the value corresponding to the preference to be changed
     * @param amount The fraction by which the preference is to be increased/decreased (depending on the sign of amount)
     */
	public void manipulatePreferenceRelative(Value valueConcerned, double amount) {
		for(Preference correspondingPreference : getPreferences()){
			if(correspondingPreference.getValue() == valueConcerned){
				manipulatePreferenceRelative(correspondingPreference, amount);
				break;
			}
		}
	}

    /**
     * Changes the preference of the agent by a fraction of its value given by amount
     *
     * @param preferenceConcerned the preference to be changed
     * @param amount The fraction by which the preference is to be increased/decreased (depending on the sign of amount)
     */
	public void manipulatePreferenceRelative(Preference preferenceConcerned, double amount) {
		if(amount < -1.0) fooLog.warn("Preference "+preferenceConcerned+" will be decreased below 0 with this preference manipulation. \nPlease ensure this is intended in the model.");
		preferenceConcerned.setStrength(preferenceConcerned.getStrength()*(1.0+amount));
	}

	/**
	 * Causes the corresponding actor (this) to adopt the product in question.
	 *
	 * @param productToAdopt product the agent is to adopt
	 */
	public void adoptProduct(Product productToAdopt) throws IllegalArgumentException{
		if(LazynessHelper.getCorrespondingProducts(adoptedProducts).contains(productToAdopt)) throw new IllegalArgumentException("ERROR!!! Agent "+this+"is trying to adopt a product already adopted ("+productToAdopt+"{})!!!");
		if(productToAdopt == null) throw new IllegalArgumentException("Error!! Agent is trying to adopt a non-existing product (probably using decision process "+ LazynessHelper.getEmployedDecisionProcess(this, null)+")!!");
		fooLog.debug("Agent {} adopts product {}", getAgentID(), productToAdopt.getName());
		fooLog.debug(adoptedProducts);
		fooLog.debug("time: {}",this.associatedSimulationContainer.getTimeModel().getSimulationTime());
		fooLog.debug("lifetime {} ",productToAdopt.getProductLifetimeDistribution().draw());
		adoptedProducts.add(new AdoptedProduct(productToAdopt, this, this.associatedSimulationContainer.getTimeModel().getSimulationTime(), productToAdopt.getProductLifetimeDistribution().draw()));
	}

	/**
	 * Removes the adoptedProduct from the list of adoptedProducts.
	 * Should only be called from the process model.
	 *
	 * @param adoptedProduct The product to unadopt
	 * @throws IllegalArgumentException Will be thrown when the adoptedProduct is not adopted by the agent.
	 */
	public void removeAdoptedProduct(AdoptedProduct adoptedProduct) throws IllegalArgumentException{
		if(!adoptedProducts.contains(adoptedProduct)) throw new IllegalArgumentException("Product "+adoptedProduct+" that is to be unadopted is not even adopted by ConsumerAgent "+this);
		else adoptedProducts.remove(adoptedProduct);
	}

	/**
	 * processes a perception of a product attribute value through the ProductAttributePerceptionScheme
	 *
	 * @param productAttributeToChange The product attribute the perception is related to
	 * @param productAttributeValuePerception The perception of the repective product attribute value
	 */
	public void addPerceivedProductAttributeValue(ProductAttribute productAttributeToChange, double productAttributeValuePerception, double timestamp, double informationWeight) {
		if(!perceivedProductAttributeValues.containsKey(productAttributeToChange)){
			ProductAttributePerceptionScheme perceptionScheme = PerceptionSchemeFactory.createPerceptionScheme(productAttributeToChange, correspondingConsumerAgentGroup.getProductPerceptionSchemes().get(productAttributeToChange.getCorrespondingProductGroupAttribute()), correspondingConsumerAgentGroup, associatedSimulationContainer.getSimulationConfiguration());
			perceivedProductAttributeValues.put(productAttributeToChange, perceptionScheme);
		}
		perceivedProductAttributeValues.get(productAttributeToChange).modifyValue(productAttributeValuePerception, informationWeight, timestamp);

	}

	/**
	 * Makes the actor aware of the product in question (changes the product awareness for this product to true
	 *
	 * @param product The product the actor is made aware of
	 * @throws IllegalArgumentException Is thrown when product is not part of a ConsumerAgents productAwarenessMap
	 */
	public void makeAwareOfProduct(Product product) throws IllegalArgumentException{
		if(productAwarenessMap.keySet().contains(product)) productAwarenessMap.put(product, true);
		else throw new IllegalArgumentException("Product "+product+" is not part of ConsumerAgents "+this+" productAwarenessMap");
	}

	public boolean isAwareOfProduct(Product productConcerned){
		return productAwarenessMap.get(productConcerned);
	}

	/**
	 * Checks whether the need in question is already satisfied through any product already adopted
	 *
	 * @param need The need to be checked
	 * @return boolean whether the need is covered by a product already known to the actor
	 */
    public boolean needAlreadySatisfied(Need need){
		for(AdoptedProduct adoptedProduct : adoptedProducts ){
			if(adoptedProduct.getCorrespondingProduct().getPartOfProductGroup().getNeedsSatisfied().contains(need)) return true;
		}
		return false;
	}

	/**
	 * Wipes a product from the knowledge of the actor, meaning the actor will
	 *  - unadopt the product
	 *  - cease to be aware of the product
	 *  - for all product attributes lose the perception of these
	 *  Should only be called when a product is taken out of the simulation
	 *
	 * @param productToBeRemoved the product that is to be wiped
	 * @throws IllegalArgumentException Will be throws when productToBeRemoved is not in the consumers productAwarenessMap or contains an attribute not in its perceivedProductAttributeValues
	 */
	public void wipeProduct(Product productToBeRemoved) throws IllegalArgumentException{
		if(LazynessHelper.anyAdoptedProductRefersToProduct(adoptedProducts, productToBeRemoved)) adoptedProducts.remove(LazynessHelper.getCorrespondingAdoptedProduct(adoptedProducts, productToBeRemoved));
		try {
			forgetProduct(productToBeRemoved);
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	/**
	 * Being a milder form of disconnecting a product from an agent than wiping, the actor will 'only' forget the product in the sense that he will not adopt it again.
	 * Previous adoptions remain untouched (actor will not unadopt the product).
	 * Should be used when a product shouldn't be available on the market anymore but still be used by actors
	 *
	 * @param productToBeRemoved product that is forgotten by the actor
	 * @throws IllegalArgumentException Will be throws when productToBeRemoved is not in the consumers productAwarenessMap or contains an attribute not in its perceivedProductAttributeValues
	 */
	public void forgetProduct(Product productToBeRemoved) throws IllegalArgumentException{
		if(productAwarenessMap.keySet().contains(productToBeRemoved)) productAwarenessMap.remove(productToBeRemoved);
		else throw new IllegalArgumentException("Product "+productToBeRemoved+" to be wiped from ConsumerAgent "+this+" is not in its awareness map!");
		for(ProductAttribute attribute : productToBeRemoved.getProductAttributes()){
			if(perceivedProductAttributeValues.keySet().contains(attribute)) perceivedProductAttributeValues.remove(attribute);
			else throw new IllegalArgumentException("ProductAttribute "+attribute+" of the product to be wiped from ConsumerAgent "+this+ "is not in its perceivedProductAttributeValues!");
		}
	}

    /**
     * Method to model how information is processed by the ConsumerAgent.
     * Depending on the type of information (and potentially other factors such as the ConsumerAgentGroup),
     * information are processed according to their properties.
     *
     * Currently, ProductAttributeInformation are processed by being added as perception, with the informationAuthority describing the strength of the perception.
     * Other types of InformationEvents are currently not implemented and will raise an UnsupportedOperationException
     *
     * @param respectiveInformation The information to be processed
     * @param systemTime The time the information is processed at
     * @throws UnsupportedOperationException Will be thrown when information is of an unimplemented type (currently everything but ProductAttributeInformation)
     */
    public void processInformation(Information respectiveInformation, double systemTime) throws UnsupportedOperationException{
        if(respectiveInformation.getClass().getSimpleName().equals("ProductAttributeInformation")){
            perceivedProductAttributeValues.get(((ProductAttributeInformation) respectiveInformation).getCorrespondingProductAttribute()).modifyValue(((ProductAttributeInformation) respectiveInformation).getProductAttributeInformationValue(), ((ProductAttributeInformation) respectiveInformation).getInformationOriginAgent().getInformationAuthority(), systemTime);
        }else throw new UnsupportedOperationException("Processing InformationEvents of type "+respectiveInformation.getClass().getSimpleName()+" is currently not supported. Sorry!");
    }
}