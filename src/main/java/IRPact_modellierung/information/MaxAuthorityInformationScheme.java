package IRPact_modellierung.information;

import IRPact_modellierung.agents.Agent;
import IRPact_modellierung.agents.InformationAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.events.InformationEvent;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.HashSet;
import java.util.Set;

/**
 * The MaxAuthorityInformationScheme is an InformationScheme that collects only information
 * from the most trustworthy (maximal authority) agents
 * and chooses a random piece of information when asked.
 *
 * @author Simon Johanning
 */
public class MaxAuthorityInformationScheme extends InformationScheme {

    private Set<Information> receivedInformation;
    private Set<ProductAttributeInformation> productAttributeInformationReceived;
    private double encounteredMaximalAuthority;

    /**
     * A MaxAuthorityInformationScheme collects all information in (initially) empty sets.
     * The ProductAttributeInformation set contains all information of type ProductAttributeInformation received within this scheme
     */
    public MaxAuthorityInformationScheme() {
        this.receivedInformation = new HashSet<>();
        this.productAttributeInformationReceived = new HashSet<>();
        encounteredMaximalAuthority = -Double.MAX_VALUE;
    }

    /**
     * The MaxAuthorityInformationScheme only regards the most trustworthy information
     *
     * @param contributingAgent The agent providing information to the information scheme
     * @param contributedInformation The information that is provided to the information scheme
     * @param systemTime The time the information is provided to the system
     * @throws IllegalArgumentException
     */
    public void provideInformation(InformationAgent contributingAgent, Information contributedInformation, double systemTime) throws IllegalArgumentException {
        //if the current agent is the most authoritative agent ever encountered, don't care about former information (if ProductAttributeInformation, otherwise only for the information set)
        if(contributingAgent.getInformationAuthority() > encounteredMaximalAuthority){
            receivedInformation = new HashSet<>();
            if(contributedInformation.getClass().equals(ProductAttributeInformation.class)) productAttributeInformationReceived = new HashSet<>();
            encounteredMaximalAuthority = contributingAgent.getInformationAuthority();
        }
        //if information is deemed worthy, add it
        if(contributingAgent.getInformationAuthority() == encounteredMaximalAuthority) {
            if (contributedInformation.getClass().equals(ProductAttributeInformation.class))
                productAttributeInformationReceived.add((ProductAttributeInformation) contributedInformation);
            receivedInformation.add(contributedInformation);
        }
    }

    /**
     * deriveProductInformation in the RandomInformationScheme will provide a random piece of ProductAttributeInformation
     * ever provided to the information scheme. If none exists an exception will be thrown
     *
     * @param informationRequestingAgent The agent that requests information about a product quality
     * @param productAttributeOfInterest The quality which an agent requests information about
     * @param simulationContainer The state of the simulation at the time of inquiry
     * @return
     */
    public Set<InformationEvent> deriveProductInformation(ConsumerAgent informationRequestingAgent, ProductAttribute productAttributeOfInterest, SimulationContainer simulationContainer) throws IllegalStateException{
        Set<InformationEvent> returnSet = new HashSet<>();
        if(productAttributeInformationReceived.isEmpty()) throw new IllegalStateException("Set holding the ProductAttributeInformation is empty; Please make sure the scheme is in a valid state before calling this method.");
        ProductAttributeInformation respectivePieceOfInformation = LazynessHelper.chooseRandomProductAttributeInformation(productAttributeInformationReceived);
        returnSet.add(new InformationEvent(simulationContainer, simulationContainer.getTimeModel().getSimulationTime(), respectivePieceOfInformation, informationRequestingAgent));
        return returnSet;
    }

    /**
     * Method that will check that the information scheme is in a valid state (i.e. deriveProductInformation will not throw an error).
     * This is the case when there is a ProductAttributeInformation to choose from.
     *
     * @return True when a ProductAttributeInformation exists, false otherwise
     */
    public boolean isValid(){
        return !productAttributeInformationReceived.isEmpty();
    }
}
