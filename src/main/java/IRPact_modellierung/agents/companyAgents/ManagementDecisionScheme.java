package IRPact_modellierung.agents.companyAgents;

/**
 * The ManagementDecisionScheme formalizes the management processes within the company agent.
 * Through this a large range of processes within the respective company can be layed out under a common framework.
 * Different managing style and decision making processes between company agents should be depicted by different management decision schemes.
 *
 * @author Simon Johanning
 */
public abstract class ManagementDecisionScheme {

    /**
     * This method describes the decision making process for management decisions within the company agent.
     * It is (generally) invoked by the process model and should lead to actions corresponding to the outcome of the taken decision.
     *
     * @param correspondingAgent The company agent taking the respective decisions
     */
    public abstract void makeManagementDecision(CompanyAgent correspondingAgent);
}
