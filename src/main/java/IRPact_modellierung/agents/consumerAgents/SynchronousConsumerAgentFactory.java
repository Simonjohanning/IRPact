package IRPact_modellierung.agents.consumerAgents;

import java.util.*;

import IRPact_modellierung.perception.PerceptionSchemeFactory;
import IRPact_modellierung.perception.ProductAttributePerceptionHistogram;
import IRPact_modellierung.perception.ProductAttributePerceptionScheme;
import IRPact_modellierung.perception.TrueValueProductAttributePerception;
import IRPact_modellierung.distributions.UnivariateDistribution;
import IRPact_modellierung.helper.FilterHelper;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.helper.ValueConversionHelper;
import IRPact_modellierung.network.SNConsumerNode;
import IRPact_modellierung.preference.Preference;
import IRPact_modellierung.preference.Value;
import IRPact_modellierung.products.FixedProductDescription;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.Configuration;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;

/**
 * Factory class to instantiate synchronous consumer agents from a given configuration.
 * Sets of consumer agents are instantiated given their ConsumerAgent group and the configuration of the simulation
 * and are linked to other simulation entities.
 *
 * @author Simon Johanning
 */
public class SynchronousConsumerAgentFactory {

	private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	private static int agentBatchNumber = 0;
	//TODO think whether to join with the ASyncCAFactory in a CAFactory
	//TODO implement post-SNcreation agent adding
	/**
	 * creates a set of synchronous consumer agents and links them other entities of the simulations.
	 * Requires the products of the simulation to be created in the simulation already!
	 * if a social network is already associated with the simulation container, the agents will be added as nodes.
	 *
	 * @param correspondingConsumerAgentGroup The ConsumerAgentGroup corresponding to the consumer agents to be created
	 * @param numberOfConsumersToCreate The number of synchronous ConsumerAgents that are to be created in this batch.
	 * @param configuration The configuration of the simulation
	 * @param simulationContainer The simulationContainer containing all entities the consumerAgent refers to. Needs to include the products of the simulation!
	 *
	 * @return batch of SynchronousConsumerAgent that are linked to other entities of the simulation with corresponding batch number in the ID
	 * @throws UnsupportedOperationException Will be thrown when the social network is set already (i.e. nodes need to be added to an existing social graph, which is not yet supported).
	 */
	public static Set<SynchronousConsumerAgent> createConsumers(ConsumerAgentGroup correspondingConsumerAgentGroup, int numberOfConsumersToCreate, SimulationContainer simulationContainer, Configuration configuration) throws UnsupportedOperationException{
		if(simulationContainer.getSocialNetwork() == null) return createConsumerAgents(correspondingConsumerAgentGroup, numberOfConsumersToCreate, simulationContainer, configuration);
		else throw new UnsupportedOperationException("Adding nodes to an existing social network is currently not supported since some networks don't support it yet.");
//			{
//			Set<SynchronousConsumerAgent> agentsToCreate = createConsumerAgents(correspondingConsumerAgentGroup, numberOfConsumersToCreate, simulationContainer, configuration);
//			//in addition to creating the consumer agents, they have to be embedded in the social network (since its not the responsibility of the creator of the SN)
//			HashSet<SNNode> agentNodes = new HashSet<SNNode>();
//			for(SynchronousConsumerAgent agent : agentsToCreate) {
//				agentNodes.add(agent.getCorrespondingNodeInSN());
//			}
//			simulationContainer.getSocialNetwork().getSocialGraph().addNodes(configuration.getSNConfiguration(), agentNodes);
//			return agentsToCreate;
//		}
	}

