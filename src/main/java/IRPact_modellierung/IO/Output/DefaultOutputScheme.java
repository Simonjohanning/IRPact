package IRPact_modellierung.IO.Output;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.helper.LazynessHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Lenovo on 10.08.2016.
 */
public class DefaultOutputScheme extends OutputScheme{

    private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    public void writeConsumerAgentState(ConsumerAgent consumerAgent, double timeStamp) {

    }

    public void writeConsumerAdoption(Set<ConsumerAgent> consumerAgents, double timeStamp) {
        int numberAgentsAdopted = LazynessHelper.countAdoptedAgents(consumerAgents);
        fooLog.info("Number of consumer with adopted products at time {}: {}", timeStamp, numberAgentsAdopted);
        ArrayList<Integer> noAdoptedProductMap = LazynessHelper.countNoProductsAdoptedPerAgent(consumerAgents);
        for(int index=0;index<noAdoptedProductMap.size();index++){
            fooLog.info("Number of consumers adopting {} products at time {}: {}", index, timeStamp, noAdoptedProductMap.get(index));
        }
    }
}
