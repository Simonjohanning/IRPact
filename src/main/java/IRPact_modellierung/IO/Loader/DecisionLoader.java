package IRPact_modellierung.IO.Loader;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.decision.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to create the decision processes used by the simulation as part of the DecisionConfiguration.
 * Will create all decision processes configured in the corresponding configuration.
 *
 * @author Simon Johanning
 */
public class DecisionLoader {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    /**
     * Method to load the implemented decision processes from the configuration path as JSON file
     *
     * @param configPath The (relative) path the decision configuration file is located at
     * @return Will return a DecisionConfiguration based on the configured decision making processes
     * @throws IllegalArgumentException Will be thrown when the configuration of the decision making processes (in the config path) is erroneous (parameters for relevant decision making processes missing)
     * @throws IOException Will be thrown when an error occurs handling the file storing the configuration of the referred decision configuration
     * @throws JsonParseException Will be thrown upon a parse error for the json file corresponding to the file handler
     * @throws JsonMappingException Will be thrown upon a mapping error for the json file corresponding to the file handler
     */
    public static DecisionConfiguration loadDecisionModel(String configPath) throws IllegalArgumentException, IOException, JsonMappingException, JsonParseException{
        ObjectMapper mapper = new ObjectMapper();
        try {
            HashMap<String, Object> decisionConfigurationJSON = mapper.readValue(new File(configPath + "DecisionConfiguration.json"), HashMap.class);
            Map<String, DecisionMakingProcess> configuredDecisionMakingProcesses = new HashMap<String, DecisionMakingProcess>();
            for (String key : decisionConfigurationJSON.keySet()) {
                if (key.equals("Kiesling")) {
                    HashMap<String, Object> parametersKiesling = (HashMap<String, Object>) decisionConfigurationJSON.get(key);
                    double rangeEpsilon;
                    UtilityFunction utilityFunction;
                    if(!parametersKiesling.containsKey("rangeEpsilon")) throw new IllegalArgumentException("Error in configuring the Kiesling DecisionMakingProcess: rangeEpsilon is not configured!!");
                    else{
                        try{
                            rangeEpsilon = (Double) parametersKiesling.get("rangeEpsilon");
                        } catch (ClassCastException cce){
                            throw new IllegalArgumentException("Error in configuring the Kiesling DecisionMakingProcess: rangeEpsilon isn't given as double!!\n"+cce);
                        }
                    }
                    if(!parametersKiesling.containsKey("utilityFunctionEmployed")) throw new IllegalArgumentException("Error in configuring the Kiesling DecisionMakingProcess: utilityFunctionEmployed is not configured!!");
                    else{
                        try{
                            utilityFunction = loadUtilityFunction((String) parametersKiesling.get("utilityFunctionEmployed"));
                        } catch (IllegalArgumentException iae){
                            throw iae;
                        }
                    }
                    configuredDecisionMakingProcesses.put(key, new KieslingUtilitarianConsumerAgentAdoptionDecisionProcess(rangeEpsilon, utilityFunction));
                }else if(key.equals("TakeTheBest")){
                    HashMap<String, Object> parametersTTB= (HashMap<String, Object>) decisionConfigurationJSON.get(key);
                    UtilityFunction utilityFunction;
                    if(!parametersTTB.containsKey("utilityFunctionEmployed")) throw new IllegalArgumentException("Error in configuring the TakeTheBest DecisionMakingProcess: utilityFunctionEmployed is not configured!!");
                    else{
                        try{
                            utilityFunction = loadUtilityFunction((String) parametersTTB.get("utilityFunctionEmployed"));
                        } catch (IllegalArgumentException iae){
                            throw iae;
                        }
                    }
                    configuredDecisionMakingProcesses.put(key, new TakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess(utilityFunction));
                }
                else if(key.equals("SchwarzTakeTheBest")){
                    HashMap<String, Object> parametersSTTB= (HashMap<String, Object>) decisionConfigurationJSON.get(key);
                    UtilityFunction utilityFunction;
                    if(!parametersSTTB.containsKey("utilityFunctionEmployed")) throw new IllegalArgumentException("Error in configuring the Schwarz TakeTheBest DecisionMakingProcess: utilityFunctionEmployed is not configured!!");
                    else{
                        try{
                            utilityFunction = loadUtilityFunction((String) parametersSTTB.get("utilityFunctionEmployed"));
                        } catch (IllegalArgumentException iae){
                            throw iae;
                        }
                    }
                    configuredDecisionMakingProcesses.put(key, new SchwarzTakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess(utilityFunction));
                }
                else if(key.equals("DeliberativeDecision")){
                    HashMap<String, Object> parametersDD = (HashMap<String, Object>) decisionConfigurationJSON.get(key);
                    UtilityFunction utilityFunction;
                    Map<String, Map<String, Double>> importanceAttitudeMap;
                    if(!parametersDD.containsKey("utilityFunctionEmployed")) throw new IllegalArgumentException("Error in configuring the DeliberativeDecision DecisionMakingProcess: utilityFunctionEmployed is not configured!!");
                    else{
                        try{
                            utilityFunction = loadUtilityFunction((String) parametersDD.get("utilityFunctionEmployed"));
                        } catch (IllegalArgumentException iae){
                            throw iae;
                        }
                    }
                    if(!parametersDD.containsKey("importanceAttitudeMap")) throw new IllegalArgumentException("Error in configuring the DeliberativeDecision DecisionMakingProcess: importanceAttitudeMap is not configured!!");
                    else{
                        try{
                            importanceAttitudeMap = loadImportanceAttitudeMap((HashMap<String, Object>) parametersDD.get("importanceAttitudeMap"));
                        } catch (IllegalArgumentException iae){
                            throw iae;
                        }
                    }
                    configuredDecisionMakingProcesses.put(key, new DeliberativeConsumerAgentAdoptionDecisionProcess(utilityFunction, importanceAttitudeMap));
                }
            }
            return new DecisionConfiguration(configuredDecisionMakingProcesses);
        } catch (JsonParseException jpe){
            throw jpe;
        } catch (JsonMappingException jme){
            throw jme;
        } catch (IOException ioe){
            throw ioe;
        }
    }