	/**
	 * Function creating the consumer agents without taking care they are added to the social network.
	 *
	 * @param correspondingConsumerAgentGroup The ConsumerAgentGroup corresponding to the consumer agents to be created
	 * @param numberOfConsumersToCreate The number of synchronous ConsumerAgents that are to be created in this batch.
	 * @param configuration The configuration of the simulation
	 * @param simulationContainer The simulationContainer containing all entities the consumerAgent refers to. Needs to include the products of the simulation!
	 * @return batch of SynchronousConsumerAgent that are linked to other entities of the simulation with corresponding batch number in the ID
	 */
	private static Set<SynchronousConsumerAgent> createConsumerAgents(ConsumerAgentGroup correspondingConsumerAgentGroup, int numberOfConsumersToCreate, SimulationContainer simulationContainer, Configuration configuration) {
		//Set to contain agents to create
		HashSet<SynchronousConsumerAgent> agents = new HashSet<SynchronousConsumerAgent>();
		//set initiallyAdoptedProducts map to use for initially adopted products
		Map<FixedProductDescription, UnivariateDistribution> initialProductConfiguration = correspondingConsumerAgentGroup.getInitialProductConfiguration();
		Map<Integer, Set<FixedProductDescription>> initiallyAdoptedFPDs = LazynessHelper.determineInitiallyAdoptedProducts(numberOfConsumersToCreate, initialProductConfiguration);
		Map<Integer, Set<Product>> initiallyAdoptedProducts = ValueConversionHelper.IntegerFPDSetMapToIntegerProductSetMap(initiallyAdoptedFPDs, simulationContainer);
		//create numberOfConsumersToCreate agents individually
		for (int index = 0; index < numberOfConsumersToCreate; index++) {
			//setup the agent attributes...
			HashSet<ConsumerAgentAttribute> attributes = new HashSet<ConsumerAgentAttribute>(correspondingConsumerAgentGroup.getConsumerAgentGroupAttributes().size());
			//...as realizations of ConsumerAgentGroupAttributes
			for (ConsumerAgentGroupAttribute caga : correspondingConsumerAgentGroup.getConsumerAgentGroupAttributes()) {
				attributes.add(new ConsumerAgentAttribute(caga.getName(), caga.getValue().draw()));
			}
			//setup the agents preferences as realizations of consumerAgentGroupValues
			HashSet<Preference> consumerPreferences = new HashSet<Preference>(correspondingConsumerAgentGroup.getConsumerAgentGroupPreferences().keySet().size());
			for (Value value : correspondingConsumerAgentGroup.getConsumerAgentGroupPreferences().keySet()) {
				consumerPreferences.add(new Preference(value, correspondingConsumerAgentGroup.getConsumerAgentGroupPreferences().get(value).draw()));
			}
			//setup the reference to the node in the social network.
			//Node will (at least should) be embedded in the SN
			SNConsumerNode associatedSNNode = new SNConsumerNode("SynchronousConsumerAgent_" + Integer.toString(agentBatchNumber) + "_" + Integer.toString(index));
			//set the product awareness for each product
			Set<Product> productsInSimulation = simulationContainer.getProducts();
			HashMap<Product, Boolean> productAwarenessMap = new HashMap<Product, Boolean>(productsInSimulation.size());
			for (Product product : productsInSimulation) {
				productAwarenessMap.put(product, ValueConversionHelper.signToBoolean(correspondingConsumerAgentGroup.getProductGroupAwarenessDistribution().get(product.getPartOfProductGroup()).draw()));
			}
			Set<Product> productsAwareOf = FilterHelper.selectKnownProducts(productAwarenessMap);
			fooLog.debug("Agent is aware of {} products",productsAwareOf.size());
			HashMap<ProductAttribute, ProductAttributePerceptionScheme> perceivedProductAttributeValues = new HashMap<ProductAttribute, ProductAttributePerceptionScheme>();
			//set papHistogram for all products the consumer is aware of
			for(Product awaredProduct : productsAwareOf){
				//set up the productAttribute perception histogram for each product attribute
				for(ProductAttribute attribute : awaredProduct.getProductAttributes()){
					ProductAttributePerceptionScheme perceptionScheme = PerceptionSchemeFactory.createPerceptionScheme(attribute, correspondingConsumerAgentGroup.getProductPerceptionSchemes().get(attribute.getCorrespondingProductGroupAttribute()), correspondingConsumerAgentGroup, configuration);
					perceivedProductAttributeValues.put(attribute, perceptionScheme);
				}
			}
			//set agents id as consumergroup_index
			String agentID = correspondingConsumerAgentGroup.getGroupName()+"_agent_"+agentBatchNumber+"_"+String.valueOf(index);
			//instantiate agent based on previously prepared information
			//fooLog.info("simulationContainer: {} \n, attributes: {} \n, preferences: {} \n, associatedSNNode: {} \n, initiallyAdoptedProducts: {} \n, perceivedProductAttributeValues: {} \n, productAwarenessMap: {} \n, correspondingConsumerAgentGroup : {} \n, dec process: {} \n, spac. distr. : {} \n, ID: {} \n ", simulationContainer, attributes, consumerPreferences, associatedSNNode, initiallyAdoptedProducts.get(index), perceivedProductAttributeValues, productAwarenessMap, correspondingConsumerAgentGroup, correspondingConsumerAgentGroup.getDecisionProcessEmployed(), correspondingConsumerAgentGroup.getSpatialDistribution().draw(simulationContainer.getSpatialModel()), agentID);
			fooLog.debug("simulationContainer {} ",simulationContainer);
			fooLog.debug("attr {} ",attributes);
			fooLog.debug("pref: {} ",consumerPreferences);
			fooLog.debug("snNode: {}", associatedSNNode);
			fooLog.debug("adop prod: {}",initiallyAdoptedProducts.get(index));
			fooLog.debug("percPAVs: {}",perceivedProductAttributeValues);
			fooLog.debug("paw: {}", productAwarenessMap);
			fooLog.debug("cag: {}",correspondingConsumerAgentGroup);
			fooLog.debug("dec. proc. : {}",correspondingConsumerAgentGroup.getDecisionProcessEmployed());
			fooLog.debug("spact: {}",correspondingConsumerAgentGroup.getSpatialDistribution().draw(simulationContainer.getSpatialModel()));
			fooLog.debug("id: {}",agentID);
			SynchronousConsumerAgent instantiatedAgent = new SynchronousConsumerAgent(simulationContainer, attributes, consumerPreferences, associatedSNNode, initiallyAdoptedProducts.get(index), perceivedProductAttributeValues, productAwarenessMap, correspondingConsumerAgentGroup, correspondingConsumerAgentGroup.getDecisionProcessEmployed(), correspondingConsumerAgentGroup.getSpatialDistribution().draw(simulationContainer.getSpatialModel()), agentID);
			simulationContainer.addSNNodeConsumerAgentMapping(associatedSNNode, instantiatedAgent);
			agents.add(instantiatedAgent);
		}
		agentBatchNumber++;
		return agents;
	}
}