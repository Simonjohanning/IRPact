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
 * The PureInformatioScheme is an information scheme where no distortion of information is assumed,
 * and all information corresponds to the true nature of things.
 * It models perfect information of the ConsumerAgents (as long as they request information),
 * and no mechanisms of subjective (or malicious) information.
 * As such no information entering this scheme is of relevance and ignored.
 *
 * @author Simon Johanning
 */
public class PureInformationScheme extends InformationScheme{

    private InformationAgent allKnowingEye;
    private boolean allKnowingEyeInstantiated;

    /**
     * In the PureInformationScheme all information stems from the allKnowingEye,
     * an InformationAgent with unlimited (maximal) information authority
     *
     * @param simulationContainer The container the simulation runs in
     */
    public PureInformationScheme(SimulationContainer simulationContainer) {
        this.allKnowingEye = new InformationAgent(simulationContainer, Double.MAX_VALUE);
        this.allKnowingEyeInstantiated = true;
    }

    /**
     * Constructor that doesn't instantiate the allKnowingEye.
     * This has to be done later in the simulation when the simulationContainer exists
     *
     */
    public PureInformationScheme() {
        this.allKnowingEyeInstantiated = false;
    }

    /**
     * Since information is 'pure' (and thus everything is known anyways),
     * providing information to this scheme is of no consequence.
     *
     * @param contributingAgent The agent providing information to the information scheme
     * @param contributedInformation The information that is provided to the information scheme
     * @param systemTime The time the information is provided to the system
     * @throws IllegalArgumentException
     */
    public void provideInformation(InformationAgent contributingAgent, Information contributedInformation, double systemTime) throws IllegalArgumentException {
        if(!allKnowingEyeInstantiated){
            this.allKnowingEye = new InformationAgent(contributingAgent.getAssociatedSimulationContainer(), Double.MAX_VALUE);
            allKnowingEyeInstantiated = true;
        }
    }

    /**
     * Since this scheme models perfect information, requesting information immediately gives
     * the true value of all product attributes of the simulation.
     *
     * @param informationRequestingAgent The agent that requests information about a product quality
     * @param productAttributeOfInterest The quality which an agent requests information about
     * @param simulationContainer The state of the simulation at the time of inquiry
     * @return An event that transmits the true value of the ProductAttribute immediately
     */
    public Set<InformationEvent> deriveProductInformation(ConsumerAgent informationRequestingAgent, ProductAttribute productAttributeOfInterest, SimulationContainer simulationContainer) {
        if(!allKnowingEyeInstantiated){
            this.allKnowingEye = new InformationAgent(informationRequestingAgent.getAssociatedSimulationContainer(), Double.MAX_VALUE);
            allKnowingEyeInstantiated = true;
        }
        Set<InformationEvent> returnSet = new HashSet<>();
        returnSet.add(new InformationEvent(simulationContainer, simulationContainer.getTimeModel().getSimulationTime(), new ProductAttributeInformation(allKnowingEye, productAttributeOfInterest.getValue(), productAttributeOfInterest),informationRequestingAgent));
        return returnSet;
    }
}
