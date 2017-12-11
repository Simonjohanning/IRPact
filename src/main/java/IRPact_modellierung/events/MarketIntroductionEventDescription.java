package IRPact_modellierung.events;

import IRPact_modellierung.products.FixedProductDescription;

/**
 * Class to represent the description of a MIE.
 * Bundles the information necessary to create a MIE as well as the product it is based on
 *
 * @author Simon Johanning
 */
public class MarketIntroductionEventDescription{

    private FixedProductDescription correspondingFixedProduct;
    private double scheduledForTime;

    /**
     * Bundles all the information nessary to create a MIE as well as the product it is based on
     *
     * @param correspondingFixedProduct The FixedProductDescription the corresponding MIE should introduce
     * @param scheduledForTime The time of market introduction of the product the MIE refers to as system time
     */
    public MarketIntroductionEventDescription(FixedProductDescription correspondingFixedProduct, double scheduledForTime){
        this.correspondingFixedProduct = correspondingFixedProduct;
        this.scheduledForTime = scheduledForTime;
    }

    public FixedProductDescription getCorrespondingFixedProduct() {
        return correspondingFixedProduct;
    }

    public double getScheduledForTime() {
        return scheduledForTime;
    }
}
