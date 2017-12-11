package IRPact_modellierung.distributions;

//TODO eventually bundle Burr distributions with and without k together to save boiler plate code

/**
 * Abstraction of the BurrDistributions within the COLT library.
 * It does not model the Burr Type XII distribution as terminology
 * is sometimes used in the literature (for this see BurrDistribution12).
 * Since all Burr type distributions in the COLT library share the parameter r,
 * this is the only value that is modeled in this abstraction.
 *
 * @author Simon Johanning
 */
public abstract class BurrDistribution extends COLTContinuousDistribution {

	protected double r;

	public double getR() {
		return this.r;
	}

	public BurrDistribution(String name, double r) {
		super(name);
		this.r = r;
	}

	public double draw(){
		return drawValue();
	}

	protected abstract double drawValue();
}