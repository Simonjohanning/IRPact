package IRPact_modellierung.network;

import IRPact_modellierung.simulation.SimulationContainer;

/**
 * This TopologyManipulationScheme is a TMS in which every edge of the communication medium present in the graph will be
 * deleted with a given, uniform probability, and every possible edge of the communication medium is created with a given, uniform probability.
 *
 * It is thus basically the RandomInDelTMS in a graph with only communication media.
 *
 * @author Simon Johanning
 */
public class RandomInDelTMSCommunication extends RandomInDelTMS{

    public RandomInDelTMSCommunication(double edgeDeletionProbability, double edgeInsertionProbability, boolean selfReferentialTopology) {
        super(edgeDeletionProbability, edgeInsertionProbability, selfReferentialTopology);
    }

    public void manipulateTopology(SimulationContainer simulationContainer, SocialGraph socialGraph) {
        super.manipulateTopology(simulationContainer, socialGraph, SocialGraph.EDGEMEDIUM.COMMUNICATION);
    }
}
