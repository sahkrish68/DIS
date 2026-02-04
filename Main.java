import java.util.*;

public final class Main {
    public static void main(String[] args) {
        // 1) Create broker
        BrokerAgent broker = new BrokerAgent();

        // 2) Create sellers with different inventories (retailers' product DBs)
        RetailerSellerAgent sellerA = new RetailerSellerAgent("Retailer-A", List.of(
                new Product("P100", "Laptop", 850.0, 5, "Standard", 5),
                new Product("P101", "Mouse", 20.0, 0, "Standard", 4) // out of stock to demonstrate REFUSE
        ));

        RetailerSellerAgent sellerB = new RetailerSellerAgent("Retailer-B", List.of(
                new Product("P200", "Laptop", 900.0, 10, "Express", 2),
                new Product("P201", "Mouse", 18.0, 50, "Standard", 6)
        ));

        RetailerSellerAgent sellerC = new RetailerSellerAgent("Retailer-C", List.of(
                new Product("P300", "Laptop", 780.0, 1, "Standard", 7), // cheap but low stock
                new Product("P301", "Mouse", 22.0, 10, "Express", 2)
        ));

        broker.registerSeller(sellerA);
        broker.registerSeller(sellerB);
        broker.registerSeller(sellerC);

        // Optional: show inventories before transactions
        sellerA.printInventory();
        sellerB.printInventory();
        sellerC.printInventory();

        // 3) Create customer profiles (broker "has access to all customer profiles")
        CustomerProfile c1 = new CustomerProfile("CUST-001", 2000.0, true, "Any");
        CustomerProfile c2 = new CustomerProfile("CUST-002", 800.0, false, "Standard");

        broker.addCustomerProfile(c1);
        broker.addCustomerProfile(c2);

        // 4) Create buyers (agents buying on behalf of customers)
        BuyerAgent buyer1 = new BuyerAgent("BuyerAgent-1", c1);
        BuyerAgent buyer2 = new BuyerAgent("BuyerAgent-2", c2);

        // 5) Run scenarios (successful transactions + declined offers)
        // Scenario A: Customer wants a Laptop, flexible delivery, prefers fast delivery, budget is high => likely chooses Express
        buyer1.placeOrder(broker, new OrderRequest("Laptop", 1, 700.0, 950.0, "Any"));

        // Scenario B: Customer has low budget, standard delivery only => may fail or choose cheap standard offer if available
        buyer2.placeOrder(broker, new OrderRequest("Laptop", 1, 700.0, 850.0, "Standard"));

        // Scenario C: Request something with out-of-stock at one seller to show REFUSE and still succeed
        buyer1.placeOrder(broker, new OrderRequest("Mouse", 2, 15.0, 25.0, "Standard"));

        // Optional: show inventories after transactions
        System.out.println("\n--- Inventory After Transactions ---");
        sellerA.printInventory();
        sellerB.printInventory();
        sellerC.printInventory();
    }
}
