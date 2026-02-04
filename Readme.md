# CSC-40045 Java Agents Simulation (Contract-Net Variant)

This project is a small **Java agent simulation** for an e-commerce buying process.  
A **BuyerAgent** asks a **BrokerAgent** to find offers from multiple **SellerAgents**.  
The broker sends a **CFP (Call For Proposal)**, sellers reply with **PROPOSE** or **REFUSE**, and then the buyer makes the final decision.

The console output shows the whole flow step-by-step.

---

## What is inside (files)

- `Main.java` – runs 3 test scenarios and prints logs
- `BrokerAgent.java` – sends CFP to sellers, collects offers, recommends best offer, executes transaction
- `BuyerAgent.java` – represents a customer buyer, submits order and chooses the final offer
- `SellerAgent.java` – seller interface (handleCFP + confirmTransaction)
- `RetailerSellerAgent.java` – seller implementation with inventory, checks constraints, proposes/refuses
- `Product.java` – product data: id, name, price, stock, delivery info
- `OrderRequest.java` – order constraints: product name, qty, price range, delivery preference
- `CustomerProfile.java` – customer constraints: max budget + delivery preference
- `Offer.java` – seller offer: price + delivery data (and created time)
- `SellerReply.java` – wrapper for seller response: PROPOSE(Offer) or REFUSE(reason)

---

## Requirements

- Java **15+** (because the code uses `String.formatted(...)`)

Check your Java version:
```bash
java -version
```

---

## How to compile and run

Open terminal in the folder containing the `.java` files.

### Compile
```bash
javac *.java
```

### Run
```bash
java Main
```

---

## How the simulation works (short)

1. Buyer creates an `OrderRequest`.
2. Broker sends CFP to all registered sellers.
3. Each seller checks:
   - product name exists in inventory
   - enough stock
   - unit price is inside request range
   - delivery type matches request + customer preference
4. Seller replies:
   - **PROPOSE** with an `Offer` OR
   - **REFUSE** with a reason string
5. Broker recommends best offer (simple scoring rule).
6. Buyer chooses final offer (can accept broker suggestion or pick fastest/cheapest).
7. Broker sends ACCEPT to chosen seller and REJECT to others.
8. Seller confirms and reduces stock.
9. Inventory is printed before and after.

---

## Test scenarios (from `Main.java`)

- Scenario A: Laptop, flexible delivery, customer prefers fast delivery, high budget
- Scenario B: Laptop, standard delivery only, low budget (may fail)
- Scenario C: Mouse standard delivery, one seller has out-of-stock to demonstrate REFUSE

---

## Notes / limitations

- Product match is done by **product name** (case-insensitive). Real systems should use product IDs.
- This is a console simulation. It uses method calls, not real network messages.
- Broker recommendation is simple and can be improved with more scoring factors.

---

## Author

- Name - Krish Bikram Sah 
- Msc Advance Computing 
- University Id : 26006907

