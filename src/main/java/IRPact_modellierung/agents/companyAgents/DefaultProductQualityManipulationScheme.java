package IRPact_modellierung.agents.companyAgents;

/**
 * Since in most models analyzed for deriving the specifications for IRPact the company agent is entirely passive
 * (or non-existent), the DefaultProductQualityManipulationScheme is a ProductQualityManipulationScheme
 * in which the company agent doesn't act (i.e. has an empty manageProductQualities method).
 *
 * It serves to provide an implementation of a ProductQualityManipulationScheme so that
 * instantiation of a CompanyAgent is possible
 *
 * @author Simon Johanning
 */
public class DefaultProductQualityManipulationScheme extends ProductQualityManipulationScheme {

    /**
     * The manageProductQualities method of this ProductQualityManipulationScheme
     * is empty, since in most models product qualities are not (actively) managed.
     *
     * @param correspondingAgent The agent managing the product qualities
     */
    public void manageProductQualities(CompanyAgent correspondingAgent) {

    }
}
