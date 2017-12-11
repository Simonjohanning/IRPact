package IRPact_modellierung.products;

import java.util.HashSet;
import java.util.Set;

import IRPact_modellierung.decision.ConsumerAgentAdoptionDecisionProcess;
import IRPact_modellierung.distributions.UnivariateDistribution;
import IRPact_modellierung.needs.Need;

/**
 * ProductGroups bundle together several aspects of products in one common structure.
 * They are made up out of similar products, be it similar in structural, functional or other aspects.
 * Products within one ProductGroup need to be similar in the aspects of featuring the same
 * productGroupAttributes, ProductGroup relations, satisfying the same need and having the same overwriteDecisionProcess (if any).
 *
 * @author Simon Johanning
 */
public class ProductGroup {

	private Set<ProductGroupAttribute> productGroupAttributes;
	private String groupName;
	private Set<Product> derivedProducts;
	private Set<ProductGroup> prerequisiteProductGroups;
	private Set<ProductGroup> excludeProductGroup;
	private Set<FixedProductDescription> fixedProducts;
	private Set<Need> needsSatisfied;
	private FixedProductDescription standardProduct;
	private ConsumerAgentAdoptionDecisionProcess overwriteDecisionProcess;
	private UnivariateDistribution productLifetimeDistribution;


	/**
	 * A ProductGroup of identifier groupName consists of derivedProducts and fixedProducts which ProductAttributes are based
	 * on the ProductGroup's productGroupAttributes. All products that are part of the ProductGroup have the same
	 * adoption dependencies (prerequisiteProductGroups and excludeProductGroup), satisfy the same needs (captured by needsSatisfied),
	 * the same overwriteDecisionProcess and (unless they 'overwrite' it, as in the case of FixedProducts),
	 * the same productLifetimeDistribution.
	 * A ProductGroup can further have a standardProduct, if a decision process used in the model needs it.
	 *
	 * For this constructor some (or all) Products of the group were already derived, and given as argument.
	 * If no products of this ProductGroup are derived, the other constructor should be chosen (or it should be provided as empty Set of Products).
	 *
	 * @param productGroupAttributes The productGroupAttributes of the ProductGroup the deriving products' ProductAttributes are based on
	 * @param groupName A qualifier for the name of the group.
	 * @param derivedProducts A set of products already derived from this ProductGroup
	 * @param prerequisiteProductGroups The ProductGroups that need to be adopted in order for a Product of this ProductGroup can be adopted
	 * @param excludeProductGroup The ProductGroups that can't be adopted when a Product of this group is adopted
	 * @param fixedProducts The FixedProduct(Descriptions) that belong to this ProductGroup
	 * @param needsSatisfied The needs the adoption of a Product of this group satisfied
	 * @param standardProduct The Product that should be used as standardProduct of this ProductGroup (if needed by DecisionProcesses)
	 * @param overwriteDecisionProcess The decision process that should be used in place of the Agents' decision process for the adoption of products of this group
	 * @param productLifetimeDistribution The lifetimeDistribution products of this group should exhibit (if not already specified)
	 * @throws IllegalArgumentException Will be thrown when a FixedProduct(Description) belong to this group already has another group noted in it
	 */
	public ProductGroup(Set<ProductGroupAttribute> productGroupAttributes, String groupName, Set<Product> derivedProducts, Set<ProductGroup> prerequisiteProductGroups, Set<ProductGroup> excludeProductGroup, Set<FixedProductDescription> fixedProducts, Set<Need> needsSatisfied, FixedProductDescription standardProduct, ConsumerAgentAdoptionDecisionProcess overwriteDecisionProcess, UnivariateDistribution productLifetimeDistribution)  throws IllegalArgumentException{
		this.productGroupAttributes = productGroupAttributes;
		this.groupName = groupName;
		this.derivedProducts = derivedProducts;
		this.prerequisiteProductGroups = prerequisiteProductGroups;
		this.excludeProductGroup = excludeProductGroup;
		this.fixedProducts = fixedProducts;
		this.needsSatisfied = needsSatisfied;
		this.standardProduct = standardProduct;
		this.overwriteDecisionProcess = overwriteDecisionProcess;
		this.productLifetimeDistribution = productLifetimeDistribution;
		try {
			attributeProductGroupToFixedProducts();
		} catch (IllegalArgumentException e) {
			throw e;
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
	 * For this constructor no Products of the group are derived.
	 * If products of this ProductGroup are already derived, the other constructor should be chosen.
	 *
	 * @param productGroupAttributes The productGroupAttributes of the ProductGroup the deriving products' ProductAttributes are based on
	 * @param groupName A qualifier for the name of the group.
	 * @param prerequisiteProductGroups The ProductGroups that need to be adopted in order for a Product of this ProductGroup can be adopted
	 * @param excludeProductGroup The ProductGroups that can't be adopted when a Product of this group is adopted
	 * @param fixedProducts The FixedProduct(Descriptions) that belong to this ProductGroup
	 * @param needsSatisfied The needs the adoption of a Product of this group satisfied
	 * @param standardProduct The Product that should be used as standardProduct of this ProductGroup (if needed by DecisionProcesses)
	 * @param overwriteDecisionProcess The decision process that should be used in place of the Agents' decision process for the adoption of products of this group
	 * @param productLifetimeDistribution The lifetimeDistribution products of this group should exhibit (if not already specified)
	 * @throws IllegalArgumentException Will be thrown when a FixedProduct(Description) belong to this group already has another group noted in it
	 */
    public ProductGroup(Set<ProductGroupAttribute> productGroupAttributes, String groupName, Set<ProductGroup> prerequisiteProductGroups, Set<ProductGroup> excludeProductGroup, Set<FixedProductDescription> fixedProducts, Set<Need> needsSatisfied, FixedProductDescription standardProduct, ConsumerAgentAdoptionDecisionProcess overwriteDecisionProcess, UnivariateDistribution productLifetimeDistribution)  throws IllegalArgumentException{
        this.productGroupAttributes = productGroupAttributes;
        this.groupName = groupName;
        this.prerequisiteProductGroups = prerequisiteProductGroups;
        this.excludeProductGroup = excludeProductGroup;
        this.fixedProducts = fixedProducts;
        this.derivedProducts = new HashSet<Product>();
		this.needsSatisfied = needsSatisfied;
		this.standardProduct = standardProduct;
		this.overwriteDecisionProcess = overwriteDecisionProcess;
		this.productLifetimeDistribution = productLifetimeDistribution;
		try {
			attributeProductGroupToFixedProducts();
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	//TODO think about whether this constructor is still needed  since  the standard product can be given as null argument if necessary
//	public ProductGroup(Set<ProductGroupAttribute> productGroupAttributes, String groupName, Set<ProductGroup> prerequisiteProductGroups, Set<ProductGroup> excludeProductGroup, Set<FixedProductDescription> fixedProducts, Set<Need> needsSatisfied, ConsumerAgentAdoptionDecisionProcess overwriteDecisionProcess, boolean singleAdoption, UnivariateDistribution productLifetimeDistribution)  throws IllegalArgumentException{
//		this.productGroupAttributes = productGroupAttributes;
//		this.groupName = groupName;
//		this.prerequisiteProductGroups = prerequisiteProductGroups;
//		this.excludeProductGroup = excludeProductGroup;
//		this.fixedProducts = fixedProducts;
//		this.derivedProducts = new HashSet<Product>();
//		this.needsSatisfied = needsSatisfied;
//		this.overwriteDecisionProcess = overwriteDecisionProcess;
//		this.singleAdoption = singleAdoption;
//		this.productLifetimeDistribution = productLifetimeDistribution;
//		try {
//			attributeProductGroupToFixedProducts();
//		} catch (IllegalArgumentException e) {
//			throw e;
//		}
//	}

	private void attributeProductGroupToFixedProducts() throws IllegalArgumentException{
		for(FixedProductDescription fpd : fixedProducts) {
			if (!fpd.getPartOfProductGroup().equals(groupName)) {
				throw new IllegalArgumentException("The FixedProductDescription " + fpd + " features a different ProductGroup (" + fpd.getPartOfProductGroup() + ") than the one attributed here (namely " + this + ")");
			}
		}
	}

	public Set<ProductGroupAttribute> getProductGroupAttributes() {
		return this.productGroupAttributes;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public Set<Product> getDerivedProducts() {
		return this.derivedProducts;
	}

	public void addDerivedProduct(Product productToAdd){
    	derivedProducts.add(productToAdd);
	}

	public Set<ProductGroup> getPrerequisiteProductGroups() {
		return this.prerequisiteProductGroups;
	}

	public Set<ProductGroup> getExcludeProductGroup() {
		return this.excludeProductGroup;
	}

	public Set<FixedProductDescription> getFixedProducts() {
		return fixedProducts;
	}

    public void setPrerequisiteProductGroups(Set<ProductGroup> prerequisiteProductGroups) {
        this.prerequisiteProductGroups = prerequisiteProductGroups;
    }

    public void setExcludeProductGroup(Set<ProductGroup> excludeProductGroup) {
        this.excludeProductGroup = excludeProductGroup;
    }

	public ConsumerAgentAdoptionDecisionProcess getOverwriteDecisionProcess() {
		return overwriteDecisionProcess;
	}

	public FixedProductDescription getStandardProduct() {
		return standardProduct;
	}

	public Set<Need> getNeedsSatisfied() {
		return needsSatisfied;
	}

	public void addPrerequisiteProductGroup(ProductGroup ppgToAdd){
		prerequisiteProductGroups.add(ppgToAdd);
	}

	public void addExcludeProductGroup(ProductGroup epgToAdd){
		excludeProductGroup.add(epgToAdd);
	}

	public String toString(){
		/*String returnString = ("\n\nProductGroup "+groupName+" has "+derivedProducts.size()+" derived products, a needIndicator of "+needIndicator+" and comprisies the following groupAttributes: \n");
		for(ProductGroupAttribute pga : productGroupAttributes){
			returnString += pga.toString()+"\n";
		}
		returnString += ("It has the following prerequisites products: \n");
		if(prerequisiteProductGroups != null){
			for(ProductGroup pg : prerequisiteProductGroups){
				returnString += pg.getGroupName()+"\n";
			}
		}
		returnString += ("It excludes the following product groups: \n");
		if(excludeProductGroup != null){
			for(ProductGroup pg : excludeProductGroup){
				returnString += pg.getGroupName()+"\n";
			}
		}
		returnString += ("It has the following fixed products: \n");
		if(fixedProducts != null){
			for(FixedProductDescription fpd : fixedProducts){
				returnString += fpd.getName()+"\n";
			}
		}*/
		return groupName;
	}

	public UnivariateDistribution getProductLifetimeDistribution() {
		return productLifetimeDistribution;
	}
}