package IRPact_modellierung.IO.Output;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;

import java.util.Set;

/**
 * Created by Lenovo on 10.08.2016.
 */
public abstract class OutputScheme {

    public abstract void writeConsumerAgentState(ConsumerAgent consumerAgent, double timeStamp);
    public abstract void writeConsumerAdoption(Set<ConsumerAgent> consumerAgents, double timeStamp);
}
