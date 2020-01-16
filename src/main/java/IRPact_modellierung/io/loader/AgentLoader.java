package IRPact_modellierung.io.loader;

import IRPact_modellierung.agents.companyAgents.advertisement.AdvertisementScheme;
import IRPact_modellierung.agents.AgentConfiguration;
import IRPact_modellierung.agents.companyAgents.*;
import IRPact_modellierung.agents.consumerAgents.*;
import IRPact_modellierung.agents.posAgents.POSAgent;
import IRPact_modellierung.events.MarketIntroductionEventDescription;
import IRPact_modellierung.perception.*;
import IRPact_modellierung.agents.policyAgent.*;
import IRPact_modellierung.agents.posAgents.POSAgentConfiguration;
import IRPact_modellierung.decision.ConsumerAgentAdoptionDecisionProcess;
import IRPact_modellierung.decision.DecisionConfiguration;
import IRPact_modellierung.distributions.*;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.helper.StructureEnricher;
import IRPact_modellierung.helper.ValueConversionHelper;
import IRPact_modellierung.messaging.*;
import IRPact_modellierung.needs.Need;
import IRPact_modellierung.needs.NeedDevelopmentFactory;
import IRPact_modellierung.needs.NeedDevelopmentScheme;
import IRPact_modellierung.preference.Value;
import IRPact_modellierung.products.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Class to load an AgentConfiguration from the respective configuration.
 * The configuration can be stored in a set of .json files or in a LiteSQL data base.
 *
 * @author Simon Johanning
 */