    /**
     * Helper method to load the importanceAttitudeMap from the configuration
     *
     * @param importanceAttitudeConfigurationMap The configuration of the importanceAttitudeMap as value of the key 'importanceAttitudeMap' in the configuration file
     * @return the importanceAttitudeProtoMap for the Deliberate Decision process
     * @throws ClassCastException Will be thrown when the data can't be cast appropriately
     */
    private static Map<String,Map<String,Double>> loadImportanceAttitudeMap(HashMap<String, Object> importanceAttitudeConfigurationMap) throws ClassCastException{
        Map<String, Map<String, Double>> importanceMap = new HashMap<>();
        for(String cagString : importanceAttitudeConfigurationMap.keySet()){
            try {
                Map<String, Double> innerMap = (HashMap<String, Double>) importanceAttitudeConfigurationMap.get(cagString);
                importanceMap.put(cagString, innerMap);
            } catch (ClassCastException cce){
                throw cce;
            }
        }
        return importanceMap;
    }





    /*
    Alternative code that parameterizes the DecisionConfiguration by creating every implemented decision making process.
    Requires a configuration of all of these however, which is why using the one above is encouraged


    public static DecisionConfiguration loadDecisionModel(String configPath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            HashMap<String, Object> decisionConfigurationJSON = mapper.readValue(new File(configPath + "DecisionConfiguration.json"), HashMap.class);
            //TODO remove null stuff
            KieslingUtilitarianConsumerAgentAdoptionDecisionProcess kucaadp = null;
            TakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess ttbhucaadp = null;
            DeliberativeConsumerAgentAdoptionDecisionProcess dd = null;
            SchwarzTakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess sttbhucaadp = null;
            for (String key : decisionConfigurationJSON.keySet()) {
                if (key.equals("Kiesling")) {
                    HashMap<String, Object> parametersKiesling = (HashMap<String, Object>) decisionConfigurationJSON.get(key);
                    kucaadp = new KieslingUtilitarianConsumerAgentAdoptionDecisionProcess((Double) parametersKiesling.get("rangeEpsilon"), loadUtilityFunction((String) parametersKiesling.get("utilityFunctionEmployed")));
                }else if(key.equals("TakeTheBest")){
                    HashMap<String, Object> parametersTTB= (HashMap<String, Object>) decisionConfigurationJSON.get(key);
                    ttbhucaadp = new TakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess(loadUtilityFunction((String) parametersTTB.get("utilityFunctionEmployed")));
                }
                else if(key.equals("SchwarzTakeTheBest")){
                    HashMap<String, Object> parametersTTB= (HashMap<String, Object>) decisionConfigurationJSON.get(key);
                    sttbhucaadp = new SchwarzTakeTheBestHeuristicUtilitarianConsumerAgentAdoptionDecisionProcess(loadUtilityFunction((String) parametersTTB.get("utilityFunctionEmployed")));
                }
                else if(key.equals("DeliberativeDecision")){
                    HashMap<String, Object> parametersTTB= (HashMap<String, Object>) decisionConfigurationJSON.get(key);
                    dd = new DeliberativeConsumerAgentAdoptionDecisionProcess(loadUtilityFunction((String) parametersTTB.get("utilityFunctionEmployed")));
                }
            }
            return new DecisionConfiguration(kucaadp, ttbhucaadp, dd, sttbhucaadp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
*/

    /**
     * Method to create an instance of an implemented utility function based on its name.
     * Will throw an error if function is not implemented (in the list of utility functions).
     *
     * @param utilityFunctionEmployed The name of the utility function to be created
     * @return The utility function corresponding to the provided name
     * @throws IllegalArgumentException Will be thrown when the function is not implemented (in the list of utility functions).
     */
    private static UtilityFunction loadUtilityFunction(String utilityFunctionEmployed) throws IllegalArgumentException{
        if (utilityFunctionEmployed.equals("linear")) return new LinearUtilityFunction();
        else if(utilityFunctionEmployed.equals("linearPreferenceOriented")) return new LinearPreferenceOrientedUtilityFunction();
        else throw new IllegalArgumentException("Utility function "+utilityFunctionEmployed+" is not implemented!!\nPlease provide valid identifiers!");
    }

}
