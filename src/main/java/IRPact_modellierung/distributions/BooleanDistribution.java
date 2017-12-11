package IRPact_modellierung.distributions;

/**
 * A distribution with two mass points (named true and false)
 * in one variable.
 * Encodes the mass points true and false as numerical values
 * 1.0 (true) and 0.0 (false), and will return them proportionally to their mass.
 *
 * @author Simon Johanning
 */
public class BooleanDistribution extends UnivariateDistribution{

    private double massTrue;
    private double massFalse;

    /**
     * Generates a boolean distribution based on two mass points,
     * generating the corresponding numbers proportionally to these
     *
     * @param name The name of the boolean distribution (will be prefixed by BooleanDistribution_)
     * @param massTrue The mass concentrated at the point representing 'true'
     * @param massFalse The mass concentrated at the point representing 'false'
     */
    public BooleanDistribution(String name, double massTrue, double massFalse) {
        super(name);
        this.massTrue = massTrue;
        this.massFalse = massFalse;
    }

    public double getMassTrue() {
        return massTrue;
    }

    public double getMassFalse() {
        return massFalse;
    }

    /**
     * Returns 1.0 (true) or 0.0 (false) proportionally to the
     * mass at the point encoding these
     *
     * @return double value encoding true (1.0) and false (0.0)
     */
    public double draw() {
        if(Math.random() < massTrue) return 1.0;
        else return 0.0;
    }
}