public class AgentLoader {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    /**
     * Method to load an AgentConfiguration from a set of .json files.
     * Will load the general configuration from a provided configuration path
     * and the individual configuration of agent groups from individual files from a folder.
     * Requires an instantiation of the product and decision configuration.
     * For more details see configuration documentation
     *
     * @param configPath String representing the path where the configuration files are kept
     * @param productConfiguration The configuration of the products used in the simulation
     * @param distributions A map of the distributions used by the agents as configured with their respective names
     * @param values Set of Values used in the simulation to which the agents refer
     * @param decisionConfiguration The configuration of the decision processes
     * @return An agent configuration based on the configuration of the agents within the simulation based on a set of .json files
     * @throws IllegalArgumentException Will be thrown when one of the arguments is errornous
     * @throws IOException Will be thrown when an error occurs reading the configuration files
     * @throws JsonParseException Will be thrown when a configuration that is part of the agent configuration experiences a JsonParseException
     * @throws JsonMappingException Will be thrown when a configuration that is part of the agent configuration experiences a JsonMappingException
     */
    public static AgentConfiguration loadAgents(String configPath, ProductConfiguration productConfiguration, Map<String, Distribution> distributions, Set<Value> values, DecisionConfiguration decisionConfiguration) throws IllegalArgumentException, IOException, JsonParseException, JsonMappingException{
        ObjectMapper mapper = new ObjectMapper();
        Set<ConsumerAgentGroup> consumerAgentGroups = new HashSet<ConsumerAgentGroup>();
        Set<CompanyAgentConfiguration> companyAgentConfigurations = new HashSet<CompanyAgentConfiguration>();
        //load product group attributes from the product configuration
        Set<ProductGroupAttribute> allProductGroupAttributes = LazynessHelper.productGroupAttributesFromProductGroups(productConfiguration.getProductGroups());
        File folder = new File(configPath+"consumerAgentGroups");
        //load all files in the consumerAgentGroups folder
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                try {
                    ConsumerAgentGroup correspondingCag = AgentLoader.loadConsumerAgentGroup(file, productConfiguration, distributions, allProductGroupAttributes, values, decisionConfiguration);
                    values.addAll(correspondingCag.getConsumerAgentGroupPreferences().keySet());
                    consumerAgentGroups.add(correspondingCag);
                } catch (JsonMappingException jme) {
                    jme.printStackTrace();
                } catch (JsonParseException jpe) {
                    throw jpe;
                } catch (IllegalArgumentException e) {
                    throw e;
                } catch (IOException ioe) {
                    throw ioe;
                }
            }
        }
        //load the company agents
        File companyFolder = new File(configPath+"companyAgents");
        //load all files in the company agent folder
        File[] companyAgentFiles = companyFolder.listFiles();
        assert companyAgentFiles != null;
        for (File file : companyAgentFiles) {
            if (file.isFile()) {
                try {
                    CompanyAgentConfiguration currentCompanyAgentConfiguration = loadCompanyAgentConfiguration(file, distributions);
                    companyAgentConfigurations.add(currentCompanyAgentConfiguration);
                } catch (JsonMappingException jme) {
                    jme.printStackTrace();
                } catch (JsonParseException jpe) {
                    throw jpe;
                } catch (IllegalArgumentException e) {
                    throw e;
                } catch (IOException ioe) {
                    throw ioe;
                }
            }
        }
        //set up POS agents
        HashSet<POSAgentConfiguration> posAgents = new HashSet<>();
        File posFolder = new File(configPath+"posAgents");
        //load all files in the pos agent folder
        File[] posFiles = posFolder.listFiles();
        assert posFiles != null;
        for (File file : posFiles) {
            if (file.isFile()) {
                try {
                    POSAgentConfiguration loadedPOSAgent = loadPOSAgentConfiguration(file, productConfiguration, distributions);
                    posAgents.add(loadedPOSAgent);
                } catch (Exception e) {
                    throw e;
                }
            }
        }
        //load the policy agent
        PolicyAgentConfiguration policyAgentConfiguration = loadPolicyAgent(configPath);
        //load general agent configuration
        try {
            //load relevant attributes from the agent configuration file
            HashMap<String, Object> agentConfigurationJSON = mapper.readValue(new File(configPath + "AgentConfiguration.json"), HashMap.class);
            //set up noAgents per group
            HashMap<ConsumerAgentGroup, Integer> noAgentsPerAgentGroup = new HashMap<ConsumerAgentGroup, Integer>(consumerAgentGroups.size());
            HashMap<String, Object> noAgentsPerAgentGroupJSON = (HashMap<String, Object>) agentConfigurationJSON.get("noAgentsPerGroup");
            for (ConsumerAgentGroup agentGroup : consumerAgentGroups) {
                noAgentsPerAgentGroup.put(agentGroup, (Integer) noAgentsPerAgentGroupJSON.get(agentGroup.getGroupName()));
            }
            //set up affinities
            HashMap<String, Object> affinitiesJSON = (HashMap<String, Object>) agentConfigurationJSON.get("consumerAgentGroupAffinities");
            Map<ConsumerAgentGroup, Map<ConsumerAgentGroup, Double>> affinities = new HashMap<ConsumerAgentGroup, Map<ConsumerAgentGroup, Double>>();
            HashMap<String, ConsumerAgentGroup> cagMapping = StructureEnricher.attachConsumerAgentGroupNames(consumerAgentGroups);
            //set affinities for each agent group
            for (ConsumerAgentGroup group : consumerAgentGroups) {
                if(!affinitiesJSON.containsKey(group.getGroupName())) throw new IllegalArgumentException("No affinities set for consumer agent group "+group.getGroupName());
                //JSON representation of affinity for the current group
                HashMap<String, Object> groupAffinityJSON = (HashMap<String, Object>) affinitiesJSON.get(group.getGroupName());
                Map<ConsumerAgentGroup, Double> groupAffinity = new HashMap<ConsumerAgentGroup, Double>();
                for (String targetGroupString : groupAffinityJSON.keySet()) {
                    groupAffinity.put(cagMapping.get(targetGroupString), (Double) groupAffinityJSON.get(targetGroupString));
                    fooLog.debug("Reading an affinity of {} between {} and {}",groupAffinityJSON.get(targetGroupString), group, targetGroupString);
                }
                affinities.put(group, groupAffinity);
            }
            return new AgentConfiguration(consumerAgentGroups, posAgents, noAgentsPerAgentGroup, new ConsumerAgentGroupAffinities(affinities), companyAgentConfigurations, policyAgentConfiguration);
        } catch (IllegalArgumentException e) {
                throw e;
        } catch (JsonMappingException jme) {
            throw jme;
        } catch (JsonParseException jpe) {
            throw jpe;
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    /**
     * Method to load the ConsumerAgentCommunicationScheme
     * specified by the string, with the specified message scheme
     *
     * @param communicationSchemeConfiguration The configuration HashMap for the CommunicationScheme of the ConsumerAgentGroup configuration
     * @param distributions The distributions used in the simulation (needs to contain at least the distribution associated with the numberMessagesSentPerTimeUnit of the MessageScheme)
     * @return The CommunicationScheme encoded by the communicationSchemeConfiguration
     * @throws IllegalArgumentException Will be thrown when the qualifier refers to a scheme not implemented, a relevant key is missing or the MessageScheme loader throws an IllegalArgumentException
     * @throws ClassCastException Will be thrown when an object can't be cast accordingly
     */
    private static CommunicationScheme loadConsumerAgentCommunicationScheme(HashMap<String, Object> communicationSchemeConfiguration, Map<String, Distribution> distributions) throws IllegalArgumentException{
        if(!communicationSchemeConfiguration.containsKey("communicationScheme")) throw new IllegalArgumentException("No configuration for the CommunicationScheme is available in the consumerAgentCommunicationScheme configuration!\nPlease provide a valid configuration!");
        else if(!communicationSchemeConfiguration.containsKey("consumerAgentMessageScheme")) throw new IllegalArgumentException("No configuration for the ConsumerAgentMessageScheme is available in the consumerAgentCommunicationScheme configuration!\nPlease provide a valid configuration!");
        else {
            try {
                String consumerAgentCommunicationScheme = (String) communicationSchemeConfiguration.get("communicationScheme");
                ConsumerAgentMessageScheme consumerAgentMessageScheme = loadConsumerAgentMessageScheme((HashMap<String, Object>) communicationSchemeConfiguration.get("consumerAgentMessageScheme"), distributions);
                switch (consumerAgentCommunicationScheme) {
                    case "ImmediateCommunicationScheme":
                        return new ImmediateCommunicationScheme(consumerAgentMessageScheme);
                    case "ContinuousMetricCommunicationScheme":
                        return new ContinuousMetricCommunicationScheme(consumerAgentMessageScheme);
                    default:
                        throw new IllegalArgumentException("consumerAgentCommunicationScheme " + consumerAgentCommunicationScheme + " is not implemented.\nPlease provide a valid CommunicationScheme!");
                }
            } catch (IllegalArgumentException iae) {
                throw iae;
            } catch (ClassCastException cce){
                throw cce;
            }
        }
    }

    /**
     * Loads the policy agent based on the configuration of the policy agent in the PolicyAgent.json in the configPath
     *
     * @param configPath The path where the configuration for the agent lies
     * @return The policyAgentConfiguration used in the simulation
     * @throws IllegalArgumentException Will be thrown when one of the arguments is errornous,e.g. a scheme isnt implemented
     * @throws IOException Will be thrown when an error occurs reading the configuration file for the policy agent
     * @throws JsonParseException Will be thrown when the configuration of the policy agent experiences a JsonParseException
     * @throws JsonMappingException Will be thrown when the configuration of the policy agent experiences a JsonMappingException
     */
    private static PolicyAgentConfiguration loadPolicyAgent(String configPath) throws IOException, JsonMappingException, JsonParseException, IllegalArgumentException{
        try{
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> policyAgentConfigurationJSON = mapper.readValue(new File(configPath+"PolicyAgent.json"), HashMap.class);
            ConsumerPolicyScheme consumerPolicyScheme = PolicyAgentFactory.consumerPolicySchemeLoader((String) policyAgentConfigurationJSON.get("ConsumerPolicyScheme"));
            MarketEvaluationScheme marketEvaluationScheme = PolicyAgentFactory.marketEvaluationSchemeLoader((String) policyAgentConfigurationJSON.get("MarketEvaluationScheme"));
            ProductPolicyScheme productPolicyScheme = PolicyAgentFactory.productPolicySchemeLoader((String) policyAgentConfigurationJSON.get("ProductPolicyScheme"));
            RegulatoryPolicyScheme regulatoryPolicyScheme = PolicyAgentFactory.regulatoryPolicySchemeLoader((String) policyAgentConfigurationJSON.get("RegulatoryPolicyScheme"));
            double informationAuthority;
            if(!policyAgentConfigurationJSON.containsKey("informationAuthority")) throw new IllegalArgumentException("Policy agent configuration doesn't contain the informationAuthority.\nPlease provide a valid configuration!");
            else informationAuthority = (Double) policyAgentConfigurationJSON.get("informationAuthority");
            return new PolicyAgentConfiguration(productPolicyScheme, consumerPolicyScheme, regulatoryPolicyScheme, marketEvaluationScheme, informationAuthority);
        } catch (JsonMappingException jme) {
            throw jme;
        } catch (JsonParseException jpe) {
            throw jpe;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    /**
     * Method to load a company agent configuration from a configuration file
     *
     * @param file File to load the configuration from
     * @param distributions The distributions used in the simulation (needs to at least contain the one associated with the numberOfMessages)
     * @return The configuration object described by the file
     * @throws IOException Will be thrown when an IOException is thrown in reading the file
     * @throws JsonMappingException Will be thrown when an IOException is thrown in loading the schemes specified in the file
     * @throws JsonParseException Will be thrown when an IOException is thrown in loading the schemes specified in the file
     * @throws IllegalArgumentException Will be thrown when an IllegalArgumentException is thrown in loading the schemes specified in the file
     */
    private static CompanyAgentConfiguration loadCompanyAgentConfiguration(File file, Map<String, Distribution> distributions) throws IOException, JsonMappingException, JsonParseException, IllegalArgumentException{
        ObjectMapper mapper = new ObjectMapper();
        try {
            HashMap<String, Object> companyAgentGroupMap = mapper.readValue(file, HashMap.class);
            String[] fileNameArray = file.getName().split("\\.");
            if(!companyAgentGroupMap.containsKey("productQualityManipulationScheme")) throw new IllegalArgumentException("Company agent "+fileNameArray[0]+" doesn't have a value for the productQualityManipulationScheme!!");
            else if(!companyAgentGroupMap.containsKey("productQualityManipulationSchemeParameters")) throw new IllegalArgumentException("Company agent configuration in "+file+" doesn't contain the productQualityManipulationSchemeParameters.\nPlease provide a valid configuration!");
            ProductQualityManipulationScheme productQualityManipulationScheme = CompanyAgentFactory.productQualityManipulationSchemeLoader((String) companyAgentGroupMap.get("productQualityManipulationScheme"), (HashMap<String, Object>) companyAgentGroupMap.get("productQualityManipulationSchemeParameters"));
            if(!companyAgentGroupMap.containsKey("managementDecisionScheme")) throw new IllegalArgumentException("Company agent "+fileNameArray[0]+" doesn't have a value for the managementDecisionScheme!!");
            else if(!companyAgentGroupMap.containsKey("managementDecisionSchemeParameters")) throw new IllegalArgumentException("Company agent configuration in "+file+" doesn't contain the managementDecisionSchemeParameters.\nPlease provide a valid configuration!");
            ManagementDecisionScheme managementDecisionSchemeScheme = CompanyAgentFactory.managementDecisionSchemeLoader((String) companyAgentGroupMap.get("managementDecisionScheme"), (HashMap<String, Object>) companyAgentGroupMap.get("managementDecisionSchemeParameters"));
            AdvertisementScheme advertisementScheme;
            if(!companyAgentGroupMap.containsKey("advertisementScheme")) throw new IllegalArgumentException("Company agent configuration in "+file+" doesn't contain the advertisementScheme.\nPlease provide a valid configuration!");
            else if(!companyAgentGroupMap.containsKey("advertisementSchemeParameters")) throw new IllegalArgumentException("Company agent configuration in "+file+" doesn't contain the advertisementSchemeParameters.\nPlease provide a valid configuration!");
            else advertisementScheme = CompanyAgentFactory.advertisementSchemeLoader((String) companyAgentGroupMap.get("advertisementScheme"), (HashMap<String, Object>) companyAgentGroupMap.get("advertisementSchemeParameters"), distributions);
            double informationAuthority;
            if(!companyAgentGroupMap.containsKey("informationAuthority")) throw new IllegalArgumentException("Company agent configuration in "+file+" doesn't contain the informationAuthority.\nPlease provide a valid configuration!");
            else informationAuthority = (Double) companyAgentGroupMap.get("informationAuthority");
            return new CompanyAgentConfiguration(productQualityManipulationScheme, managementDecisionSchemeScheme, advertisementScheme, fileNameArray[0], informationAuthority);
        } catch (JsonMappingException jme) {
            throw jme;
        } catch (JsonParseException jpe) {
            throw jpe;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    /**
     * Method to load a consumer agent group from a configuration file.
     *
     * @param file The file storing the configuration of the consumer agent group to load (in a HashMap style form like .json)
     * @param productConfiguration The configuration object for the products
     * @param distributions Map associating the distributions used for the configuration of these agents to their names (as strings)
     * @param productGroupAttributes The product group attributes for establishing product perceptions for the respective agents
     * @param values The values referenced by the agents to create
     * @param decisionConfiguration The decision configuration the decision functions of the agents use
     * @return An instance of the ConsumerAgentGroup as corresponding to the configuration within the file specified.
     * @throws IllegalArgumentException Will be thrown when one of the arguments used for the creation of the ConsumerAgentGroup has an illegal value
     * @throws IOException Will be thrown when an error occurs handling the file storing the configuration of the referred ConsumerAgentGroup
     * @throws JsonParseException Will be thrown upon a parse error for the json file corresponding to the file handler
     * @throws JsonMappingException Will be thrown upon a mapping error for the json file corresponding to the file handler
     */
    private static ConsumerAgentGroup loadConsumerAgentGroup(File file, ProductConfiguration productConfiguration, Map<String, Distribution> distributions, Set<ProductGroupAttribute> productGroupAttributes, Set<Value> values, DecisionConfiguration decisionConfiguration) throws IllegalArgumentException, IOException, JsonMappingException, JsonParseException{
        ObjectMapper mapper = new ObjectMapper();
        try {
            HashMap<String, Object> consumerAgentGroupMap = mapper.readValue(file, HashMap.class);
            //set consumerAgentGroupAttributes
            Set<ConsumerAgentGroupAttribute> caga;// = new HashSet<ConsumerAgentGroupAttribute>();
            if(!consumerAgentGroupMap.containsKey("consumerAgentGroupAttributes"))  throw new IllegalArgumentException("read file "+file+" does not list any consumerAgentGroupAttributes!!");
            else{
                try {
                    caga = loadConsumerAgentGroupAttributes(consumerAgentGroupMap.get("consumerAgentGroupAttributes"), distributions);
                } catch (IllegalArgumentException e) {
                    throw e;
                }
            }
            //set productGroupAwarenessDistribution
            HashMap<ProductGroup, BooleanDistribution> pgad;
            if(!consumerAgentGroupMap.containsKey("productGroupAwarenessDistribution")) throw new IllegalArgumentException("No entry 'productGroupAwarenessDistribution' could be found in the configuration file "+file.toString());
            else{
                try {
                    pgad = loadProductGroupAwarenessDistribution(consumerAgentGroupMap.get("productGroupAwarenessDistribution"), productConfiguration.getProductGroups(), distributions);
                } catch (IllegalArgumentException e) {
                    throw e;
                }
            }
            //set fixedProductAwarenessDistribution
            HashMap<FixedProductDescription, BooleanDistribution> fpad;
            if(!consumerAgentGroupMap.containsKey("fixedProductsAwarenessDistribution")) throw new IllegalArgumentException("No entry 'fixedProductsAwarenessDistribution' was found in the configuration file "+file.toString());
            else{
                try {
                    fpad = loadFixedProductAwarenessDistribution(consumerAgentGroupMap.get("fixedProductsAwarenessDistribution"), productConfiguration, distributions, file);
                } catch (IllegalArgumentException e) {
                    throw e;
                }
            }
            //set consumerAgentGroupValues
            HashMap<Value, UnivariateDistribution> cagp;
            if(!consumerAgentGroupMap.containsKey("consumerAgentGroupValues")) throw new IllegalArgumentException("No entry 'consumerAgentGroupValues' was found in the configuration file "+file.toString());
            else{
                try {
                    cagp = loadConsumerAgentGroupValues(consumerAgentGroupMap.get("consumerAgentGroupValues"), values, distributions);
                } catch (IllegalArgumentException e) {
                    throw e;
                }
            }
            //get name
            String[] fileNameArray = file.getName().split("\\.");
            //get initial product configuration
            Map<FixedProductDescription, UnivariateDistribution> initialProductConfiguration;
            if(!consumerAgentGroupMap.containsKey("initialProductConfiguration")) throw new IllegalArgumentException("No entry 'initialProductConfiguration' was found in the configuration file "+file.toString());
            else{
                try {
                    initialProductConfiguration = loadInitialProductConfiguration(consumerAgentGroupMap.get("initialProductConfiguration"), productConfiguration.getProductGroups(), distributions);
                } catch (IllegalArgumentException e) {
                    throw e;
                }
            }
            //load informationAuthority
            double informationAuthority;
            if(!consumerAgentGroupMap.containsKey("informationAuthority")) throw new IllegalArgumentException("No entry 'informationAuthority' was found in the configuration file "+file.toString());
            else{
                try {
                    informationAuthority = (Double) consumerAgentGroupMap.get("informationAuthority");
                } catch (ClassCastException e) {
                    throw e;
                }
            }
            //load needDevelopmentScheme
            NeedDevelopmentScheme needDevelopmentScheme;
            if(!consumerAgentGroupMap.containsKey("needDevelopmentScheme")) throw new IllegalArgumentException("No entry 'needDevelopmentScheme' was found in the configuration file "+file.toString());
            else{
                try {
//                    if(!consumerAgentGroupMap.containsKey("needDevelopment")) throw new IllegalArgumentException("No entry 'needDevelopment' was found in the configuration file "+file.toString());
                    needDevelopmentScheme = NeedDevelopmentFactory.createNeedDevelopmentScheme((String) consumerAgentGroupMap.get("needDevelopmentScheme"), loadNeedMap(consumerAgentGroupMap.get("needIndicatorMap"), LazynessHelper.aggregateNeeds(productConfiguration.getProductGroups())));
                } catch (IllegalArgumentException e) {
                    throw e;
                }
            }
            //load decisionMakingProcess
            ConsumerAgentAdoptionDecisionProcess decisionMakingProcess;
            if(!consumerAgentGroupMap.containsKey("decisionProcessEmployed")) throw new IllegalArgumentException("No entry 'decisionProcessEmployed' was found in the configuration file "+file.toString());
            else{
                decisionMakingProcess = decisionConfiguration.getDecisionMakingProcess((String) consumerAgentGroupMap.get("decisionProcessEmployed"));
            }
            //load spatial distribution
            SpatialDistribution spatialDistribution;
            if(!consumerAgentGroupMap.containsKey("spatialDistribution")) throw new IllegalArgumentException("No entry 'spatialDistribution' was found in the configuration file "+file.toString());
            else{
                spatialDistribution = (SpatialDistribution) distributions.get(consumerAgentGroupMap.get("spatialDistribution"));
            }
            //load perception schemes map
            Map<ProductGroupAttribute, PerceptionSchemeConfiguration> productPerceptionSchemes = loadProductPerceptionSchemes(productConfiguration, (HashMap<String, Object>) consumerAgentGroupMap.get("perceptionSchemeConfiguration"), distributions, file);
            //load communication scheme
            CommunicationScheme communicationScheme = loadConsumerAgentCommunicationScheme((HashMap<String, Object>) consumerAgentGroupMap.get("consumerAgentCommunicationScheme"), distributions);
            return new ConsumerAgentGroup(caga, fileNameArray[0], new HashSet<ConsumerAgent>(), communicationScheme , productPerceptionSchemes, pgad, fpad, decisionMakingProcess, cagp, spatialDistribution, initialProductConfiguration, needDevelopmentScheme, informationAuthority);
        } catch (JsonParseException jpe){
            throw jpe;
        }catch (JsonMappingException jme){
            throw jme;
        }
        catch (IOException e) {
           throw e;
        }
    }

    /**
     * Method to load the ProductPerceptionSchemes for the respective ProductGroupAttributes
     * based on the perceptionSchemeConfiguration.
     * Will load all perceptionSchemeConfigurations belonging to product attributes in the productConfiguration
     *
     * @param productConfiguration The configuration of the products of the simulation; Used to load the perceptionSchemeConfiguration of all ProductGroupAttributes set in the configuration
     * @param perceptionSchemeConfiguration The configuration for the PerceptionSchemes belonging to the ProductGroupAttributes listed in the productConfiguration
     * @param consumerAgentConfiguration The file containing the configuration of the current consumer agent group
     * @return A map of all ProductGroupAttributes in the simulation and their respective PerceptionSchemeConfiguration
     * @throws IOException Will be thrown when an error occurs reading the file from which the perceptionSchemeConfiguration is read
     * @throws IllegalArgumentException Will be thrown when a perceptionSchemeConfiguration lacks a relevant parameter or uses an unimplemented perceptionScheme
     */
    private static Map<ProductGroupAttribute, PerceptionSchemeConfiguration> loadProductPerceptionSchemes(ProductConfiguration productConfiguration, HashMap<String, Object> perceptionSchemeConfiguration, Map<String, Distribution> distributionMap, File consumerAgentConfiguration) throws IOException, IllegalArgumentException {
        Map<ProductGroupAttribute, PerceptionSchemeConfiguration> perceptionConfigurationMap = new HashMap<>();
        try{
            for(ProductGroup currentProductGroup : productConfiguration.getProductGroups()){
                if(!perceptionSchemeConfiguration.containsKey(currentProductGroup.getGroupName())) throw new IllegalArgumentException("Errornous configuration!! No perceptionSchemeConfiguration set for product group "+currentProductGroup.getGroupName()+" of consumer agent group "+consumerAgentConfiguration.getName()+"!!\nPlease provide a valid configuration!!");
                Map<String, Object> productGroupPerceptionSchemeConfiguration = (HashMap<String, Object>) perceptionSchemeConfiguration.get(currentProductGroup.getGroupName());
                for(ProductGroupAttribute currentPGA : currentProductGroup.getProductGroupAttributes()){
                    if(!productGroupPerceptionSchemeConfiguration.containsKey(currentPGA.getName())) throw new IllegalArgumentException("Errornous configuration!! No perceptionSchemeConfiguration set for product group attribute "+currentPGA.getName()+" of product group "+currentProductGroup.getGroupName()+"of consumer agent group "+consumerAgentConfiguration.getName()+"!!\nPlease provide a valid configuration!!");
                    Map<String, Object> productAttributePerceptionMap = (HashMap<String, Object>) productGroupPerceptionSchemeConfiguration.get(currentPGA.getName());
                    if(!productAttributePerceptionMap.containsKey("perceptionScheme")) throw new IllegalArgumentException("Errornous configuration!! No perceptionScheme set for the perception configuration of product group attribute "+currentPGA.getName()+" of product group "+currentProductGroup.getGroupName()+"!!\nPlease provide a valid configuration!!");
                    else{
                        switch((String) productAttributePerceptionMap.get("perceptionScheme")){
                            case "perceptionHistogram" :
                                perceptionConfigurationMap.put(currentPGA, loadHistogramPerceptionScheme((HashMap<String, Object>) productAttributePerceptionMap.get("parameters"), distributionMap));
                                break;
                            case "TrueValueProductAttributePerceptionScheme" :
                                perceptionConfigurationMap.put(currentPGA, new PerceptionSchemeConfiguration("TrueValueProductAttributePerceptionScheme", new HashMap<>(), new TrueValueInitializationScheme()));
                                break;
                            case "MemoryLessProductAttributePerceptionScheme":
                                if(!productAttributePerceptionMap.containsKey("parameters")) throw new IllegalArgumentException("MemoryLessProductAttributePerceptionScheme is not equipped with parameters!");
                                else {
                                    HashMap<String, Object> perceptionSchemeParameters = (HashMap<String, Object>) productAttributePerceptionMap.get("parameters");
                                    if (!perceptionSchemeParameters.containsKey("initialPerception"))
                                        throw new IllegalArgumentException("The parameter map of the MemoryLessProductAttributePerceptionScheme does not contain the parameter 'initialPerception'!\nPlease make sure to provide a valid configuration!");
                                    else
                                        perceptionConfigurationMap.put(currentPGA, new PerceptionSchemeConfiguration("MemoryLessProductAttributePerceptionScheme", perceptionSchemeParameters, new StochasticPerceptionInitializationScheme((BoundedUnivariateDistribution) distributionMap.get(perceptionSchemeParameters.get("initialPerception")))));
                                }
                                break;
                            default: throw new IllegalArgumentException("Perception scheme "+(String) productAttributePerceptionMap.get("perceptionScheme")+" not implemented!!");
                        }
                    }
                }
            }
            return perceptionConfigurationMap;
        }catch (IllegalArgumentException iae) {
            throw iae;
        } catch (ClassCastException cce) {
            throw cce;
        }
    }

    /**
     * Helper method to load the HistogramPerceptionScheme for histogram-based PerceptionSchemes,
     * based on the histograms parameters.
     * Will throw exceptions when the parameters are not in the right form.
     *
     * @param histogramParameters The parameters the HPS is based on
     * @return The HistogramPerceptionSchemeConfiguration defined by the configuration
     * @throws IllegalArgumentException Will be thrown when a necessary parameters is not in the parameter map
     * @throws ClassCastException Will be thrown when a necessary parameter can't be cast into the data type it is required to have
     */
    private static PerceptionSchemeConfiguration loadHistogramPerceptionScheme(HashMap<String, Object> histogramParameters, Map<String, Distribution> distributionMap) throws IllegalArgumentException, ClassCastException{
        try {
            if(!histogramParameters.containsKey("noBins")) throw new IllegalArgumentException("The parameter map for the respective histogram does not specify the number of bins!!");
            else if(!histogramParameters.containsKey("lambda")) throw new IllegalArgumentException("The parameter map for the respective histogram does not specify the lambda value!!");
            else if(!histogramParameters.containsKey("histogramMin")) throw new IllegalArgumentException("The parameter map for the respective histogram does not specify the histogramMin value!!");
            else if(!histogramParameters.containsKey("histogramMax")) throw new IllegalArgumentException("The parameter map for the respective histogram does not specify the histogramMax value!!");
            else if(!histogramParameters.containsKey("histogramInitializationScheme")) throw new IllegalArgumentException("The parameter map for the respective histogram does not specify the histogramInitializationScheme!!");
            else{
                HistogramInitializationScheme hIScheme;
                if(histogramParameters.containsKey("histogramInitializationSchemeParameters")) hIScheme = HistogramInitializationFactory.createHistogramInitializationScheme((String) histogramParameters.get("histogramInitializationScheme"), (HashMap<String, Object>) histogramParameters.get("histogramInitializationSchemeParameters"), distributionMap);
                else hIScheme = HistogramInitializationFactory.createHistogramInitializationScheme((String) histogramParameters.get("histogramInitializationScheme"), new HashMap<String, Object>(), distributionMap);
                return new HistogramPerceptionSchemeConfiguration("perceptionHistogram", new HashMap<>(), (int) histogramParameters.get("noBins"), (double) histogramParameters.get("lambda"), (double) histogramParameters.get("histogramMin"), (double) histogramParameters.get("histogramMax"), hIScheme);
            }
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (ClassCastException cce) {
            throw cce;
        }
    }

//    /**
//     * Helping method for 'loadConsumerAgentGroup' that extracts the importance attitude map for a ConsumerAgentGroup
//     * based on the configuration object importanceAttitude
//     *
//     * @param importanceAttitude Object representing the importanceAttitude for an agent group. Must parse to a String-Double hashMap, with keys corresponding to valid product groups
//     * @param productGroups A set of product groups containing at least the product group the configuration object importanceAttitude assigns values to
//     * @return A map of ProductGroups and their corresponding importanceAttitude (how important the attitude of an agent is upon evaluating a product of the corresponding group)
//     * @throws IllegalArgumentException Will be thrown when importanceAttitude is parsed to something empty, contains an entry not corresponding to the name of a product in the set of product groups or the set of product groups is illegal itself
//     */
//    private static Map<ProductGroup,Double> loadImportanceAttitudeMap(Object importanceAttitude, Set<ProductGroup> productGroups) throws IllegalArgumentException{
//        HashMap<String, Double> importanceAttitudeProductGroups;
//        try {
//            importanceAttitudeProductGroups = (HashMap<String, Double>) importanceAttitude;
//        } catch (ClassCastException cce) {
//            throw new IllegalArgumentException("Object describing the importanceAttitude couldn't be cast to a HashMap<String, Double>; \nPlease provide valid arguments!!\n"+ cce.toString());
//        }
//        Map<ProductGroup, Double> importanceAttitudeMap = new HashMap<ProductGroup, Double>();
//        Map<String, ProductGroup> productGroupMap;
//        try {
//            productGroupMap= StructureEnricher.attachProductGroupNames(productGroups);
//        } catch (IllegalArgumentException e) {
//            throw e;
//        }
//        for(String attitudePG : importanceAttitudeProductGroups.keySet()){
//            if(productGroupMap.containsKey(attitudePG)) importanceAttitudeMap.put(productGroupMap.get(attitudePG), importanceAttitudeProductGroups.get(attitudePG));
//            else throw new IllegalArgumentException("The provided set of productGroups does not contain the productGroup "+attitudePG+", which is associated with an importanceAttitude in the corresponding configuration!!");
//        }
//        return importanceAttitudeMap;
//    }

    /**
     * Helping method for 'loadConsumerAgentGroup' that extracts the initial product configuration for a ConsumerAgentGroup
     * based on the configuration object initialProductConfigurationObject
     *
     * @param initialProductConfigurationObject Object representing the initialProductConfigurationObject for an agent group. Must parse to a String-String hashMap, with keys corresponding to valid fixedProductDescriptions
     * @param productGroups A set of product groups containing at least the product groups the configuration object initialProductConfigurationObject refers to (as product group names)
     * @param distributions A set of distributions containing at least the distributions with the names the configuration object initialProductConfigurationObject refers to
     * @return A map of FixedProductDescriptions and the corresponding (univariate) distributions, describing how the corresponding fixed product is distributed among the corresponding agent group at the beginning of the simulation
     * @throws IllegalArgumentException Will be thrown when initialProductConfigurationObject can't be parse, contains an entry not corresponding to the name of a fixed product, a non-existing distribution is referred or the name of a fixed product can't be derived.
     */
    private static Map<FixedProductDescription,UnivariateDistribution> loadInitialProductConfiguration(Object initialProductConfigurationObject, Set<ProductGroup> productGroups, Map<String, Distribution> distributions) throws IllegalArgumentException{
        Map<String, String> initialProductConfigurationStrings;
        try{
            initialProductConfigurationStrings = (Map<String, String>) initialProductConfigurationObject;
        } catch (ClassCastException cce){
            throw new IllegalArgumentException("Object describing the initialProductConfiguration couldn't be cast to a HashMap<String, String>; \nPlease provide valid arguments!!\n"+ cce.toString());
        }
        Map<FixedProductDescription, UnivariateDistribution> initialProductConfiguration = new HashMap<FixedProductDescription, UnivariateDistribution>(initialProductConfigurationStrings.size());
        Map<String, FixedProductDescription> fixedProductMap;
        try {
            fixedProductMap = StructureEnricher.attachFixedProductDescriptionNames(LazynessHelper.fixedProductDescriptionsFromProductGroups(ValueConversionHelper.ProductGroupCollectionToSet(productGroups)));
        } catch (IllegalArgumentException e) {
            throw e;
        }
        for(String productConfigurationString : initialProductConfigurationStrings.keySet()){
            if(!fixedProductMap.containsKey(productConfigurationString)) throw new IllegalArgumentException("Fixed product "+productConfigurationString+" to which the initial product configuration refers is not a valid fixedProduct within the simulation, since it is not contained in the list of fixed products " +fixedProductMap);
            else if(!distributions.containsKey(initialProductConfigurationStrings.get(productConfigurationString))) throw new IllegalArgumentException("Distribution "+initialProductConfigurationStrings.get(productConfigurationString)+" to link the initial distribution of fixed products to the respective agent group can't be found!\n Make sure a distibution corresponds to "+initialProductConfigurationStrings.get(productConfigurationString)+" when modeling!");
            else initialProductConfiguration.put(fixedProductMap.get(productConfigurationString), (UnivariateDistribution) distributions.get(initialProductConfigurationStrings.get(productConfigurationString)));
        }
        return initialProductConfiguration;
    }

    /**
     * Helping method for 'loadConsumerAgentGroup' that extracts the need configuration for a ConsumerAgentGroup
     * based on the configuration object needDevelopment
     *
     * @param needDevelopment Object representing the need configuration for an agent group. Must parse to an ArrayList of String-Object hashMaps, with keys corresponding to valid names of needs and Objects that parse to Double, representing the needIndicator of the agent
     * @param needs A set of needs that needs to include at least the needs referred to needDevelopment with valid (String) names
     * @return A map of needs and their correspondent needIndicator values as representing the needIndicatorMap for the respective ConsumerAgentGroup
     * @throws IllegalArgumentException Will be thrown when the needDevelopment configuration object can't be casted to an ArrayList of String-Object HashMaps, the name of needs can't be derived or an unrecognized need is referenced in the need configuration of the ConsumerAgentGroup
     */
    private static Map<Need,Double> loadNeedMap(Object needDevelopment, Set<Need> needs) throws IllegalArgumentException{
        Map<Need, Double> needDevelopmentMap = new HashMap<Need, Double>();
        Map<String, Double> needDevelopmentStringMap;
        try {
            needDevelopmentStringMap = (Map<String, Double>) needDevelopment;
        } catch (ClassCastException cce) {
            throw new IllegalArgumentException("Object describing the needDevelopment couldn't be cast to a String-double HashMap; \nPlease provide valid arguments!!\n"+ cce.toString());
        }
        Map<String, Need> needMap;
        try {
            needMap = StructureEnricher.attachNeedNames(needs);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        for (String needDevelopmentString : needDevelopmentStringMap.keySet()) {
            if (!needMap.containsKey(needDevelopmentString))
                throw new IllegalArgumentException("Configuration for corresponding consumerAgent group refers to an unrecognized need: " + needDevelopmentString + "!\nPlease change this to a valid need or add this to the need configuration!");
            else
                needDevelopmentMap.put(needMap.get(needDevelopmentString), needDevelopmentStringMap.get(needDevelopmentString));
        }
        return needDevelopmentMap;
    }

    /**
     * Helping method for 'loadConsumerAgentGroup' that extracts the value configuration for a ConsumerAgentGroup
     * based on the configuration object consumerAgentGroupValues
     *
     * @param consumerAgentGroupValues Object representing the value-distribution configuration for an agent group. Must parse to an ArrayList of String-Object hashMaps, with keys value and strength and values for 'value'-keys valid Values within the simulation and values for 'strength'-keys being (valid) names of distributions (as in the argument)
     * @param values A set of values containing the Values referred in consumerAgentGroupValues (their corresponding strings). If it doesn't exist it will be created
     * @param distributions A map of distributions and their corresponding names (as Strings), containing at least the distributions referred to in the consumerAgentGroupValues
     * @return A map of the Values employed by the corresponding ConsumerAgentGroup and their respective distributions as referred to in the consumerAgentGroupValues configuration object
     * @throws IllegalArgumentException
     */
    private static HashMap<Value,UnivariateDistribution> loadConsumerAgentGroupValues(Object consumerAgentGroupValues, Set<Value> values, Map<String, Distribution> distributions) throws IllegalArgumentException{
        HashMap<String, String> cagvMap;
        try{
            cagvMap = (HashMap<String, String>) consumerAgentGroupValues;
        } catch (ClassCastException cce){
            throw new IllegalArgumentException("Object describing the values of the respective ConsumerAgentGroup couldn't be cast to an ArrayList of String-Object HashMaps; \nPlease provide valid arguments!!\n"+ cce.toString());
        }
        HashMap<Value, UnivariateDistribution> cagp = new HashMap<Value, UnivariateDistribution>();
        Map<String, Value> valueMap;
        try {
            valueMap = StructureEnricher.attachValueNames(values);
        } catch (IllegalArgumentException e){
            throw e;
        }
        for (String currentValueString : cagvMap.keySet()) {
            if (!valueMap.keySet().contains(currentValueString)){
                Value newValue = new Value(currentValueString);
                values.add(newValue);
                valueMap.put(currentValueString, newValue);
            }
            if (!distributions.containsKey(cagvMap.get(currentValueString))) throw new IllegalArgumentException("Value " + currentValueString + " refers to a distribution not configured (or incorrectly passed to the ConsumerAgentGroup loader).\nPlease ensure that only existing distributions are referred or add the distribution " + cagvMap.get(currentValueString) + " to the configuration.");
            else cagp.put(valueMap.get(currentValueString), (UnivariateDistribution) distributions.get(cagvMap.get(currentValueString)));
        }
        return cagp;
    }

    /**
     * Helping method for 'loadConsumerAgentGroup' that extracts the awareness configuration for fixed products for a ConsumerAgentGroup
     * based on the configuration object fixedProductsAwarenessDistribution
     *
     * @param fixedProductsAwarenessDistribution Object representing the FixedProductDescription-BooleanDistribution configuration for an agent group. Must parse to a HashMap of String-String hashMaps, with keys referring to the names of FixedProducts and values corresponding to the names of the the (boolean) distributions to use
     * @param productConfiguration The configuration of the products within the simulation
     * @param distributions A map of distributions and their corresponding names (as Strings), containing at least the distributions referred to in the values of the map encoded by fixedProductsAwarenessDistribution
     * @return A map of the FixedProductDescriptions and their distributions as configured by FixedProductDescription
     * @throws IllegalArgumentException Will be thrown when the fixedProductsAwarenessDistribution can't be cast to a String-String hashmap, extracting and attaching the names to the fixedProductDescriptions goes wrong or one of the entries in the fixedProductsAwarenessDistribution is not included in the fixed products or distributions
     */
    private static HashMap<FixedProductDescription,BooleanDistribution> loadFixedProductAwarenessDistribution(Object fixedProductsAwarenessDistribution, ProductConfiguration productConfiguration, Map<String, Distribution> distributions, File referenceFile) throws IllegalArgumentException{
        HashMap<FixedProductDescription, BooleanDistribution> fpad = new HashMap<FixedProductDescription, BooleanDistribution>();
        HashMap<String, String> fpadMap;
        try{
            fpadMap = (HashMap<String, String>) fixedProductsAwarenessDistribution;
        } catch (ClassCastException cce){
            throw new IllegalArgumentException("Object describing the fixedProductsAwarenessDistribution of the respective ConsumerAgentGroup couldn't be cast to a String-String HashMap; \nPlease provide valid arguments!!\n"+ cce.toString());
        }
        HashMap<String, FixedProductDescription> fpd;
        try{
            fpd = StructureEnricher.attachFixedProductDescriptionNames(LazynessHelper.fixedProductDescriptionsFromProductGroups(productConfiguration.getProductGroups()));
            for(MarketIntroductionEventDescription mied : productConfiguration.getMarketIntroductionEvents()){
                fpd.put(mied.getCorrespondingFixedProduct().getName(), mied.getCorrespondingFixedProduct());
            }
        }  catch (IllegalArgumentException e){
            throw e;
        }
        for (String key : fpadMap.keySet()) {
            if(!fpd.containsKey(key)) throw new IllegalArgumentException("No FixedProduct with the name "+key+" could be found in file "+referenceFile+"!!\nPlease provide valid arguments!!");
            else if(!distributions.containsKey(fpadMap.get(key))) throw new IllegalArgumentException("Distribution "+fpadMap.get(key)+" is not in the set of configured distributions (or this was passed incorrectly to the loader of this ConsumerAgentGroup!!\nPlease provide valid data!!");
            else fpad.put(fpd.get(key), (BooleanDistribution) distributions.get(fpadMap.get(key)));
        }
        return fpad;
    }

    /**
     * Helping method for 'loadConsumerAgentGroup' that extracts the awareness distributions for product groups for a ConsumerAgentGroup
     * based on the configuration object productGroupAwarenessDistribution
     *
     * @param productGroupAwarenessDistribution Object representing the productGroupAwarenessDistribution-BooleanDistribution configuration for an agent group. Must parse to a HashMap of String-String hashMaps, with keys referring to the names of product groups and values corresponding to the names of the the (boolean) distributions to use
     * @param productGroups A set of productGroups containing the product groups referred in the keys of productGroupAwarenessDistribution
     * @param distributions A map of distributions and their corresponding names (as Strings), containing at least the distributions referred to in the values of the map encoded by productGroupAwarenessDistribution
     * @return A map of the ProductGroups and their distributions as configured by productGroupAwarenessDistribution
     * @throws IllegalArgumentException Will be thrown when the productGroupAwarenessDistribution can't be cast to a String-String hashmap, one of the entries in the productGroupAwarenessDistribution does not correspond to the product groups or distributions or the names of the product groups cant be attached
     */
    private static HashMap<ProductGroup, BooleanDistribution> loadProductGroupAwarenessDistribution(Object productGroupAwarenessDistribution, Set<ProductGroup> productGroups, Map<String, Distribution> distributions) throws IllegalArgumentException{
        Map<String, ProductGroup> productGroupMap;
        try {
            productGroupMap = StructureEnricher.attachProductGroupNames(productGroups);
        } catch (IllegalArgumentException e) {
            throw e;
        }
        HashMap<String, String> pgadMap;
        try {
            pgadMap = (HashMap<String, String>) productGroupAwarenessDistribution;
        } catch (ClassCastException cce){
            throw new IllegalArgumentException("An error occurred parsing the productGroupAwarenessDistribution to a string-string hashmap in extracting the ProductGroupAwarenessDistribution!!\n"+cce.toString());
        }
        HashMap<ProductGroup, BooleanDistribution> pgad = new HashMap<ProductGroup, BooleanDistribution>(pgadMap.keySet().size());
        //Map<String, ProductGroup> productGroupMap = StructureEnricher.attachProductGroupNames(productConfiguration.getProductGroups());
        for (String pgadKey : pgadMap.keySet()) {
            if(!productGroupMap.containsKey(pgadKey)) throw new IllegalArgumentException("Entry "+pgadKey+" for productGroup of the productGroupAwarenessDistribution of the respective ConsumerAgentGroup is not in the set of product groups passed to loadProductGroupAwarenessDistribution!!\nPlease make sure to use a valid identifier in the configuration and to pass valid data to the function");
            else if(!distributions.containsKey(pgadMap.get(pgadKey))) throw new IllegalArgumentException("Distribution "+pgadMap.get(pgadKey)+" referenced in the productGroupAwarenessDistribution configuration of the respective ConsumerAgentGroup is not in the set of product groups passed to loadProductGroupAwarenessDistribution!!\nPlease make sure to use a valid identifier in the configuration and to pass valid data to the function");
            else pgad.put(productGroupMap.get(pgadKey), (BooleanDistribution) distributions.get(pgadMap.get(pgadKey)));
        }
        return pgad;
    }

    /**
     * Helping method for 'loadConsumerAgentGroup' that extracts the perceived product attribute value distributions for product group attributes for a ConsumerAgentGroup
     * based on the configuration object perceivedProductAttributeValueDistribution
     *
     * @param perceivedProductAttributeValueDistribution Object representing the configuration of the perceivedProductAttributeValueDistributions for the productGroupAttributes by product groups. Must parse to a HashMap of String-HashMap hashMaps, with keys as product groups and values as String-String hashmaps with keys productGroupAttributes and values strings of valid distributions
     * @param productGroups A set of productGroups containing the product groups referred in the keys of the perceivedProductAttributeValueDistribution
     * @param distributions A map of distributions and their corresponding names (as Strings), containing at least the distributions referred to in the values of the inner maps encoded by perceivedProductAttributeValueDistribution
     * @param productGroupAttributes A set of ProductGroupAttributes containing at least the ones referred to as keys of the inner hashmaps of perceivedProductAttributeValueDistribution
     * @return A map of the ProductGroupAttributes and their distributions as configured by perceivedProductAttributeValueDistribution
     * @throws IllegalArgumentException Will be thrown when the perceivedProductAttributeValueDistribution can't be cast to a String-Object hashmap, the inner hashmaps (represented as these objects) cannot be casted to string-string hashmaps, invalid arguments are passed to the methods extract productGroup or productGroupAttribute names, or one of the entries in the perceivedProductAttributeValueDistribution does not correspond to the product groups, product group attributes or distributions passed to this method (and probably not within the configuration)
     */
    private static Map<ProductGroupAttribute, UnivariateDistribution> loadPerceivedProductAttributeValueDistribution(Object perceivedProductAttributeValueDistribution, Set<ProductGroup> productGroups, Set<ProductGroupAttribute> productGroupAttributes, Map<String, Distribution> distributions) throws IllegalArgumentException{
        //Map of strings of product groups and their configuration (outer hashmaps)
        Map<String, Object> productPPAVDMappingsOuter;
        try {
            productPPAVDMappingsOuter = (HashMap<String, Object>) perceivedProductAttributeValueDistribution;
        } catch (ClassCastException cce){
            throw new IllegalArgumentException("An error occurred parsing the perceivedProductAttributeValueDistribution to a string-object hashmap!!\n"+cce.toString());
        }
        Map<String, ProductGroup> productGroupMap;
        try {
            productGroupMap = StructureEnricher.attachProductGroupNames(productGroups);
        } catch (IllegalArgumentException e) {
            throw e;
        }
        Map<ProductGroup, Map<String, ProductGroupAttribute>> productGroupAttributeMap;
        try {
            productGroupAttributeMap = StructureEnricher.attachProductGroupAttributeNames(productGroupAttributes, productGroups);
        } catch (IllegalArgumentException e) {
            throw e;
        }
        Map<ProductGroupAttribute, UnivariateDistribution> ppavd = new HashMap<ProductGroupAttribute, UnivariateDistribution>();
        for(String productGroupString : productPPAVDMappingsOuter.keySet()){
            //map of product group attributes of the corresponding product group and strings of the corresponding distribution
            HashMap<String, String> ppavdMappingsInner;
            try {
                ppavdMappingsInner = (HashMap<String, String>) productPPAVDMappingsOuter.get(productGroupString);
            } catch (ClassCastException cce){
                throw new IllegalArgumentException("An error occurred parsing the inner hashmap of perceivedProductAttributeValueDistribution corresponding to entry "+productGroupString+" to a string-object hashmap!!\n"+cce.toString());
            }
            for(String productAttributeString : ppavdMappingsInner.keySet()) {
                if(!productGroupMap.containsKey(productGroupString)) throw new IllegalArgumentException("ProductGroup "+productGroupString+" referred to in the outer map of perceivedProductAttributeValueDistribution does not correspond to any product group passed to the loadPerceivedProductAttributeValueDistribution method!! Make sure to use valid entries in the configuration and pass correct parameters to the method!!");
                else if(!productGroupAttributeMap.containsKey(productGroupMap.get(productGroupString))) throw new IllegalArgumentException("ProductGroup "+productGroupMap.get(productGroupString).toString()+" referred to in the outer map of perceivedProductAttributeValueDistribution  is not a valid entry in the map describing the productGroup-productGroupAttribute maps. Something must have gone wrong extracting the productGroupAttributes!!");
                else if(!productGroupAttributeMap.get(productGroupMap.get(productGroupString)).containsKey(productAttributeString)) throw new IllegalArgumentException("ProductGroupAttribute "+productAttributeString+" referred to in the inner map of perceivedProductAttributeValueDistribution does not correspond to any product group attribute passed to the loadPerceivedProductAttributeValueDistribution method!! Make sure to use valid entries in the configuration and pass correct parameters to the method!!");
                else if(!distributions.containsKey(ppavdMappingsInner.get(productAttributeString))) throw new IllegalArgumentException("Distribution "+ppavdMappingsInner.get(productAttributeString)+" referred to in the inner map of perceivedProductAttributeValueDistribution does not correspond to any distribution passed to the loadPerceivedProductAttributeValueDistribution method!! Make sure to use valid entries in the configuration and pass correct parameters to the method!!");
                else ppavd.put(productGroupAttributeMap.get(productGroupMap.get(productGroupString)).get(productAttributeString), (UnivariateDistribution) distributions.get(ppavdMappingsInner.get(productAttributeString)));
            }
        }
        return ppavd;
    }

    /**
     * Helping method for 'loadConsumerAgentGroup' that extracts the consumerAgentGroupAttributes for a ConsumerAgentGroup
     * based on the configuration object consumerAgentGroupAttributes
     *
     * @param consumerAgentGroupAttributes Object representing the configuration of the attributes of the respective consumerAgentGroup. Must parse to an ArrayList of HashMaps of String-String values, with entries hashMaps that link the name of a consumerAgentGroupAttribute to a distribution to instantiate it.
     * @param distributions A map of distributions and their corresponding names (as Strings), containing at least the distributions referred to in the values of the hash maps in the entries of the array list in consumerAgentGroupAttributes with keys 'values'.
     * @return A set of ConsumerAgentGroupAttributes as extracted from the consumerAgentGroupAttributes configuration object
     * @throws IllegalArgumentException Will be thrown when the consumerAgentGroupAttributes can't be cast to an ArrayList of String-String hashmaps, or one of the values associated with a distribution (values of key 'value' in the respective entries of the ArrayList) is not passed to this method (and probably not within the configuration)
     */
    private static Set<ConsumerAgentGroupAttribute> loadConsumerAgentGroupAttributes(Object consumerAgentGroupAttributes, Map<String, Distribution> distributions) throws IllegalArgumentException{
        //set of return values
        Set<ConsumerAgentGroupAttribute> caga = new HashSet<ConsumerAgentGroupAttribute>();
        ArrayList<HashMap<String, Object>> entries;
        try {
            entries = (ArrayList<HashMap<String, Object>>) consumerAgentGroupAttributes;
        } catch (ClassCastException cce){
            throw new IllegalArgumentException("An error occurred parsing the consumerAgentGroupAttributes to an array list of string-string hashmaps!!\n"+cce.toString());
        }
        for (HashMap<String, Object> entry : entries) {
            if(!distributions.containsKey(entry.get("value"))) throw new IllegalArgumentException("Map of distributions passed to loadConsumerAgentGroup to load ConsumerAgentAttributes does not contain the required distribution "+entry.get("value"));
            else caga.add(new ConsumerAgentGroupAttribute((String) entry.get("name"), (UnivariateDistribution) distributions.get(entry.get("value"))));
        }
        return caga;
    }

    /**
     * Method to load a ConsumerAgentMessageScheme corresponding to the passed string.
     * Works as a factory method
     *
     * @param messageSchemeConfiguration Configuration of the respective MessageScheme
     * @param distributions The distributions used in the simulation (needs to contain at least the distribution associated with the numberMessagesSentPerTimeUnit)
     * @return The consumerAgentMessageScheme corresponding to the respective string
     * @throws IllegalArgumentException Will be thrown when the string representing to the consumerAgentMessageScheme does not correspond to an implemented consumerAgentMessageScheme
     * @throws ClassCastException Will be thrown when something can't be casted during the MessageScheme loading
     */
    private static ConsumerAgentMessageScheme loadConsumerAgentMessageScheme(HashMap<String, Object> messageSchemeConfiguration, Map<String, Distribution> distributions) throws IllegalArgumentException, ClassCastException{
        //System.out.println(messageSchemeConfiguration.get("numberMessagesSentPerTimeUnit"));
        if(!messageSchemeConfiguration.containsKey("messageScheme")) throw new IllegalArgumentException("Message scheme is not specified in the CommunicationScheme configuration!!");
        else if(!messageSchemeConfiguration.containsKey("numberMessagesSentPerTimeUnit")) throw new IllegalArgumentException("Distribution for the number of messages to send by a ConsumerAgent is not specified in the CommunicationScheme configuration!!");
        else if(!distributions.containsKey((String) messageSchemeConfiguration.get("numberMessagesSentPerTimeUnit"))) throw new IllegalArgumentException("Distribution "+((String) messageSchemeConfiguration.get("numberMessagesSentPerTimeUnit"))+" specifying the number of messages to send does not exist!\nPlease make sure to provide a valid configuration!");
        try {
            switch((String) messageSchemeConfiguration.get("messageScheme")) {
                case "DefaultConsumerAgentMessageScheme":
                    return new DefaultConsumerAgentMessageScheme((UnivariateDistribution) distributions.get((String) messageSchemeConfiguration.get("numberMessagesSentPerTimeUnit")));
                default:
                    throw new IllegalArgumentException("ConsumerAgentMessageScheme " + (String) messageSchemeConfiguration.get("messageScheme") + " has not been implemented!!!\nPlease provide a valid key!!");
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (ClassCastException cce){
            throw cce;
        }
    }

    /**
     * Method to load the configuration of points-of-sale (POS) agents, based on a configuration arraylist
     *
     * @param posAgentConfigurationFile The configuration file describing the configuration of the POS agents.
     * @param productConfiguration The configuration of the products within the simulation
     * @param distributions A map of distributions and their names (as keys) containing at least the distributions the configuration of the POS agents refer to
     * @return A POSAgentConfiguration corresponding to the configuration of the single POS agent as defined in the respective file
     * @throws IllegalArgumentException
     */
    private static POSAgentConfiguration loadPOSAgentConfiguration (File posAgentConfigurationFile, ProductConfiguration productConfiguration, Map<String, Distribution> distributions) throws IllegalArgumentException {
    ObjectMapper mapper = new ObjectMapper();
    try {
        HashMap<String, Object> posMap = mapper.readValue(posAgentConfigurationFile, HashMap.class);
        String[] fileNameArray = posAgentConfigurationFile.getName().split("\\.");
        if (!posMap.containsKey("productGroupAvailability"))
            throw new IllegalArgumentException("POS agent " + fileNameArray[0] + " doesn't have a value for the productGroupAvailability!!");
        else if (!posMap.containsKey("productGroupPriceFactor"))
            throw new IllegalArgumentException("POS agent " + fileNameArray[0] + " doesn't have a value for the productGroupPriceFactor!!");
        else if (!posMap.containsKey("spatialDistribution"))
            throw new IllegalArgumentException("POS agent " + fileNameArray[0] + " doesn't have a value for the spatialDistribution!!");
        else if (!posMap.containsKey("informationAuthority"))
            throw new IllegalArgumentException("POS agent " + fileNameArray[0] + " doesn't have a value for the informationAuthority!!");
        else if (!posMap.containsKey("PurchaseProcessSchemeIdentifier"))
            throw new IllegalArgumentException("POS agent " + fileNameArray[0] + " doesn't have a value for the PurchaseProcessSchemeIdentifier!!");
        else {
            Map<String, ProductGroup> productGroupByKeys = StructureEnricher.attachProductGroupNames(productConfiguration.getProductGroups());
            ArrayList<HashMap<String, Object>> productGroupConfigurationEntry = (ArrayList<HashMap<String, Object>>) posMap.get("productGroupConfiguration");
            Map<ProductGroup, BooleanDistribution> productGroupAvailability = new HashMap<ProductGroup, BooleanDistribution>(productConfiguration.getProductGroups().size());
            Map<ProductGroup, UnivariateDistribution> productGroupPriceFactor = new HashMap<ProductGroup, UnivariateDistribution>(productConfiguration.getProductGroups().size());
            //configure the different productGroup entries for the current POS agent
            for (HashMap<String, Object> aProductGroupConfigurationEntry : productGroupConfigurationEntry) {
                fooLog.debug("Configuring POSagent {} for product group {}\nkey is {}, value is {}, availability {}, keyset {}", (String) posMap.get("name"), productGroupByKeys.get((String) aProductGroupConfigurationEntry.get("productGroup")), productGroupByKeys.get((String) aProductGroupConfigurationEntry.get("productGroup")), (BooleanDistribution) distributions.get((String) aProductGroupConfigurationEntry.get("availability")), (String) aProductGroupConfigurationEntry.get("availability"), distributions.keySet());
                if (!productGroupByKeys.containsKey(aProductGroupConfigurationEntry.get("productGroup")))
                    throw new IllegalArgumentException("Product group " + aProductGroupConfigurationEntry.get("productGroup") + " used by POS configuration is not a valid product group (not contained in the list of product groups");
                else if (!aProductGroupConfigurationEntry.containsKey("availability"))
                    throw new IllegalArgumentException("Availability has not been configured for product group " + aProductGroupConfigurationEntry + "!!");
                else if (!distributions.containsKey(aProductGroupConfigurationEntry.get("availability")))
                    throw new IllegalArgumentException("Distribution " + aProductGroupConfigurationEntry.get("availability") + " referrred to in the POS agent configuration does not exist. Please make sure it is configured correctly!!");
                else if (!aProductGroupConfigurationEntry.containsKey("productGroupPriceFactor"))
                    throw new IllegalArgumentException("productGroupPriceFactor has not been configured for product group " + aProductGroupConfigurationEntry + "!!");
                else if (!distributions.containsKey(aProductGroupConfigurationEntry.get("productGroupPriceFactor")))
                    throw new IllegalArgumentException("Distribution " + aProductGroupConfigurationEntry.get("productGroupPriceFactor") + " referred to in the POS agent configuration does not exist. Please make sure it is configured correctly!!");
                else {
                    productGroupAvailability.put(productGroupByKeys.get((String) aProductGroupConfigurationEntry.get("productGroup")), (BooleanDistribution) distributions.get((String) aProductGroupConfigurationEntry.get("availability")));
                    productGroupPriceFactor.put(productGroupByKeys.get((String) aProductGroupConfigurationEntry.get("productGroup")), (UnivariateDistribution) distributions.get((String) aProductGroupConfigurationEntry.get("productGroupPriceFactor")));
                }
            }
            //fooLog.info("SpatialDistribution in config: {} with key {}", (SpatialDistribution) distributions.get((String) posAgentConfigurationJSON.get(index).get("spatialDistribution")), (String) posAgentConfigurationJSON.get(index).get("spatialDistribution"));
            if (!posMap.containsKey("spatialDistribution"))
                throw new IllegalArgumentException("No spatial distribution was configured for the current POS agent!! Make sure the configuration is complete!!");
            else if (!distributions.containsKey(posMap.get("spatialDistribution")))
                throw new IllegalArgumentException("Spatial distribution " + posMap.get("spatialDistribution") + " configured for the current POS agent is not a valid distribution!! Please make sure you configured it correctly!!");
            else if (!posMap.containsKey("name"))
                throw new IllegalArgumentException("No name has been configured for the current POS agent!! Make sure the configuration is complete!!");
            double informationAuthority;
            if (!posMap.containsKey("informationAuthority"))
                throw new IllegalArgumentException("Configuration of pos agent " + posMap.get("name") + " errornous!!\n No informationAuthority set!");
            else informationAuthority = (Double) posMap.get("informationAuthority");
            String purchaseProcessIdentifier;
            if (!posMap.containsKey("purchaseProcessIdentifier"))
                throw new IllegalArgumentException("Configuration of pos agent " + posMap.get("name") + " errornous!!\n No purchaseProcessIdentifier set!");
            else purchaseProcessIdentifier = (String) posMap.get("purchaseProcessIdentifier");
            return new POSAgentConfiguration(productGroupAvailability, productGroupPriceFactor, (SpatialDistribution) distributions.get((String) posMap.get("spatialDistribution")), (String) posMap.get("name"), informationAuthority, purchaseProcessIdentifier);
        }
    }
    catch (IllegalArgumentException iae){}
    catch (Exception e){}
    return null;
    }
}