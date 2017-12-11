package IRPact_modellierung.decision;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;
import antlr.SemanticException;

import java.util.Set;

/**
 * Abstraction to represent an adoption decision process executed by a ConsumerAgent.
 * This process describes the decision finding process of the agent between a number of alternatives,
 * based on the agents' state and 'cognitive makeup', and gives the one Product the agent would like to adopt
 * (between the choices given)
 * It further provides a mechanism to compare one product to a range of other products.
 *
 * @author Simon Johanning
 */
public abstract class ConsumerAgentAdoptionDecisionProcess extends DecisionMakingProcess {

    /**
     * Method to describe the process to chose one Product out of a range
     * of alternatives, based on the consumerAgents' status and 'cognitive makeup'.
     * Should generally chose the 'best' product (i.e. where betterProductAvailable evaluates to false with the same state),
     * although that is not always the case, as for example in stochastic processes.
     * The product chosen will then be adopted by the ConsumerAgent
     *
     * @param simulationContainer The container the simulation takes place in
     * @param consumerAgent The ConsumerAgent making the decision
     * @param potentialProducts The range of alternatives for the adoption decision
     * @param systemTime The time the decision is taken
     * @return The product the ConsumerAgent decides to adopt
     * @throws SemanticException will be thrown when something occurs that violates the semantics of the process
     */
    public abstract Product makeProductAdoptionDecision(SimulationContainer simulationContainer, ConsumerAgent consumerAgent, Set<Product> potentialProducts, double systemTime) throws SemanticException;

    /**
     * Method to compare the quality of a product against the quality of a collection of other products,
     * within the same evaluative scheme.
     * If the process is stochastic, it doesn't have to return the same value for the same arguments
     *
     * @param simulationContainer The container the simulation takes place in
     * @param consumerAgent The ConsumerAgent making the decision
     * @param productToCompare The product to compare against the other products
     * @param potentialProducts The range of alternatives for the adoption decision to compare productToCompare to
     * @param systemTime The time the decision is taken
     * @return True when a product in potentialProduct is preferred by the consumerAgent, false when productToCompare is better than any of the alternatives
     * @throws SemanticException will be thrown when something occurs that violates the semantics of the process
     */
    public abstract boolean betterProductAvailable(SimulationContainer simulationContainer, ConsumerAgent consumerAgent, Product productToCompare, Set<Product> potentialProducts, double systemTime) throws SemanticException;

}