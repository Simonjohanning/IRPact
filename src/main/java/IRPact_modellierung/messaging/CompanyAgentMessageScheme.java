package IRPact_modellierung.messaging;

import IRPact_modellierung.agents.companyAgents.CompanyAgent;
import IRPact_modellierung.simulation.SimulationContainer;

import java.util.Set;

/**
 * Class representing an abstraction of message schemes originating from company agents.
 * This class is (currently) used only to structure the respective MessageSchemes
 * and doesn't offer any additional functionality.
 *
 * @author Simon Johanning
 */
public abstract class CompanyAgentMessageScheme extends MessageScheme{

    public abstract Set<Message> createMessages(SimulationContainer simulationContainer, CompanyAgent sender);
}
