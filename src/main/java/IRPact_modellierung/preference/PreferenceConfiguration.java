package IRPact_modellierung.preference;

import org.apache.logging.log4j.LogManager;

import java.util.HashSet;
import java.util.Set;

//TODO failcheck to see if variables have been instantiated??

/**
 * Class that describes the configuration of the preference setup of the simulation
 *
 * @author
 */
public class PreferenceConfiguration {

	private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

	private Set<ProductGroupAttributeValueMapping> preferenceProductAttributeMapping;
	private double preferenceHomogenizingFactor;
	private Set<Value> valuesUsed;

	/**
	 * A PreferenceConfiguration consists of the mapping of ProductGroupAttributes to Values (as a ProductGroupAttributeValueMapping data structure), a preferenceHomogenizationFactor indicating how strongly the preference between two agents will be homogenized, and the values used in the productGroupAttributeValueMapping
	 *
	 * @param productGroupAttributeValueMapping mapping of ProductGroupAttributes to Values (as a ProductGroupAttributeValueMapping data structure), coupling values, ProductGroupAttributes and the strength of their relationship
	 * @param preferenceHomogenizingFactor The preferenceHomogenizationFactor indicates how strongly the preference between two agents will be homogenized upon receiving messages acting on preference
	 * @param valuesUsed The values used in the productGroupAttributeValueMapping (in this constructor explicitly)
	 */
	public PreferenceConfiguration(Set<ProductGroupAttributeValueMapping> productGroupAttributeValueMapping, double preferenceHomogenizingFactor, Set<Value> valuesUsed) {
		this.preferenceProductAttributeMapping = productGroupAttributeValueMapping;
		this.preferenceHomogenizingFactor = preferenceHomogenizingFactor;
		this.valuesUsed = valuesUsed;
	}

	/**
	 * A PreferenceConfiguration consists of the mapping of ProductGroupAttributes to Values (as a ProductGroupAttributeValueMapping data structure), a preferenceHomogenizationFactor indicating how strongly the preference between two agents will be homogenized, and the values used in the productGroupAttributeValueMapping (which will be derived in this constructor from the productGroupAttributeValueMapping)
	 *
	 * @param productGroupAttributeValueMapping mapping of ProductGroupAttributes to Values (as a ProductGroupAttributeValueMapping data structure), coupling values, ProductGroupAttributes and the strength of their relationship
	 * @param preferenceHomogenizingFactor The preferenceHomogenizationFactor indicates how strongly the preference between two agents will be homogenized upon receiving messages acting on preference
	 */
	public PreferenceConfiguration(Set<ProductGroupAttributeValueMapping> productGroupAttributeValueMapping, double preferenceHomogenizingFactor) {
		this.preferenceProductAttributeMapping = productGroupAttributeValueMapping;
		this.preferenceHomogenizingFactor = preferenceHomogenizingFactor;
		valuesUsed = new HashSet<Value>();
		for(ProductGroupAttributeValueMapping papMapping : productGroupAttributeValueMapping){
			valuesUsed.add(papMapping.getValue());
		}
	}

	/**
	 * Constructor to create a preference configuration without the use of the homogenization factor.
	 * Invoking the corresponding getter might lead to undesired behaviour!!
	 *
	 * @param productGroupAttributeValueMapping mapping of ProductGroupAttributes to Values (as a ProductGroupAttributeValueMapping data structure), coupling values, ProductGroupAttributes and the strength of their relationship
	 */
	public PreferenceConfiguration(Set<ProductGroupAttributeValueMapping> productGroupAttributeValueMapping) {
		this.preferenceProductAttributeMapping = productGroupAttributeValueMapping;
		valuesUsed = new HashSet<Value>();
		for(ProductGroupAttributeValueMapping papMapping : productGroupAttributeValueMapping){
			valuesUsed.add(papMapping.getValue());
		}
	}

	public Set<ProductGroupAttributeValueMapping> getProductGroupAttributePreferenceMapping() {
		return this.preferenceProductAttributeMapping;
	}

	public double getPreferenceHomogenizingFactor() {
		return this.preferenceHomogenizingFactor;
	}

	public Set<Value> getValuesUsed() {
		return valuesUsed;
	}

	public String toString(){
		StringBuilder returnString = new StringBuilder(("PreferenceConfiguration holds " + preferenceProductAttributeMapping.size() + " ppa-mappings concerning " + valuesUsed.size() + " values.\n"));
		returnString.append("The preferenceHomogenizingFactor is ").append(preferenceHomogenizingFactor).append(". The other attributes concerned are as follows:\n");
		for(ProductGroupAttributeValueMapping pgapMapping : preferenceProductAttributeMapping){
			returnString.append(pgapMapping.toString());
		}
		for(Value value : valuesUsed){
			returnString.append(value.toString());
		}
		return returnString.toString();
	}
}