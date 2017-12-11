package IRPact_modellierung.distributions;

import IRPact_modellierung.space.SpatialModel;

import java.awt.geom.Point2D;

//TODO think about how to do and implement
//TODO should also probably be abstract and implemented by different schemes

/**
 * Distribution that describes how an entity will be situated spatially.
 * Needs to be specified and is not much more than a place holder at the current state.
 *
 * Is currently based on drawing independent values for both coordinates without paying attention to the spatial model,
 * and wraps them into a 2D.Double point
 *
 * @author Simon Johanning
 */
public class ContinuousSpatialDistribution extends SpatialDistribution{

    //TODO think about how to do and uncomment stuff
    //SpatialModel associatedSpace;

    ContinuousDistribution dummyDistr;

    public ContinuousSpatialDistribution(String name, ContinuousDistribution coordsBasedOn)
    {
        super(name);
        dummyDistr = coordsBasedOn;
    }
/*
    public ContinuousSpatialDistribution(String name, SpatialModel associatedSpace) {
        super(name);
        this.associatedSpace = associatedSpace;
    }*/

    /**
     * In the current implementation, a value is provided by drawing on the
     * ContinuousDistribution this distribution is based on independently.
     * This should not be used and is more of a placeholder than anything
     *
     * @return A 2 dimensional point with (independently drawn) double coordinates
     */
    public Point2D.Double draw(SpatialModel spatialModel) {
/*
        double xCoord = (Math.random() * (associatedSpace.getLatitudeMax() - associatedSpace.getLatitudeMin()) + associatedSpace.getLatitudeMin());
        double yCoord = (Math.random() * (associatedSpace.getLongitudeMax() - associatedSpace.getLongitudeMin()) + associatedSpace.getLongitudeMin());
*/
        double xCoord = dummyDistr.draw();
        double yCoord = dummyDistr.draw();

       return new Point2D.Double(xCoord, yCoord);
    }
}
