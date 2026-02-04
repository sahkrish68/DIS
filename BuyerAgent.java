import java.util.*;

public final class BuyerAgent {
    private final String buyerId;
    private final CustomerProfile profile;

    public BuyerAgent(String buyerId, CustomerProfile profile) {
        this.buyerId = Objects.requireNonNull(buyerId);
        this.profile = Objects.requireNonNull(profile);
    }

    public String getBuyerId() { return buyerId; }
    public CustomerProfile getProfile() { return profile; }

    public void placeOrder(BrokerAgent broker, OrderRequest request) {
        System.out.println("\n==============================");
        System.out.println("[BUYER " + buyerId + "] Customer profile: " + profile);
        System.out.println("[BUYER " + buyerId + "] Submitting order: " + request);

        List<Offer> offers = broker.collectOffers(request, profile);

        if (offers.isEmpty()) {
            System.out.println("[BUYER " + buyerId + "] No offers received. Ending.");
            return;
        }

        Offer recommended = broker.recommendBest(offers, request, profile);
        System.out.println("\n[BROKER] Recommended offer: " + (recommended == null ? "NONE (budget/constraints)" : recommended));

        Offer chosen = chooseFinalOffer(offers, recommended, request);
        if (chosen == null) {
            System.out.println("[BUYER " + buyerId + "] Decided NOT to purchase (no suitable offer).");
            return;
        }

        System.out.println("[BUYER " + buyerId + "] Final decision (customer chooses): " + chosen);

        boolean ok = broker.executeTransaction(chosen, request);
        if (ok) {
            System.out.println("[BUYER " + buyerId + "] SUCCESS: Purchased " + request.getQuantity() + " x " + request.getProductName()
                    + " from " + chosen.getSellerId()
                    + " (total=" + String.format("%.2f", chosen.totalCost(request.getQuantity())) + ")");
        } else {
            System.out.println("[BUYER " + buyerId + "] FAILED: Transaction could not be completed.");
        }
        System.out.println("==============================\n");
    }

    /**
     * IMPORTANT: customer makes final decision, not broker.
     * Demonstrate decision logic that may differ from the broker recommendation.
     */
    public Offer chooseFinalOffer(List<Offer> offers, Offer recommended, OrderRequest request) {
        // Filter offers within customer's total budget
        List<Offer> affordable = new ArrayList<>();
        for (Offer o : offers) {
            if (o.totalCost(request.getQuantity()) <= profile.getMaxTotalBudget()) {
                affordable.add(o);
            }
        }
        if (affordable.isEmpty()) return null;

        // Example decision rule:
        // - if preferFastDelivery: choose fastest delivery within budget (even if not the cheapest)
        // - else: choose cheapest within budget
        Offer chosen = null;
        if (profile.isPreferFastDelivery()) {
            affordable.sort(Comparator
                    .comparingInt(Offer::getEstimatedDays)
                    .thenComparingDouble(Offer::getUnitPrice));
            chosen = affordable.get(0);

            // Demonstration: if recommended exists and is equally fast, follow recommendation.
            if (recommended != null && recommended.getEstimatedDays() == chosen.getEstimatedDays()) {
                chosen = recommended;
            }
        } else {
            affordable.sort(Comparator
                    .comparingDouble(Offer::getUnitPrice)
                    .thenComparingInt(Offer::getEstimatedDays));
            chosen = affordable.get(0);
        }
        return chosen;
    }
}
