package IRPact_modellierung.processModel;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;

/**
 * 'Empty' AdoptionReplacementScheme that represents the lack of readoption.
 * Discontinued products will not trigger any behavior of the agents and
 * they will just forget about the product
 *
 */
public class NoAdoptionReplacementScheme extends AdoptionReplacementScheme {

    public void readopt(ConsumerAgent consumerAgentConcerned, Product productConcerned, double simulationTime) {
        //do nothing
    }

    public void removeProductFromAgents(SimulationContainer simulationContainer, Product productToRemove) {
        for(ConsumerAgent currentAgent : simulationContainer.getConsumerAgents()){
            currentAgent.forgetProduct(productToRemove);
        }
    }
}
