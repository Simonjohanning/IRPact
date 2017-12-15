package IRPact_modellierung.agents.posAgents;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductGroup;

import java.util.HashSet;
import java.util.Set;

/**
 * The TrivialPurchaseProcessScheme is a default purchase process
 * that acts as a place holder until a good PP is defined.
 * It basically only filters out all products not available at the POS,
 * and if a purchase process features in the model, the modeler should
 * probably implement their own purchase process.
 *
 * @author Simon Johanning
 */
public class TrivialPurchaseProcessScheme extends PurchaseProcessScheme{

    public TrivialPurchaseProcessScheme(POSAgent correspondingPOS) {
        super(correspondingPOS);
    }

    /**
     * Method that filters out all products not sold at the respective POS,
     * and doesn't model much about the purchase process itself.
     * It is thought more as a placeholder than anything else
     *
     * @param consumerAgent The consumer agent entering the purchase process
     * @param productGroupToChooseFrom The product group the appropriate products are supposed to come from
     * @return All products available at the POS at this time.
     */
    public Set<Product> purchaseProduct(ConsumerAgent consumerAgent, ProductGroup productGroupToChooseFrom) {
        Set<Product> allProductsOfProductGroup = new HashSet<>();
        for(Product currentProduct : productGroupToChooseFrom.getDerivedProducts()){
            if(correspondingPOS.productAvailable(currentProduct)) allProductsOfProductGroup.add(currentProduct);
        }
        return allProductsOfProductGroup;
    }

}
