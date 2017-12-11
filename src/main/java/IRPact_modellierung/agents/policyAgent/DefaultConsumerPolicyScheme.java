package IRPact_modellierung.agents.policyAgent;

import IRPact_modellierung.simulation.SimulationContainer;

/**
 * Since in most models analyzed for deriving the specifications for IRPact the policy agent is entirely passive
 * (or non-existent), the DefaultConsumerPolicyScheme is a ConsumerPolicyScheme
 * in which the policy agent doesn't act (i.e. has an empty implementConsumerPolicy method).
 *
 * It serves to provide an implementation of a ConsumerPolicyScheme so that
 * instantiation of a PolicyAgent is possible
 *
 * @author Simon Johanning
 */
public class DefaultConsumerPolicyScheme extends ConsumerPolicyScheme{

    /**
     * The implementConsumerPolicy method of this ConsumerPolicyScheme
     * is empty, since in most models the policy agent doesn't implement consumer policies
     *
     * @param simulationContainer The contained the simulation takes place in
     */
     public void implementConsumerPolicy(SimulationContainer simulationContainer) {

    }
}


