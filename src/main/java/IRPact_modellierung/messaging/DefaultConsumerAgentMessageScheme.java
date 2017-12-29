package IRPact_modellierung.messaging;

import IRPact_modellierung.agents.Agent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.distributions.UnivariateDistribution;
import IRPact_modellierung.network.SocialGraph;
import IRPact_modellierung.preference.Preference;
import IRPact_modellierung.preference.Value;
import IRPact_modellierung.helper.FilterHelper;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.helper.StructureEnricher;
import IRPact_modellierung.network.SNEdge;
import IRPact_modellierung.network.SNNode;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This message scheme describes a message scheme from a ConsumerAgent to another ConsumerAgent.
 * It is not grounded on anything and shouldn't be used for more than a place holder,
 * and the only reason it is within IRPact is for the purpose of testing mechanisms.
 *
 * It sends a number of ConsumerConsumerProductAttributePerceptionManipulationMessages from the sendingAgent
 * to the receiving agent based on the NumberMessagesSentPerTimeUnit distribution of the corresponding
 * ConsumerAgentGroup to a number of random nodes in its neighbourhood within the social graph.
 *
 * @author Simon Johanning
 */
public class DefaultConsumerAgentMessageScheme extends ConsumerAgentMessageScheme{

    private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    public DefaultConsumerAgentMessageScheme(UnivariateDistribution numberMessagesPerTimeUnit) {
        super(numberMessagesPerTimeUnit);
    }

    /**
     * Method to send a number (according to the NumberMessagesSentPerTimeUnit distribution of the corresponding
     * ConsumerAgentGroup) of ConsumerConsumerPreferenceManipulationMessages or ConsumerConsumerProductAttributePerceptionManipulationMessages
     * to the receiver, depending on the strength of their connection.
     *
     * @param simulationContainer The container the simulation runs in
     * @param sendingAgent The agent from whom the messages originate
     * @return A set of ConsumerConsumer messages communicating from the sending agent to the receiving agent
     */
    public Set<Message> createMessages(SimulationContainer simulationContainer, ConsumerAgent sendingAgent) {
        ConsumerAgent sender = (ConsumerAgent) sendingAgent;
        int noMessagesToSend = (int) (Math.floor(numberMessagesPerTimeUnit.draw()));
        fooLog.debug("Creating {} messages for {} with {} potential receivers",noMessagesToSend, sendingAgent, simulationContainer.getSocialNetwork().getSocialGraph().getNeighbours(sender.getCorrespondingNodeInSN()).size());
        Set<Message> messagesToSend = new HashSet<Message>(noMessagesToSend);
        if(simulationContainer.getSocialNetwork().getSocialGraph().getNeighbours(sender.getCorrespondingNodeInSN()).size() > 0) {
            for (int messageIndex = 0; messageIndex < noMessagesToSend; messageIndex++) {
//                System.out.println("neighbourlist");
//                System.out.println(simulationContainer.getSocialNetwork().getSocialGraph().getNeighbours());
//                System.out.println("communicationNeighbourLists");
//                System.out.println(simulationContainer.getSocialNetwork().getSocialGraph().getNeighbours().get(SocialGraph.EDGEMEDIUM.COMMUNICATION));
//                System.out.println("correponding node");
//                System.out.println(sender.getCorrespondingNodeInSN());
//                System.out.println("communicationNeighbourLists of corresponding node");
//                System.out.println(simulationContainer.getSocialNetwork().getSocialGraph().getNeighbours().get(SocialGraph.EDGEMEDIUM.COMMUNICATION).get(sender.getCorrespondingNodeInSN()));
                Set<SNNode> potentialTargetNodes = simulationContainer.getSocialNetwork().getSocialGraph().getNeighbours().get(SocialGraph.EDGEMEDIUM.COMMUNICATION).get(sender.getCorrespondingNodeInSN());
                if(!potentialTargetNodes.isEmpty()) {
                    SNNode targetNode = LazynessHelper.getRandomSNNode(potentialTargetNodes);
                    SNEdge correspondingEdge = LazynessHelper.getCorrespondingEdge(sender.getCorrespondingNodeInSN(), targetNode, simulationContainer.getSocialNetwork().getSocialGraph().getEdges());
                    ConsumerAgent targetAgent = LazynessHelper.retrieveCorrespondingConsumerAgent(simulationContainer, targetNode);
                    Set<Product> potentialProducts = FilterHelper.filterProductsNotOnMarketYet(FilterHelper.selectKnownProducts(sender.getProductAwarenessMap()));
                    if (potentialProducts.size() > 0) {
                        Product productConcerned = LazynessHelper.chooseProduct(potentialProducts);
                        String messageType = decideForMessageType();
                        if (messageType.equals("PreferenceMessage")) {
                            Preference preferenceConcerned = decideForPreference(simulationContainer.getValuesUsed(), sender.getPreferences());
                            messagesToSend.add(new ConsumerConsumerPreferenceManipulationMessage(sender, targetAgent, preferenceConcerned, simulationContainer.getSimulationConfiguration().getPreferenceConfiguration().getPreferenceHomogenizingFactor()));
                        } else if (messageType.equals("ProductAttributeMessage")) {
                            messagesToSend.add(new ConsumerConsumerPerceptionManipulationMessage(sender, targetAgent, decideForProductAttribute(productConcerned.getProductAttributes()), productConcerned));
                        } else {
                            fooLog.info("ERROR!!! The message type {} is not implemented!!!", messageType);
                        }
                    }
                }
            }
        }else fooLog.debug("Node has no neighbours");
        fooLog.debug("ConsumerAgent {} sends {} messages ",sendingAgent, messagesToSend.size());
        return messagesToSend;
    }

    private String decideForMessageType(){
//        if(Math.random() < 0.0) return "PreferenceMessage";
        return "ProductAttributeMessage";
    }

    private ProductAttribute decideForProductAttribute(Set<ProductAttribute> potentialAttributes){
        ArrayList<ProductAttribute> attributeList = new ArrayList<ProductAttribute>(potentialAttributes);
        int attributeIndex = (int) Math.floor(Math.random()*potentialAttributes.size());
        return attributeList.get(attributeIndex);
    }

    private Preference decideForPreference(Set<Value> potentialValue, Set<Preference> preferences){
        HashMap<Value, Preference> preferenceMap = StructureEnricher.attachPreferenceValues(preferences);
        ArrayList<Value> valueList = new ArrayList<Value> (potentialValue);
        int attributeIndex = (int) Math.floor(Math.random()*potentialValue.size());
        return preferenceMap.get(valueList.get(attributeIndex));
    }

}
