package IRPact_modellierung.agents.consumerAgents;

import java.util.Set;

/**
 * Class to create objects of type AsynchornousConsumerAgent for the simulation
 */
//TODO pretty much everything
public class ASynchronousConsumerAgentFactory {


	/**
	 * NOT IMPLEMENTED!!! DO NOT USE!!!!
	 *
	 * @param correspondingConsumerAgentGroup
	 * @param numberOfConsumersToCreate
	 */
	public Set<SynchronousConsumerAgent> createConsumers(ConsumerAgentGroup correspondingConsumerAgentGroup, int numberOfConsumersToCreate) {
		// TODO - implement ASynchronousConsumerAgentFactory.createConsumers
		throw new UnsupportedOperationException();
	}

/*
  /*

	For each agent:
	final IInitialisationService service = SServiceProvider.getDeclaredService(agent.getAccess(), IInitialisationService.class).get();
				service.start();
				//creation of the agents (as jadex components)
					final ITuple2Future<IComponentIdentifier, Map<String, Object>> componentFuture = cms.createComponent(agentClass+"Agent_"+i+"_ID_" + agentID, ProsumerAgent.class.getName() + ".class", creationParent);
					final IComponentIdentifier component = componentFuture.getFirstResult();
					//fetch the external access handle for the component from the component management system
					final IExternalAccess external = cms.getExternalAccess(component).get();


					//TODO change to the corresponding interface for the agents (now its David Georgs IInitializationService that will eventually be replaced with a (consumer?) interface)
		//Get a handle for the initialization of services of the corresponding agent IExternalAccess and the IInitializationService interface for the agents
		final IInitialisationService serviceSQ = SServiceProvider.getDeclaredService(agent.getAccess(), IInitialisationService.class).get();

		//Set the attributes of the agent through the IInitializationService handle corresponding to the agent
		serviceSQ.setEinkommen(random.nextDouble());
		serviceSQ.setOekoNeigung(random.nextDouble() * 2 / 3);
		// previous: serviceSQ.setPosition(new Vector2Double(agentID / dim, agentID % dim));
		initLogger.info("Setting agent position to ({},{})",agentXPos, agentYPos);
		serviceSQ.setPosition(new Vector2Double(agentXPos, agentYPos));
		debugLogger.debug("Set velocity to ({},{})", agentXVelocity, agentYVelocity);
		serviceSQ.setVelocity(new Vector2Double(agentXVelocity, agentYVelocity));

		// Add neighbour for the nodes the current node is connected to
				final IInitialisationService serviceStart = SServiceProvider.getDeclaredService(node.getNodeAccess(), IInitialisationService.class).get();
				serviceStart.addNachbar(edge.getTarget().getNodeAccess());


		private static void nachbarSetzen(final IExternalAccess start, final IExternalAccess end) {
		final IInitialisationService serviceStart = SServiceProvider.getDeclaredService(start, IInitialisationService.class).get();
		serviceStart.addNachbar(end);
		final IInitialisationService serviceEnd = SServiceProvider.getDeclaredService(end, IInitialisationService.class).get();
		serviceEnd.addNachbar(start);
	}

*/

}