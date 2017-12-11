package IRPact_modellierung.events;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.helper.FilterHelper;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.needs.Need;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;
import antlr.SemanticException;
import org.apache.logging.log4j.LogManager;

import java.util.Set;

/**
 * A need event is an event that represents the development of a need of a consumer agent.
 * Since in this framework of innovation diffusion, needs are fulfilled by adopting a product,
 * processing a need event means to trigger an (adoption) decision for the consumer agent associated with this need through the need event.
 * For this a number of relevant products are selected, and the decision process associated with the product or the consumer is invoked.
 * Since in this framework a decision process results in the adoption of a product (or the decision process is responsible for (re-)scheduling the need event),
 * the need is assumed to be satisfied after the need event is processed.
 *
 * @author Simon Johanning
 */
public class NeedEvent extends Event {

	private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	private Need need;
	private ConsumerAgent consumerAgentConcerned;

	/**
	 * Event that associates a need with a consumer agent that will be fulfilled at the specified time of the simulation
	 * taking place in the specified container
	 *
	 * @param scheduledForTime Scheduled time of the execution of the event
	 * @param consumerAgentConcerned Consumer agent developing the need to be satisfied through this event
	 * @param need Need that is to be satisfied through processing this need event
	 * @param simulationContainer Container in which this simulation takes place
	 *
	 * @throws IllegalArgumentException Will be thrown if the consumer agent is not part of the simulation or the need is already satisfied
	 */
	public NeedEvent(double scheduledForTime, ConsumerAgent consumerAgentConcerned, Need need, SimulationContainer simulationContainer) throws IllegalArgumentException{
		super(simulationContainer, scheduledForTime);
		if(!simulationContainer.getConsumerAgents().contains(consumerAgentConcerned)) throw new IllegalArgumentException("Consumer agent the need event to be created refers to is not part of the simulation the need is to be part of!!!");
		//TODO check if need really mustn't be satisfied (at the time of scheduling the need event)!
		else if(LazynessHelper.aggregateNeedsFromProducts(LazynessHelper.getCorrespondingProducts(consumerAgentConcerned.getAdoptedProducts())).contains(need)) throw new IllegalArgumentException("Need "+need+" the need event refers to is already satisfied by an adopted product of the consumer agent the need event refers to ("+consumerAgentConcerned+")!!");
		this.consumerAgentConcerned = consumerAgentConcerned;
		this.need = need;
		fooLog.debug("Need event scheduled for need {} at time {}",need, scheduledForTime);
	}

	public Need getNeed() {
		return need;
	}

	public ConsumerAgent getConsumerAgentConcerned() {
		return consumerAgentConcerned;
	}

	/**
	 * Processing a need event results is compiling a list of relevant products for the decision and selecting the decision process used for product adoption.
	 * It will trigger the decision process, which should either result in the adoption of an eligable product or the scheduling of a new need event.
	 * Relevant products are product the consumer is aware of, that are introduced to the market, that satisfy this need and that are adoptable.
	 * The decision process used for product adoption is either a overwrite decision process common to all relevant products or the decision process associated with the consumer agent group of the consumer associated to the event
	 *
	 * @param systemTime The current time of the system for execution
	 */
	public void processEvent(double systemTime) throws IllegalStateException, IllegalArgumentException{
		if(systemTime < getScheduledForTime()) throw new IllegalArgumentException("The evaluation time of the event lies before the scheduled time, implying temporal inconsistency!!");
		fooLog.debug("Need event processed for need {} and consumer agent {}",need.getName(), consumerAgentConcerned.getAgentID());
		//filter uneligable products (products the consumer isn't aware of, that are not in the market, that don't satisfy the relevant need and that are not disqualified from adoption
		Set<Product> productsConsumerIsAwareOf = FilterHelper.selectKnownProducts(consumerAgentConcerned.getProductAwarenessMap());
		Set<Product> productsIntroducedToMarket = FilterHelper.filterUnintroducedProducts(productsConsumerIsAwareOf);
		Set<Product> productsWithNeedsFor = FilterHelper.filterProductsForNeed(productsIntroducedToMarket, need);
		Set<Product> productsQualifyingForDecisison = FilterHelper.filterUnadoptableProducts(productsWithNeedsFor, consumerAgentConcerned);
		if(!(consumerAgentConcerned.needAlreadySatisfied(need)) && !(productsQualifyingForDecisison.isEmpty())) {
			fooLog.debug("Need event leaves "+productsQualifyingForDecisison.size()+" products to decide to adopt");
			for(Product prod : productsQualifyingForDecisison){
				fooLog.debug("Current product: "+prod.getName());
			}
			//check if all product groups involved share the same decision process overwrite rule (null if not)
			try {
				if(LazynessHelper.findCommonOverwriteDecisionMakingProcessProducts(productsQualifyingForDecisison)) ((Product) productsQualifyingForDecisison.toArray()[0]).getPartOfProductGroup().getOverwriteDecisionProcess().makeProductAdoptionDecision(associatedSimulationContainer, consumerAgentConcerned, productsQualifyingForDecisison, systemTime);
                else{
                	Product productToAdopt = consumerAgentConcerned.getCorrespondingConsumerAgentGroup().getDecisionProcessEmployed().makeProductAdoptionDecision(associatedSimulationContainer, consumerAgentConcerned, productsQualifyingForDecisison, systemTime);
                	fooLog.debug("About to adopt {}", productToAdopt);
                	if(productToAdopt != null) consumerAgentConcerned.adoptProduct(productToAdopt);
                	else throw new IllegalStateException("Decision Process "+ consumerAgentConcerned.getCorrespondingConsumerAgentGroup().getDecisionProcessEmployed() + " didnt give a product to adopt for "+consumerAgentConcerned+" between products "+productsQualifyingForDecisison);
				}
			} catch (SemanticException e) {
				e.printStackTrace();
			}
		}//if need satisfied or no product qualifies, invoke the NeedDevelopmentScheme
		else consumerAgentConcerned.getCorrespondingConsumerAgentGroup().getNeedDevelopmentScheme().createNeedEvents(associatedSimulationContainer, consumerAgentConcerned);
	}
}