# Fixed-Amount QR Payment Tracker

## Project Overview
The Fixed-Amount QR Payment Tracker is a Java-based web application developed using Spring Boot, SQLite, HTML, CSS and JavaScript.

It allows users to generate QR codes for a fixed payment amount, simulate payment results and track transactions.

## Technologies Used
- Java
- Spring Boot 3.5.6
- Maven
- SQLite
- JDBC
- HTML
- CSS
- JavaScript

## Main Features
- QR code generation
- Account holder name entry
- Fixed amount validation (₹250)
- Simulated successful and failed payments
- Transaction ID generation and duplicate validation
- Payment history
- Search by transaction ID
- Filter transactions by payment status
- Dashboard statistics
- SQLite database storage
- Payment details and status verification

## Team Roles
- Member 1: User Interface
- Member 2: Payment Logic
- Member 3: Database Development
- Member 4: Integration and Testing

## How to Run
1. Install Java and Eclipse or another Java IDE.
2. Clone or download this repository.
3. Import the project as an Existing Maven Project.
4. Run QRTrackerApplication.java.
5. Open http://localhost:8080 in your browser.

## Database
The application uses SQLite to store payment transaction details.

The local database is excluded from GitHub using .gitignore.

## Important Note
This project uses simulated payment transactions for demonstration. It does not perform real bank or UPI payment verification.