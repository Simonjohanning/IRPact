package IRPact_modellierung.distributions;

import IRPact_modellierung.helper.LazynessHelper;

import java.util.HashMap;
import java.util.Map;

//TODO make more efficient for large sets by research (first idea sort by mass) -> make list
//TODO also check for order issues!! with the wrong order, point with a higher cumulative distribution can be returned!!!
//TODO change to 1d-case, since values are double and not a measure space in general

/**
 * Class that models a distribution that has its mass concentrated in a finite number of points.
 * Values are stored in a cumulative distribution map.
 * Distribution assumes valid values to be larger than 0 (not designed for signed measures), and normalized should the values not add up to 1.
 * Probably not very efficient for large number of points, and for this purpose a different solution should be used.
 *
 * It is not recommended that this distribution is used yet since a few (potentially) problems could exist with it!!
 *
 * @author Simon Johanning
 */
public class FiniteMassPointsDiscreteDistribution extends BoundedUnivariateDistribution {

	/**
	 * Map of mass points and their mass at that point
	 */
	private Map<Double, Double> massPoints;
	/**
	 * Cumulated distribution function corresponding to the masses as specified in massPoints.
	 */
	private Map<Double, Double> cumulatedDistribution;

	/**
	 * Generates the cumulative distribution corresponding to the mass points if all mass points are valid
	 * (non-negative mass, since signed measures are outside of the scope of this distribution).
	 * Will ignore points with 0 mass and normalize the massPoints provided
	 *
	 * @param name Name of the distribution (will be prefixed with FiniteMassPointsDiscreteDistribution_)
	 * @param massPoints Mass points as a map (x, mass(x)), must have non-negative mass
	 * @throws IllegalArgumentException will be thrown when an illegal mass point (negative mass) is encountered
	 */
	public FiniteMassPointsDiscreteDistribution(String name, Map<Double, Double> massPoints) throws IllegalArgumentException{
		super(name, LazynessHelper.getMinimum(massPoints.keySet()), LazynessHelper.getMaximum(massPoints.keySet()));
		this.massPoints = massPoints;
		try {
			cumulatedDistribution = calculateCumulatedDistribution(massPoints);
		} catch (IllegalArgumentException iae) {
			throw iae;
		}
	}

	//TODO Cumulative probability might fuck up the order in the following function!

	/**
	 * Calculates a map of the cumulated distribution of the respective mass points.
	 * Assumes all of them are non-negative (will throw an exception if otherwise) and
	 * will normalize them should they not sum up to unity
	 *
	 * @param massPoints The points of mass and (x, mass(x)) as a map
	 * @return A map of the value and its cumulative distribution up to that point
	 * @throws IllegalArgumentException will be thrown when points with negative mass are encountered
	 */
	private Map<Double, Double> calculateCumulatedDistribution(Map<Double, Double> massPoints) throws IllegalArgumentException{
		Map<Double, Double> cumulatedDistribution = new HashMap<Double, Double>(massPoints);
		double cumulatedProbability = 0.0;
		for(Double massPoint : massPoints.keySet()){
			if(massPoints.get(massPoint) > 0.0) {
				cumulatedProbability += massPoints.get(massPoint);
				cumulatedDistribution.put(massPoint, cumulatedProbability);
			}else if(massPoints.get(massPoint) < 0.0) throw new IllegalArgumentException("A negative mass has been assigned to "+massPoint+"!! \n Consider a distribution modeling a signed measure!");
		}
		if(cumulatedProbability == 1.0) return cumulatedDistribution;
		else return normalizeCumulatedDistribution(cumulatedProbability, cumulatedDistribution);

	}

	/**
	 * Method to normlize the distribution.
	 * Assumes that the total mass of the distribution is already known and inputted as a parameter
	 *
	 * @param cumulatedProbability The probability the sum of all mass points of the distribution amount to (by which it is normalized)
	 * @param cumulatedDistribution The distribution that is to be normalized
	 * @return
	 */
	private Map<Double, Double> normalizeCumulatedDistribution(double cumulatedProbability, Map<Double, Double> cumulatedDistribution) {
		if(cumulatedProbability <= 0.0) throw new IllegalArgumentException("Cumulated probability should not be 0 or negative!!! Is "+cumulatedProbability);
		else {
			Map<Double, Double> normalizedMap = new HashMap<Double, Double>(cumulatedDistribution.size());
			for (Double massPoint : normalizedMap.keySet()) {
				normalizedMap.put(massPoint, cumulatedDistribution.get(massPoint) / cumulatedProbability);
			}
			return normalizedMap;
		}
	}

	private static double determineUpperBound(Map<Double, Double> massPoints) {
		double maximimalMassPoint = (Double) massPoints.keySet().toArray()[0];
		for(Double massPoint : massPoints.keySet()){
			if (massPoint > maximimalMassPoint) maximimalMassPoint = massPoint;
		}
		return maximimalMassPoint;
	}

	private static double determineLowerBound(Map<Double, Double> massPoints) {
		double minimalMassPoint = (Double) massPoints.keySet().toArray()[0];
		for(Double massPoint : massPoints.keySet()){
			if (massPoint < minimalMassPoint) minimalMassPoint = massPoint;
		}
		return minimalMassPoint;
	}

	public Map<Double, Double> getMassPoints() {
		return this.massPoints;
	}

	public Map<Double, Double> getCumulatedDistribution() {
		return this.cumulatedDistribution;
	}

	//TODO ORDER!!!
	public double draw(){//} throws SemanticException{
		double valueDrawn = Math.random();
		//return the first point where the cumulated distribution is larger than the drawn number
		for(Double massPoint : cumulatedDistribution.keySet()){
			if(cumulatedDistribution.get(massPoint) > valueDrawn) return massPoint;
		}
		//TODO error handling if no mass point was found!!
//		throw new SemanticException("Error!!! No mass point was found in finite mass points discrete distribution for value "+valueDrawn+"!!");
		return -1.0;
	}

	//TODO check if thats whats intended
	public double drawValue() {
		return draw();
	}
}