package IRPact_modellierung.decision;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO write documentation

public abstract class UtilitarianConsumerAgentAdoptionDecisionProcess extends ConsumerAgentAdoptionDecisionProcess {

    private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    protected UtilityFunction associatedUtilityFunction;

    public UtilitarianConsumerAgentAdoptionDecisionProcess(UtilityFunction associatedUtilityFunction) {
        fooLog.debug("creating a UtilitarianConsumerAgentAdoptionDecisionProcess with utility function {}",associatedUtilityFunction);
        this.associatedUtilityFunction = associatedUtilityFunction;
    }

    public UtilityFunction getUtilityFunctionUsed() {
        return associatedUtilityFunction;
    }

    protected abstract double calculateUtility(SimulationContainer simulationContainer, Product product, ConsumerAgent consumerAgent, double systemTime);
}