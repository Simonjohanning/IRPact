package IRPact_modellierung.information;

import IRPact_modellierung.distributions.Distribution;

import java.util.Map;

/**
 * Class to provide functionality to instantiate aspects of information within the simulation.
 *
 * @author Simon Johanning
 */
public class InformationFactory {

    public static InformationScheme createInformationScheme(String informationScheme, Map<String, Object> informationSchemeParamters, Map<String, Distribution> distributions) throws IllegalArgumentException, UnsupportedOperationException{
        switch(informationScheme){
            case "NoInformationScheme": return new NoInformationScheme();
            case "MaxAuthorityInformationScheme": return new MaxAuthorityInformationScheme();
            case "PureInformationScheme": return new PureInformationScheme();
            case "RandomInformationScheme": return new RandomInformationScheme();
            default: throw new IllegalArgumentException("The information scheme "+informationScheme+" is not implemented!!\nPlease provide valid data!");
        }
    }
}
