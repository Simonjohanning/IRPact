package IRPact_modellierung.agents.consumerAgents;

import IRPact_modellierung.perception.ProductAttributePerceptionScheme;
import IRPact_modellierung.decision.ConsumerAgentAdoptionDecisionProcess;
import IRPact_modellierung.network.SNConsumerNode;
import IRPact_modellierung.preference.Preference;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.SimulationContainer;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Set;

/**
 * A SynchronousConsumerAgent is an implementation of a Consumer agent that acts synchronously (centrally coordinated in determined order 'at the same time' in the same thread)
 * This class is an implementation and doesn't add any more semantics to the agents.
 *
 * @author Simon Johanning
 */
public class SynchronousConsumerAgent extends ConsumerAgent {

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
    public SynchronousConsumerAgent(SimulationContainer simulationContainer, Set<ConsumerAgentAttribute> attributes, Set<Preference> preferences, SNConsumerNode correspondingNodeInSN, Set<Product> adoptedProducts, Map<ProductAttribute, ProductAttributePerceptionScheme> perceivedProductAttributeValues, Map<Product, Boolean> productAwarenessMap, ConsumerAgentGroup correspondingConsumerAgentGroup, ConsumerAgentAdoptionDecisionProcess decisionProcessEmployed, Point2D spatialPosition, String agentID) {
        super(simulationContainer, attributes, preferences, correspondingNodeInSN, adoptedProducts, perceivedProductAttributeValues, productAwarenessMap, correspondingConsumerAgentGroup, decisionProcessEmployed, spatialPosition, agentID);
    }
}