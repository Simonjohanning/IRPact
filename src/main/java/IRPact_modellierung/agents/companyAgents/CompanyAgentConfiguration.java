package IRPact_modellierung.agents.companyAgents;

import IRPact_modellierung.agents.companyAgents.advertisement.AdvertisementScheme;

/**
 * The CompanyAgentConfiguration (CAC) describes the configuration of a CompanyAgent in the simulation.
 * It exhibits the relevant schemes; The products the CompanyAgent described in the
 * CAC are set with the products however, as the (name of the) managing agent.
 *
 * @author Simon Johanning
 */
public class CompanyAgentConfiguration {

    private ProductQualityManipulationScheme productQualityManipulationScheme;
    private ManagementDecisionScheme managementDecisionScheme;
    private AdvertisementScheme advertisementScheme;
    private String companyAgentName;
    private double informationAuthority;

    public CompanyAgentConfiguration(ProductQualityManipulationScheme productQualityManipulationScheme, ManagementDecisionScheme managementDecisionScheme, AdvertisementScheme advertisementScheme, String companyAgentName, double informationAuthority){
        this.productQualityManipulationScheme = productQualityManipulationScheme;
        this.managementDecisionScheme = managementDecisionScheme;
        this.advertisementScheme = advertisementScheme;
        this.companyAgentName = companyAgentName;
        this.informationAuthority = informationAuthority;
    }

    public ProductQualityManipulationScheme getProductQualityManipulationScheme() {
        return productQualityManipulationScheme;
    }



    public ManagementDecisionScheme getManagementDecisionScheme() {
        return managementDecisionScheme;
    }

    public AdvertisementScheme getAdvertisementScheme() {
        return advertisementScheme;
    }

    public String getCompanyAgentName() {
        return companyAgentName;
    }

    public double getInformationAuthority() {
        return informationAuthority;
    }
}
