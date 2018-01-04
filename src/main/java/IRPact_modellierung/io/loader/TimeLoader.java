package IRPact_modellierung.io.loader;

import IRPact_modellierung.time.TemporalConfiguration;
import IRPact_modellierung.time.TimeModel;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class to load the temporal configuration of the simulation based on the its configuration.
 *
 * @author Simon Johanning
 */
public class TimeLoader {

    /**
     * Method to load the temporal configuration of the simulation.
     *
     *
     * @param configPath The (relative) path the files of the temporal configuration are located at
     * @return A configuration object based on the configuration files
     * @throws IllegalArgumentException Will be thrown if simulationLength, synchronousity or timeModel in the configuration file are missing, in the wrong format or have an unimplemented value!
     * @throws IOException Will be thrown when an error occurs handling the file storing the configuration of the referred temporal configuration
     * @throws JsonParseException Will be thrown upon a parse error for the json file corresponding to the file handler
     * @throws JsonMappingException Will be thrown upon a mapping error for the json file corresponding to the file handler
     */
    public static TemporalConfiguration loadTemporalModel(String configPath) throws IllegalArgumentException, IOException, JsonMappingException, JsonParseException{
        try {
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> spatialMap = mapper.readValue(new File(configPath+"TemporalConfiguration.json"), HashMap.class);
            double simulationLength;
            if(!spatialMap.containsKey("simulationLength")) throw new IllegalArgumentException("Temporal configuration does not specify the length of the simulation!!\nPlease make sure to provide a valid configuration!!");
            else{
                try{
                    simulationLength = (Double) spatialMap.get("simulationLength");
                }catch(ClassCastException cce){
                    throw new IllegalArgumentException("simulationLength could not be cast to a Double variable!!\nPlease ensure to provide the data for the temporal configuration in the correct format!!");
                }
            }
            boolean synchronousity;
            if(!spatialMap.containsKey("synchronousity")) throw new IllegalArgumentException("Temporal configuration does not specify the synchronousity of the simulation!!\nPlease make sure to provide a valid configuration!!");
            else{
                try{
                    synchronousity = (Boolean) spatialMap.get("synchronousity");
                }catch(ClassCastException cce){
                    throw new IllegalArgumentException("synchronousity could not be cast to a Boolean variable!!\nPlease ensure to provide the data for the temporal configuration in the correct format!!");
                }
            }
            String timeModel;
            if(!spatialMap.containsKey("timeModel")) throw new IllegalArgumentException("Temporal configuration does not specify the time model of the simulation!!\nPlease make sure to provide a valid configuration!!");
            else{
                try{
                    timeModel = (String) spatialMap.get("timeModel");
                }catch(ClassCastException cce){
                    throw new IllegalArgumentException("timeModel could not be cast to a String variable!!\nPlease ensure to provide the data for the temporal configuration in the correct format!!");
                }
            }
            if(timeModel.equals("discrete")){
                return new TemporalConfiguration(simulationLength, TimeModel.TEMPORALMODE.DISCRETE, synchronousity);
            }else if(timeModel.equals("continuous")){
                return new TemporalConfiguration(simulationLength, TimeModel.TEMPORALMODE.CONTINUOUS, synchronousity);
            }else throw new IllegalArgumentException("Time model has an implemented value ("+timeModel+")!!\nPlease ensure to provide correct values in the configuration!!");
        } catch (JsonParseException jpe){
            throw jpe;
        }catch (JsonMappingException jme){
            throw jme;
        }
        catch (IOException e) {
            throw e;
        }
    }
}
