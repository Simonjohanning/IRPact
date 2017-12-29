package IRPact_modellierung.simulation;

import IRPact_modellierung.agents.consumerAgents.*;
import IRPact_modellierung.agents.posAgents.POSAgent;
import IRPact_modellierung.agents.posAgents.POSAgentFactory;
import IRPact_modellierung.events.*;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.helper.StructureEnricher;
import IRPact_modellierung.helper.ValueConversionHelper;
import IRPact_modellierung.network.SNFactory;
import IRPact_modellierung.network.SNNode;
import IRPact_modellierung.network.SocialNetwork;
import IRPact_modellierung.preference.Value;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductFactory;
import IRPact_modellierung.products.ProductGroup;
import IRPact_modellierung.space.SpatialFactory;
import IRPact_modellierung.time.DiscreteTimeModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Scheduled;

import java.util.HashSet;
import java.util.Set;

/**
 * The SimulationFactory is used to instantiate the simulation by creating the SimulationContainer
 * and setting it up.
 *
 * It is used to manage a simulation.
 *
 * @author Simon Johanning
 */
public class SimulationFactory {

	private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	public static SimulationContainer createSimulation(Configuration configuration) {
		if (configuration.getTemporalConfiguration().isSynchronous()) return createDiscreteSynchronousSimulation(configuration);
		else return createASynchronousSimulation(configuration);
	}

	//TODO implement
	private static SimulationContainer createASynchronousSimulation(Configuration configuration) throws UnsupportedOperationException{
		throw new UnsupportedOperationException("The simulation with asynchronous agents is not yet implemented (v1.0)");
	}

	/**
	 * Method to create a simulation container for a discrete time model and synchronous agents based on the given configuration.
	 *
	 * @param configuration The configuration of the model the simulation is based upon.
	 * @return The container the simulation runs in
	 * @throws IllegalArgumentException Will be thrown when one of the methods called in this throws an IllegalArgumentException
	 * @throws UnsupportedOperationException Will be thrown when one of the methods called in this throws an UnsupportedOperationException
	 */
	private static SimulationContainer createDiscreteSynchronousSimulation(Configuration configuration) throws UnsupportedOperationException, IllegalArgumentException{
		try {
			SimulationContainer container = new SimulationContainer(configuration);
			//set temporal model
			container.setTimeModel(new DiscreteTimeModel(container, configuration.getProcessModel(), (int) Math.floor(configuration.getTemporalConfiguration().getSimulationLength()), true));
			//set spatial model
			container.setSpatialModel(SpatialFactory.createSpatialModel(configuration.getSpatialConfiguration(), container));
			//set event scheduler
			container.setEventScheduler(new DiscreteEventScheduler(container));
			//Set all generated products of the simulation
			HashSet<Product> products = new HashSet<Product>();
			for(ProductGroup productGroup : configuration.getProductConfiguration().getProductGroups()){
                Set<Product> productsOfProductGroup = ProductFactory.createProducts(productGroup, configuration, container);
                fooLog.debug("Finished creating the products {}",productsOfProductGroup);
                for(Product productInPG : productsOfProductGroup) {
                    productGroup.addDerivedProduct(productInPG);
                }
                products.addAll(productsOfProductGroup);
            }
            //add all products already set, so MIEs can refer to them
			container.setProducts(products);
			Set<MarketIntroductionEvent> mies = ProductFactory.createMarketIntroductionEvents(configuration.getProductConfiguration(), container);
			products.addAll(LazynessHelper.getConcernedProducts(mies));
			//add the new products to the simulation
			container.setProducts(products);
			Set<ProductDiscontinuationEvent> pdes = ProductFactory.createProductDiscontinuationEvents(container, configuration.getProductConfiguration().getProductDiscontinuationEvents());
			//set all consumer agents of the simulation
			HashSet<ConsumerAgent> consumerAgents = new HashSet<ConsumerAgent>();
			for(ConsumerAgentGroup consumerAgentGroup : configuration.getAgentConfiguration().getConsumerAgentGroups()){
                fooLog.debug("Attempting to create consumers for consumer group {} of which there should be {}",consumerAgentGroup.getGroupName(),configuration.getAgentConfiguration().getNoAgentsPerGroup().get(consumerAgentGroup));
                Set<SynchronousConsumerAgent> agentsInCAG = SynchronousConsumerAgentFactory.createConsumers(consumerAgentGroup, configuration.getAgentConfiguration().getNoAgentsPerGroup().get(consumerAgentGroup), container, configuration);
                consumerAgentGroup.setConsumerAgentsInGroup(ValueConversionHelper.synchronousConsumerAgentsToConsumerAgents(agentsInCAG));
                consumerAgents.addAll(agentsInCAG);
            }
			container.setConsumerAgents(consumerAgents);
			fooLog.info("Consumer Agents initialized");
			//retrieve values
			Set<Value> valuesUsed = StructureEnricher.getValuesUsed(configuration.getAgentConfiguration().getConsumerAgentGroups());
			container.setValuesUsed(valuesUsed);
			//set POS agents of the simulation
			if(configuration.getAgentConfiguration().getPosAgentConfiguration() != null){
                if(configuration.getAgentConfiguration().getPosAgentConfiguration().size() > 0){
                    //if POS agents are configured
                    container.setPosAgents(POSAgentFactory.createPOSAgents(container, container.getProducts(), configuration.getAgentConfiguration().getPosAgentConfiguration()));
                } else{
                    HashSet<POSAgent> posAgents = new HashSet<POSAgent>(1);
                    posAgents.add(POSAgentFactory.createDefaultPOSAgent(container));
                    container.setPosAgents(posAgents);
                }
            }else{
                HashSet<POSAgent> posAgents = new HashSet<POSAgent>(1);
                posAgents.add(POSAgentFactory.createDefaultPOSAgent(container));
                container.setPosAgents(posAgents);
            }
			//set social network
			container.setSocialNetwork(new SocialNetwork(SNFactory.createSocialGraph(container, configuration.getSNConfiguration(), getAssociatedSNNodes(consumerAgents)), configuration.getSNConfiguration().getEdgeWeightMappingScheme(), configuration.getSNConfiguration().getTopologyManipulationScheme()));
			//add scheduled events to event scheduler
			Set<ScriptedEvent> scheduledEvents = new HashSet<>();
			scheduledEvents.addAll(mies);
			scheduledEvents.addAll(pdes);
			for(ScriptedEvent currentEvent : scheduledEvents) {
                container.getEventScheduler().scheduleEvent(currentEvent);
            }
            fooLog.info("Container initialized");
			return container;
		} catch (UnsupportedOperationException uoe) {
			throw uoe;
		} catch (IllegalArgumentException iae) {
			throw iae;
		}
	}

	private static Set<SNNode> getAssociatedSNNodes(HashSet<ConsumerAgent> consumerAgents) {
		HashSet<SNNode> nodes = new HashSet<SNNode>(consumerAgents.size());
		for(ConsumerAgent consumerAgent : consumerAgents){
			nodes.add(consumerAgent.getCorrespondingNodeInSN());
		}
		return nodes;
	}


}