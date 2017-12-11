package IRPact_modellierung.decision;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.preference.Preference;
import IRPact_modellierung.preference.Value;
import IRPact_modellierung.helper.LazynessHelper;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.products.ProductGroupAttribute;
import org.apache.logging.log4j.LogManager;

import java.util.Map;

/**
 * Utility function that calculates the utility of a product attribute as the sum of partial utilities of
 * the preferences (contributing to it).
 * The preferences contribute with the product of the strength of the preference,
 * the product group attribute preference map between the corresponding value and the product attribute
 * and the value of the product attribute.
 *
 * It thus takes the preference of the agent into account, as well what it binds.
 *
 * @author Simon Johanning
 */
public class LinearPreferenceOrientedUtilityFunction extends UtilityFunction {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    /**
     * Calculates the partial utility of a product attribute based on the preferences of the corresponding agent.
     *
     * @param correspondingAgent The agent who evaluates the product attribute
     * @param correspondingProductAttribute The product attribute that is to be evaluated
     * @param systemTime The time of the simulation at the evaluation
     *
     * @return the partial utility with which this product attribute contributes
     */
    public double calculatePartialUtility(ConsumerAgent correspondingAgent, ProductAttribute correspondingProductAttribute, double systemTime) {
        double cumulatedUtility = 0.0;
        Map<ProductGroupAttribute, Map<Value, Double>> pgaPreferenceMap = LazynessHelper.derivePGAPreferenceMap(correspondingAgent.getAssociatedSimulationContainer().getSimulationConfiguration().getPreferenceConfiguration());
        for(Preference currentPreference : correspondingAgent.getPreferences()){
            //for each preference, add the preference strength*pgaPreference value (how well does pa correspond to this)*quality of this product attribute to the cumulated utility
            cumulatedUtility += currentPreference.getStrength()*(pgaPreferenceMap.get(correspondingProductAttribute.getCorrespondingProductGroupAttribute()).get(currentPreference.getValue())*correspondingAgent.getPerceivedProductAttributeValues().get(correspondingProductAttribute).calculateProductAttributePerception(systemTime));
            fooLog.debug("pref ({}: {}) increases cumUt for agent {} with respect to pa {} with value {} by {}", currentPreference.getValue().getName(), currentPreference.getStrength(), correspondingAgent.getAgentID(), correspondingProductAttribute.getCorrespondingProductGroupAttribute().getName(), correspondingProductAttribute.getValue(), currentPreference.getStrength()*(pgaPreferenceMap.get(correspondingProductAttribute.getCorrespondingProductGroupAttribute()).get(currentPreference.getValue())*correspondingAgent.getPerceivedProductAttributeValues().get(correspondingProductAttribute).calculateProductAttributePerception(systemTime)));
            //fooLog.info("Preference strength for preference {} is {} (.strength is {})", currentPreference.getValue(), (pgaPreferenceMap.get(correspondingProductAttribute.getCorrespondingProductGroupAttribute()).get(currentPreference.getValue())), currentPreference.getStrength());
        }
        return cumulatedUtility;
    }
}
