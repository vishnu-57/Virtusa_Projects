# SmartPay – Utility Bill Generator

A console-based Java application that calculates electricity and water bills for municipal customers using progressive tax slabs.

---

## About the Project

SmartPay is built for a local municipality to digitize its utility billing process. Instead of charging a flat rate, it uses a slab-based pricing model to encourage resource conservation — the more you consume, the higher the rate per unit.

---

## Features

- Progressive slab billing (3 pricing tiers)
- Input validation for meter readings
- 10% tax calculation on base charge
- Digital receipt printed to console
- Handles multiple customers in a loop
- Exits cleanly when user types `Exit`

---

## Slab Rate Chart

| Units Consumed | Rate per Unit |
|----------------|---------------|
| 0 – 100        | $1.00         |
| 101 – 300      | $2.00         |
| Above 300      | $5.00         |

> Tax of 10% is applied on top of the base charge.

---

## Project Structure

```
SmartPay/
├── Billable.java       # Interface with calculateTotal() method
├── UtilityBill.java    # Implements Billable, holds slab logic
└── SmartPayApp.java    # Main class, input loop, receipt display
```

---

## How to Run

**Requirements:** Java JDK 8 or above

```bash
# Step 1 - Compile
javac *.java

# Step 2 - Run
java SmartPayApp
```

---

## Sample Output

```
==============================
   SmartPay Utility Billing
==============================

Enter Customer Name (Exit to quit): Ravi Kumar
Enter Previous Meter Reading: 150
Enter Current Meter Reading: 420

------ Digital Receipt ------
Customer Name  : Ravi Kumar
Units Consumed : 270
Base Charge    : $440.00
Tax Amount     : $44.00
Final Total    : $484.00
-----------------------------
```

---

## Concepts Used

- Interface and implementation (`Billable`)
- Object-oriented class design
- if-else slab logic
- Input validation
- Loops and Scanner input
- Formatted console output

---

## Author

Made as part of a Core Java assignment.
