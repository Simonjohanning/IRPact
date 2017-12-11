package IRPact_modellierung.information;

import IRPact_modellierung.agents.Agent;
import IRPact_modellierung.agents.InformationAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.events.InformationEvent;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.Set;

/**
 * An InformationScheme describes how the process of information search by ConsumerAgents is done,
 * and how other agents interact with the information ecosystem.
 *
 * InformationSchemes determine these mechanisms by implementing the methods provideInformation,
 * where all agents can contribute information to the information ecosystem
 * (and which is to change the status of the information scheme), and deriveProductInformation
 * which is used by ConsumerAgents to derive information events about a product of their interest.
 *
 * @author Simon Johanning
 */
public abstract class InformationScheme {

    /**
     * Method to provide information of some kind to the simulation.
     * Information schemes should implement specific methods to deal with specific kinds of information
     * and contributing agents, and this method should be used primarily to determine how these are accessed.
     *
     * @param contributingAgent The agent providing information to the information scheme
     * @param contributedInformation The information that is provided to the information scheme
     * @param systemTime The time the information is provided to the system
     * @throws IllegalArgumentException Will be thrown when any problems occur with the arguments
     */
    public abstract void provideInformation(InformationAgent contributingAgent, Information contributedInformation, double systemTime) throws IllegalArgumentException;

    /**
     * Method to describe how ConsumerAgents derive information about products from the information ecosystem.
     *
     * Will create a number of InformationEvents that represent these information.
     *
     * @param informationRequestingAgent The agent that requests information about a product quality
     * @param productAttributeOfInterest The quality which an agent requests information about
     * @param simulationContainer The state of the simulation at the time of inquiry
     * @return The InformationEvents describing how/when the information requested is processed by the agent
     */
    public abstract Set<InformationEvent> deriveProductInformation(ConsumerAgent informationRequestingAgent, ProductAttribute productAttributeOfInterest, SimulationContainer simulationContainer);
}
