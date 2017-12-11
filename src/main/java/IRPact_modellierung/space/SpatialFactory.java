package IRPact_modellierung.space;

import IRPact_modellierung.IO.Loader.SpaceLoader;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Class to create an instance of a spatial model based on the given spatial configuration
 *
 * @author Simon Johanning
 */
public class SpatialFactory {

    /**
     * Method to instantiate the spatial model given in the spatialConfiguration,
     * respective to the parameters given in the parameter map.
     * The parameter map must at least contain the metric characteristic of the spatial model.
     *
     * @param spatialConfiguration The configuration the spatial model is based on
     * @param simulationContainer The container the simulation is to run in
     * @return The SpatialModel within the simulationContainer the spatialConfiguration paramterized
     * @throws IllegalArgumentException Will be throw when a relevant parameter missed from the parameter map
     * @throws ClassCastException Will be thrown when a parameter can't be casted to the right type
     */
    public static SpatialModel createSpatialModel(SpatialConfiguration spatialConfiguration, SimulationContainer simulationContainer) throws IllegalArgumentException, ClassCastException{
        if(!spatialConfiguration.getSpatialModelParameters().containsKey("metric")) throw new IllegalArgumentException("SpatialModel requires the parameter 'metric', which is not given in the configuration.\nPlease provide a valid configuration!!");
        try {
            switch (spatialConfiguration.getSpatialModelQualifier()){
                case "CenteredRectangularSpatialModel": return loadCenteredRectangularSpatialModel(spatialConfiguration.getSpatialModelParameters(), simulationContainer);
                case "CenteredUnitSquareSpatialModel": return loadCenteredUnitSquareSpatialModel(spatialConfiguration.getSpatialModelParameters(), simulationContainer);
                case "RectangularSpatialModel" : return loadRectangularSpatialModel(spatialConfiguration.getSpatialModelParameters(), simulationContainer);
                case "GeoSpatialModel" : return loadGeoSpatialModel(spatialConfiguration.getSpatialModelParameters(), simulationContainer);
                default: throw new IllegalArgumentException("SpatialConfiguration "+spatialConfiguration+" used to instatiate the spatial model contains an unknown qualifier ("+spatialConfiguration.getSpatialModelQualifier()+")!!\nPlease make sure to provide a valid configuration");
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (ClassCastException e) {
            throw e;
        }
    }

    private static SpatialModel loadCenteredUnitSquareSpatialModel(HashMap<String, Object> spatialModelParameters, SimulationContainer simulationContainer) throws IllegalArgumentException, ClassCastException{
        String metric;
        try {
            metric = (String) spatialModelParameters.get("metric");
        } catch (ClassCastException e) {
            throw new ClassCastException("Value for key metric in the SpatialConfiguration could not be cast to a String. Please ensure to provide the right data type!!");
        }
        return new CenteredUnitSquareSpatialModel(simulationContainer, metric);
    }

    private static SpatialModel loadCenteredRectangularSpatialModel(HashMap<String, Object> spatialModelParameters, SimulationContainer simulationContainer) throws IllegalArgumentException, ClassCastException{
        if(!spatialModelParameters.containsKey("height")) throw new IllegalArgumentException("CenteredRectangularSpatialModel requires the parameter 'height', which is not given in the configuration.\nPlease provide a valid configuration!!");
        else if(!spatialModelParameters.containsKey("width")) throw new IllegalArgumentException("CenteredRectangularSpatialModel requires the parameter 'width', which is not given in the configuration.\nPlease provide a valid configuration!!");
        else{
            String metric;
            try {
                metric = (String) spatialModelParameters.get("metric");
            } catch (ClassCastException e) {
                throw new ClassCastException("Value for key metric in the SpatialConfiguration could not be cast to a String. Please ensure to provide the right data type!!");
            }
            double height;
            try {
                height = (Double) spatialModelParameters.get("height");
            } catch (ClassCastException e) {
                throw new ClassCastException("Value for key height in the SpatialConfiguration could not be cast to a Double. Please ensure to provide the right data type!!");
            }
            double width;
            try {
                width = (Double) spatialModelParameters.get("width");
            } catch (ClassCastException e) {
                throw new ClassCastException("Value for key width in the SpatialConfiguration could not be cast to a Double. Please ensure to provide the right data type!!");
            }
            return new CenteredRectangularSpatialModel(simulationContainer, metric, height, width);
        }
    }

    private static SpatialModel loadRectangularSpatialModel(HashMap<String, Object> spatialModelParameters, SimulationContainer simulationContainer) throws IllegalArgumentException, ClassCastException{
        if(!spatialModelParameters.containsKey("height")) throw new IllegalArgumentException("RectangularSpatialModel requires the parameter 'height', which is not given in the configuration.\nPlease provide a valid configuration!!");
        else if(!spatialModelParameters.containsKey("width")) throw new IllegalArgumentException("RectangularSpatialModel  requires the parameter 'width', which is not given in the configuration.\nPlease provide a valid configuration!!");
        else if(!spatialModelParameters.containsKey("latitudeCentred")) throw new IllegalArgumentException("RectangularSpatialModel  requires the parameter 'latitudeCentred', which is not given in the configuration.\nPlease provide a valid configuration!!");
        else if(!spatialModelParameters.containsKey("longitudeCentred")) throw new IllegalArgumentException("RectangularSpatialModel  requires the parameter 'longitudeCentred', which is not given in the configuration.\nPlease provide a valid configuration!!");
        else{
            String metric;
            try {
                metric = (String) spatialModelParameters.get("metric");
            } catch (ClassCastException e) {
                throw new ClassCastException("Value for key metric in the SpatialConfiguration could not be cast to a String. Please ensure to provide the right data type!!");
            }
            double height;
            try {
                height = (Double) spatialModelParameters.get("height");
            } catch (ClassCastException e) {
                throw new ClassCastException("Value for key height in the SpatialConfiguration could not be cast to a Double. Please ensure to provide the right data type!!");
            }
            double width;
            try {
                width = (Double) spatialModelParameters.get("width");
            } catch (ClassCastException e) {
                throw new ClassCastException("Value for key width in the SpatialConfiguration could not be cast to a Double. Please ensure to provide the right data type!!");
            }
            double latitudeCentred;
            try {
                latitudeCentred = (Double) spatialModelParameters.get("latitudeCentred");
            } catch (ClassCastException e) {
                throw new ClassCastException("Value for key latitudeCentred in the SpatialConfiguration could not be cast to a Double. Please ensure to provide the right data type!!");
            }
            double longitudeCentred;
            try {
                longitudeCentred = (Double) spatialModelParameters.get("longitudeCentred");
            } catch (ClassCastException e) {
                throw new ClassCastException("Value for key width in the SpatialConfiguration could not be cast to a Double. Please ensure to provide the right data type!!");
            }
            return new RectangularSpatialModel(simulationContainer, metric, height, width, latitudeCentred, longitudeCentred);
        }
    }

    private static SpatialModel loadGeoSpatialModel(HashMap<String, Object> spatialModelParameters, SimulationContainer simulationContainer) throws IllegalArgumentException, ClassCastException{
        if(!spatialModelParameters.containsKey("shapefiles")) throw new IllegalArgumentException("GeoSpatialModel requires the parameter 'shapefiles', which is not given in the configuration.\nPlease provide a valid configuration!!");
        else{
            String metric;
            try {
                metric = (String) spatialModelParameters.get("metric");
            } catch (ClassCastException e) {
                throw new ClassCastException("Value for key metric in the SpatialConfiguration could not be cast to a String. Please ensure to provide the right data type!!");
            }
            ArrayList<Object> shapefiles;
            try {
                shapefiles = (ArrayList<Object>) spatialModelParameters.get("shapefiles");
            } catch (ClassCastException e) {
                throw new ClassCastException("Value for key shapefiles in the SpatialConfiguration could not be cast to a Double. Please ensure to provide the right data type!!");
            }
            Set<ShapeFile> shapeFiles;
            try {
                shapeFiles = SpaceLoader.loadShapefiles(shapefiles);
            } catch (Exception e) {
                throw new IllegalArgumentException("Shapefile parameters of the GeoSpatialModel has led to the following exception: \n"+e);
            }
            return new GeoSpatialModel(simulationContainer, metric, shapeFiles);
        }
    }

}
