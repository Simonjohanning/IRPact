package IRPact_modellierung.agents.policyAgent;

import IRPact_modellierung.simulation.SimulationContainer;

/**
 * Since in most models analyzed for deriving the specifications for IRPact the policy agent is entirely passive
 * (or non-existent), the DefaultProductPolicyScheme is a ProductPolicyScheme
 * in which the policy agent doesn't act (i.e. has an empty implementProductPolicy method).
 *
 * It serves to provide an implementation of a ProductPolicyScheme so that
 * instantiation of a PolicyAgent is possible
 *
 * @author Simon Johanning
 */
public class DefaultProductPolicyScheme extends ProductPolicyScheme {

    /**
     * The implementProductPolicy method of this ProductPolicyScheme
     * is empty, since in most models the policy agent doesn't implement any product policies
     *
     * @param simulationContainer The contained the simulation takes place in
     */
    public void implementProductPolicy(SimulationContainer simulationContainer) {

    }
}
