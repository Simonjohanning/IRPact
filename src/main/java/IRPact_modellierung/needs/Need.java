package IRPact_modellierung.needs;

/**
 * A Need is the basic object leading to the motivation of ConsumerAgents to act.
 * Needs are associated with ConsumerAgents, who, when the need is not fulfilled by a product,
 *
 */
public class Need {

    private String name;

    public Need(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return ("Need "+name);
    }
}
