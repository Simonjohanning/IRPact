package IRPact_modellierung.network;

import IRPact_modellierung.simulation.SimulationContainer;

import java.util.HashSet;
import java.util.Set;

/**
 * The RandomInDel TopologyManipulationScheme is a TMS in which every edge present in the graph will be
 * deleted with a given, uniform probability, and every possible edge is created with a given, uniform probability.
 * This is done for all media in the simulation.
 *
 * @author Simon Johanning
 */
public class RandomInDelTMS extends TopologyManipulationScheme {

	public double edgeDeletionProbability;
	private double edgeInsertionProbability;

	public RandomInDelTMS(double edgeDeletionProbability, double edgeInsertionProbability, boolean selfReferentialTopology) {
		super(selfReferentialTopology);
		this.edgeDeletionProbability = edgeDeletionProbability;
		this.edgeInsertionProbability = edgeInsertionProbability;
	}

	public double getEdgeDeletionProbability() {
		return this.edgeDeletionProbability;
	}

	public double getEdgeInsertionProbability() {
		return this.edgeInsertionProbability;
	}

	/**
	 * Method that add / removes edges with a fixed, uniform probability from the graph for all media
	 *
	 * @param simulationContainer The container the simulation takes place in
	 * @param socialGraph The graph whose topology is to be manipulated
	 */
	public void manipulateTopology(SimulationContainer simulationContainer, SocialGraph socialGraph) {
		for(SocialGraph.EDGEMEDIUM currentMedium : SocialGraph.EDGEMEDIUM.values()){
			manipulateTopology(simulationContainer, socialGraph, currentMedium);
		}
	}

	/**
	 * This method manipulates the topology of the graph for only one given medium,
	 * where edges are added and removed proportionally to the probabilities of the scheme.
	 *
	 * @param simulationContainer The container the simulation takes place in
	 * @param socialGraph The graph whose topology is to be manipulated
	 * @param edgemedium The medium for which the topology is manipulated
	 */
	protected void manipulateTopology(SimulationContainer simulationContainer, SocialGraph socialGraph, SocialGraph.EDGEMEDIUM edgemedium){
		Set<SNEdge> edgesToDelete = new HashSet<>();
		Set<SNEdge> edgesToAdd = new HashSet<>();
		//for every combination of nodes (i.e. potential edges of the respective medium)
		for(SNNode currentSourceNode : socialGraph.getNodes()){
			for(SNNode currentTargetNode : socialGraph.getNodes()){
				//if an edge exists between both nodes, it is checked if it needs to be removed
				if(socialGraph.neighbours.get(edgemedium).get(currentSourceNode).contains(currentTargetNode)){
					if(Math.random() < edgeDeletionProbability){
						edgesToDelete.addAll(socialGraph.outgoingEdgesMedium.get(edgemedium).get(currentSourceNode));
					}
				}
				//if no edge (of this medium) exists between the two nodes, it is checked whether one needs to be added
				else{
					if(Math.random() < edgeInsertionProbability){
						edgesToAdd.add(SNFactory.createEdge(currentSourceNode, currentTargetNode, simulationContainer.getSocialNetwork().getEdgeWeightManipulationScheme().weighEdge(currentSourceNode, currentTargetNode, edgemedium), edgemedium));
					}
				}
			}
		}
		//respective edges are added / deleted (done at this point in order to prevent interference of edges)
		for(SNEdge currentEdge : edgesToDelete){
			socialGraph.removeEdge(currentEdge);
		}
		for(SNEdge currentEdge : edgesToAdd){
			socialGraph.addEdge(currentEdge);
		}
	}
}