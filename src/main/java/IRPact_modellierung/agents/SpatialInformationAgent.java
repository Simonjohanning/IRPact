package IRPact_modellierung.agents;

import IRPact_modellierung.simulation.SimulationContainer;

import java.awt.geom.Point2D;

/**
 * A SpatialInformationAgent brings together the spatiality of a SpatialAgent as well as
 * the informational nature of an InformationAgent, through having associated with it
 * an informationAuthority representing the credibility of the information
 * stemming from this agent.
 *
 * @author Simon Johanning
 **/
public class SpatialInformationAgent extends SpatialAgent{

    private double informationAuthority;

    /**
     * An information agent is an agent within the simulation that can create information, and
     * is spatially situated.
     * Associated with it it has an informationAuthority representing the credibility of the information
     * stemming from this agent.
     *
     * @param simulationContainer The container the simulation runs in
     * @param coordinates The spatial position the spatial agent is initialized with
     * @param informationAuthority The authority information stemming from this agent has
     */
    public SpatialInformationAgent(SimulationContainer simulationContainer, Point2D coordinates, double informationAuthority) {
        super(simulationContainer, coordinates);
        this.informationAuthority = informationAuthority;
    }

}
