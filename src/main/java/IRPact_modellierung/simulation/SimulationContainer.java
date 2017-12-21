package IRPact_modellierung.simulation;

import java.util.*;

import IRPact_modellierung.agents.companyAgents.CompanyAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.network.SocialNetwork;
import IRPact_modellierung.preference.Value;
import IRPact_modellierung.agents.policyAgent.PolicyAgent;
import IRPact_modellierung.agents.posAgents.POSAgent;
import IRPact_modellierung.events.EventScheduler;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.messaging.CompanyAgentMessageScheme;
import IRPact_modellierung.messaging.ConsumerAgentMessageScheme;
import IRPact_modellierung.needs.Need;
import IRPact_modellierung.network.SNNode;
import IRPact_modellierung.products.FixedProductDescription;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.space.SpatialModel;
import IRPact_modellierung.time.TimeModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO URGENT check what simulation container needs

public class SimulationContainer {

	private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	private Set<Product> products;
	private Set<ConsumerAgent> consumerAgents;
	private SocialNetwork socialNetwork;
	private Set<POSAgent> posAgents;
	private SpatialModel spatialModel;
	private TimeModel timeModel;
	private Set<Value> valuesUsed;
	private Configuration simulationConfiguration;
	private EventScheduler eventScheduler;
	private Map<FixedProductDescription, Product> fixedProductMap;
	private Map<SNNode, ConsumerAgent> sNMap;
	private Map<ConsumerAgent, SNNode> casNMap;
	private Set<Product> historicalProducts;
	private Set<CompanyAgent> companyAgents;
	private PolicyAgent policyAgent;
	private Set<Need> needsInSimulation;

	public SimulationContainer(Set<Product> products, Set<ConsumerAgent> consumerAgents, SocialNetwork socialNetwork, Set<POSAgent> posAgents, SpatialModel spatialModel, TimeModel timeModel, Set<Value> valuesUsed, Configuration simulationConfiguration, EventScheduler eventScheduler, Map<FixedProductDescription, Product> fixedProductMap, Map<SNNode, ConsumerAgent> sNMap, Set<CompanyAgent> companyAgents) {
		this.products = products;
		this.consumerAgents = consumerAgents;
		this.socialNetwork = socialNetwork;
		this.posAgents = posAgents;
		this.spatialModel = spatialModel;
		this.timeModel = timeModel;
		this.valuesUsed = valuesUsed;
		this.simulationConfiguration = simulationConfiguration;
		this.eventScheduler = eventScheduler;
		this.fixedProductMap = fixedProductMap;
		this.sNMap = sNMap;
		historicalProducts = new HashSet<Product>();
		this.companyAgents = companyAgents;
		this.policyAgent = null;
		needsInSimulation = LazynessHelper.aggregateNeeds(simulationConfiguration.getProductConfiguration().getProductGroups());
		casNMap = new HashMap<>();
		for(SNNode currentNode : sNMap.keySet()){
			casNMap.put(sNMap.get(currentNode), currentNode);
		}
	}

	public SimulationContainer(Configuration simulationConfiguration) {
		this.simulationConfiguration = simulationConfiguration;
		fixedProductMap = new HashMap<FixedProductDescription, Product>();
		sNMap = new HashMap<SNNode, ConsumerAgent>();
		casNMap = new HashMap<ConsumerAgent, SNNode>();
		historicalProducts = new HashSet<Product>();
		companyAgents = new HashSet<CompanyAgent>();
		policyAgent = null;
		needsInSimulation = LazynessHelper.aggregateNeeds(simulationConfiguration.getProductConfiguration().getProductGroups());
	}

	public Set<Product> getProducts() {
		return products;
	}

	public Set<Product> getActiveProducts(){
		Set<Product> activeProducts = new HashSet<Product>();
		for(Product currentProduct : products){
			//fooLog.info("Product {}'s state of market introduction is {}", currentProduct.isIntroducedToMarket());
			if(currentProduct.isIntroducedToMarket()) activeProducts.add(currentProduct);
		}
		return activeProducts;
	}

	public Set<ConsumerAgent> getConsumerAgents() {
		return consumerAgents;
	}


