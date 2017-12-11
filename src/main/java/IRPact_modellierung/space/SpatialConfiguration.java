package IRPact_modellierung.space;

import java.util.HashMap;

/**
 * Class describing the spatial configuration of the model.
 * Due to the diversity of spatial models it is basically a wrapper for what is loaded in the SpaceLoader
 *
 * @author Simon Johanning
 */
public class SpatialConfiguration {

	private String spatialModelQualifier;
	private HashMap<String, Object> spatialModelParameters;

	/**
	 * A SpatialConfiguration is based on the kind of model that is specified
	 * and the paramters of the respective model.
	 *
	 * @param spatialModelQualifier A string representing which spatial model to initialize
	 * @param spatialModelParameters The map of parameters to initialize the given spatial model
	 */
	public SpatialConfiguration(String spatialModelQualifier, HashMap<String, Object> spatialModelParameters) {
		this.spatialModelQualifier = spatialModelQualifier;
		this.spatialModelParameters = spatialModelParameters;
	}

	public String getSpatialModelQualifier() {
		return spatialModelQualifier;
	}

	public HashMap<String, Object> getSpatialModelParameters() {
		return spatialModelParameters;
	}
}