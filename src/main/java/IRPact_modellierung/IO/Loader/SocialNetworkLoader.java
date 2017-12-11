package IRPact_modellierung.IO.Loader;

import IRPact_modellierung.network.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class to load the configuration of the social network
 * (i.e. SocialGraph, EdgeWeightManipulationScheme and TopologymanipulationScheme) from the respective file,
 * and wrap them in a SNConfiguration object.
 *
 * @author Simon Johanning
 */
public class SocialNetworkLoader {

    /**
     * Method to load a social network configuration from the respective files
     * (individual files for at least the networks used, configuration of the concrete network in the network configuration file)
     *
     * @param configPath The (relative) path the configuration is located at
     * @return A SNConfiguration object corresponding to the configuration as specified in the configuration files
     * @throws IllegalArgumentException Will be thrown when an entry is missing from the configuration or is provided in the wrong format (can't be cast to the desired format) or is thrown loading the configured networks
     * @throws IOException Is thrown when an error occurs in handling the files the configuration is located in
     * @throws JsonMappingException Will be thrown when an error occurs in mapping the JSON data
     * @throws JsonParseException Will be thrown when an error occurs in parsing the json data
     */
    public static SNConfiguration loadSocialNetwork(String configPath) throws IllegalArgumentException, IOException, JsonMappingException, JsonParseException{
        try{
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> networkConfiguration = mapper.readValue(new File(configPath+"SocialNetworkConfiguration.json"), HashMap.class);
            //load the SocialGraph configuration
            HashMap<String, Object> socialGraphMap;
            if(!networkConfiguration.containsKey("SocialGraph")) throw new IllegalArgumentException("Network configuration doesn't contain a configuration for the Social Graph used by the social networks!!\nPlease make sure to provide a valid configuration!!");
            else {
                try {
                    socialGraphMap = (HashMap<String, Object>) networkConfiguration.get("SocialGraph");
                } catch (ClassCastException cce) {
                    throw new IllegalArgumentException("SocialGraph configuration format invalid!! Can't be cast to a String-Object Hashmap!!\n"+cce);
                }
            }
            String socialGraphTopology;
            if(!socialGraphMap.containsKey("topology")) throw new IllegalArgumentException("SocialGraph configuration doesn't contain a configuration for the scheme used by the TopologyManipulationScheme!!\nPlease make sure to provide a valid configuration!!");
            else {
                try {
                    socialGraphTopology = (String) socialGraphMap.get("topology");
                } catch (ClassCastException cce) {
                    throw new IllegalArgumentException("SocialGraph configuration format invalid!! Can't be cast to a String!!\n"+cce);
                }
            }
            HashMap<String, Object> socialGraphParameters;
            if(!socialGraphMap.containsKey("parameters")) throw new IllegalArgumentException("SocialGraph configuration doesn't contain the parameters used by the topology manipulation scheme!!\nPlease make sure to provide a valid configuration!!");
            else {
                try {
                    socialGraphParameters = (HashMap<String, Object>) socialGraphMap.get("parameters");
                } catch (ClassCastException cce) {
                    throw new IllegalArgumentException("SocialGraph parameter configuration format invalid!! Can't be cast to a String-Object Hashmap!!\n"+cce);
                }
            }
            //load TopologyManipulationScheme configuration
            HashMap<String, Object> tmsMap;
            if(!networkConfiguration.containsKey("TopologyManipulationScheme")) throw new IllegalArgumentException("Network configuration doesn't contain a configuration for the Topology manipulation scheme used by the social networks!!\nPlease make sure to provide a valid configuration!!");
            else {
                try {
                    tmsMap = (HashMap<String, Object>) networkConfiguration.get("TopologyManipulationScheme");
                } catch (ClassCastException cce) {
                    throw new IllegalArgumentException("TopologyManipulationScheme configuration format invalid!! Can't be cast to a String-Object Hashmap!!\n"+cce);
                }
            }
            String tmsScheme;
            if(!tmsMap.containsKey("scheme")) throw new IllegalArgumentException("TopologyManipulationScheme configuration doesn't contain a configuration for the scheme used by the TopologyManipulationScheme!!\nPlease make sure to provide a valid configuration!!");
            else {
                try {
                    tmsScheme = (String) tmsMap.get("scheme");
                } catch (ClassCastException cce) {
                    throw new IllegalArgumentException("TopologyManipulationScheme configuration format invalid!! Can't be cast to a String!!\n"+cce);
                }
            }
            HashMap<String, Object> tmsParameters;
            if(!tmsMap.containsKey("parameters")) throw new IllegalArgumentException("TopologyManipulationScheme configuration doesn't contain the parameters used by the topology manipulation scheme!!\nPlease make sure to provide a valid configuration!!");
            else {
                try {
                    tmsParameters = (HashMap<String, Object>) tmsMap.get("parameters");
                } catch (ClassCastException cce) {
                    throw new IllegalArgumentException("TopologyManipulationScheme parameter configuration format invalid!! Can't be cast to a String-Object Hashmap!!\n"+cce);
                }
            }
            //load the EdgeWeightMapping configuration
            HashMap<String, Object> ewmMap;
            if(!networkConfiguration.containsKey("EdgeWeightManipulationScheme")) throw new IllegalArgumentException("Network configuration doesn't contain a configuration for the EdgeWeightManipulationScheme used by social networks!!\nPlease make sure to provide a valid configuration!!");
            else {
                try {
                    ewmMap = (HashMap<String, Object>) networkConfiguration.get("EdgeWeightManipulationScheme");
                } catch (ClassCastException cce) {
                    throw new IllegalArgumentException("EdgeWeightManipulationScheme configuration format invalid!! Can't be cast to a String-Object Hashmap!!\n"+cce);
                }
            }
            String ewmScheme;
            if(!ewmMap.containsKey("scheme")) throw new IllegalArgumentException("EdgeWeightManipulationScheme configuration doesn't contain a configuration for the scheme used!!\nPlease make sure to provide a valid configuration!!");
            else {
                try {
                    ewmScheme = (String) ewmMap.get("scheme");
                } catch (ClassCastException cce) {
                    throw new IllegalArgumentException("EdgeWeightManipulationScheme scheme configuration format invalid!! Can't be cast to a String!!\n"+cce);
                }
            }
            HashMap<String, Object> ewmParameters;
            if(!ewmMap.containsKey("parameters")) throw new IllegalArgumentException("EdgeWeightManipulationScheme configuration doesn't contain a the parameters used by the EdgeWeightMapping scheme!!\nPlease make sure to provide a valid configuration (at least an empty hashmap if no parameters are desired for the EdgeWeightMapping scheme!!");
            else {
                try {
                    ewmParameters = (HashMap<String, Object>)  ewmMap.get("parameters");
                } catch (ClassCastException cce) {
                    throw new IllegalArgumentException("EdgeWeightManipulationScheme parameter configuration format invalid!! Can't be cast to a String-Object Hashmap!!\n"+cce);
                }
            }
            return new SNConfiguration(socialGraphTopology, socialGraphParameters, tmsScheme, tmsParameters, ewmScheme, ewmParameters);
        } catch(JsonMappingException jme){
            throw jme;
        } catch(JsonParseException jpe){
            throw jpe;
        } catch(IOException ioe){
            throw ioe;
        }
    }
}
