package IRPact_modellierung.agents.companyAgents.advertisement;

import IRPact_modellierung.agents.companyAgents.CompanyAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.events.CommunicationEvent;
import IRPact_modellierung.messaging.CompanyConsumerPerceptionManipulationMessage;
import IRPact_modellierung.messaging.UniformMessageScheme;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductAttribute;

/**
 * The UniformAdvertisementScheme is an AdvertisementScheme that uniformously creates CompanyConsumerProductAttributeManipulationMessages,
 * that are directed to all ConsumerAgents in the simulation and regard all ProductAttributes of all Products the CompanyAgent manages.
 * The messages communicate the true value of the product attribute and are scheduled for one time unit after the scheduling of the messages.
 */
public class UniformAdvertisementScheme extends AdvertisementScheme {

    public UniformAdvertisementScheme() {
        super(new UniformMessageScheme());
    }

    /**
     * With this advertisement method, the company agent advertises all aspects of all products of their portfolio
     * to any consumer agent with the true product value in the next time step.
     *
     * @param correspondingAgent The company agent managing the products in its portfolio
     * @throws IllegalArgumentException Will be thrown when a process in the message or event scheduling throws an IllegalArgumentException themselves.
     */
    public void advertiseProductPortfolio(CompanyAgent correspondingAgent) throws IllegalArgumentException {
        try {
            for (Product currentProduct : correspondingAgent.getProductPortfolio()) {
                for (ProductAttribute currentProductAttribute : currentProduct.getProductAttributes()) {
                    for (ConsumerAgent currentAdvertisementTarget : correspondingAgent.getAssociatedSimulationContainer().getConsumerAgents()) {
                        CompanyConsumerPerceptionManipulationMessage advertisementMessage = new CompanyConsumerPerceptionManipulationMessage(correspondingAgent, currentAdvertisementTarget, currentProductAttribute, currentProduct, currentProductAttribute.getValue(), 1.0);
                        correspondingAgent.getAssociatedSimulationContainer().getEventScheduler().scheduleEvent(new CommunicationEvent(advertisementMessage, correspondingAgent.getAssociatedSimulationContainer().getTimeModel().getSimulationTime() + 1.0, correspondingAgent.getAssociatedSimulationContainer()));
                    }
                }
            }
        } catch (IllegalArgumentException e) {
           throw e;
        }
    }
}