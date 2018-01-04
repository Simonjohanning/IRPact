package IRPact_modellierung.io.loader;

import IRPact_modellierung.agents.consumerAgents.*;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.helper.StructureEnricher;
import IRPact_modellierung.preference.PreferenceConfiguration;
import IRPact_modellierung.preference.Value;
import IRPact_modellierung.products.ProductConfiguration;
import IRPact_modellierung.products.ProductGroup;
import IRPact_modellierung.products.ProductGroupAttribute;
import IRPact_modellierung.preference.ProductGroupAttributeValueMapping;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Class to load the preference configuration from a configuration.
 *
 * @author Simon Johanning
 */
public class PreferenceLoader {

    private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    /**
     * Method to load the preference configuration from the configPath as JSON file
     *
     * @param configPath The (relative) path the decision configuration file is located at
     * @param productConfiguration The configuration object of the products used in the simulation. Needs to contain at least the products the preference configuration refers to
     * @param consumerAgentGroups The configuration object of the consumer agent groups used in the simulation. Needs to contain at least the consumer agent groups the preference configuration refers to
     * @return Will return a PreferenceConfiguration based on the configuration file
     * @throws IllegalArgumentException Will be thrown when the configuration of the preferences (in the config path) is erroneous
     * @throws IOException Will be thrown when an error occurs handling the file storing the configuration of the referred preference configuration
     * @throws JsonParseException Will be thrown upon a parse error for the json file corresponding to the file handler
     * @throws JsonMappingException Will be thrown upon a mapping error for the json file corresponding to the file handler
     */
    public static PreferenceConfiguration loadPreferenceConfiguration(String configPath, ProductConfiguration productConfiguration, Set<ConsumerAgentGroup> consumerAgentGroups) throws IllegalArgumentException, IOException, JsonParseException, JsonMappingException{
        ObjectMapper mapper = new ObjectMapper();
        try {
            HashMap<String, Object> preferenceMap = mapper.readValue(new File(configPath+"PreferenceConfiguration.json"), HashMap.class);
            Set<ProductGroupAttributeValueMapping> preferenceProductAttributeMapping;
            ArrayList<HashMap<String, Object>> preferenceProductAttributeMappingJSON;
            if(!preferenceMap.containsKey("preferenceProductAttributeMapping")) throw new IllegalArgumentException("Preference configuration does not contain the preferenceProductAttributeMapping!!\nPlease make sure to provide a valid configuration file!!");
            else{
                try{
                    preferenceProductAttributeMappingJSON = (ArrayList<HashMap<String, Object>>) preferenceMap.get("preferenceProductAttributeMapping");
                    preferenceProductAttributeMapping = loadProductAttributePreferenceMapping(preferenceProductAttributeMappingJSON, StructureEnricher.attachProductGroupAttributeNames(LazynessHelper.productGroupAttributesFromProductGroups(productConfiguration.getProductGroups()), productConfiguration.getProductGroups()), consumerAgentGroups);
                } catch (ClassCastException cce){
                    throw new IllegalArgumentException("preferenceProductAttributeMapping can't be cast to an arrayList of String-Object HashMaps!!\nMake sure the internal structure of the preferenceProductAttributeMapping is correct!!\n"+cce);
                } catch (IllegalArgumentException iae){
                    throw iae;
                }
            }
            if(preferenceMap.containsKey("preferenceHomogenizingFactor")){
                try{
                    double preferenceHomogenizingFactor = (Double) preferenceMap.get("preferenceHomogenizingFactor");
                    return new PreferenceConfiguration(preferenceProductAttributeMapping, preferenceHomogenizingFactor);
                } catch (ClassCastException cce){
                    throw new IllegalArgumentException("preferenceHomogenizingFactor can't be cast to a double!!\n"+cce);
                }
            } else return new PreferenceConfiguration(preferenceProductAttributeMapping);
        } catch (JsonMappingException jme) {
            throw jme;
        } catch (JsonParseException jpe) {
            throw jpe;
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    /**
     * Method to load a ProductAttributePreferenceMapping from a preference configuration object (papaMapping), a map of product groups and their corresponding attributes and a set of consumer agents
     *
     * @param papMapping A configuration object representing a preference product attribute mapping
     * @param productGroupMap A map associating product group attributes and their name with their respective product groups
     * @param consumerAgentGroups The consumer agents whose values are to be mapped to product group attributes
     * @return A set of ProductGroupAttributeValueMappings corresponding to the arguments provided
     * @throws IllegalArgumentException Will be thrown when an entry (value or mapping strength) misses in the configuration or is invalid or one of the helping methods throws an (IA) exception
     */
    public static Set<ProductGroupAttributeValueMapping> loadProductAttributePreferenceMapping(ArrayList<HashMap<String, Object>> papMapping, Map<ProductGroup, Map<String, ProductGroupAttribute>> productGroupMap, Set<ConsumerAgentGroup> consumerAgentGroups) throws IllegalArgumentException{
        Set<ProductGroupAttributeValueMapping> returnSet = new HashSet<ProductGroupAttributeValueMapping>();
        Set<Value> values = new HashSet<Value>();
        //derive values
        for(ConsumerAgentGroup cag : consumerAgentGroups){
            values.addAll(cag.getConsumerAgentGroupPreferences().keySet());
        }
        HashMap<String, Value> valueMap;
        try{
            valueMap = StructureEnricher.attachValueNames(values);
        } catch (IllegalArgumentException iae){
            throw iae;
        }
        //go through the product groups to find product group attributes that are referred here
        for(ProductGroup currentProductGroup : productGroupMap.keySet()){
            //go through all entries and filter out the ones relative to the product group
            for(int index=0; index<papMapping.size(); index++) {
                //if entry is relevant for the product group, create a new mapping
                if(productGroupMap.get(currentProductGroup).keySet().contains((String) papMapping.get(index).get("productGroupAttribute")))
                {
                    Value value;
                    if(!papMapping.get(index).containsKey("value")) throw new IllegalArgumentException(index+"th entry of the ProductAttributePreferenceMapping is not associated with a value!!\nPlease make sure to provide a valid configuration!!");
                    else if(!valueMap.containsKey(papMapping.get(index).get("value"))) throw new IllegalArgumentException("The provided map of values does not contain the configured value "+papMapping.get(index).get("value")+"!!\nPlease make sure all values used in the preference configuration are valid values!!!");
                    else value = valueMap.get(papMapping.get(index).get("value"));
                    double mappingStrength;
                    if(!papMapping.get(index).containsKey("mappingStrength")) throw new IllegalArgumentException(index+"th entry of the ProductAttributePreferenceMapping is not associated with a mapping strength!!\nPlease make sure to provide a valid configuration!!");
                    else mappingStrength = (Double) papMapping.get(index).get("mappingStrength");
                    returnSet.add(new ProductGroupAttributeValueMapping(productGroupMap.get(currentProductGroup).get(papMapping.get(index).get("productGroupAttribute")), value, mappingStrength));
                }
            }
        }
        return returnSet;
    }
}
