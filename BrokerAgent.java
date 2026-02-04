import java.util.*;

public final class BrokerAgent {
    private final List<SellerAgent> sellers = new ArrayList<>();
    private final Map<String, CustomerProfile> customerProfiles = new HashMap<>();

    public void registerSeller(SellerAgent seller) {
        sellers.add(Objects.requireNonNull(seller));
    }

    public void addCustomerProfile(CustomerProfile profile) {
        customerProfiles.put(profile.getCustomerId(), Objects.requireNonNull(profile));
    }

    public CustomerProfile getCustomerProfile(String customerId) {
        return customerProfiles.get(customerId);
    }

    /**
     * Step 1: Buyer initiates purchasing process through broker with order constraints.
     * Step 2: Broker sends CFP to selected sellers (here: all sellers).
     * Step 3: Sellers PROPOSE or REFUSE.
     */
    public List<Offer> collectOffers(OrderRequest request, CustomerProfile profile) {
        System.out.println("\n[BROKER] Evaluating order: " + request);
        System.out.println("[BROKER] Sending CFP to sellers (" + sellers.size() + "):");

        List<Offer> offers = new ArrayList<>();
        for (SellerAgent s : sellers) {
            System.out.println("  -> CFP to " + s.getSellerId());
            SellerReply reply = s.handleCFP(request, profile);
            if (reply.getType() == SellerReply.Type.PROPOSE) {
                System.out.println("     " + s.getSellerId() + " PROPOSED: " + reply.getOffer());
                offers.add(reply.getOffer());
            } else {
                System.out.println("     " + s.getSellerId() + " REFUSED: " + reply.getReason());
            }
        }
        return offers;
    }

    /**
     * Broker may analyse offers and recommend the best offer (customer decides final purchase).
     * A simple scoring rule:
     * - discard offers exceeding customer's max budget
     * - if preferFastDelivery: prioritize smallest estimatedDays then cheapest
     * - else: prioritize cheapest then faster
     */
    public Offer recommendBest(List<Offer> offers, OrderRequest request, CustomerProfile profile) {
        if (offers == null || offers.isEmpty()) return null;

        List<Offer> affordable = new ArrayList<>();
        for (Offer o : offers) {
            double total = o.totalCost(request.getQuantity());
            if (total <= profile.getMaxTotalBudget()) {
                affordable.add(o);
            }
        }
        if (affordable.isEmpty()) return null;

        Comparator<Offer> cmp;
        if (profile.isPreferFastDelivery()) {
            cmp = Comparator
                    .comparingInt(Offer::getEstimatedDays)
                    .thenComparingDouble(Offer::getUnitPrice);
        } else {
            cmp = Comparator
                    .comparingDouble(Offer::getUnitPrice)
                    .thenComparingInt(Offer::getEstimatedDays);
        }
        affordable.sort(cmp);
        return affordable.get(0);
    }

    /**
     * Execute: broker sends ACCEPT to chosen seller (and could send REJECT to others).
     * Here, we confirm transaction with chosen seller and print outcome.
     */
    public boolean executeTransaction(Offer chosen, OrderRequest request) {
        if (chosen == null) return false;

        SellerAgent chosenSeller = null;
        for (SellerAgent s : sellers) {
            if (s.getSellerId().equals(chosen.getSellerId())) {
                chosenSeller = s;
                break;
            }
        }
        if (chosenSeller == null) return false;

        System.out.println("\n[BROKER] ACCEPT sent to " + chosen.getSellerId());
        // (Optional) REJECT to others for completeness
        for (SellerAgent s : sellers) {
            if (!s.getSellerId().equals(chosen.getSellerId())) {
                System.out.println("[BROKER] REJECT sent to " + s.getSellerId());
            }
        }

        boolean ok = chosenSeller.confirmTransaction(chosen, request.getQuantity());
        if (ok) {
            System.out.println("[BROKER] Transaction CONFIRMED by " + chosen.getSellerId());
        } else {
            System.out.println("[BROKER] Transaction FAILED at seller confirmation stage");
        }
        return ok;
    }
}
