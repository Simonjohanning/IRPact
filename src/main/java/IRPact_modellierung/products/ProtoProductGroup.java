package IRPact_modellierung.products;

import IRPact_modellierung.decision.ConsumerAgentAdoptionDecisionProcess;
import IRPact_modellierung.distributions.UnivariateDistribution;
import IRPact_modellierung.helper.StructureEnricher;

import java.util.Set;


/**
 * As with ProductGroups, ProtoProductGroups bundle together several aspects of products in one common structure.
 * They are made up out of similar products, be it similar in structural, functional or other aspects.
 * Products within one ProductGroup need to be similar in the aspects of featuring the same
 * productGroupAttributes, ProductGroup relations, satisfying the same need and having the same overwriteDecisionProcess (if any).
 *
 * In contrast to ProductGroups, their ProductGroup dependencies (prerequisiteProductGroup and excludeProductGroupStrings)
 * as well as needsSatisfied don't refer to ProductGroups and Needs but Strings in order to be able
 * to load the product configuration without a specified load order.
 *
 * Different constructors exist in order for allowing a different setup of the standardProduct.
 *
 * @author Simon Johanning
 */
public class ProtoProductGroup {

    private Set<ProductGroupAttribute> productGroupAttributes;
    private String groupName;
    private Set<String> prerequisiteProductGroupStrings;
    private Set<String> excludeProductGroupStrings;
    private Set<FixedProductDescription> fixedProducts;
    private Set<String> needsSatisfied;
    private FixedProductDescription standardProduct;
    private ConsumerAgentAdoptionDecisionProcess overwriteDecisionProcess;
    private UnivariateDistribution productLifetimeDistribution;

    public Set<ProductGroupAttribute> getProductGroupAttributes() {
        return productGroupAttributes;
    }

    public String getGroupName() {
        return groupName;
    }

    public Set<String> getNeedsSatisfied() {
        return needsSatisfied;
    }

    public Set<String> getPrerequisiteProductGroupStrings() {
        return prerequisiteProductGroupStrings;
    }

    public Set<String> getExcludeProductGroupStrings() {
        return excludeProductGroupStrings;
    }

    public Set<FixedProductDescription> getFixedProducts() {
        return fixedProducts;
    }

    public FixedProductDescription getStandardProduct() {
        return standardProduct;
    }

    /**
     * A ProductGroup of identifier groupName consists of derivedProducts and fixedProducts which ProductAttributes are based
     * on the ProductGroup's productGroupAttributes. All products that are part of the ProductGroup have the same
     * adoption dependencies (prerequisiteProductGroups and excludeProductGroup), satisfy the same needs (captured by needsSatisfied),
     * the same overwriteDecisionProcess and (unless they 'overwrite' it, as in the case of FixedProducts),
     * the same productLifetimeDistribution.
     * A ProductGroup can further have a standardProduct, if a decision process used in the model needs it.
     *
     * For this constructor the standardProduct is set to null, assuming no standardProduct exists.
     *
     * @param productGroupAttributes The productGroupAttributes of the ProductGroup the deriving products' ProductAttributes are based on
     * @param groupName A qualifier for the name of the group.
     * @param prerequisiteProductGroupStrings An identifier of the ProductGroups that need to be adopted in order for a Product of this ProductGroup can be adopted
     * @param excludeProductGroupStrings An identifier of the ProductGroups that can't be adopted when a Product of this group is adopted
     * @param fixedProducts The FixedProduct(Descriptions) that belong to this ProductGroup
     * @param needsSatisfied An identifier of the needs the adoption of a Product of this group satisfied
     * @param overwriteDecisionProcess The decision process that should be used in place of the Agents' decision process for the adoption of products of this group
     * @param productLifetimeDistribution The lifetimeDistribution products of this group should exhibit (if not already specified)
     */
    public ProtoProductGroup(Set<ProductGroupAttribute> productGroupAttributes, String groupName, Set<String> prerequisiteProductGroupStrings, Set<String> excludeProductGroupStrings, Set<FixedProductDescription> fixedProducts, Set<String> needsSatisfied, ConsumerAgentAdoptionDecisionProcess overwriteDecisionProcess, UnivariateDistribution productLifetimeDistribution, String standardProductString) {
        this.productGroupAttributes = productGroupAttributes;
        this.groupName = groupName;
        this.prerequisiteProductGroupStrings = prerequisiteProductGroupStrings;
        this.excludeProductGroupStrings = excludeProductGroupStrings;
        this.fixedProducts = fixedProducts;
        this.needsSatisfied = needsSatisfied;
        this.overwriteDecisionProcess = overwriteDecisionProcess;
        this.productLifetimeDistribution = productLifetimeDistribution;
        for(FixedProductDescription currentFPD : fixedProducts){
            if(currentFPD.getName().equals(standardProductString)){
                this.standardProduct = currentFPD;
                break;
            }
        }
    }

