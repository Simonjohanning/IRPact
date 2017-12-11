package IRPact_modellierung.agents.policyAgent;

import IRPact_modellierung.simulation.SimulationContainer;

/**
 * A ProductPolicyScheme represents the policies that lawmakers and corporate agents employ for controlling
 the quality of products. Through these policy schemes, product attributes values are
 manipulated, often in a coupled manner.
 *
 * @author Simon Johanning
 */
public abstract class ProductPolicyScheme {

    /**
     * Invoking this method formalizes the processes of devising and implementing product policies.
     * These range from policy initiative via discussion and ratification of these within the policy sphere
     * to the implementation of these policies by company actors.
     * These processes are generally triggered by the process model, but can also be invoked by other processes.
     *
     * @param simulationContainer The container the simulation takes place in
     */
    public abstract void implementProductPolicy(SimulationContainer simulationContainer);
}
