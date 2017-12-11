package IRPact_modellierung.needs;

import org.apache.logging.log4j.LogManager;

import java.util.Map;

/**
 * Class to create the need development scheme within the simulation.
 *
 * @author Simon Johanning
 */
public class NeedDevelopmentFactory {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    /**
     * Method to instantiate an implemented NeedDevelopmentScheme, as specified by the corresponding String
     * and the provided NeedNeedIndicatorMap
     *
     * @param needDevelopmentScheme Qualifier of the Scheme to instantiate
     * @param needIndicatorMap The needIndicatorMap of the scheme to instantiate
     * @return An instance of the Scheme corresponding to the qualifier
     * @throws IllegalArgumentException Will be thrown when referenced Scheme is not implemented
     */
    public static NeedDevelopmentScheme createNeedDevelopmentScheme(String needDevelopmentScheme, Map<Need, Double> needIndicatorMap) throws IllegalArgumentException{
        switch(needDevelopmentScheme){
            case "DefaultNeedDevelopmentScheme": return new DefaultNeedDevelopmentScheme(needIndicatorMap);
            case "StochasticNeedDevelopmentScheme" : return new StochasticNeedDevelopmentScheme(needIndicatorMap);
            default: throw new IllegalArgumentException("Error!! NeedDevelopmentScheme "+needDevelopmentScheme+" not implemented!! Will return null, which will yield further errors!!!");
        }
    }
}
