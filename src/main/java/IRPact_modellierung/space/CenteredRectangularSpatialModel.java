package IRPact_modellierung.space;

import IRPact_modellierung.simulation.SimulationContainer;

/**
 * Spatial model representing a RectangularSpatialModel where the rectangle is centered around the origin
 * (i.e. (0,0)).
 *
 * @author Simon Johanning
 */
public class CenteredRectangularSpatialModel extends RectangularSpatialModel{

    public CenteredRectangularSpatialModel(SimulationContainer container, String metric, double height, double width) {
        super(container, metric, height, width, 0,0);
    }
}
