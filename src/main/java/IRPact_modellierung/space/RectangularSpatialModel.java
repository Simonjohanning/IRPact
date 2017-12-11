package IRPact_modellierung.space;

import IRPact_modellierung.helper.MetricHelper;
import IRPact_modellierung.simulation.SimulationContainer;

import java.awt.geom.Point2D;

/**
 * The RectangularSpatialModel is a spatial model for toy models, or models that require
 * spatiality, but use it in a more abstract way.
 * It is a low-resolution geographic scheme with little geographic features, and more
 * or less only used to situate agents in different spatial proximity to one another.
 *
 * @author Simon Johanning
 */
public class RectangularSpatialModel extends SpatialModel{

    private double height;
    private double width;
    private double latitudeCentred;
    private double longitudeCentred;

    public RectangularSpatialModel(SimulationContainer container, String metric, double height, double width, double latitudeCentred, double longitudeCentred) {
        super(container, metric);
        this.height = height;
        this.width = width;
        this.latitudeCentred = latitudeCentred;
        this.longitudeCentred = longitudeCentred;
    }

    /**
     * Method to check whether a point lies within the boundaries of the spatial model.
     * A point lies within the space described by the RectangularSpatialModel when it lies within a rectangular
     * box of given width and height around the centered coordinates (i.e. x in [longitudeCentred - width/2.0, longitudeCentred + width/2.0]
     * and y in [latitudeCentred - height/2.0,latitudeCentred + height/2.0].
     *
     * @param pointToCheck The coordinates to check the box with
     * @return true if the point lies within the box (including corners), false otherwise
     */
    public boolean isWithinBoundaries(Point2D pointToCheck) {
        if((pointToCheck.getX() < (longitudeCentred - width/2.0)) || pointToCheck.getX() > (longitudeCentred + width/2.0)) return false;
        else if ((pointToCheck.getY() < (latitudeCentred - height/2.0)) || pointToCheck.getY() > (latitudeCentred + height/2.0)) return false;
        else return true;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getLatitudeCentred() {
        return latitudeCentred;
    }

    public double getLongitudeCentred() {
        return longitudeCentred;
    }
}
