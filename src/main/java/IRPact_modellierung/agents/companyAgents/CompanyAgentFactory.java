package IRPact_modellierung.agents.companyAgents;

import IRPact_modellierung.agents.companyAgents.advertisement.AdvertisementScheme;
import IRPact_modellierung.agents.companyAgents.advertisement.DefaultAdvertisementScheme;
import IRPact_modellierung.agents.companyAgents.advertisement.UniformAdvertisementScheme;
import IRPact_modellierung.distributions.Distribution;
import IRPact_modellierung.distributions.UnivariateDistribution;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.simulation.SimulationContainer;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The CompanyAgentFactory serves to add the company agents to the simulation.
 * It manages the agent IDs, maps them to the agent names and serves to add them to the simulation container.
 *
 * @author Simon Johanning
**/
public class CompanyAgentFactory {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    private static int companyAgentID = 0;
    private static HashMap<Integer, String> companyAgentNames = new HashMap<Integer, String>();
    private static HashMap<CompanyAgent, Integer> companyAgentIDs = new HashMap<>();

    public CompanyAgent createCompanyAgent(SimulationContainer simulationContainer, CompanyAgentConfiguration agentConfiguration, Set<Product> productPortfolio){
        CompanyAgent newAgent = new CompanyAgent(simulationContainer, agentConfiguration.getInformationAuthority(), agentConfiguration.getCompanyAgentName(), productPortfolio, agentConfiguration.getProductQualityManipulationScheme(), agentConfiguration.getManagementDecisionScheme(), agentConfiguration.getAdvertisementScheme());
        companyAgentNames.put(companyAgentID, agentConfiguration.getCompanyAgentName());
        companyAgentIDs.put(newAgent, companyAgentID);
        companyAgentID++;
        simulationContainer.addCompanyAgent(newAgent);
        return newAgent;
    }

    /**
     * Method to create the agents, manage their ID and names and add them to the simulation.
     * An agent with the specified parameters and a running index will be created and added to the simulation container.
     *
     * @param simulationContainer The container the simulation runs in
     * @param productPortfolio The products the company agent manages
     * @param productQualityManipulationScheme The productQualityManipulationScheme used by the company agent to create
     * @param managementDecisionScheme The managementDecisionScheme used by the company agent to create
     * @param advertisementScheme The advertisementScheme used by the company agent to create
     * @return The agent created by the method
     */
    public CompanyAgent createCompanyAgent(SimulationContainer simulationContainer, double informationAuthority, Set<Product> productPortfolio, ProductQualityManipulationScheme productQualityManipulationScheme, ManagementDecisionScheme managementDecisionScheme, AdvertisementScheme advertisementScheme){
        CompanyAgent newAgent = new CompanyAgent(simulationContainer, informationAuthority,  Integer.toString(companyAgentID), productPortfolio, productQualityManipulationScheme, managementDecisionScheme, advertisementScheme);
        companyAgentNames.put(companyAgentID, Integer.toString(companyAgentID));
        companyAgentIDs.put(newAgent, companyAgentID);
        companyAgentID++;
        simulationContainer.addCompanyAgent(newAgent);
        return newAgent;
    }

    /**
     * Method to create the agents, manage their ID and names and add them to the simulation.
     * An agent with the specified parameters and a running index will be created and added to the simulation container.
     *
     * @param simulationContainer The container the simulation runs in
     * @param productPortfolio The products the company agent manages
     * @param productQualityManipulationScheme The productQualityManipulationScheme used by the company agent to create
     * @param managementDecisionScheme The managementDecisionScheme used by the company agent to create
     * @param advertisementScheme The advertisementScheme used by the company agent to create
     * @param name The name the agent should get
     * @return The agent created by the method
     */
    public CompanyAgent createCompanyAgent(SimulationContainer simulationContainer, double informationAuthority, Set<Product> productPortfolio, ProductQualityManipulationScheme productQualityManipulationScheme, ManagementDecisionScheme managementDecisionScheme, AdvertisementScheme advertisementScheme, String name){
        CompanyAgent newAgent = new CompanyAgent(simulationContainer, informationAuthority, name, productPortfolio, productQualityManipulationScheme, managementDecisionScheme, advertisementScheme);
        companyAgentNames.put(companyAgentID, name);
        companyAgentIDs.put(newAgent, companyAgentID);
        companyAgentID++;
        simulationContainer.addCompanyAgent(newAgent);
        return newAgent;
    }


