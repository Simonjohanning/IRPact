package IRPact_modellierung.messaging;

import IRPact_modellierung.agents.Agent;
import IRPact_modellierung.agents.companyAgents.CompanyAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.distributions.Distribution;
import IRPact_modellierung.distributions.UnivariateDistribution;
import IRPact_modellierung.preference.Value;
import IRPact_modellierung.helper.FilterHelper;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.network.SNNode;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Messaging scheme to send advertisement messages by a CompanyAgent.
 * Is not grounded in anything justified and just thought to be a placeholder.
 * Should really not be used for anything but testing tbh...
 *
 * @author Simon Johanning
 */
public class DefaultCompanyAgentMessageScheme extends CompanyAgentMessageScheme{

    private UnivariateDistribution numberOfMessages;
    private double messagePreferenceIncrease;
    private double advertisingImpactFactor;
    private double fractionPreferenceMessages;
    private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    /**
     * Message scheme to send different messages from a CompanyAgent to customers.
     * Is not implemented well and doesn't make much sense; should just be regarded as a placeholder
     *
     * @param numberOfMessages The (distribution of the) number of messages that are to be created every time step
     * @param messagePreferenceIncrease The amount the preference of a target agent is to be improved (relative to its current one) for preference increase messages
     * @param advertisingImpactFactor The effectiveness of advertisement for productPerceptionManipulationMessages
     * @param fractionPreferenceMessages The amount of preference messages to perception manipulation messages
     */
    public DefaultCompanyAgentMessageScheme(UnivariateDistribution numberOfMessages, double messagePreferenceIncrease, double advertisingImpactFactor, double fractionPreferenceMessages) {
        this.numberOfMessages = numberOfMessages;
        this.messagePreferenceIncrease = messagePreferenceIncrease;
        this.advertisingImpactFactor = advertisingImpactFactor;
        this.fractionPreferenceMessages = fractionPreferenceMessages;
    }

    /**
     * Creates a number of messages to either increase preference or direct perception towards the true value of the product.
     * Which message (and which value / product attribute is directed) is created is chosen stochastically.
     * Also the target agent is chosen randomly from the agents' population
     *
     * @param simulationContainer The container the simulation runs in
     * @param sender The agent from whom the messages originate
     * @return The messages the company agent is to send
     */
    public Set<Message> createMessages(SimulationContainer simulationContainer, CompanyAgent sender) {
        Set<Message> messagesToSend = new HashSet<Message>();
        for(int messageIndex = 0; messageIndex < (int) Math.floor(numberOfMessages.draw()); messageIndex++){
            SNNode targetNode = LazynessHelper.getRandomSNNode(simulationContainer.getSocialNetwork().getSocialGraph().getNodes());
            ConsumerAgent targetAgent = LazynessHelper.retrieveCorrespondingConsumerAgent(simulationContainer, targetNode);
            String messageType = decideForMessageType();
            Product productConcerned = LazynessHelper.chooseProduct(FilterHelper.filterProductsNotOnMarketYet(sender.getProductPortfolio()));
            if(messageType.equals("PreferenceMessage")){
                Value valueConcerned = decideForValue(simulationContainer.getValuesUsed());
                messagesToSend.add(new CompanyConsumerPreferenceManipulationMessage(sender, targetAgent, valueConcerned, messagePreferenceIncrease, true));
            }else if(messageType.equals("ProductAttributeMessage")){
                Set<ProductAttribute> knownProductAttributes = LazynessHelper.selectProductAttributes(simulationContainer.getProducts());
                ProductAttribute concernedProductAttribute = decideForProductAttribute(knownProductAttributes);
                messagesToSend.add(new CompanyConsumerPerceptionManipulationMessage(sender, targetAgent, concernedProductAttribute, productConcerned, concernedProductAttribute.getValue(), advertisingImpactFactor));
            }else{
                fooLog.info("ERROR!!! The message type {} is not implemented!!!", messageType);
            }
        }
        return messagesToSend;
    }


    private String decideForMessageType(){
        if(Math.random() < fractionPreferenceMessages) return "PreferenceMessage";
        else return "ProductAttributeMessage";
    }

    private ProductAttribute decideForProductAttribute(Set<ProductAttribute> potentialAttributes){
        ArrayList<ProductAttribute> attributeList = (ArrayList<ProductAttribute>) potentialAttributes;
        int attributeIndex = (int) Math.floor(Math.random()*potentialAttributes.size());
        return attributeList.get(attributeIndex);
    }

    private Value decideForValue(Set<Value> potentialValue){
        ArrayList<Value> valueList = (ArrayList<Value>) potentialValue;
        int attributeIndex = (int) Math.floor(Math.random()*potentialValue.size());
        return valueList.get(attributeIndex);
    }

    /**
     * Method to conform to the Message scheme but have createMessages with a CompanyAgent.
     * Downcasts the sendingAgent to a CompanyAgent and will print the respective exception here.
     *
     * @param simulationContainer The container the simulation runs in
     * @param sendingAgent The agent from whom the messages originate
     * @return The messages the company agent is to send
     */
    public Set<Message> createMessages(SimulationContainer simulationContainer, Agent sendingAgent) {
        try{
            return createMessages(simulationContainer, (CompanyAgent) sendingAgent);
        }catch(ClassCastException cce){
            cce.printStackTrace();
            return null;
        }
    }
}
