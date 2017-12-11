package IRPact_modellierung.products;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;

/**
 * An AdoptedProduct represents the adopted instance of a product by a consumer agent
 * at a given time, together with the lifespan of the adopted instance.
 *
 * @author Simon Johanning
 */
public class AdoptedProduct {

    private Product correspondingProduct;
    private ConsumerAgent productAdopter;
    private double adoptionTime;
    private double productLifetime;

    /**
     * The correspondingProduct is adopted by the productAdopter at adoptionTime for productLifetime.
     *
     * @param correspondingProduct The product being adopted
     * @param adopter The ConsumerAgent adopting the product
     * @param adoptionTime The time the product is adopted at
     * @param productLifetime The time the product is adopted before it can't fulfill the need anymore
     */
    public AdoptedProduct(Product correspondingProduct, ConsumerAgent adopter, double adoptionTime, double productLifetime) {
        this.correspondingProduct = correspondingProduct;
        this.productAdopter = adopter;
        this.adoptionTime = adoptionTime;
        this.productLifetime = productLifetime;
    }

    public boolean productExpired(double systemTime){
        return (systemTime > (adoptionTime + productLifetime));
    }

    public double productExpirationDate(){
        return adoptionTime + productLifetime;
    }

    public Product getCorrespondingProduct() {
        return correspondingProduct;
    }

    public ConsumerAgent getProductAdopter() {
        return productAdopter;
    }

    public double getAdoptionTime() {
        return adoptionTime;
    }

    public double getProductLifetime() {
        return productLifetime;
    }
}
