import java.util.*;

public final class RetailerSellerAgent implements SellerAgent {
    private final String sellerId;
    private final Map<String, Product> inventoryById = new HashMap<>();

    public RetailerSellerAgent(String sellerId, List<Product> initialInventory) {
        this.sellerId = Objects.requireNonNull(sellerId);
        for (Product p : initialInventory) {
            inventoryById.put(p.getProductId(), p);
        }
    }

    @Override
    public String getSellerId() { return sellerId; }

    @Override
    public SellerReply handleCFP(OrderRequest request, CustomerProfile customerProfile) {
        // Find product by name (case-insensitive). Real systems would match IDs.
        Product match = null;
        for (Product p : inventoryById.values()) {
            if (p.getName().equalsIgnoreCase(request.getProductName())) {
                match = p;
                break;
            }
        }
        if (match == null) {
            return SellerReply.refuse("Product not found in inventory");
        }
        if (!match.hasStock(request.getQuantity())) {
            return SellerReply.refuse("Insufficient stock (available: " + match.getStock() + ")");
        }
        if (match.getUnitPrice() < request.getMinUnitPrice() || match.getUnitPrice() > request.getMaxUnitPrice()) {
            return SellerReply.refuse("Price not in requested range (unitPrice=" + match.getUnitPrice() + ")");
        }

        // Delivery constraint: request may specify deliveryPreference, and customer may also have a preferred type.
        String reqPref = request.getDeliveryPreference();
        String custPref = customerProfile.getPreferredDeliveryType();

        boolean okDelivery = isDeliveryCompatible(reqPref, match.getDeliveryType()) && isDeliveryCompatible(custPref, match.getDeliveryType());
        if (!okDelivery) {
            return SellerReply.refuse("Delivery type not compatible (seller offers: " + match.getDeliveryType() + ")");
        }

        Offer offer = new Offer(
                sellerId,
                match.getProductId(),
                match.getName(),
                match.getUnitPrice(),
                match.getStock(),
                match.getDeliveryType(),
                match.getEstimatedDays()
        );
        return SellerReply.propose(offer);
    }

    private boolean isDeliveryCompatible(String preference, String offered) {
        if (preference == null) return true;
        if (preference.equalsIgnoreCase("Any")) return true;
        return preference.equalsIgnoreCase(offered);
    }

    @Override
    public boolean confirmTransaction(Offer acceptedOffer, int requestedQty) {
        if (acceptedOffer == null) return false;
        Product p = inventoryById.get(acceptedOffer.getProductId());
        if (p == null) return false;
        if (!p.hasStock(requestedQty)) return false;
        p.reduceStock(requestedQty);
        return true;
    }

    public void printInventory() {
        System.out.println("Inventory of " + sellerId + ":");
        for (Product p : inventoryById.values()) {
            System.out.println("  - " + p);
        }
    }
}
