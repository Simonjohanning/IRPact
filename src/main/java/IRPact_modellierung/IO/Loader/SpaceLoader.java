package IRPact_modellierung.IO.Loader;

import IRPact_modellierung.space.ShapeFile;
import IRPact_modellierung.space.SpatialConfiguration;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Class to load the spatial configuration of the simulation from the respective configuration file
 * (SpatialConfiguration.json)
 *
 * @author Simon Johanning
 */
public class SpaceLoader {

    /**
     * Method to load the spatial configuration from the configuration file located in the configPath folder
     *
     * @param configPath The (relative) path the spatial configuration is located at
     * @return Object that represents the spatial configuration of the simulation
     * @throws IOException Will be thrown when an error occurs in the file handling while loading the spatial configuration
     * @throws JsonParseException Will be thrown upon a parse error for the json file corresponding to the file handler
     * @throws JsonMappingException Will be thrown upon a mapping error for the json file corresponding to the file handler
     */
    public static SpatialConfiguration loadSpatialConfiguration(String configPath) throws IOException, JsonParseException, JsonMappingException{
        ObjectMapper mapper = new ObjectMapper();
        try{
            HashMap<String, Object> spatialMap = mapper.readValue(new File(configPath+"SpatialConfiguration.json"), HashMap.class);
            String spatialModel;
            if(!spatialMap.containsKey("spatialModel")) throw new IllegalArgumentException("Spatial configuration doesn't contain a configuration for the qualifier of the SpatialModel!!\nPlease make sure to provide a valid configuration!!");
            else {
                try {
                    spatialModel = (String) spatialMap.get("spatialModel");
                } catch (ClassCastException cce) {
                    throw new IllegalArgumentException("SpatialModel qualifier configuration format invalid!! Can't be cast to a String!!\n"+cce);
                }
            }
            HashMap<String, Object> spatialParameters;
            if(!spatialMap.containsKey("parameters")) throw new IllegalArgumentException("Spatial configuration doesn't contain a configuration for the parameters of the SpatialModel!!\nPlease make sure to provide a valid configuration!!");
            else {
                try {
                    spatialParameters = (HashMap<String, Object>) spatialMap.get("parameters");
                } catch (ClassCastException cce) {
                    throw new IllegalArgumentException("SpatialModel parameter configuration format invalid!! Can't be cast to a String-Object Hashmap!!\n"+cce);
                }
            }
            return new SpatialConfiguration(spatialModel, spatialParameters);
        }catch (JsonMappingException jme){
            throw jme;
        }catch (JsonParseException jpe){
            throw jpe;
        }catch (IOException ioe){
            throw ioe;
        }
    }

    /**
     * Method to load a number of shape files from the respective array list containing these shapefiles
     * (as e.g. given by the parameters of a GeoSpatialModel)
     *
     * @param shapefiles The shapefiles used for a GeoSpatialModel
     * @return The shape file objects corresponding to the given list
     * @throws ClassCastException Will be thrown when the ArrayList elements can't be cast to the shapefile objects
     * @throws Exception Will be thrown when other errors occur
     */
    public static Set<ShapeFile> loadShapefiles(ArrayList<Object> shapefiles) throws ClassCastException, Exception{
        Set<ShapeFile> shapeFiles = new HashSet<>();
        try {
            for (ShapeFile shapeFile : shapeFiles) {
                shapeFiles.add(shapeFile);
            }
        } catch (ClassCastException cce){
            throw cce;
        }
        catch (Exception e) {
            throw e;
        }
        return shapeFiles;
    }
}
