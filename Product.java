import java.util.Objects;

public final class Product {
    private final String productId;
    private final String name;
    private final double unitPrice;
    private int stock;
    private final String deliveryType;   // e.g., Standard / Express
    private final int estimatedDays;     // delivery time estimate for this product at this seller

    public Product(String productId, String name, double unitPrice, int stock, String deliveryType, int estimatedDays) {
        this.productId = Objects.requireNonNull(productId);
        this.name = Objects.requireNonNull(name);
        this.unitPrice = unitPrice;
        this.stock = stock;
        this.deliveryType = Objects.requireNonNull(deliveryType);
        this.estimatedDays = estimatedDays;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public double getUnitPrice() { return unitPrice; }
    public int getStock() { return stock; }
    public String getDeliveryType() { return deliveryType; }
    public int getEstimatedDays() { return estimatedDays; }

    public boolean hasStock(int qty) { return stock >= qty; }

    public void reduceStock(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty must be > 0");
        if (qty > stock) throw new IllegalStateException("Not enough stock to reduce");
        stock -= qty;
    }

    @Override
    public String toString() {
        return "Product{id=%s, name=%s, unitPrice=%.2f, stock=%d, delivery=%s(%d days)}"
                .formatted(productId, name, unitPrice, stock, deliveryType, estimatedDays);
    }
}
