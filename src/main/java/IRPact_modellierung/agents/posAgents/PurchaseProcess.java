package IRPact_modellierung.agents.posAgents;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgent;
import IRPact_modellierung.products.Product;
import IRPact_modellierung.products.ProductGroup;

import java.util.Set;

/**
 * A PurchaseProcess models the process of how a consumer agent
 * behaves during the purchase of a product at a point of sale.
 * It can become arbitrarily complex, but basically filters out a number
 * of products relevant for the consumer agent at that point
 * that enter a decision process.
 *
 * @author Simon Johanning
 *
 */
public abstract class PurchaseProcess {

    protected POSAgent correspondingPOS;

    public PurchaseProcess(POSAgent correspondingPOS){
        this.correspondingPOS = correspondingPOS;
    }

    public abstract Set<Product> purchaseProduct(ConsumerAgent consumerAgent, ProductGroup productGroupToChooseFrom);
}
