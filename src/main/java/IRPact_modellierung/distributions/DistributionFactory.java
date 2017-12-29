package IRPact_modellierung.distributions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

//TODO make complete, is currently used as on-demand!

/**
 * A class to generate instances of distributions used in the simulation.
 * In the current version it is based on demand, and thus just a fraction of the provided distributions are offered
 * within this factory.
 *
 * @author Simon Johanning
 */
public class DistributionFactory {

    private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    //TODO Fix Continuous / Spatial distribution!!!!

    /**
     * Method to generate instances of distributions.
     * Will return a distribution corresponding to the requested key.
     *
     * @param name The name of the distribution (will be preceded by a prefix specific to each concrete type of distribution)
     * @param distribution The key word for the respective distribution. For a list of valid keywords (and their correspondence to distributions), see the userDocumentation
     * @param parameters A map that should / needs to contain all relevant parameters to specify the requested distribution
     *
     * @return An instance of the distribution corresponding to the specified key (distribution)
     * @throws IllegalArgumentException this exception will be thrown if the necessary parameters for the specified distribution are not contained in the respective parameter
     */
    public static Distribution createDistribution(String name, String distribution, Map<String, Object> parameters) throws IllegalArgumentException{
        switch(distribution){
            case "BurrDistribution2":
                //return new BurrDistribution2(name, determineLowerBound(parameters), determineUpperBound(parameters), (Double) parameters.get("r"));
                if(parameters.containsKey("r")) return new BurrDistribution2(name, (Double) parameters.get("r"));
                else throw new IllegalArgumentException("The parameter 'r' is not contained in the parameter map specified for the generation of a BurrDistribution2");
            case "FiniteMassPointsDiscreteDistribution":
                HashMap<Double, Double> distributionValues = new HashMap<Double, Double>(parameters.size());
                if(!parameters.keySet().isEmpty()) {
                    for (String massPoint : parameters.keySet()) {
                        distributionValues.put(Double.valueOf(massPoint), (Double) parameters.get(massPoint));
                    }
                    return new FiniteMassPointsDiscreteDistribution(name, distributionValues);
                }else throw new IllegalArgumentException("No mass points were provided for the generation of a FiniteMassPointsDiscreteDistribution");
            case "BooleanDistribution":
                if((parameters.containsKey("true")) && (parameters.containsKey("false"))) return new BooleanDistribution(name, (Double) parameters.get("true"),(Double) parameters.get("false"));
                else throw new IllegalArgumentException("Not both mass points necessary for a boolean distribution ('true' and 'false') were included in the parameter map");
            case "DummySpatialDistribution":
                return new DummySpatialDistribution(name);
            case "PascalDistribution" :
                if(!(parameters.containsKey("p"))) throw new IllegalArgumentException("The parameter map for the distribution "+name+" of type PascalDistribution lacks the parameter p!!");
                else if(!(parameters.containsKey("r"))) throw new IllegalArgumentException("The parameter map for the distribution "+name+" of type PascalDistribution lacks the parameter r!!");
                else{
                    try {
                        return new PascalDistribution(name, (Integer) parameters.get("r"), (Double) parameters.get("p"));
                    } catch (IllegalArgumentException e) {
                        throw e;
                    } catch (ClassCastException cce){
                        throw cce;
                    }
                }
            case "ContSpatialDistribution":
                return new ContinuousSpatialDistribution(name, loadContinuousSpatialDistribution(parameters));
            default: throw new IllegalArgumentException("WARNING!!! distribution "+distribution+" not implemented!!!");
        }
    }

    private static ContinuousDistribution loadContinuousSpatialDistribution(Map<String, Object> parameters) {
        switch((String) parameters.get("type")){
            case "NormalDistribution" : return new NormDistribution("fooNorm", (double) parameters.get("mean"), (double) parameters.get("variance"));
            default: throw new IllegalArgumentException("parameters contains invalid type "+parameters.get("type"));
        }
    }

    //TODO check if these methods are still of any use (and possibly delete them)

   /* private static double determineLowerBound(Map<String, Object> parameters){
        fooLog.debug("In distribution factory, determining lower bound as "+(Double) parameters.get("lowerBound"));
        if(parameters.get("lowerBound") != null){
            try{
                return (Double) parameters.get("lowerBound");
            }catch(Exception e){
                return -Double.MAX_VALUE;
            }
        }else return -Double.MAX_VALUE;
    }

    private static double determineUpperBound(Map<String, Object> parameters){
        fooLog.debug("In distribution factory, determining upper bound as "+(Double) parameters.get("upperBound"));
        if(parameters.get("upperBound") != null){
            try{
                return (Double) parameters.get("upperBound");
            }catch(Exception e){
                return Double.MAX_VALUE;
            }
        }else return Double.MAX_VALUE;
    }*/
}
