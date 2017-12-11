package IRPact_modellierung.messaging;

import IRPact_modellierung.distributions.UnivariateDistribution;

/**
 * Class representing an abstraction of message schemes originating from consumer agents.
 * This class is (currently) used only to structure the respective MessageSchemes
 * and doesn't offer any additional functionality.
 *
 * @author Simon Johanning
 */
public abstract class ConsumerAgentMessageScheme extends MessageScheme {

    UnivariateDistribution numberMessagesPerTimeUnit;

    public ConsumerAgentMessageScheme(UnivariateDistribution numberMessagesPerTimeUnit) {
        this.numberMessagesPerTimeUnit = numberMessagesPerTimeUnit;
    }

    public UnivariateDistribution getNumberMessagesPerTimeUnit() {
        return numberMessagesPerTimeUnit;
    }
}
