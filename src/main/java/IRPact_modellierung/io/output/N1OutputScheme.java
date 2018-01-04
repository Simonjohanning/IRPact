package IRPact_modellierung.io.output;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.helper.LazynessHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Lenovo on 15.08.2016.
 */
public class N1OutputScheme extends OutputScheme {

    private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    public void writeConsumerAgentState(ConsumerAgent consumerAgent, double timeStamp) {
       /* for(ProductAttribute pa : consumerAgent.getPerceivedProductAttributeValues().keySet()){
            fooLog.info("Consumer agent {} has a perceived value of {} for product attribute {}", consumerAgent.getAgentID(), consumerAgent.getPerceivedProductAttributeValues().get(pa).calculateProductPerception(timeStamp), pa.getCorrespondingProductGroupAttribute().getName());
        }*/
    }

    public void writeConsumerAdoption(Set<ConsumerAgent> consumerAgents, double timeStamp) {
        int numAdopters = LazynessHelper.countAdoptedAgents(consumerAgents);
        ArrayList<Integer> noAdoptedProductMap = LazynessHelper.countNoProductsAdoptedPerAgent(consumerAgents);
        fooLog.info("{}/{} adopters in step {}",numAdopters, consumerAgents.size(), timeStamp);
    }
}
