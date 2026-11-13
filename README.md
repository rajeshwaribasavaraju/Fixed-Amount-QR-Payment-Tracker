# Fixed-Amount QR Payment Verification and Tracker

## Project Overview
The Fixed-Amount QR Payment Verification and Tracker is a Java-based web application developed using Spring Boot and SQLite.

The system allows users to verify payment amounts against a predefined fixed amount and track payment transactions.

## Technologies Used
- Java 21
- Spring Boot 3.5.6
- Maven
- SQLite Database
- JDBC
- HTML, CSS and JavaScript

## Main Features
- Fixed amount payment verification
- Payment status tracking
- Store payment transaction records
- View complete payment history
- Search payments using transaction ID
- Filter payments by status
- Display total, successful, failed and pending payment counts

## Project Structure
```text
FixedAmountQRTRacker/
├── data/
│   ├── payment_tracker.db
│   └── payment_tracker.sqbpro
├── src/
│   └── main/
│       ├── java/com/qrtracker/
│       │   ├── database/
│       │   ├── model/
│       │   ├── service/
│       │   ├── QRController.java
│       │   ├── HomeController.java
│       │   └── QRTrackerApplication.java
│       └── resources/static/
│           └── index.html
├── pom.xml
└── README.md
```

## How to Run
1. Install Java 21 and Maven.
2. Clone or download this repository.
3. Open the project folder in VS Code.
4. Run the following command:

```bash
mvn spring-boot:run
```

5. Open your browser and visit:

http://localhost:8080

## Database
The project uses SQLite to store payment transaction details.

Database location: `data/payment_tracker.db`

## Note
Payment verification currently uses local amount comparison and transaction ID validation. It does not perform live bank or UPI payment verification.