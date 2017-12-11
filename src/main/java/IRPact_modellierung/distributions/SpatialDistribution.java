package IRPact_modellierung.distributions;

import IRPact_modellierung.space.SpatialModel;

import java.awt.geom.Point2D;

//TODO check whether it should be situated in 2D or should be generalized
//TODO think about how to model a spatial distribution

/**
 * Abstraction to model distributions with values in (2 dimensional) space.
 * Values are taken in coordinate space with double coordinates
 *
 * @author Simon Johanning
 */
public abstract class SpatialDistribution extends Distribution {

    protected String name;

    public SpatialDistribution(String name) {
        super(name);
    }

    public abstract Point2D.Double draw(SpatialModel spatialModel);
}
