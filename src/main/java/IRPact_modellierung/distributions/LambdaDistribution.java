package IRPact_modellierung.distributions;

import cern.jet.random.Distributions;

/**
 * Class to model a lambda distribution based on the implementation in the COLT distribution library.
 *
 * @author Simon Johanning
 */
public class LambdaDistribution extends COLTContinuousDistribution {

	/**
	 * see http://search.tugraz.at/search?q=C-RAND&site=Alle&btnG=Suchen&client=tug_portal&output=xml_no_dtd&proxystylesheet=tug_portal&sitesearch=www.tugraz.at%2Finstitute%2Fstat&ulang=de&sort=date%3AD%3AL%3Ad1&entqr=3&entqrm=0&entsp=a&wc=200&wc_mc=1&oe=UTF-8&ie=UTF-8&ud=1&filter=1 or https://dst.lbl.gov/ACSSoftware/colt/api/index.html for more information
	 */
	private double l3;
	/**
	 * see http://search.tugraz.at/search?q=C-RAND&site=Alle&btnG=Suchen&client=tug_portal&output=xml_no_dtd&proxystylesheet=tug_portal&sitesearch=www.tugraz.at%2Finstitute%2Fstat&ulang=de&sort=date%3AD%3AL%3Ad1&entqr=3&entqrm=0&entsp=a&wc=200&wc_mc=1&oe=UTF-8&ie=UTF-8&ud=1&filter=1 or https://dst.lbl.gov/ACSSoftware/colt/api/index.html for more information
	 */
	private double l4;

	//TODO check parameters

	/**
	 * Will create a lambda distribution based on the COLT library.
	 *
	 * @param name Name of the distribution (will be prefixed by LambdaDistribution_)
	 * @param l3 parameter to the probability distribution function for the COLT implementation (see original paper for more information)
	 * @param l4 parameter of the probability distribution function for the COLT implementation (see original paper for more information)
	 */
	public LambdaDistribution(String name,double l3, double l4) {
		super(name);
		this.l3 = l3;
		this.l4 = l4;
	}

	public double getL3() {
		return this.l3;
	}

	public double getL4() {
		return this.l4;
	}

	public double draw() {
		return Distributions.nextLambda(l3, l4, generator);
	}
}