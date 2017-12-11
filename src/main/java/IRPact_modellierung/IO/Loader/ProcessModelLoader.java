package IRPact_modellierung.IO.Loader;

import IRPact_modellierung.processModel.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class to load the process model from the configuration path.
 */
public class ProcessModelLoader {

    /**
     * Loads the process model from the specified configuration path
     *
     * @param configPath The (relative) path from which to load the process model
     * @return Will return the process model configuration as an object, corresponding to the configuration provided in the configuration file
     * @throws IOException Will be thrown when an error occurs handling the file storing the configuration of the referred preference configuration
     * @throws JsonParseException Will be thrown upon a parse error for the json file corresponding to the file handler
     * @throws JsonMappingException Will be thrown upon a mapping error for the json file corresponding to the file handler
     * @throws IllegalArgumentException Will be thrown when configuration file has no entries for the process model or the adoption replacement scheme, when an unimplemented process model is specified or the method to load an adoption replacement scheme throws one
     */
    public static ProcessModel loadProcessModel(String configPath) throws IllegalArgumentException, IOException, JsonParseException, JsonMappingException{
        try{
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> processModelConfigurationJSON = mapper.readValue(new File(configPath+"ProcessModelConfiguration.json"), HashMap.class);
            if(!processModelConfigurationJSON.containsKey("processModelToEmploy")) throw new IllegalArgumentException("No process model was specified in the process model configuration!!\nPlease make sure to provide a valid configuration!!");
            else if(!processModelConfigurationJSON.containsKey("adoptionReplacementScheme")) throw new IllegalArgumentException("No adoption replacement scheme was specified in the process model configuration!!\nPlease make sure to provide a valid configuration!!");
            else{
                AdoptionReplacementScheme adoptionReplacementScheme;
                try {
                    adoptionReplacementScheme = AdoptionReplacementSchemeFactory.createAdoptionReplacementScheme((String) processModelConfigurationJSON.get("adoptionReplacementScheme"));
                } catch (IllegalArgumentException iae){
                    throw iae;
                }
                if(processModelConfigurationJSON.get("processModelToEmploy").equals("KieslingsAdoptionFiveStepModel")){
                    return new KieslingsAdoptionFiveStepModel(adoptionReplacementScheme);
                }else if(((String) processModelConfigurationJSON.get("processModelToEmploy")).equals("SchwarzProcessModel")){
                    return new SchwarzProcessModel(adoptionReplacementScheme);
                }
                else throw new IllegalArgumentException(processModelConfigurationJSON.get("processModelToEmploy")+" is not implemented!!\nPlease make sure to provide a valid configuration!!");
            }
        } catch (JsonMappingException jme) {
            throw jme;
        } catch (JsonParseException jpe) {
            throw jpe;
        } catch (IOException ioe) {
            throw ioe;
        }
    }
}
