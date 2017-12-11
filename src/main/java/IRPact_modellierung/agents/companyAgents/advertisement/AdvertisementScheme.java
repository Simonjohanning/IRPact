package IRPact_modellierung.agents.companyAgents.advertisement;

import IRPact_modellierung.agents.companyAgents.CompanyAgent;
import IRPact_modellierung.messaging.CompanyAgentMessageScheme;

/**
 * The advertisement scheme formalizes the promotive processes the company agent engages in in order to promote the products in their portfolio.
 * An advertisement scheme should specify what products are evaluated on the market in which way, and what measures are taken in order to promote these products.
 *
 * @author Simon Johanning
 */
public abstract class AdvertisementScheme {

    protected CompanyAgentMessageScheme messageScheme;

    public AdvertisementScheme(CompanyAgentMessageScheme messageScheme) {
        this.messageScheme = messageScheme;
    }

    /**
     * This method specifies the process the promotion of the product portfolio underlies.
     * It is generally invoked by the process model and should include a market evaluation of the products, as well as practical promotive measures for the advertisement of these products.
     *
     * @param correspondingAgent The company agent managing the products in its portfolio
     */
    public abstract void advertiseProductPortfolio(CompanyAgent correspondingAgent);
}
