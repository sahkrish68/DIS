public final class SellerReply {
    public enum Type { PROPOSE, REFUSE }

    private final Type type;
    private final Offer offer;     // non-null if PROPOSE
    private final String reason;   // non-empty if REFUSE

    private SellerReply(Type type, Offer offer, String reason) {
        this.type = type;
        this.offer = offer;
        this.reason = reason;
    }

    public static SellerReply propose(Offer offer) {
        if (offer == null) throw new IllegalArgumentException("offer cannot be null");
        return new SellerReply(Type.PROPOSE, offer, "");
    }

    public static SellerReply refuse(String reason) {
        if (reason == null || reason.isBlank()) reason = "No reason provided";
        return new SellerReply(Type.REFUSE, null, reason);
    }

    public Type getType() { return type; }
    public Offer getOffer() { return offer; }
    public String getReason() { return reason; }
}
