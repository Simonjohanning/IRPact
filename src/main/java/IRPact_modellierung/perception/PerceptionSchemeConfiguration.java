package IRPact_modellierung.perception;

import java.util.Map;

/**
 * The PerceptionSchemeConfiguration bundles data relevant for instantiating ProductPerceptionSchemes
 * and is used by the respective factory to parameterize these.
 * It thus describes how the perception schemes come about, and is the more abstract counterpart of these
 * (on the level of ProductGroupAttributes instead of ProductAttributes and ConsumerAgentGroups instead of ConsumerAgents).
 *
 *
 * @author Simon Johanning
 */
public class PerceptionSchemeConfiguration {

    protected String associatedPerceptionScheme;
    protected Map<String, Object> perceptionSchemeParameters;
    protected PerceptionInitializationScheme perceptionInitializationScheme;

    /**
     * A PerceptionSchemeConfiguration describes the relevant data for creating ProductPerceptionSchemes.
     *
     * @param associatedPerceptionScheme The name of the scheme to instantiate
     * @param perceptionSchemeParameters The paramters of the scheme to instantiate
     * @param perceptionInitializationScheme The perceptionInitializationScheme used for initializing the respective ProductAttributePerceptionScheme
     */
    public PerceptionSchemeConfiguration(String associatedPerceptionScheme, Map<String, Object> perceptionSchemeParameters, PerceptionInitializationScheme perceptionInitializationScheme) {
        this.associatedPerceptionScheme = associatedPerceptionScheme;
        this.perceptionSchemeParameters = perceptionSchemeParameters;
        this.perceptionInitializationScheme = perceptionInitializationScheme;
    }

    public String getAssociatedPerceptionScheme() {
        return associatedPerceptionScheme;
    }

    public Map<String, Object> getPerceptionSchemeParameters() {
        return perceptionSchemeParameters;
    }

    public PerceptionInitializationScheme getPerceptionInitializationScheme() {
        return perceptionInitializationScheme;
    }
}
