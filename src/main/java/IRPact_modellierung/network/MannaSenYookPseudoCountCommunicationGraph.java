package IRPact_modellierung.network;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MannaSenYookPseudoCountCommunicationGraph extends ScalefreePseudoCountCommunicationGraph {

    private double alpha;
    private double beta;

    public MannaSenYookPseudoCountCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes, Set<SNEdge> edges, HashMap<SNNode, Set<SNNode>> neighbours) {
        super(associatedSimulationContainer, sNConfiguration, nodes, edges, neighbours);
        if(!sNConfiguration.getSocialGraphParameters().containsKey("alpha")) throw new IllegalArgumentException("A scalefree communication graph was chosen, but no parameter noInitSeeds given!!");
        else if(!sNConfiguration.getSocialGraphParameters().containsKey("beta")) throw new IllegalArgumentException("A scalefree communication graph was chosen, but no parameter noInitSeeds given!!");
        else{
            alpha = (Double) sNConfiguration.getSocialGraphParameters().get("alpha");
            beta = (Double) sNConfiguration.getSocialGraphParameters().get("beta");
        }
    }

    public MannaSenYookPseudoCountCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
        super(associatedSimulationContainer, sNConfiguration, initialNodes);
        if(!sNConfiguration.getSocialGraphParameters().containsKey("alpha")) throw new IllegalArgumentException("A scalefree communication graph was chosen, but no parameter noInitSeeds given!!");
        else if(!sNConfiguration.getSocialGraphParameters().containsKey("beta")) throw new IllegalArgumentException("A scalefree communication graph was chosen, but no parameter noInitSeeds given!!");
        else{
            alpha = (Double) sNConfiguration.getSocialGraphParameters().get("alpha");
            beta = (Double) sNConfiguration.getSocialGraphParameters().get("beta");
        }
        Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
        for(SNEdge edge : initialEdges){
            addEdge(edge);
        }
    }

    public MannaSenYookPseudoCountCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) {
        super(associatedSimulationContainer, sNConfiguration, numberOfNodes);
        if(!sNConfiguration.getSocialGraphParameters().containsKey("alpha")) throw new IllegalArgumentException("A scalefree communication graph was chosen, but no parameter noInitSeeds given!!");
        else if(!sNConfiguration.getSocialGraphParameters().containsKey("beta")) throw new IllegalArgumentException("A scalefree communication graph was chosen, but no parameter noInitSeeds given!!");
        else{
            alpha = (Double) sNConfiguration.getSocialGraphParameters().get("alpha");
            beta = (Double) sNConfiguration.getSocialGraphParameters().get("beta");
        }
        Set<SNEdge> initialEdges = createEdges(sNConfiguration, nodes);
        for(SNEdge edge : initialEdges){
            addEdge(edge);
        }
    }

    public double getAlpha() {
        return this.alpha;
    }

    public double getBeta() {
        return this.beta;
    }


    public void addNodes(SNConfiguration sNconfiguration, Set<SNNode> nodesToAdd) {

    }

    protected double calculateProbabilityConnected(SNNode sourceNode, SNNode targetNode, Map<SNNode, Integer> nodeDegree){
        ConsumerAgent sourceAgent = associatedSimulationContainer.getsNMap().get(sourceNode);
        ConsumerAgent targetAgent = associatedSimulationContainer.getsNMap().get(targetNode);
        double distance = associatedSimulationContainer.getSpatialModel().measureDistance(sourceAgent.getCoordinates(), targetAgent.getCoordinates());
        int degree;
        if(nodeDegree.containsKey(targetNode)) degree = nodeDegree.get(targetNode);
        else degree = 0;
        return Math.pow(degree, beta) * Math.pow(distance, alpha);
    }

}