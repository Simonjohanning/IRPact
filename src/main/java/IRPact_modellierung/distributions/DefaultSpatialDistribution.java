package IRPact_modellierung.distributions;

import IRPact_modellierung.space.SpatialModel;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * The DefaultSpatialDistribution is a simple spatial distribution
 * that is associated with a bivariate distribution (representing the two spatial dimensions
 * latitude and longitude), and which draws values according to this distribution
 * until it draws a value valid in the spatial model.
 *
 * It is the simple go-to choice used until some thought and development time is put into the matter.
 *
 * @author Simon Johanning
 */
public class DefaultSpatialDistribution extends SpatialDistribution{

    private MultivariateDistribution associatedDistribution;

    public DefaultSpatialDistribution(String name, MultivariateDistribution associatedDistribution) throws IllegalArgumentException{
        super(name);
        if(associatedDistribution.getDimension() != 2) throw new IllegalArgumentException("Dimensionality of the distribution associated with the DefaultSpatialDistribution "+name+" parameterized wrongly!! Is "+associatedDistribution.getDimension()+", but should be 2!");
        this.associatedDistribution = associatedDistribution;
    }

    /**
     * Method drawing the two-dimensional coordinates of a (in the spatial model) valid point distributed according to the
     * distribution associated to this DefaultSpatialDistribution.
     *
     * Will keep drawing values until a valid point is found, and will not do testing whether
     * a non-negligible mass lies within the valid region of the spatial model
     * (i.e. it is the responsibility of the modeler to ensure this, and this method call
     * might take a lot of processing time, or in the worst case not even terminate!)
     *
     * @param spatialModel The spatial model a valid point is to be determined for
     * @return A valid 2D point lying within the boundaries of the spatialModel, distibuted according to the distribution associated with this distribution
     */
    public Point2D.Double draw(SpatialModel spatialModel) {
        boolean validPointFound = false;
        //quick and dirty, I know...
        while(!validPointFound) {
            ArrayList<Double> drawnPointCoordinates = associatedDistribution.draw();
            Point2D.Double respectivePoint = new Point2D.Double(drawnPointCoordinates.get(0), drawnPointCoordinates.get(1));
            if (spatialModel.isWithinBoundaries(respectivePoint)) return respectivePoint;
        }
        throw new IllegalStateException("Reached a point in the program that should not be possible to reach!!\nPlease check program logic or programmer sanity / programming skills");
    }
}
