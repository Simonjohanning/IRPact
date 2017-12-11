package IRPact_modellierung.perception;

import IRPact_modellierung.distributions.BoundedUnivariateDistribution;
import IRPact_modellierung.distributions.Distribution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple factory that creates a HistogramInitializationScheme based on a string and a parameter map provided
 *
 * @author Simon Johanning
 */
public class HistogramInitializationFactory {

    private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    /**
     * Method to create a HistogramInitializationScheme based on the scheme string and a parameter map provided
     *
     * @param scheme A string specifying the HistogramInitializationScheme to be created
     * @param parameters A map of the relevant parameters for the respective scheme
     * @return A freshly created instance of the HistogramInitializationScheme specified by the 'scheme' String
     * @throws IllegalArgumentException will be thrown when the scheme is not implemented or the respective scheme requires parameters not given as arguments
     * @throws ClassCastException Will be thrown when a parameter can't be cast into the required data type
     */
    public static HistogramInitializationScheme createHistogramInitializationScheme(String scheme, Map<String, Object> parameters, Map<String, Distribution> distributions) throws IllegalArgumentException, ClassCastException{
        try {
            switch(scheme){
                case "RandomPADistributionHistogramInitializationScheme": return new RandomPADistributionHistogramInitializationScheme();
                case "TrueValueInitializationScheme": return new TrueValueInitializationScheme();
                case "UniformRandomBoundedHistogramInitializationScheme": return new UniformRandomBoundedHistogramInitializationScheme();
                case "ConsumerPerceivedValueHistogramInitializationScheme": return new ConsumerPerceivedValueHistogramInitializationScheme((BoundedUnivariateDistribution) distributions.get("initialPerceptionDistribution"));
                case "ConstantHistogramInitializationScheme":
                    if(parameters.containsKey("valueToAssign")) return new ConstantHistogramInitializationScheme((double) parameters.get("valueToAssign"));
                    else throw new IllegalArgumentException("Parameter map does not contain the parameter 'valueToAssign', which is essential to a ConstantHistogramInitializationScheme!!");
                default: throw new IllegalArgumentException("HistogramInitializationScheme "+scheme+" is not implemented!!!\nPlease provide a legal HistogramInitializationScheme!!");
            }
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (ClassCastException cce){
            throw cce;
        }
    }
}
