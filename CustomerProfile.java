import java.util.Objects;

public final class CustomerProfile {
    private final String customerId;
    private final double maxTotalBudget;
    private final boolean preferFastDelivery;
    private final String preferredDeliveryType; // "Standard", "Express", or "Any"

    public CustomerProfile(String customerId, double maxTotalBudget, boolean preferFastDelivery, String preferredDeliveryType) {
        this.customerId = Objects.requireNonNull(customerId);
        this.maxTotalBudget = maxTotalBudget;
        this.preferFastDelivery = preferFastDelivery;
        this.preferredDeliveryType = Objects.requireNonNull(preferredDeliveryType);
    }

    public String getCustomerId() { return customerId; }
    public double getMaxTotalBudget() { return maxTotalBudget; }
    public boolean isPreferFastDelivery() { return preferFastDelivery; }
    public String getPreferredDeliveryType() { return preferredDeliveryType; }

    @Override
    public String toString() {
        return "CustomerProfile{id='%s', maxBudget=%.2f, preferFastDelivery=%s, preferredDelivery='%s'}"
                .formatted(customerId, maxTotalBudget, preferFastDelivery, preferredDeliveryType);
    }
}
