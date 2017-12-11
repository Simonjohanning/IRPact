package IRPact_modellierung.network;

import IRPact_modellierung.simulation.SimulationContainer;
import edu.uci.ics.jung.graph.event.GraphEvent;
import org.apache.logging.log4j.LogManager;

import java.util.*;


/**
 * The GilbertCommunicationGraph is a graph whose edges are all communicative edges and whose topology
 * is initialized after the Gilbert random model, i.e. a random graph with a fixed probability for every potential edge.
 *
 * The probability for a communication edge is given by p.
 *
 * @author Simon Johanning
 */
public class GilbertCommunicationGraph extends CommunicationGraph {

	private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	private double p;

	public double getP() {
		return this.p;
	}

	/**
	 * Constructor giving the parameter for the GilbertCommunicationGraph and the edges,
	 * as well as neighbourhood relations explicitly
	 *
	 * @param associatedSimulationContainer The container the social network is embedded in
	 * @param sNConfiguration The configuration object of the social network
	 * @param nodes The nodes the graph contains
	 * @param edges The edges the graph contains
	 * @param neighbours The neighbourhood relationships of the graph
	 */
	public GilbertCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes, Set<SNEdge> edges, HashMap<SNNode, Set<SNNode>> neighbours) {
		super(associatedSimulationContainer, sNConfiguration, nodes, edges, neighbours);
		this.p = (Double) sNConfiguration.getSocialGraphParameters().get("p");
	}

	/**
	 * Constructor for the GilbertCommunicationGraph giving the nodes in the social graph explicitly
	 *
	 * @param associatedSimulationContainer The container the social network is embedded in
	 * @param sNConfiguration The configuration object of the social network with the socialGraph parameter containing the parameter 'p'
	 * @param nodes The nodes the socialGraph is based upon
	 */
	public GilbertCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes) {
		super(associatedSimulationContainer, sNConfiguration, nodes);
		this.p = (Double) sNConfiguration.getSocialGraphParameters().get("p");
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}

	}

	/**
	 * Constructor for the GilbertCommunicationGraph giving the number of nodes in the social graph explicitly
	 *
	 * @param associatedSimulationContainer The container the social network is embedded in
	 * @param sNConfiguration The configuration object of the social network with the socialGraph parameter containing the parameter 'p'
	 * @param numberOfNodes The nodes the socialGraph is based upon
	 */
	public GilbertCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) {
		super(associatedSimulationContainer, sNConfiguration, numberOfNodes);
		this.p = (Double) sNConfiguration.getSocialGraphParameters().get("p");
		Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
		for(SNEdge edge : initialEdges){
			addEdge(edge);
		}
	}

	/**
	 * Method to add a number of nodes to an existing graph.
	 * Considers all possible (new) communication edges in the graph, and adds them stochastically,
	 * proportionally to the set parameter
	 *
	 * @param sNConfiguration The configuration the graph is based upon
	 * @param nodesToAdd The nodes to be added to the social graph
	 */
	public void addNodes(SNConfiguration sNConfiguration, Set<SNNode> nodesToAdd) {
		if(p > 0.0) {
			//new edges are the ones coming and leading to new nodes
			for (SNNode newNode : nodesToAdd) {
				addIsolatedNode(newNode);
				for (SNNode oldNode : getNodes()) {
					//check for incoming edge
					if (Math.random() < p) {
						SNCommunicationEdge edgeToAdd = new SNCommunicationEdge(oldNode, newNode, getAssociatedSimulationContainer().getSocialNetwork().getEdgeWeightManipulationScheme().weighEdge(oldNode, newNode, EDGEMEDIUM.COMMUNICATION));
						addEdge(edgeToAdd);
					}
					//check for outgoing edge
					if (Math.random() < p) {
						SNCommunicationEdge edgeToAdd = new SNCommunicationEdge(newNode, oldNode, getAssociatedSimulationContainer().getSocialNetwork().getEdgeWeightManipulationScheme().weighEdge(newNode, oldNode, EDGEMEDIUM.COMMUNICATION));
						addEdge(edgeToAdd);
					}
				}
				if (sNConfiguration.getTopologyManipulationScheme().isSelfReferential()){
					if (Math.random() < p) addEdge(new SNCommunicationEdge(newNode, newNode, getAssociatedSimulationContainer().getSocialNetwork().getEdgeWeightManipulationScheme().weighEdge(newNode, newNode, EDGEMEDIUM.COMMUNICATION)));
				}

			}
		}
	}

	/**
	 * For creating the edges for a set of nodes, all possible communicative edges are iterated,
	 * and for each edge it is decided whether it will be present or not
	 *
	 * @param sNConfiguration The configuration of the social graph of interest
	 * @param initialNodes The nodes the social graph should be initialized with
	 * @return The set of (stochastically chosen) edges within the network
	 */
	public Set<SNEdge> createEdges(SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
		Set<SNEdge> edgesToAdd = new HashSet<>();
		//iterate through all possible edges, and not which ones to set
		int noIterations = 0;
		for(SNNode sourceNode : initialNodes){
			for(SNNode targetNode : initialNodes){
				if(Math.random() < p){
					if(!sourceNode.equals(targetNode) || sNConfiguration.getTopologyManipulationScheme().isSelfReferential()) edgesToAdd.add(new SNCommunicationEdge(sourceNode, targetNode, sNConfiguration.getEdgeWeightMappingScheme().weighEdge(sourceNode, targetNode, EDGEMEDIUM.COMMUNICATION)));
				}
				noIterations++;
			}
		}
		fooLog.info("Gilbert created "+edgesToAdd.size()+" edges in "+noIterations+" iterations. p="+p);
		return edgesToAdd;
	}
}