	public SocialNetwork getSocialNetwork() {
		return socialNetwork;
	}

	public Set<POSAgent> getPosAgents() {
		return posAgents;
	}

	public SpatialModel getSpatialModel() {
		return spatialModel;
	}

	public TimeModel getTimeModel() {
		return timeModel;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public void setConsumerAgents(Set<ConsumerAgent> consumerAgents) {
		this.consumerAgents = consumerAgents;
	}

	public void setSocialNetwork(SocialNetwork socialNetwork) {
		this.socialNetwork = socialNetwork;
	}

	public void setPosAgents(Set<POSAgent> posAgents) {
		this.posAgents = posAgents;
	}

	public void setSpatialModel(SpatialModel spatialModel) {
		this.spatialModel = spatialModel;
	}

	public void setTimeModel(TimeModel timeModel) {
		this.timeModel = timeModel;
	}

	public Map<SNNode, ConsumerAgent> getsNMap() {
		return sNMap;
	}

	public Map<ConsumerAgent, SNNode> getCasNMap() {
		return casNMap;
	}

	public void addSNNodeConsumerAgentMapping(SNNode associatedNode, ConsumerAgent containingAgent){
		if(!(sNMap.containsKey(associatedNode))){
			sNMap.put(associatedNode, containingAgent);
			casNMap.put(containingAgent, associatedNode);
		}else fooLog.warn("Tried to modify an existing SNNode-Consumeragent tuple ({},{})! Will be ignored", associatedNode, containingAgent);
	}

	public Set<Value> getValuesUsed() {
		return valuesUsed;
	}

	public Configuration getSimulationConfiguration() {
		return simulationConfiguration;
	}

	public EventScheduler getEventScheduler() {
		return eventScheduler;
	}

	public void setEventScheduler(EventScheduler eventScheduler) {
		this.eventScheduler = eventScheduler;
	}

	public void setValuesUsed(Set<Value> valuesUsed) {
		this.valuesUsed = valuesUsed;
	}

	public Map<FixedProductDescription, Product> getFixedProductMap() {
		return fixedProductMap;
	}

	public void addFixedProductMapEntry(FixedProductDescription fixedProduct, Product correspondingProduct){
		//fooLog.info("fixedProduct is {}, correspondingProduct is {}", fixedProduct, correspondingProduct);
		fixedProductMap.put(fixedProduct, correspondingProduct);
	}

	public void addProduct(Product productToAdd){
		products.add(productToAdd);
	}

	public void activateProduct(Product productToActivate){
		productToActivate.setIntroducedToMarket(true);
		/*for(ConsumerAgent consumerAgent : consumerAgents){
			//TODO not necessarily make consumer aware!!!
			if((consumerAgent.getCorrespondingConsumerAgentGroup().getProductGroupAwarenessDistribution().get(productToActivate.getPartOfProductGroup()).draw() > Math.random()) && !(consumerAgent.isAwareOfProduct(productToActivate))) consumerAgent.makeAwareOfProduct(productToActivate);
		}*/
	}

	public void removeProduct(Product productToBeRemoved){
		productToBeRemoved.setIntroducedToMarket(false);
		products.remove(productToBeRemoved);
		historicalProducts.add(productToBeRemoved);
		fooLog.debug("removing product {} from simulation container. ARS is {}",productToBeRemoved,getSimulationConfiguration().getProcessModel().getAdoptionReplacementScheme());
		getSimulationConfiguration().getProcessModel().getAdoptionReplacementScheme().removeProductFromAgents(this, productToBeRemoved);
	}

	public Set<Product> getHistoricalProducts() {
		return historicalProducts;
	}

	public Set<CompanyAgent> getCompanyAgents() {
		return companyAgents;
	}

	public void addCompanyAgent(CompanyAgent agentToAdd){
		companyAgents.add(agentToAdd);
	}

	public void setPolicyAgent(PolicyAgent policyAgent) throws IllegalStateException{
		if(this.policyAgent != null) throw new IllegalStateException("Policy agent for the simulation already set!! ("+this.policyAgent+")!");
		else this.policyAgent = policyAgent;
	}

	public Set<Need> getNeedsInSimulation() {
		return needsInSimulation;
	}


}