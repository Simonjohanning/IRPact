package IRPact_modellierung.agents.consumerAgents;

import java.util.Map;

/**
 * Class to model the affinity between different ConsumerAgentGroups, conceptually as matrix of ConsumerAgentGroup to other ConsumerAgentGroup.
 * Is implemented as a Map of ConsumerAgentGroup and Map of ConsumerAgentGroup and
 * Class is purely a data structure and does not provide any functionality
 *
 * @author Simon Johanning
 */
public class ConsumerAgentGroupAffinities {

    private Map<ConsumerAgentGroup, Map<ConsumerAgentGroup, Double>> affinities;

    /**
     *
     * @param affinities Map with keys ConsumerAgentGroup signifying the ConsumerAgentGroup that has affinity to a following and values a map of 'target' ConsumerAgentGroup (the group the affinity is towards) as keys as numerical values as values. These values (for each ConsumerAgentGroup in the (outer) key) should add to 1.0 in order to expect expected behavior
     *
     */
    public ConsumerAgentGroupAffinities(Map<ConsumerAgentGroup, Map<ConsumerAgentGroup, Double>> affinities){
        this.affinities = affinities;
    }

    public Map<ConsumerAgentGroup, Double> getAffinities(ConsumerAgentGroup consumerAgentGroup){
        return affinities.get(consumerAgentGroup);
    }

    public Map<ConsumerAgentGroup, Map<ConsumerAgentGroup, Double>> getAffinities() {
        return affinities;
    }
}
