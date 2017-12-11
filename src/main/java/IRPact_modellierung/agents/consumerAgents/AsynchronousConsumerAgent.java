package IRPact_modellierung.agents.consumerAgents;

import IRPact_modellierung.perception.ProductAttributePerceptionScheme;
import IRPact_modellierung.decision.ConsumerAgentAdoptionDecisionProcess;
import IRPact_modellierung.network.SNConsumerNode;
import IRPact_modellierung.preference.Preference;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.SimulationContainer;
import jadex.bridge.IExternalAccess;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Set;

/**
 * A ConsumerAgent written for asynchronous processing within the simulation, based on the jadex framework
 * !!!CURRENTLY A STUB AND NOT USED!!!
 *
 * @author Simon Johanning
 */
public class AsynchronousConsumerAgent extends ConsumerAgent {

	//reference to the agent within the jadex world
	private IExternalAccess jadexHandle;

	/**
	 * The AsynchronousConsumerAgent is basically a ConsumerAgent with a jadex handle to interact with the environment in the Jadex context
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
	 * @param jadexHandle The object corresponding to the agent within the jadex environment (state changes need to be propagated to the 'Jadex world')
	 */
	public AsynchronousConsumerAgent(SimulationContainer simulationContainer, Set<ConsumerAgentAttribute> attributes, Set<Preference> preferences, SNConsumerNode correspondingNodeInSN, Set<Product> adoptedProducts, Map<ProductAttribute, ProductAttributePerceptionScheme> perceivedProductAttributeValues, Map<Product, Boolean> productAwarenessMap, ConsumerAgentGroup correspondingConsumerAgentGroup, ConsumerAgentAdoptionDecisionProcess decisionProcessEmployed, Point2D spatialPosition, IExternalAccess jadexHandle, String agentID) {
		super(simulationContainer, attributes, preferences, correspondingNodeInSN, adoptedProducts, perceivedProductAttributeValues, productAwarenessMap, correspondingConsumerAgentGroup, decisionProcessEmployed, spatialPosition, agentID);
		this.jadexHandle = jadexHandle;
	}

	public IExternalAccess getJadexHandle() {
		return jadexHandle;
	}
}