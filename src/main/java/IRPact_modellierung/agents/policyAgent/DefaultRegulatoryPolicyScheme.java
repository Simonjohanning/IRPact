package IRPact_modellierung.agents.policyAgent;

import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.HashSet;
import java.util.Set;

/**
 * Since in most models analyzed for deriving the specifications for IRPact the policy agent is entirely passive
 * (or non-existent), the DefaultRegulatoryPolicyScheme is a RegulatoryPolicyScheme
 * in which the policy agent doesn't act (i.e. has an evaluateRegulatoryPolicy which returns the empty set)
 *
 * It serves to provide an implementation of a RegulatoryPolicyScheme so that
 * instantiation of a PolicyAgent is possible
 *
 * @author Simon Johanning
 */
public class DefaultRegulatoryPolicyScheme extends RegulatoryPolicyScheme{


    /**
     * The evaluateRegulatoryPolicy method of this RegulatoryPolicyScheme
     * returns an empty set, since in most models the policy agent doesn't evaluate regulatory policies
     *
     * @param simulationContainer The contained the simulation takes place in
     */
    public Set<Product> evaluateRegulatoryPolicy(SimulationContainer simulationContainer) {
        return new HashSet<Product>();
    }
}



