package IRPact_modellierung.decision;

import org.apache.logging.log4j.LogManager;

import java.util.Map;

/**
 * Data structure to bundle the decision processes.
 * The decision configuration consists of instances of the decision processes implemented,
 * and offers access to them via getDecisionMakingProcess (see userDocumentation for more details).
 *
 * Currently two ways exist to represent the decision processes;
 * Either a configuration is provided for every single decision process implemented,
 * or a map with the decision processes implemented is provided
 *
 * @author Simon Johanning
 */
public class DecisionConfiguration {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    private KieslingUtilitarianConsumerAgentAdoptionDecisionProcess kiesling;
    private TakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess takeTheBest;
    private DeliberativeConsumerAgentAdoptionDecisionProcess deliberativeDecision;
    private SchwarzTakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess schwarzTakeTheBest;

    private Map<String, DecisionMakingProcess> configuredDecisionMakingProcesses;

    /**
     * Constructor to associate instances of (concrete) decision making processes with the DecisionConfiguration.
     * Implements the manner in which all decision processes are configured, independent whether they are used or not
     *
     * @param kiesling An instance of the KieslingUtilitarianConsumerAgentAdoptionDecisionProcess
     * @param ttbhucaadp An instance of the TakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess
     * @param dd An instance of the DeliberativeConsumerAgentAdoptionDecisionProcess
     * @param sttbhucaadp An instance of the SchwarzTakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess
     */
    public DecisionConfiguration(KieslingUtilitarianConsumerAgentAdoptionDecisionProcess kiesling, TakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess ttbhucaadp, DeliberativeConsumerAgentAdoptionDecisionProcess dd, SchwarzTakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess sttbhucaadp) {
        this.kiesling = kiesling;
        this.takeTheBest = ttbhucaadp;
        this.deliberativeDecision = dd;
        this.schwarzTakeTheBest = sttbhucaadp;
    }

    /**
     * Constructor to associate instances of (concrete) decision making processes with the DecisionConfiguration.
     * Implements the manner in which only a set decision processes are configured,
     * and the decision processes that can be used by the simulation are given implicitly.
     *
     * @param configuredDecisionMakingProcesses Map of all decision making processes configured for this simulation
     */
    public DecisionConfiguration(Map<String, DecisionMakingProcess> configuredDecisionMakingProcesses){
        this.configuredDecisionMakingProcesses = configuredDecisionMakingProcesses;
        for(String currentKey : configuredDecisionMakingProcesses.keySet()){
            switch(currentKey){
                case "Kiesling":
                    this.kiesling = (KieslingUtilitarianConsumerAgentAdoptionDecisionProcess) configuredDecisionMakingProcesses.get("Kiesling");
                    break;
                case "TakeTheBest":
                    this.takeTheBest = (TakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess) configuredDecisionMakingProcesses.get("TakeTheBest");
                    break;
                case "DeliberativeDecision":
                    this.deliberativeDecision = (DeliberativeConsumerAgentAdoptionDecisionProcess) configuredDecisionMakingProcesses.get("DeliberativeDecision");
                    break;
                case "SchwarzTakeTheBest":
                    this.schwarzTakeTheBest = (SchwarzTakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess) configuredDecisionMakingProcesses.get("SchwarzTakeTheBest");
                    break;
            }
        }
    }

    public KieslingUtilitarianConsumerAgentAdoptionDecisionProcess getKiesling() throws IllegalStateException{
        try{
            if(configuredDecisionMakingProcesses.containsKey("Kiesling")) return (KieslingUtilitarianConsumerAgentAdoptionDecisionProcess) configuredDecisionMakingProcesses.get("Kiesling");
        } catch (ClassCastException cce){
            throw new IllegalStateException("The KieslingUtilitarianConsumerAgentAdoptionDecisionProcess was configured, but can't be casted into a KieslingUtilitarianConsumerAgentAdoptionDecisionProcess!! Please make sure that the KieslingUtilitarianConsumerAgentAdoptionDecisionProcess is configured corretly!!");
        }
        if(kiesling != null) return kiesling;
        else throw new IllegalStateException("The KieslingUtilitarianConsumerAgentAdoptionDecisionProcess for the simulation was configured incorrectly!!\nPlease make sure that every DecisionMakingProcess used in the simulation is configured!!");
    }

    public TakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess getTakeTheBest() throws IllegalStateException{
        try{
            if(configuredDecisionMakingProcesses.containsKey("TakeTheBest")) return (TakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess) configuredDecisionMakingProcesses.get("TakeTheBest");
        } catch (ClassCastException cce){
            throw new IllegalStateException("The TakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess was configured, but can't be casted into a TakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess!! Please make sure that the TakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess is configured corretly!!");
        }
        if(kiesling != null) return takeTheBest;
        else throw new IllegalStateException("The TakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess for the simulation was configured incorrectly!!\nPlease make sure that every DecisionMakingProcess used in the simulation is configured!!");
    }

    public SchwarzTakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess getSchwarzTakeTheBest() throws IllegalStateException{
        try{
            if(configuredDecisionMakingProcesses.containsKey("SchwarzTakeTheBest")) return (SchwarzTakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess) configuredDecisionMakingProcesses.get("SchwarzTakeTheBest");
        } catch (ClassCastException cce){
            throw new IllegalStateException("The SchwarzTakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess was configured, but can't be casted into a SchwarzTakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess!! Please make sure that the SchwarzTakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess is configured corretly!!");
        }
        if(kiesling != null) return schwarzTakeTheBest;
        else throw new IllegalStateException("The SchwarzTakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess for the simulation was configured incorrectly!!\nPlease make sure that every DecisionMakingProcess used in the simulation is configured!!");
    }

    public DeliberativeConsumerAgentAdoptionDecisionProcess getDeliberativeDecision() throws IllegalStateException{
        try{
            if(configuredDecisionMakingProcesses.containsKey("DeliberativeDecision")) return (DeliberativeConsumerAgentAdoptionDecisionProcess) configuredDecisionMakingProcesses.get("DeliberativeDecision");
        } catch (ClassCastException cce){
            throw new IllegalStateException("The DeliberativeConsumerAgentAdoptionDecisionProcess was configured, but can't be casted into a DeliberativeConsumerAgentAdoptionDecisionProcess!! Please make sure that the DeliberativeConsumerAgentAdoptionDecisionProcess is configured corretly!!");
        }
        if(kiesling != null) return deliberativeDecision;
        else throw new IllegalStateException("The DeliberativeConsumerAgentAdoptionDecisionProcess for the simulation was configured incorrectly!!\nPlease make sure that every DecisionMakingProcess used in the simulation is configured!!");
    }

    /**
     * method to retrieve the (parameterized) instances of implementations of a DecisionProcess.
     * For a list of implemented keywords the reader is referred to the userDocumentation.
     *
     * @param decisionProcessName String representing the decision process to be retrieved (see userDocumentation for a list)
     * @return An instance of the decision process represented by key
     * @throws IllegalArgumentException Will be thrown when
     */
    public ConsumerAgentAdoptionDecisionProcess getDecisionMakingProcess(String decisionProcessName) throws IllegalArgumentException{
        switch(decisionProcessName){
            case "Kiesling":
                return kiesling;
            case "TakeTheBest":
                return takeTheBest;
            case "DeliberativeDecision":
                return deliberativeDecision;
            case "SchwarzTakeTheBest":
                return schwarzTakeTheBest;
            default: throw new IllegalArgumentException("Decision process "+decisionProcessName+" is either not implemented or was not parameterized correctly the configuration of this simulation!!\nPlease make sure to provide a valid configuration!");
        }
    }

}
