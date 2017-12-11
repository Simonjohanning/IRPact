package IRPact_modellierung.information;

import IRPact_modellierung.agents.Agent;
import IRPact_modellierung.agents.InformationAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.events.InformationEvent;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.HashSet;
import java.util.Set;

/**
 * (Dummy) information scheme that represents an 'informational black hole'.
 * No information is exchanged in this scheme, and it is suited for models that ignore information
 *
 * @author Simon Johanning
 */
public class NoInformationScheme extends InformationScheme{

    public NoInformationScheme() {
    }

    /**
     * No information provided is of relevance in this scheme, so this is basically a NOP.
     *
     * @param contributingAgent The agent providing information to the information scheme
     * @param contributedInformation The information that is provided to the information scheme
     * @param systemTime The time the information is provided to the system
     * @throws IllegalArgumentException
     */
    public void provideInformation(InformationAgent contributingAgent, Information contributedInformation, double systemTime) throws IllegalArgumentException {

    }

    /**
     * Since no information is of relevant, all product information derived is empty (i.e. no InformationEvents)
     *
     * @param informationRequestingAgent The agent that requests information about a product quality
     * @param productAttributeOfInterest The quality which an agent requests information about
     * @param simulationContainer The state of the simulation at the time of inquiry
     * @return Since no information is regarded in this scheme, the set of information events will always be empty
     */
    public Set<InformationEvent> deriveProductInformation(ConsumerAgent informationRequestingAgent, ProductAttribute productAttributeOfInterest, SimulationContainer simulationContainer) {
        return new HashSet<>();
    }
}