    /**
     * A ProductGroup of identifier groupName consists of derivedProducts and fixedProducts which ProductAttributes are based
     * on the ProductGroup's productGroupAttributes. All products that are part of the ProductGroup have the same
     * adoption dependencies (prerequisiteProductGroups and excludeProductGroup), satisfy the same needs (captured by needsSatisfied),
     * the same overwriteDecisionProcess and (unless they 'overwrite' it, as in the case of FixedProducts),
     * the same productLifetimeDistribution.
     * A ProductGroup can further have a standardProduct, if a decision process used in the model needs it.
     *
     * @param productGroupAttributes The productGroupAttributes of the ProductGroup the deriving products' ProductAttributes are based on
     * @param groupName A qualifier for the name of the group.
     * @param prerequisiteProductGroupStrings An identifier of the ProductGroups that need to be adopted in order for a Product of this ProductGroup can be adopted
     * @param excludeProductGroupStrings An identifier of the ProductGroups that can't be adopted when a Product of this group is adopted
     * @param fixedProducts The FixedProduct(Descriptions) that belong to this ProductGroup
     * @param needsSatisfied An identifier of the needs the adoption of a Product of this group satisfied
     * @param standardProduct The Product that should be used as standardProduct of this ProtoProductGroup
     * @param overwriteDecisionProcess The decision process that should be used in place of the Agents' decision process for the adoption of products of this group
     * @param productLifetimeDistribution The lifetimeDistribution products of this group should exhibit (if not already specified)
     */
    public ProtoProductGroup(Set<ProductGroupAttribute> productGroupAttributes, String groupName, Set<String> prerequisiteProductGroupStrings, Set<String> excludeProductGroupStrings, Set<FixedProductDescription> fixedProducts, Set<String> needsSatisfied, FixedProductDescription standardProduct, ConsumerAgentAdoptionDecisionProcess overwriteDecisionProcess, UnivariateDistribution productLifetimeDistribution) {
        this.productGroupAttributes = productGroupAttributes;
        this.groupName = groupName;
        this.prerequisiteProductGroupStrings = prerequisiteProductGroupStrings;
        this.excludeProductGroupStrings = excludeProductGroupStrings;
        this.fixedProducts = fixedProducts;
        this.needsSatisfied = needsSatisfied;
        this.standardProduct = standardProduct;
        this.overwriteDecisionProcess = overwriteDecisionProcess;
        this.productLifetimeDistribution = productLifetimeDistribution;
    }

    /**
     * A ProductGroup of identifier groupName consists of derivedProducts and fixedProducts which ProductAttributes are based
     * on the ProductGroup's productGroupAttributes. All products that are part of the ProductGroup have the same
     * adoption dependencies (prerequisiteProductGroups and excludeProductGroup), satisfy the same needs (captured by needsSatisfied),
     * the same overwriteDecisionProcess and (unless they 'overwrite' it, as in the case of FixedProducts),
     * the same productLifetimeDistribution.
     * A ProductGroup can further have a standardProduct, if a decision process used in the model needs it.
     *
     *
     * @param productGroupAttributes The productGroupAttributes of the ProductGroup the deriving products' ProductAttributes are based on
     * @param groupName A qualifier for the name of the group.
     * @param prerequisiteProductGroupStrings An identifier of the ProductGroups that need to be adopted in order for a Product of this ProductGroup can be adopted
     * @param excludeProductGroupStrings An identifier of the ProductGroups that can't be adopted when a Product of this group is adopted
     * @param fixedProducts The FixedProduct(Descriptions) that belong to this ProductGroup
     * @param needsSatisfied An identifier of the needs the adoption of a Product of this group satisfied
     * @param standardProduct The qualifier of the product that should be used as standardProduct of this ProtoProductGroup (as a String). Needs to be the qualifier of a FixedProductDescription provided in the fixedProducts set.
     * @param overwriteDecisionProcess The decision process that should be used in place of the Agents' decision process for the adoption of products of this group
     * @param productLifetimeDistribution The lifetimeDistribution products of this group should exhibit (if not already specified)
     * @throws IllegalArgumentException Will be thrown when the qualifier for the standardProduct doesn't appear in the set of FixedProductDescriptions
     */
    public ProtoProductGroup(Set<ProductGroupAttribute> productGroupAttributes, String groupName, Set<String> prerequisiteProductGroupStrings, Set<String> excludeProductGroupStrings, Set<FixedProductDescription> fixedProducts, Set<String> needsSatisfied, String standardProduct, ConsumerAgentAdoptionDecisionProcess overwriteDecisionProcess, UnivariateDistribution productLifetimeDistribution) throws IllegalArgumentException{
        this.productGroupAttributes = productGroupAttributes;
        this.groupName = groupName;
        this.prerequisiteProductGroupStrings = prerequisiteProductGroupStrings;
        this.excludeProductGroupStrings = excludeProductGroupStrings;
        this.fixedProducts = fixedProducts;
        this.needsSatisfied = needsSatisfied;
        this.overwriteDecisionProcess = overwriteDecisionProcess;
        this.productLifetimeDistribution = productLifetimeDistribution;
        if(StructureEnricher.attachFixedProductDescriptionNames(fixedProducts).containsKey("standardProduct")) this.standardProduct = StructureEnricher.attachFixedProductDescriptionNames(fixedProducts).get(standardProduct);
        else throw new IllegalArgumentException("The FixedProduct "+standardProduct+" used as a standardProduct was not given as an argument!!");
    }

    public ConsumerAgentAdoptionDecisionProcess getOverwriteDecisionProcess() {
        return overwriteDecisionProcess;
    }

    public UnivariateDistribution getProductLifetimeDistribution() {
        return productLifetimeDistribution;
    }
}
