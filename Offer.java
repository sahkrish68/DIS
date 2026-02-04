import java.time.Instant;
import java.util.Objects;

public final class Offer {
    private final String sellerId;
    private final String productId;
    private final String productName;
    private final double unitPrice;
    private final int quantityOffered;
    private final String deliveryType;
    private final int estimatedDays;
    private final Instant createdAt;

    public Offer(String sellerId, String productId, String productName, double unitPrice, int quantityOffered, String deliveryType, int estimatedDays) {
        this.sellerId = Objects.requireNonNull(sellerId);
        this.productId = Objects.requireNonNull(productId);
        this.productName = Objects.requireNonNull(productName);
        this.unitPrice = unitPrice;
        this.quantityOffered = quantityOffered;
        this.deliveryType = Objects.requireNonNull(deliveryType);
        this.estimatedDays = estimatedDays;
        this.createdAt = Instant.now();
    }

    public String getSellerId() { return sellerId; }
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantityOffered() { return quantityOffered; }
    public String getDeliveryType() { return deliveryType; }
    public int getEstimatedDays() { return estimatedDays; }
    public Instant getCreatedAt() { return createdAt; }

    public double totalCost(int requestedQty) { return unitPrice * requestedQty; }

    @Override
    public String toString() {
        return "Offer{seller=%s, product=%s(%s), unitPrice=%.2f, delivery=%s(%d days)}"
                .formatted(sellerId, productName, productId, unitPrice, deliveryType, estimatedDays);
    }
}