    /**
     * The productQualityManipulationSchemeLoader creates an instance of the ProductQualityManipulationScheme
     * qualified by the productQualityManipulationSchemeQualifier, if the corresponding scheme was implemented.
     * If not it will raise the respective IllegalArgumentException.
     *
     * @param productQualityManipulationSchemeQualifier A String corresponding to the ProductManipulationScheme to be loaded
     * @return An instance of the ProductManipulationScheme specified by the ProductManipulationSchemeQualifier
     * @throws IllegalArgumentException will be thrown when the ProductManipulationSchemeQualifier refers to an unimplemented scheme
     */
    public static ProductQualityManipulationScheme productQualityManipulationSchemeLoader(String productQualityManipulationSchemeQualifier) throws IllegalArgumentException{
        switch(productQualityManipulationSchemeQualifier) {
            case "DefaultProductQualityManipulationScheme":
                return new DefaultProductQualityManipulationScheme();
            default:
                throw new IllegalArgumentException("ProductManipulationScheme " + productQualityManipulationSchemeQualifier + " is not implemented!!\nMake sure to specify an implemented scheme or implement is yourself!");
        }
    }

    /**
     * The ManagementDecisionSchemeLoader creates an instance of the ManagementDecisionScheme
     * qualified by the managementDecisionSchemeQualifier, if the corresponding scheme was implemented.
     * If not it will raise the respective IllegalArgumentException.
     *
     * @param managementDecisionSchemeQualifier A String corresponding to the ManagementDecisionScheme to be loaded
     * @return An instance of the ManagementDecisionScheme specified by the managementDecisionSchemeQualifier
     * @throws IllegalArgumentException will be thrown when the managementDecisionSchemeQualifier refers to an unimplemented scheme
     */
    public static ManagementDecisionScheme managementDecisionSchemeLoader(String managementDecisionSchemeQualifier) throws IllegalArgumentException{
        switch(managementDecisionSchemeQualifier) {
            case "DefaultManagementDecisionScheme":
                return new DefaultManagementDecisionScheme();
            default:
                throw new IllegalArgumentException("ManagementDecisionScheme " + managementDecisionSchemeQualifier + " is not implemented!!\nMake sure to specify an implemented scheme or implement is yourself!");
        }
    }

    /**
     * The AdvertisementSchemeLoader creates an instance of the AdvertisementScheme
     * qualified by the advertisementSchemeQualifier, if the corresponding scheme was implemented.
     * If not it will raise the respective IllegalArgumentException.
     *
     * @param advertisementSchemeQualifier A String corresponding to the AdvertisementScheme to be loaded
     * @param distributions The distributions used in the simulation
     * @return An instance of the AdvertisementScheme specified by the advertisementSchemeQualifier
     * @throws IllegalArgumentException will be thrown when the advertisementSchemeQualifier refers to an unimplemented scheme
     */
    public static AdvertisementScheme advertisementSchemeLoader(String advertisementSchemeQualifier, HashMap<String, Object> advertisementSchemeParameters, Map<String, Distribution> distributions) throws IllegalArgumentException{
        try {
            switch(advertisementSchemeQualifier) {
                case "DefaultAdvertisementScheme":
                    if(!advertisementSchemeParameters.containsKey("numberOfMessages")) throw new IllegalArgumentException("No parameter numberOfMessages specified in advertisementScheme configuration of the corresponding advertisementScheme");
                    else if(!advertisementSchemeParameters.containsKey("messagePreferenceIncrease")) throw new IllegalArgumentException("No parameter messagePreferenceIncrease specified in advertisementScheme configuration of the corresponding advertisementScheme");
                    else if(!advertisementSchemeParameters.containsKey("advertisingImpactFactor")) throw new IllegalArgumentException("No parameter advertisingImpactFactor specified in advertisementScheme configuration of the corresponding advertisementScheme");
                    else if(!advertisementSchemeParameters.containsKey("fractionPreferenceMessages")) throw new IllegalArgumentException("No parameter fractionPreferenceMessages specified in advertisementScheme configuration of the corresponding advertisementScheme");
                    else return new DefaultAdvertisementScheme((UnivariateDistribution) distributions.get((String) advertisementSchemeParameters.get("numberOfMessages")), (Double) advertisementSchemeParameters.get("messagePreferenceIncrease"), (Double) advertisementSchemeParameters.get("advertisingImpactFactor"), (Double) advertisementSchemeParameters.get("fractionPreferenceMessages"));
                case "UniformAdvertisementScheme":
                    return new UniformAdvertisementScheme();
                default:
                    throw new IllegalArgumentException("AdvertisementScheme " + advertisementSchemeQualifier + " is not implemented!!\nMake sure to specify an implemented scheme or implement is yourself!");
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch(ClassCastException cce){
            throw cce;
        }
    }

    public static HashMap<Integer, String> getCompanyAgentNames() {
        return companyAgentNames;
    }
}
