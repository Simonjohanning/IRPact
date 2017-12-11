package IRPact_modellierung.agents.policyAgent;

public class PolicyAgentConfiguration {

    private ProductPolicyScheme productPolicyScheme;
    private ConsumerPolicyScheme consumerPolicyScheme;
    private RegulatoryPolicyScheme regulatoryPolicyScheme;
    private MarketEvaluationScheme marketEvaluationScheme;
    private double informationAuthority;


    public PolicyAgentConfiguration(ProductPolicyScheme productPolicyScheme, ConsumerPolicyScheme consumerPolicyScheme, RegulatoryPolicyScheme regulatoryPolicyScheme, MarketEvaluationScheme marketEvaluationScheme, double informationAuthority) {
        this.productPolicyScheme = productPolicyScheme;
        this.consumerPolicyScheme = consumerPolicyScheme;
        this.regulatoryPolicyScheme = regulatoryPolicyScheme;
        this.marketEvaluationScheme = marketEvaluationScheme;
        this.informationAuthority = informationAuthority;
    }

    public ProductPolicyScheme getProductPolicyScheme() {
        return productPolicyScheme;
    }

    public ConsumerPolicyScheme getConsumerPolicyScheme() {
        return consumerPolicyScheme;
    }

    public RegulatoryPolicyScheme getRegulatoryPolicyScheme() {
        return regulatoryPolicyScheme;
    }

    public MarketEvaluationScheme getMarketEvaluationScheme() {
        return marketEvaluationScheme;
    }

    public double getInformationAuthority() {
        return informationAuthority;
    }
}
