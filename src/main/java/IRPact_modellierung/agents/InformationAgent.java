package IRPact_modellierung.agents;

import IRPact_modellierung.simulation.SimulationContainer;

/**
 * An information agent is an agent within the simulation that can create information.
 * Associated with it it has an informationAuthority representing the credibility of the information
 * stemming from this agent.
 *
 * @author Simon Johanning
 **/
 public class InformationAgent extends Agent{

    private double informationAuthority;

    /**
     * An information agent is an agent within the simulation that can create information.
     * Associated with it it has an informationAuthority representing the credibility of the information
     * stemming from this agent.
     *
     * @param simulationContainer The container the simulation runs in
     * @param informationAuthority The authority information stemming from this agent has
     */
    public InformationAgent(SimulationContainer simulationContainer, double informationAuthority) {
        super(simulationContainer);
        this.informationAuthority = informationAuthority;
    }

    public double getInformationAuthority() {
        return informationAuthority;
    }
}
