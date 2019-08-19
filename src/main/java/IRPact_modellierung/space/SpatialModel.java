package IRPact_modellierung.space;

import java.awt.geom.Point2D;

import IRPact_modellierung.SimulationEntity;
import IRPact_modellierung.agents.SpatialAgent;
import IRPact_modellierung.helper.MetricHelper;
import IRPact_modellierung.simulation.SimulationContainer;

/**
 * The spatial model describes the spatial character of the simulation.
 * It defines the metric spatial entities are situated to one another with,
 * and provides an ability to check whether a coordinate is within the spatial model.
 *
 * @author Simon Johanning
 */
public abstract class SpatialModel extends SimulationEntity {
	// TODO Make the metric scheme into a class
	String metric;

	/**
	 * The SpatialModel in its generality is situated within a simulation container
	 * and associated with a metric under which the distances of entities are interpreted
	 *
	 * @param container The container the simulation is contained in
	 * @param metric The metric by which the distances are determined
	 */
	public SpatialModel(SimulationContainer container, String metric) {
		super(container);
		this.metric = metric;
	}


	/**
	 * Method to check whether a coordinate falls within or outside the spatial model
	 *
	 * @param pointToCheck Coordinate to check whether it lies in the model or not
	 * @return true if coordinate lies within the spatial model, false otherwise
	 */
	public abstract boolean isWithinBoundaries(Point2D pointToCheck);

	public double measureDistance(Point2D a, Point2D b){
		return MetricHelper.calculateDistance(a,b,metric);
	}

	public String getMetric() {
		return metric;
	}
}
