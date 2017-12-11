package IRPact_modellierung.network;

import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.simulation.SimulationContainer;


/**
 * TopologyManipulationScheme that randomly rewires the edges in the network,
 * i.e. with a certain probability changes the source and target of an edge uniformly, as long as no other edge
 * lies between the new source and target.
 *
 * @author Simon Johanning
 */
public class RandomRewireTMS extends TopologyManipulationScheme {

	private double rewireProbability;

	public RandomRewireTMS(double rewireProbability, boolean selfReferentialTopology) {
		super(selfReferentialTopology);
		this.rewireProbability = rewireProbability;
	}

	public double getRewireProbability() {
		return this.rewireProbability;
	}

	/**
	 * With this scheme, the topology is manipulated by uniformly replacing edges with a certain probability.
	 * Edges get replaced by an edge that is not yet in the graph and that is either not self-referential or self-referentiality is given in the graph
	 *
	 * @param simulationContainer The container the simulation takes place in
	 * @param socialGraph The graph that is to be manipulated
	 */
	public void manipulateTopology(SimulationContainer simulationContainer, SocialGraph socialGraph) {
		//for all edges in the graph, with uniform probability, see if edge needs to be rewired
		for(SNEdge currentEdge : socialGraph.getEdges()){
			if(Math.random() < rewireProbability){
				//for edges that are rewired, try until finding a valid edge rewiring
				boolean validNewEdgeFound = false;
				while(!validNewEdgeFound){
					SNNode source = LazynessHelper.chooseRandomNode(socialGraph.getNodes());
					SNNode target = LazynessHelper.chooseRandomNode(socialGraph.getNodes());
					//An edge rewiring is valid when the new edge doesn't yet exist, and either the edge is not self-referential or self-referentiality is given
					if(socialGraph.getNeighbours(source,currentEdge.getMedium()).isEmpty()){
						if((source != target) || selfReferentialTopology){
							//when a valid edge was found, replace the edge and end the search for a valid edge
							socialGraph.replaceEdge(currentEdge, SNFactory.createEdge(source, target, currentEdge.getEdgeWeight(), currentEdge.getMedium()));
							validNewEdgeFound = true;
						}
					}
				}
			}
		}
	}
}