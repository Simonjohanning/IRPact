package IRPact_modellierung.perception;

import IRPact_modellierung.distributions.Distribution;

import java.util.Map;

/**
 * The HistogramPerceptionSchemeConfiguration is a PerceptionSchemeConfiguration for
 * histogram-based PerceptionSchemes.
 * It thus also contains Histogram-specific data such as the number of bins and the lambda value of the histogram
 *
 * @author Simon Johanning
 */
public class HistogramPerceptionSchemeConfiguration extends PerceptionSchemeConfiguration{

    private int noBins;
    private double lambda;
    private double histogramMinValue;
    private double histogramMaxValue;

    public HistogramPerceptionSchemeConfiguration(String associatedPerceptionScheme, Map<String, Object> perceptionSchemeParameters, int noBins, double lambda, double histogramMinValue, double histogramMaxValue, String histogramInitializationScheme, Map<String, Object> histogramInitializationParameters, Map<String, Distribution> distributions) {
        super(associatedPerceptionScheme, perceptionSchemeParameters, HistogramInitializationFactory.createHistogramInitializationScheme(histogramInitializationScheme, histogramInitializationParameters, distributions));
        this.noBins = noBins;
        this.lambda = lambda;
        this.histogramMinValue = histogramMinValue;
        this.histogramMaxValue = histogramMaxValue;
    }

    public HistogramPerceptionSchemeConfiguration(String associatedPerceptionScheme, Map<String, Object> perceptionSchemeParameters, int noBins, double lambda, double histogramMinValue, double histogramMaxValue,HistogramInitializationScheme histogramInitializationScheme) {
        super(associatedPerceptionScheme, perceptionSchemeParameters, histogramInitializationScheme);
        this.noBins = noBins;
        this.lambda = lambda;
        this.histogramMinValue = histogramMinValue;
        this.histogramMaxValue = histogramMaxValue;
    }

    public int getNoBins() {
        return noBins;
    }

    public double getLambda() {
        return lambda;
    }

    public double getHistogramMinValue() {
        return histogramMinValue;
    }

    public double getHistogramMaxValue() {
        return histogramMaxValue;
    }
}
