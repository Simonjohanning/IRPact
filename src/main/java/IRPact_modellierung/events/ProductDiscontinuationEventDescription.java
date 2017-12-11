package IRPact_modellierung.events;

import org.apache.logging.log4j.LogManager;

/**
 * Class to bundle the information needed to create a ProductDiscontinuationEvent
 * (removing the specified product from the simulation at a specified time).
 * Is generally created from a configuration, but can be created by simulation entities as well.
 *
 * @author Simon Johanning
 */
public class ProductDiscontinuationEventDescription {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    private String productName;
    private double scheduledForTime;

    /**
     * Data to create a ProductDiscontinuationEvent.
     * The Product is described as a String referring to a fixed product (which should be a unique mapping)
     *
     * @param productName A String referring to the name of a fixed product representing the product which should be removed by the corresponding ProductDiscontinuationEvent
     * @param scheduledForTime The simulation time the ProductDiscontinuationEvent should be scheduled for
     *
     * @throws IllegalArgumentException Will be thrown when the product name is empty or scheduled time is negative
     */
    public ProductDiscontinuationEventDescription(String productName, double scheduledForTime) throws IllegalArgumentException{
        if(productName.equals("")) throw new IllegalArgumentException("Product name needs to refer to a fixed product and can thus not be empty");
        else if(scheduledForTime<0.0) throw new IllegalArgumentException("Product is scheduled for a time before the simulation (namely "+scheduledForTime+"); Can't be correct");
        fooLog.debug("pded constructor with product name {}",productName);
        this.productName = productName;
        this.scheduledForTime = scheduledForTime;
    }

    public String getProductName() {
        return productName;
    }

    public double getScheduledForTime() {
        return scheduledForTime;
    }
}
