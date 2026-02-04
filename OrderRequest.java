import java.util.Objects;

public final class OrderRequest {
    private final String productName;     // simple matching by name for this coursework
    private final int quantity;
    private final double minUnitPrice;
    private final double maxUnitPrice;
    private final String deliveryPreference; // "Standard", "Express", or "Any"

    public OrderRequest(String productName, int quantity, double minUnitPrice, double maxUnitPrice, String deliveryPreference) {
        this.productName = Objects.requireNonNull(productName);
        this.quantity = quantity;
        this.minUnitPrice = minUnitPrice;
        this.maxUnitPrice = maxUnitPrice;
        this.deliveryPreference = Objects.requireNonNull(deliveryPreference);
    }

    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getMinUnitPrice() { return minUnitPrice; }
    public double getMaxUnitPrice() { return maxUnitPrice; }
    public String getDeliveryPreference() { return deliveryPreference; }

    @Override
    public String toString() {
        return "OrderRequest{productName='%s', qty=%d, unitPriceRange=%.2f..%.2f, deliveryPref='%s'}"
                .formatted(productName, quantity, minUnitPrice, maxUnitPrice, deliveryPreference);
    }
}
