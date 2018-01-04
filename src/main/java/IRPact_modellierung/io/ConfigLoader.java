package IRPact_modellierung.io;

import IRPact_modellierung.io.loader.*;
import IRPact_modellierung.agents.AgentConfiguration;
import IRPact_modellierung.information.InformationFactory;
import IRPact_modellierung.information.InformationScheme;
import IRPact_modellierung.preference.PreferenceConfiguration;
import IRPact_modellierung.preference.Value;
import IRPact_modellierung.decision.DecisionConfiguration;
import IRPact_modellierung.distributions.*;
import IRPact_modellierung.helper.StructureEnricher;
import IRPact_modellierung.network.SNConfiguration;
import IRPact_modellierung.processModel.ProcessModel;
import IRPact_modellierung.products.*;
import IRPact_modellierung.simulation.Configuration;
import IRPact_modellierung.space.SpatialConfiguration;
import IRPact_modellierung.time.TemporalConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The ConfigLoader is used to bundle the configuration load processes.
 * It creates the configuration object from a number of other loader classes it uses.
 *
 * @author Simon Johanning
 */
public class ConfigLoader {

    private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    /**
     * Method to load the simulation configuration from the respective files in the configuration path
     * using a number of other loader classes for specific aspects of the simulation,
     * and compiles them in one object
     *
     * @param configPath The (relative) path where the configuration files of the simulation lie
     * @return The Configuration object containing all relevant information to set up the configuration of the simulation
     * @throws IOException Will be thrown when another loader raises an IOException
     * @throws ClassCastException Will be thrown when another loader raises a ClassCastException
     */
    public static Configuration loadConfiguration(String configPath) throws IOException, ClassCastException{
        try{
            Set<Distribution> distributions = loadDistributions(configPath);
            Map<String, Distribution> distributionMap = StructureEnricher.attachDistributionNames(distributions);
            Set<Value> values = new HashSet<>(); // loadValues(configPath);

            DecisionConfiguration decisionConfiguration = DecisionLoader.loadDecisionModel(configPath);;
            ProductConfiguration productConfiguration = ProductLoader.loadProductConfiguration(configPath, distributionMap, decisionConfiguration);;
            SpatialConfiguration spatialConfiguration = SpaceLoader.loadSpatialConfiguration(configPath);
            TemporalConfiguration temporalConfiguration = TimeLoader.loadTemporalModel(configPath);
            AgentConfiguration agentConfiguration = AgentLoader.loadAgents(configPath, productConfiguration, distributionMap, values, decisionConfiguration);
            PreferenceConfiguration preferenceConfiguration = PreferenceLoader.loadPreferenceConfiguration(configPath, productConfiguration, agentConfiguration.getConsumerAgentGroups());
            SNConfiguration sNConfiguration = SocialNetworkLoader.loadSocialNetwork(configPath);
            ProcessModel processModel = ProcessModelLoader.loadProcessModel(configPath);
            InformationScheme informationScheme = loadInformationScheme(configPath, distributionMap);

            fooLog.debug("Added {} values in the process",values.size());
            return new Configuration(productConfiguration, agentConfiguration, spatialConfiguration, temporalConfiguration, sNConfiguration, preferenceConfiguration, processModel, decisionConfiguration, distributionMap, informationScheme);
        } catch (IOException ioe) {
            throw ioe;
        } catch (ClassCastException cce) {
            throw cce;
        }
    }

//    private static Set<Value> loadValues(String configPath) throws IllegalArgumentException, IOException{
//        Set<Value> values = new HashSet<>();
//        Set<String> valueNames = new HashSet<>();
//        ObjectMapper mapper = new ObjectMapper();
//        File folder = new File(configPath + "consumerAgentGroups");
//        //load all files in the consumerAgentGroups folder
//        File[] listOfFiles = folder.listFiles();
//        for (File file : listOfFiles) {
//            if (file.isFile()) {
//                try {
//                    HashMap<String, Object> consumerAgentGroupMap = mapper.readValue(file, HashMap.class);
//                    if (!consumerAgentGroupMap.containsKey("consumerAgentGroupValues"))
//                        throw new IllegalArgumentException("ConsumerAgentGroup coded in " + file.getName() + " has no values. Please check the configuration!");
//                    ArrayList<HashMap<String, Object>> valueMap = (ArrayList<HashMap<String, Object>>) consumerAgentGroupMap.get("consumerAgentGroupValues");
//                    Iterator<HashMap<String, Object>> valueMapIterator = valueMap.iterator();
//                    while (valueMapIterator.hasNext()) {
//                        HashMap<String, Object> currentValue = valueMapIterator.next();
//                        if (!currentValue.containsKey("value"))
//                            throw new IllegalArgumentException("Value is not associated with a value!");
//                        valueNames.add((String) currentValue.get("value"));
//                    }
//                } catch (IOException ioe) {
//                    throw ioe;
//                }
//            }
//        }
//        for (String valueName : valueNames) {
//            values.add(new Value(valueName));
//        }
//        return values;
//    }

    private static InformationScheme loadInformationScheme(String configPath, Map<String, Distribution> distributionMap) throws IllegalArgumentException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        try {
            HashMap<String, Object> informationConfiguration = mapper.readValue(new File(configPath + "InformationConfiguration.json"), HashMap.class);
            if (!informationConfiguration.containsKey("informationScheme"))
                throw new IllegalArgumentException("Information configuration does not contain the information scheme!!\nPlease provide a valid configuration.");
            else if (!informationConfiguration.containsKey("informationParameter"))
                throw new IllegalArgumentException("Information configuration does not contain the information parameter!!\\nPlease provide a valid configuration.");
            else {
                return InformationFactory.createInformationScheme((String) informationConfiguration.get("informationScheme"), (HashMap<String, Object>) informationConfiguration.get("informationParameter"), distributionMap);
            }
        }catch (IllegalArgumentException iae){
            throw iae;
        }catch (UnsupportedOperationException uoe){
            throw uoe;
        }catch (IOException ioe){
            throw ioe;
        }
    }

    private static Set<Distribution> loadDistributions(String configPath) throws ClassCastException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        try {
            HashMap<String, Object>[] distributionMap = mapper.readValue(new File(configPath+"Distributions.json"), HashMap[].class);
            Set<Distribution> returnSet = new HashSet<Distribution>(distributionMap.length);
            for (HashMap<String, Object> aDistributionMap : distributionMap) {
                HashMap<String, Object> distributionParameters = (HashMap<String, Object>) aDistributionMap.get("parameters");
                returnSet.add(DistributionFactory.createDistribution((String) aDistributionMap.get("name"), (String) aDistributionMap.get("distribution"), distributionParameters));
            }
            return returnSet;
        } catch (ClassCastException cce){
            throw cce;
        } catch (IOException ioe) {
            throw ioe;
        }
    }
}

