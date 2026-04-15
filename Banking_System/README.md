# Nexus Bank | Banking System Simulation

A modern, Java-based banking simulation system with a sleek web UI. Demonstrates core real-world financial operations, object-oriented design, and secure abstractions over a lightweight custom HTTP web server using Java 21.

## Key Features

- **Object-Oriented Design**: Utilizes core Java abstractions with inheritance (`Account` -> `SavingsAccount` / `CurrentAccount`) and encapsulation to protect state mutation.
- **REST-like API via Pure Core Java**: No external bulky framework. Uses `com.sun.net.httpserver` to handle custom routing and API requests.
- **In-Memory Thread-Safe Data Store**: Uses `ConcurrentHashMap` within the `BankService` to manage Accounts and Users safely.
- **Rich Aesthetics UI**:
  - Secure Authentication via Login and Registration UI panel.
  - Interactive SPA (Single Page Application) dashboard for checking balances and creating transactions.
  - Transactions history viewer matching modern banking workflows.
  - Sleek dark theme with glassmorphism overlays layout.

## Tech Stack

- **Backend**: Core Java 21, Java Collections, `HttpServer`.
- **Frontend**: Vanilla HTML5, CSS3, JavaScript (Fetch API).

## How to Run Locally

1. **Compile**:
   ```sh
   javac -d out src/bank/*.java src/bank/model/*.java
   ```

2. **Run Server**:
   ```sh
   java -cp out bank.BankingApp
   ```

3. **Access**:
   Open a browser and navigate to `http://localhost:8080`.

## Demo Video
*(See Walkthrough artifact for the complete end-to-end recording)*

## Project Structure
- `src/bank` - Core domain, Server handler, API routers
  - `model/` - Models (Account, CurrentAccount, SavingsAccount, User, Transaction)
- `public/` - Static Frontend resources (HTML/CSS/JS)
