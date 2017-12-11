package IRPact_modellierung.network;

import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class ScalefreePseudoCountCommunicationGraph extends CommunicationGraph {


    private int noInitSeeds;
    private int noConnectedEdges;
    private int pseudoCount;

    public ScalefreePseudoCountCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> nodes, Set<SNEdge> edges, HashMap<SNNode, Set<SNNode>> neighbours) {
        super(associatedSimulationContainer, sNConfiguration, nodes, edges, neighbours);
        if(!sNConfiguration.getSocialGraphParameters().containsKey("noInitSeeds")) throw new IllegalArgumentException("A scalefree communication graph was chosen, but no parameter noInitSeeds given!!");
        else if(!sNConfiguration.getSocialGraphParameters().containsKey("noConnectedEdges")) throw new IllegalArgumentException("A scalefree communication graph was chosen, but no parameter noInitSeeds given!!");
        else if(!sNConfiguration.getSocialGraphParameters().containsKey("pseudoCount")) throw new IllegalArgumentException("A pseudo count scalefree communication graph was chosen, but no parameter pseudoCount given!!");
        else if((Integer) sNConfiguration.getSocialGraphParameters().get("noInitSeeds") <= 0) throw new IllegalArgumentException("noInitSeeds must be larger than 0, but is set to "+sNConfiguration.getSocialGraphParameters().get("noInitSeeds")+"!!");
        else if((Integer) sNConfiguration.getSocialGraphParameters().get("noConnectedEdges") < 0)  throw new IllegalArgumentException("noConnectedEdges must not be negative, but is set to "+sNConfiguration.getSocialGraphParameters().get("noConnectedEdges")+"!!");
        else if((Integer) sNConfiguration.getSocialGraphParameters().get("pseudoCount")  < 0) throw new IllegalArgumentException("pseudoCount must not be negative, but is set to "+sNConfiguration.getSocialGraphParameters().get("pseudoCount")+"!!");
        else{
            noInitSeeds = (Integer) sNConfiguration.getSocialGraphParameters().get("noInitSeeds");
            noConnectedEdges = (Integer) sNConfiguration.getSocialGraphParameters().get("noConnectedEdges");
            pseudoCount = (Integer) sNConfiguration.getSocialGraphParameters().get("pseudoCount");
        }
    }

    public ScalefreePseudoCountCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
        super(associatedSimulationContainer, sNConfiguration, initialNodes);
        if(!sNConfiguration.getSocialGraphParameters().containsKey("noInitSeeds")) throw new IllegalArgumentException("A scalefree communication graph was chosen, but no parameter noInitSeeds given!!");
        else if(!sNConfiguration.getSocialGraphParameters().containsKey("noConnectedEdges")) throw new IllegalArgumentException("A scalefree communication graph was chosen, but no parameter noInitSeeds given!!");
        else if((Integer) sNConfiguration.getSocialGraphParameters().get("noInitSeeds") > initialNodes.size()) throw new IllegalArgumentException("More initial seed nodes are required for the social graph than provided!!");
        else if((Integer) sNConfiguration.getSocialGraphParameters().get("noConnectedEdges") > (Integer) sNConfiguration.getSocialGraphParameters().get("noInitSeeds")) throw new IllegalArgumentException("noConnectedEdges > noInitialSeeds, meaning that not all edges can be formed in a non-multi graph!!");
        else if((Integer) sNConfiguration.getSocialGraphParameters().get("noInitSeeds") <= 0) throw new IllegalArgumentException("noInitSeeds must be larger than 0, but is set to "+sNConfiguration.getSocialGraphParameters().get("noInitSeeds")+"!!");
        else if((Integer) sNConfiguration.getSocialGraphParameters().get("noConnectedEdges") < 0)  throw new IllegalArgumentException("noConnectedEdges must not be negative, but is set to "+sNConfiguration.getSocialGraphParameters().get("noConnectedEdges")+"!!");
        else if((Integer) sNConfiguration.getSocialGraphParameters().get("pseudoCount")  < 0) throw new IllegalArgumentException("pseudoCount must not be negative, but is set to "+sNConfiguration.getSocialGraphParameters().get("pseudoCount")+"!!");
        else{
            noInitSeeds = (Integer) sNConfiguration.getSocialGraphParameters().get("noInitSeeds");
            noConnectedEdges = (Integer) sNConfiguration.getSocialGraphParameters().get("noConnectedEdges");
            pseudoCount = (Integer) sNConfiguration.getSocialGraphParameters().get("pseudoCount");
        }
    }

    public ScalefreePseudoCountCommunicationGraph(SimulationContainer associatedSimulationContainer, SNConfiguration sNConfiguration, int numberOfNodes) {
        super(associatedSimulationContainer, sNConfiguration, numberOfNodes);
        if(!sNConfiguration.getSocialGraphParameters().containsKey("noInitSeeds")) throw new IllegalArgumentException("A scalefree communication graph was chosen, but no parameter noInitSeeds given!!");
        else if(!sNConfiguration.getSocialGraphParameters().containsKey("noConnectedEdges")) throw new IllegalArgumentException("A scalefree communication graph was chosen, but no parameter noInitSeeds given!!");
        else if((Integer) sNConfiguration.getSocialGraphParameters().get("noInitSeeds") > numberOfNodes) throw new IllegalArgumentException("More initial seed nodes are required for the social graph than provided!!");
        else if((Integer) sNConfiguration.getSocialGraphParameters().get("noConnectedEdges") > (Integer) sNConfiguration.getSocialGraphParameters().get("noInitSeeds")) throw new IllegalArgumentException("noConnectedEdges > noInitialSeeds, meaning that not all edges can be formed in a non-multi graph!!");
        else if((Integer) sNConfiguration.getSocialGraphParameters().get("noInitSeeds") <= 0) throw new IllegalArgumentException("noInitSeeds must be larger than 0, but is set to "+sNConfiguration.getSocialGraphParameters().get("noInitSeeds")+"!!");
        else if((Integer) sNConfiguration.getSocialGraphParameters().get("noConnectedEdges") < 0)  throw new IllegalArgumentException("noConnectedEdges must not be negative, but is set to "+sNConfiguration.getSocialGraphParameters().get("noConnectedEdges")+"!!");
        else if((Integer) sNConfiguration.getSocialGraphParameters().get("pseudoCount")  < 0) throw new IllegalArgumentException("pseudoCount must not be negative, but is set to "+sNConfiguration.getSocialGraphParameters().get("pseudoCount")+"!!");
        else{
            noInitSeeds = (Integer) sNConfiguration.getSocialGraphParameters().get("noInitSeeds");
            noConnectedEdges = (Integer) sNConfiguration.getSocialGraphParameters().get("noConnectedEdges");
            pseudoCount = (Integer) sNConfiguration.getSocialGraphParameters().get("pseudoCount");
        }
    }

    public int getNoInitSeeds() {
        return this.noInitSeeds;
    }

    public int getNoConnectedEdges() {
        return this.noConnectedEdges;
    }

    //TODO implement
    public void addNodes(SNConfiguration sNconfiguration, Set<SNNode> nodesToAdd) {

    }

    public Set<SNEdge> createEdges(SNConfiguration sNConfiguration, Set<SNNode> initialNodes) {
        //choose a number of seed nodes to start the network
        Set<SNNode> seedNodes = LazynessHelper.pickNNodes(initialNodes, noInitSeeds);
        Set<SNNode> subsequentNodes = new HashSet<>();
        //map to order nodes already processed by a constructed index
        Map<Integer, SNNode> processedNodesIndexMap = new HashMap<>();
        //map to hold the (in-)degree of nodes
        Map<SNNode, Integer> nodeInDegreeMap = new HashMap<>();
        int currentNodeIndex = 0;
        //process nodes initially
        for(SNNode currentNode : initialNodes){
            //by putting them into the respective set
            if(!seedNodes.contains(currentNode))  subsequentNodes.add(currentNode);
            else{
                if(processedNodesIndexMap.containsKey(currentNodeIndex)) throw new IllegalStateException("processedNodesIndexMap already contains a node at index "+currentNodeIndex);
                //and putting the initial one in the processed set
                processedNodesIndexMap.put(currentNodeIndex, currentNode);
                currentNodeIndex++;
                //initial nodes will form a regular network, i.e. already be connected to all nodes but themselves
                nodeInDegreeMap.put(currentNode, noInitSeeds-1);
            }
        }
        //construct the initial edges (as regular graph)
        Set<SNEdge> returnSet = RegularCommunicationGraph.createStaticEdges(noInitSeeds-1, seedNodes, sNConfiguration.getEdgeWeightMappingScheme());
        LazynessHelper.detectDoubleEdges(returnSet, initialNodes);
        //process all nodes not processed in the initial step
        for(SNNode currentNode : subsequentNodes){
            //structure to remember which nodes they already have edges to
            Set<SNNode> currentNodeNeighbours = new HashSet<>();
            //hook them up with the respective number of edges
            for(int edgeIndex=0;edgeIndex<noConnectedEdges;edgeIndex++){
                Map<Integer, Double> indexConnectionProbabilityMap = new HashMap<>();
                for(Integer currentIndex : processedNodesIndexMap.keySet()){
                    //if an edge already exists towards the respective node, set a probability of 0
                    if(currentNodeNeighbours.contains(processedNodesIndexMap.get(currentIndex))) indexConnectionProbabilityMap.put(currentIndex, 0.0);
                        //else derive the probability from the implementing schemes
                    else{
                        double probInSCG = calculateProbabilityConnected(currentNode, processedNodesIndexMap.get(currentIndex), nodeInDegreeMap);
                        indexConnectionProbabilityMap.put(currentIndex, probInSCG);
                    }
//					System.out.println("Put a probability of "+indexConnectionProbabilityMap.get(currentIndex));
                }
                //based on the constructed probability distribution, choose a respective node
                SNNode targetNode = processedNodesIndexMap.get(LazynessHelper.chooseIntegerByDistribution(indexConnectionProbabilityMap));
                if(LazynessHelper.existsEdge(returnSet, currentNode, targetNode)) System.out.println("Edge between currentNode and target node "+targetNode+"exists: "+neighbours.get(currentNode));
                //and construct an edge
                returnSet.add(new SNCommunicationEdge(currentNode, targetNode, sNConfiguration.getEdgeWeightMappingScheme().weighEdge(currentNode, targetNode, SocialGraph.EDGEMEDIUM.COMMUNICATION)));
                //add the node which currentNode now posses an edge towards to the list of nodes to exclude
                currentNodeNeighbours.add(targetNode);
                //and increase the node degree of the connected node
                if(nodeInDegreeMap.keySet().contains(targetNode)) {
                    int targetNodeDegree = nodeInDegreeMap.get(targetNode);
                    nodeInDegreeMap.put(targetNode, targetNodeDegree + 1);
                }else nodeInDegreeMap.put(targetNode, 1);
            }
            //after all edges have been constructed, add the node to the set of unprocessed nodes
            if(processedNodesIndexMap.containsKey(currentNodeIndex)) throw new IllegalStateException("processedNodesIndexMap already contains a node at index "+currentNodeIndex);
            processedNodesIndexMap.put(currentNodeIndex, currentNode);
            nodeInDegreeMap.put(currentNode, pseudoCount);
            currentNodeIndex++;
        }
        return returnSet;
    }

    protected abstract double calculateProbabilityConnected(SNNode sourceNode, SNNode targetNode, Map<SNNode, Integer> nodeInDegreeMap);
}