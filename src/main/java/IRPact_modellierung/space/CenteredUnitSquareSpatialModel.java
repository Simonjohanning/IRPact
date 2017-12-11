package IRPact_modellierung.space;

import IRPact_modellierung.simulation.SimulationContainer;

import java.awt.geom.Point2D;

/**
 * Spatial model that is described by the unit square (i.e. [0,1] x [0,1])
 *
 * @author Simon Johanning
 */
public class CenteredUnitSquareSpatialModel extends RectangularSpatialModel{

    public CenteredUnitSquareSpatialModel(SimulationContainer container, String metric) {
        super(container, metric, 1.0, 1.0, 0.5, 0.5);
    }

    public boolean isWithinBoundaries(Point2D pointToCheck) {
        if((pointToCheck.getX() < 0.0) || (pointToCheck.getX() > 1.0) || (pointToCheck.getY() < 0.0) || (pointToCheck.getY() > 1.0)) return false;
        else return true;
    }
}
