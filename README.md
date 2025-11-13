# Midas
Project repo for the JPMC Advanced Software Engineering Forage program
Midas Core

A Java + Spring Boot microservice simulating real-time financial transactions, external incentive awards, and REST-based balance queries. Built using event-driven architecture with Kafka, JPA persistence, and a clean service-controller design.

⸻

🚀 Project Overview

Midas Core processes transactions between users while integrating with an external Incentives API. It models real-world backend workflows including validation, external service calls, and REST API exposure.

The system includes:
	•	Transaction Processor (Kafka listener → validation → persistence)
	•	External Incentive API Integration (REST call using RestTemplate)
	•	User Balance REST API (/balance on port 33400)
	•	Automated Integration Tests (embedded Kafka + H2)

⸻

🧰 Tech Stack
	•	Java 17
	•	Spring Boot
	•	Apache Kafka (embedded for tests)
	•	H2 in-memory database
	•	Spring Data JPA
	•	RestTemplate
	•	Maven
	•	IntelliJ IDEA

⸻

📦 Features

1️⃣ Transaction Processing Engine
	•	Listens on Kafka topic transactions
	•	Validates sender/recipient
	•	Ensures positive amount + sufficient balance
	•	Applies atomic balance updates with @Transactional
	•	Records transaction history using JPA

⸻

2️⃣ Incentives API Integration

Each transaction is posted to the Incentive API:
	•	POST request → http://localhost:8080/incentive
	•	Body: serialized Transaction object
	•	Response: { "amount": <value> }
	•	Incentive is added ONLY to the recipient’s balance

⸻

3️⃣ Balance Retrieval REST API

Exposed directly by Midas Core on port 33400.

Endpoint:

GET /balance?userId=123

Returns:

Balance { amount=1234.56 }

	•	Returns 0.0 if user does not exist
	•	Uses Balance class exactly as provided

⸻

📁 Project Structure

src/main/java/com/jpmc/midascore
│
├── bootstrap/
├── component/
├── controller/
│   └── BalanceController.java
├── entity/
├── foundation/
├── messaging/
│   ├── TransactionListener.java
│   └── Kafka config
├── repository/
├── service/
│   └── TransactionService.java
└── MidasCoreApplication.java


⸻

⚙️ Running the Project

1. Start Incentives API

cd services/
java -jar transaction-incentive-api.jar

2. Start Midas Core

mvn spring-boot:run

3. Query a user balance

curl "http://localhost:33400/balance?userId=10"


⸻

🧪 Testing

Automated tests include:
	•	TaskThreeTests — Transaction validation
	•	TaskFourTests — Incentive API integration
	•	TaskFiveTests — Balance API

Uses:
	•	Embedded Kafka
	•	H2 in-memory DB
	•	Spring Boot test environment

⸻

📄 Task Five Final Output

---begin output ---
Balance {amount=0.0}
Balance {amount=1144.41}
Balance {amount=1444.53}
Balance {amount=983.8}
Balance {amount=2344.0}
Balance {amount=2275.54}
Balance {amount=15.74}
Balance {amount=926.63}
Balance {amount=557.87}
Balance {amount=209.11}
Balance {amount=3511.86}
Balance {amount=2121.54}
Balance {amount=779421.3}
---end output ---


⸻

🎯 Skills Demonstrated
	•	Microservice architecture
	•	Kafka event-driven processing
	•	REST API design
	•	External service integration
	•	JPA/Hibernate persistence
	•	Debugging distributed flows
	•	Automated testing & embedded Kafka
	•	Spring Boot configuration

⸻

📌 How to Reference This Project (Resume)

Software Engineering Project — Midas Core (Java, Spring Boot, Kafka)
	•	Built a real-time financial microservice consuming Kafka transactions, performing validation, updating balances, and persisting records.
	•	Integrated an external Incentive API via REST to award incentives per transaction.
	•	Developed /balance GET endpoint, exposed on port 33400.
	•	Implemented transactional integrity and tested components using embedded Kafka & H2.

⸻

📎 License

This project is part of the JPMC Midas Simulation environment.
