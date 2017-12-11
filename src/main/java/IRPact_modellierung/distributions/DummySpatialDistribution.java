package IRPact_modellierung.distributions;

import IRPact_modellierung.space.RectangularSpatialModel;
import IRPact_modellierung.space.SpatialModel;

import java.awt.geom.Point2D;

//TODO remove when spatial model is implemented and spatial distibutions are sensible

/**
 * A spatial distribution that chooses an (evenly distributed) coordinate
 * within the rectangle if the spatial model is an instance of a RectangularSpatialModel),
 * and (0.0,0.0) if not.
 * Should not be used for other models than RectangularSpatialModel and will be deleted in the future.
 *
 * @author Simon Johanning
 */
public class DummySpatialDistribution extends SpatialDistribution {


    public DummySpatialDistribution(String name) {
        super(name);
    }

    public Point2D.Double draw(SpatialModel spatialModel) {
        if(spatialModel instanceof RectangularSpatialModel){
            RectangularSpatialModel correspondingModel = (RectangularSpatialModel) spatialModel;
            double xCoordinate = correspondingModel.getLongitudeCentred()+ (Math.random()*2.0-1.0)*correspondingModel.getWidth();
            double yCoordinate = correspondingModel.getLatitudeCentred()+ (Math.random()*2.0-1.0)*correspondingModel.getHeight();
            return new Point2D.Double(xCoordinate, yCoordinate);
        }
        return new Point2D.Double(0.0,0.0);
    }
}
