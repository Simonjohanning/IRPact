package IRPact_modellierung.information;

import IRPact_modellierung.agents.InformationAgent;

/**
 * Information represents a piece of information (as knowledge and the agent from which it originates),
 * as well as further aspects of relevance.
 *
 * Since information comes in many ways, this class is held very abstract, and concrete kinds of information
 * specify their semantics themselves.
 *
 * @author Simon Johanning
 */
public abstract class Information {

    private InformationAgent originator;

    /**
     * Due to its abstract nature, the only (relevant) commonality (in the scope of IRPact)
     * is the source of the information, that is the originator
     *
     * @param originator The source of the information
     */
    public Information(InformationAgent originator) {
        this.originator = originator;
    }

    public InformationAgent getInformationOriginAgent(){
        return originator;
    }
}
