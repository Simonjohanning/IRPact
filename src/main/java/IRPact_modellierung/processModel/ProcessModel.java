package IRPact_modellierung.processModel;

import IRPact_modellierung.agents.companyAgents.CompanyAgent;
import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.agents.policyAgent.PolicyAgent;
import IRPact_modellierung.agents.posAgents.POSAgent;
import IRPact_modellierung.products.AdoptedProduct;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;

import java.util.HashSet;
import java.util.Set;

/**
 * A process model governs the execution of dynamic aspects of the model within the temporal model.
 * It specifies how the different actors within the simulation are processed,
 * and describes the processes governing their behavior.
 *
 * It further provides a few auxiliary methods regarding product discontinuation.
 *
 * @author Simon Johanning
 */
public abstract class ProcessModel {

    private AdoptionReplacementScheme adoptionReplacementScheme;
    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");


    public ProcessModel(AdoptionReplacementScheme adoptionReplacementScheme) {
        this.adoptionReplacementScheme = adoptionReplacementScheme;
    }

    public AdoptionReplacementScheme getAdoptionReplacementScheme() {
        return adoptionReplacementScheme;
    }

    /**
     * Method to describe the processes governing the behavior of ConsumerAgents.
     * Describes how (and in what order) ConsumerAgents perceive, interact and act.
     *
     * @param consumerAgentsToProcess The consumer agents to process
     * @param simulationContainer The container the simulation runs in
     * @param simulationTime The current system time
     */
    public abstract void processConsumerAgents(Set<ConsumerAgent> consumerAgentsToProcess, SimulationContainer simulationContainer, double simulationTime);

    /**
     * Method to describe the processes governing the behavior of CompanyAgents.
     * Describes how (and in what order) CompanyAgents perceive, interact and act.
     *
     * @param agentsToProcess The company agents to process
     * @param simulationContainer The container the simulation runs in
     * @param simulationTime The current system time
     */
    public abstract void processCompanyAgents(Set<CompanyAgent> agentsToProcess, SimulationContainer simulationContainer, double simulationTime);

    /**
     * Method to describe the processes governing the behavior of the PolicyAgent of the simulation.
     * Describes how (and in what order) the PolicyAgent perceives and acts.
     *
     * @param simulationContainer The container the simulation runs in
     * @param simulationTime The current system time
     */
    public abstract void processPolicyAgent(SimulationContainer simulationContainer, double simulationTime);

    /**
     * Method to describe the processes governing the behavior of POSAgents.
     * Describes how (and in what order) POSAgents perceive, interact and act.
     *
     * @param pOSAgentsToProcess The consumer agents to process
     * @param simulationContainer The container the simulation runs in
     * @param simulationTime The current system time
     */
    public abstract void processPOSAgents(Set<POSAgent> pOSAgentsToProcess, SimulationContainer simulationContainer, double simulationTime);

    /**
     * Method to describe how consumer agents react after
     * a product they adopted is discontinued.
     * This is done by invoking the adoptionReplacementScheme of the process model
     *
     * @param consumerAgentConcerned The consumer agent that needs to readopt
     * @param productConcerned The product that is discontinued
     * @param simulationTime The current simulation time
     */
    public void readoptAfterDiscontinuation(ConsumerAgent consumerAgentConcerned, Product productConcerned, double simulationTime){
        fooLog.info("attempting to readopt");
        adoptionReplacementScheme.readopt(consumerAgentConcerned, productConcerned, simulationTime);
    }

    /**
     * Helper method that checks if any consumer agent has a product adopted whose
     * expiration date past.
     * If this is the case, the product will be removed from the list of adopted products of that agent
     *
     * @param simulationContainer The container the simulation runs in
     */
    public void checkForProductExpiration(SimulationContainer simulationContainer){
        for(ConsumerAgent currentAgent : simulationContainer.getConsumerAgents()){
            fooLog.debug("Checking expiration for agent {}",currentAgent);
            Set<AdoptedProduct> productsToRemove = new HashSet<>();
            for(AdoptedProduct adoptedProduct : currentAgent.getAdoptedProducts()){
                fooLog.debug("Checking expiration for product {} of agent {}",adoptedProduct, currentAgent);
                if(adoptedProduct.productExpired(simulationContainer.getTimeModel().getSimulationTime())){
                    productsToRemove.add(adoptedProduct);
                }
            }
            //Products need to removed after the fact to prevent concurrent arraylist errors
            for(AdoptedProduct productToRemove : productsToRemove){
                currentAgent.removeAdoptedProduct(productToRemove);
            }
        }
    }

}
