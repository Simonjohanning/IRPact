package IRPact_modellierung.agents;

import IRPact_modellierung.SimulationEntity;
import IRPact_modellierung.simulation.SimulationContainer;

/**
 * Abstraction for any kind of agent within the simulation as SimulationEntity.
 * Every agent also holds an informationAuthority, describing how credible information
 * stemming from this agent is.
 *
 * @author Simon Johanning
 */
public abstract class Agent extends SimulationEntity {

    /**
     * An Agent is a SimulationEntity that is thought to exhibit (at least)
     * autonomy, social ability, reactivity and proactiveness.
     *
     * Due to the diversity of Agent types however, these properties are not enforced by the
     * definition of an agent as an (abstract) entity, but need to be ensured by the corresponding agents.
     *
     * As an entity that (also) interacts with other agents by providing information, every
     * agent has an informationAuthority associated with them
     *
     * @param associatedSimulationContainer The container the simulation runs in

     */
    public Agent(SimulationContainer associatedSimulationContainer) {
        super(associatedSimulationContainer);
    }

}