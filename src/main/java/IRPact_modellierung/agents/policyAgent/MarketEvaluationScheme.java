package IRPact_modellierung.agents.policyAgent;

import IRPact_modellierung.simulation.SimulationContainer;

/**
 * The market evaluation scheme describes the perceptive end of the policy agent.
 * It describes how the policy agent derives information about the market and its actors for other activities,
 * and can be used as auxiliary scheme for other schemes.
 * It allows for imperfect knowledge and internal political mechanisms,
 * as well as for models with perfect transparency.
 *
 * @author Simon Johanning
 */
public abstract class MarketEvaluationScheme {

    /**
     * Method to formalize the market evaluation process of the policy agent.
     * It can be used to derive perceptions about the market from the policy agents perspective.
     *
     * @param simulationContainer The simulation container whose market is to be evaluated
     * @return A string containing the derived market information
     */
    public abstract String evaluateMarket(SimulationContainer simulationContainer);
    //TODO think of good data structure for market evaluation
}
