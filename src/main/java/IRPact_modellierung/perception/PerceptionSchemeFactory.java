package IRPact_modellierung.perception;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.products.ProductAttribute;
import IRPact_modellierung.simulation.Configuration;

import java.util.Map;

/**
 * The PerceptionSchemeFactory is a class that offers functionality to create the PerceptionSchemes
 * initially.
 *
 * @author Simon Johanning
 */
public class PerceptionSchemeFactory {

    public static ProductAttributePerceptionScheme createPerceptionScheme(ProductAttribute productAttribute, PerceptionSchemeConfiguration perceptionSchemeConfiguration, ConsumerAgentGroup correspondingConsumerAgentGroup, Configuration configuration){
        switch(perceptionSchemeConfiguration.associatedPerceptionScheme){
            case "TrueValueProductAttributePerceptionScheme": return new TrueValueProductAttributePerception(productAttribute);
            case "MemoryLessProductAttributePerceptionScheme": return new MemoryLessProductAttributePerceptionScheme(productAttribute, perceptionSchemeConfiguration.perceptionInitializationScheme.determineInitialValue(productAttribute, correspondingConsumerAgentGroup, configuration));
            case "perceptionHistogram":
                if(perceptionSchemeConfiguration.getPerceptionInitializationScheme() == null) throw new IllegalArgumentException("No histogramInitializationScheme given in the perception scheme parameters of the perceptionHistogram.\nPlease provide a valid configuraiton!!");
                else {
                    HistogramPerceptionSchemeConfiguration hpsConfiguration = (HistogramPerceptionSchemeConfiguration) perceptionSchemeConfiguration;
                    HistogramInitializationScheme hIScheme = (HistogramInitializationScheme) perceptionSchemeConfiguration.getPerceptionInitializationScheme();
                    return new ProductAttributePerceptionHistogram(hpsConfiguration.getLambda(), hpsConfiguration.getNoBins(), hpsConfiguration.getHistogramMinValue(), hpsConfiguration.getHistogramMaxValue(), productAttribute, hIScheme, correspondingConsumerAgentGroup, configuration);
                }
            default: throw new IllegalArgumentException("Perception scheme "+perceptionSchemeConfiguration.associatedPerceptionScheme+" is not implemented!!\nMake sure to provide a valid scheme!");
        }
    }

}
