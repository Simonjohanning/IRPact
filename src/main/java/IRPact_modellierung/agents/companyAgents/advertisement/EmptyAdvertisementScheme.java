package IRPact_modellierung.agents.companyAgents.advertisement;

import IRPact_modellierung.agents.companyAgents.CompanyAgent;
import IRPact_modellierung.distributions.FiniteMassPointsDiscreteDistribution;
import IRPact_modellierung.messaging.DefaultCompanyAgentMessageScheme;

import java.util.HashMap;
import java.util.Map;

/**
 * Since in most models analyzed for deriving the specifications for IRPact the company agent is entirely passive
 * (or non-existent), the DefaultAdvertisementScheme is a AdvertisementScheme
 * in which the company agent doesn't act (i.e. has an empty advertiseProductPortfolio method).
 *
 * It serves to provide an implementation of a AdvertisementScheme so that
 * instantiation of a CompanyAgent is possible
 *
 * @author Simon Johanning
 */
public class EmptyAdvertisementScheme extends AdvertisementScheme{

    /**
     * Since the EmptyAdvertisementScheme is supposed to do nothing, the MessageScheme it is based on doesn't matter.
     * For this, the DefaultCompanyAgentMessageScheme with 0-parameters is chosen, which is however arbitrary
     */
    public EmptyAdvertisementScheme() {
        super(new DefaultCompanyAgentMessageScheme(new FiniteMassPointsDiscreteDistribution("zeroDistribution", createZeroDistribution()),0, 0.0, 0.0));
    }

    private static Map<Double, Double> createZeroDistribution() {
        Map<Double, Double> zeroDistro = new HashMap<>();
        zeroDistro.put(0.0, 1.0);
        return zeroDistro;
    }

    /**
     * Since this scheme represents no advertisement it basically does NOP
     *
     * @param correspondingAgent The company agent managing the products in its portfolio
     */
    public void advertiseProductPortfolio(CompanyAgent correspondingAgent) {
        //NOP
    }
}
