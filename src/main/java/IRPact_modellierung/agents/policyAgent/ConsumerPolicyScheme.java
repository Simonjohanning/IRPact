package IRPact_modellierung.agents.policyAgent;

import IRPact_modellierung.simulation.SimulationContainer;

/**
 * A ConsumerPolicyScheme describes what policies are used by the policy agent(s) for influencing consumer agents.
 * These represent campaigns, laws and incentives to influence consumer attitudes and behaviors.
 * Generally they affect perceptions and preferences of consumer agents, but are not necessarily limited to this.
 *
 * @author Simon Johanning
 */
public abstract class ConsumerPolicyScheme {

    /**
     * This method invokes the processes of devising and implementing consumer-directed policies.
     * These range from policy initiative via discussion and ratification of these within the policy sphere
     * to processing their effects on consumer agents.
     * These processes are generally triggered by the process model, but can also be invoked by other processes.
     *
     * @param simulationContainer The container the simulation takes place in
     */
    public abstract void implementConsumerPolicy(SimulationContainer simulationContainer);
}
