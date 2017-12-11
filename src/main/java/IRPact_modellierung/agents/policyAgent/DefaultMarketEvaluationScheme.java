package IRPact_modellierung.agents.policyAgent;

import IRPact_modellierung.simulation.SimulationContainer;

/**
 * Since in most models analyzed for deriving the specifications for IRPact the policy agent is entirely passive
 * (or non-existent), the DefaultMarketEvaluationScheme is a MarketEvaluationScheme
 * in which the policy agent doesn't act (i.e. has an empty evaluateMarket method).
 *
 * It serves to provide an implementation of a MarketEvaluationScheme so that
 * instantiation of a PolicyAgent is possible
 *
 * @author Simon Johanning
 */
public class DefaultMarketEvaluationScheme extends MarketEvaluationScheme {

    /**
     * The evaluateMarket method of this MarketEvaluationScheme
     * returns the empty string, since in most models the policy agent doesn't evaluate the market
     *
     * @param simulationContainer The contained the simulation takes place in
     */
    public String evaluateMarket(SimulationContainer simulationContainer) {
        return "";
    }
}

