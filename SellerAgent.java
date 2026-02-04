public interface SellerAgent {
    String getSellerId();

    /**
     * Contract-Net style: Broker sends a CFP (call for proposal) to a seller.
     * Seller can PROPOSE an offer or REFUSE (decline) with a reason.
     */
    SellerReply handleCFP(OrderRequest request, CustomerProfile customerProfile);

    /**
     * If broker/buyer accepts an offer, seller confirms and updates its internal stock.
     */
    boolean confirmTransaction(Offer acceptedOffer, int requestedQty);
}
