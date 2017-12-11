package IRPact_modellierung.agents.companyAgents.advertisement;

import IRPact_modellierung.agents.companyAgents.CompanyAgent;
import IRPact_modellierung.distributions.Distribution;
import IRPact_modellierung.distributions.UnivariateDistribution;
import IRPact_modellierung.events.CommunicationEvent;
import IRPact_modellierung.events.EventScheduler;
import IRPact_modellierung.messaging.DefaultCompanyAgentMessageScheme;
import IRPact_modellierung.messaging.Message;

import java.util.Set;

/**
 * The DefaultAdvertisementScheme is an AdvertisementScheme based on the DefaultCompanyAgentMessageScheme.
 * It uses the same parameters and uses these to base the messages on.
 *
 * Messages derived from this scheme will be scheduled for immediate execution in the simulation container
 * and corresponding EventScheduler of the respective company agent.
 *
 * @author Simon Johannning
 */
public class DefaultAdvertisementScheme extends AdvertisementScheme {

    /**
     * The DefaultAdvertisementScheme is based on the DefaultCompanyAgentMessageScheme and thus takes the same parameters.
     * In fact it just hands them over to the respective message scheme
     *
     * @param numberOfMessages The distribution of the number of messages that are to be created every invocation
     * @param messagePreferenceIncrease The amount the preference of a target agent is to be improved (relative to its current one) for preference increase messages
     * @param advertisingImpactFactor The effectiveness of advertisement for productPerceptionManipulationMessages
     * @param fractionPreferenceMessages The amount of preference messages to perception manipulation messages
     */
    public DefaultAdvertisementScheme(UnivariateDistribution numberOfMessages, double messagePreferenceIncrease, double advertisingImpactFactor, double fractionPreferenceMessages){
        super(new DefaultCompanyAgentMessageScheme(numberOfMessages, messagePreferenceIncrease, advertisingImpactFactor, fractionPreferenceMessages));
    }

    /**
     * The DefaultAdvertisementScheme uses the DefaultCompanyAgentMessageScheme
     * to create a number of messages about the products.
     * These are scheduled for immediate execution in the simulation container
     * and the respective EventScheduler of the company agent
     *
     * @param correspondingAgent The CompanyAgent advertising their product portfolio
     */
    public void advertiseProductPortfolio(CompanyAgent correspondingAgent) {
        Set<Message> messagesToSchedule = messageScheme.createMessages(correspondingAgent.getAssociatedSimulationContainer(), correspondingAgent);
        EventScheduler eventScheduler = correspondingAgent.getAssociatedSimulationContainer().getEventScheduler();
        for(Message currentMessage : messagesToSchedule){
            eventScheduler.scheduleEvent(new CommunicationEvent(currentMessage, correspondingAgent.getAssociatedSimulationContainer().getTimeModel().getSimulationTime(), correspondingAgent.getAssociatedSimulationContainer()));
        }
    }
}


