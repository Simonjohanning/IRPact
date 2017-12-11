package IRPact_modellierung.processModel;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;

/**
 * An AdoptionReplacementScheme models the process of replacing an adopted product that was discontinued
 * on the market. It describes how ConsumerAgents respond to discontinuations and whether / how quickly
 * they replace these products.
 *
 * @author Simon Johanning
 */
public abstract class AdoptionReplacementScheme {

    /**
     * This method describes how ConsumerAgents behave when they
     * readopt a product (or how they choose a similar product,
     * one that satisfies the needs).
     * It is triggered by the process model and can be considered part of it.
     *
     * @param consumerAgentConcerned The ConsumerAgent readopting
     * @param productConcerned The Product being replaced, causing the readoption
     * @param simulationTime The current time of the simulation
     */
    public abstract void readopt(ConsumerAgent consumerAgentConcerned, Product productConcerned, double simulationTime);

    /**
     * Method to describe what happens when a product gets 'removed' from the agents.
     * Losing an adopted product might cause agents to readopt at some point, or to seek information
     * or act in any other way. This is specified by this method.
     *
     * @param simulationContainer The container the simulation runs in
     * @param productToRemove The product that is being removed from the agent
     */
    public abstract void removeProductFromAgents(SimulationContainer simulationContainer, Product productToRemove);

}
