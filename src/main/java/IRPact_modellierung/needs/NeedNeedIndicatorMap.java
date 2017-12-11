package IRPact_modellierung.needs;

/**
 * The NeedNeedIndicatorMap bundles together a Need and a numerical value of the importance (needIndicator)
 * of the respective Need. It is used to weigh otherwise incomparable Needs in one ConsumerAgent against one another.
 * Since a NeedNeedIndicatorMap is a comparable data structure, Lists based on this can be quickly sorted.
 *
 * @author Simon Johanning
 */
public class NeedNeedIndicatorMap implements Comparable<NeedNeedIndicatorMap>{

    private Need need;
    private double needIndicator;

    /**
     * The NeedNeedIndicatorMap bundles together a Need and a numerical value of the importance (needIndicator)
     * of the respective Need, with its interpretation depending on the context (NeedDevelopmentScheme) it is used in.
     *
     * @param need Thee need to couple to a value
     * @param needIndicator The value to couple the Need to
     */
    public NeedNeedIndicatorMap(Need need, double needIndicator) {
        this.need = need;
        this.needIndicator = needIndicator;
    }

    public Need getNeed() {
        return need;
    }

    public double getNeedIndicator() {
        return needIndicator;
    }

    public int compareTo(NeedNeedIndicatorMap o) {
        if(this.needIndicator > o.getNeedIndicator()) return -1;
        else return 1;
    }
}
