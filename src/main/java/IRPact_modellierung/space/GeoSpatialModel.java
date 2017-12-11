package IRPact_modellierung.space;

import IRPact_modellierung.simulation.SimulationContainer;

import java.awt.geom.Point2D;
import java.util.Set;
//TODO think about whether this is concrete enough or this should be declared abstract

/**
 * A GeoSpatialModel is a SpatialModel with a high resolution and many geographic features.
 * It uses shape files to determine the borders of the region of the simulation, and is
 * supposed to support a large number of geographic features and entities,
 * as well as sophisticated GIS support.
 *
 * @author Simon Johanning
 */
public class GeoSpatialModel extends SpatialModel{

    private Set<ShapeFile> shapeFiles;

    public Set<ShapeFile> getShapeFiles() {
        return this.shapeFiles;
    }

    public GeoSpatialModel(SimulationContainer container, String metric, Set<ShapeFile> shapeFiles){
        super(container, metric);
        this.shapeFiles = shapeFiles;
    }

    public boolean isWithinBoundaries(Point2D pointToCheck) {
        throw new UnsupportedOperationException("This method is not implemented yet. Please blame the programmer or read the respective guides more carefully.");
    }
}
