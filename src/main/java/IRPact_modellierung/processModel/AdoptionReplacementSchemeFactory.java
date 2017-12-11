package IRPact_modellierung.processModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Factory class to instantiate AdoptionReplacementSchemes based on the specified scheme.
 *
 * @author Simon Johanning
 */
public class AdoptionReplacementSchemeFactory {

    private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    /**
     * Method to create an AdoptionReplacementSchemes based on the
     * respective argument. Will throw an exception if a non-implemented on is chosen.
     *
     * @param adoptionReplacementScheme A qualifier for the respective scheme
     * @return A AdoptionReplacementSchemes based on the qualifier
     * @throws IllegalArgumentException Will be thrown when the adoptionReplacementScheme String refers to an unimplemented scheme
     */
    public static AdoptionReplacementScheme createAdoptionReplacementScheme(String adoptionReplacementScheme) throws IllegalArgumentException{
        switch(adoptionReplacementScheme){
            case "KieslingAdoptionReplacementScheme": return new KieslingAdoptionReplacementScheme();
            case "NoAdoptionReplacementScheme": return new NoAdoptionReplacementScheme();
            case "BetterProductReadoptionScheme": return new BetterProductReadoptionScheme();
            default: throw new IllegalArgumentException("Adoption replacement scheme "+adoptionReplacementScheme+" was not implemented! Please provide a valid string!");
        }
    }
}
