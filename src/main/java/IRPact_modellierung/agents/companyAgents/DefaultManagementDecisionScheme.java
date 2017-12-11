package IRPact_modellierung.agents.companyAgents;

/**
 * Since in most models analyzed for deriving the specifications for IRPact the company agent is entirely passive 
 * (or non-existent), the DefaultManagementDecisionScheme is a ManagementDecisionScheme
 * in which the company agent doesn't act (i.e. has an empty makeManagementDecision method).
 *
 * It serves to provide an implementation of a ManagementDecisionScheme so that 
 * instantiation of a CompanyAgent is possible
 *
 * @author Simon Johanning
 */
public class DefaultManagementDecisionScheme extends ManagementDecisionScheme {

    /**
     * The makeManagementDecision method of this ManagementDecisionScheme
     * is empty, since in most models the company agent doesn't make any management decisions.
     *
     * @param correspondingAgent The agent managing the product qualities
     */
    public void makeManagementDecision(CompanyAgent correspondingAgent) {

    }
}
