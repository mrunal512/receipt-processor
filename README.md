# Receipt Processor

**This repository contains my solution to the [Fetch Rewards Receipt Processor Challenge](https://github.com/fetch-rewards/receipt-processor-challenge).**  
The project is implemented as a Spring Boot REST API that processes receipts and calculates reward points based on several business rules.

---

## Overview

The application exposes two main endpoints:

- **POST** `/receipts/process`  
  Accepts a JSON receipt payload, processes it, and returns a unique receipt ID.

- **GET** `/receipts/{id}/points`  
  Returns the calculated reward points for the specified receipt.

Data is stored in an embedded H2 database and does not persist between application restarts, matching the challenge requirements.

---

## Features

- **Receipt Processing**  
  Calculates reward points based on retailer name, total amount, item details, purchase date, and purchase time.
- **Validation**  
  Ensures valid formats for dates, times, totals, and item details.
- **Docker Support**  
  Provides a Dockerfile to build and run the application in a container.

---

## Technology Stack

- **Java 17**
- **Spring Boot 3.4.3**
- **Spring Data JPA**
- **H2 Database**
- **Maven**
- **Docker**

---

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8 or higher
- Docker (if you plan to run the application in a container)

### Running Locally

1. **Clone the Repository**
   ```bash
   git clone https://github.com/mrunal512/receipt-processor.git
   cd receipt-processor

2. **Build the Project**
   ```bash
   mvn clean install
   ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the Application**
   The application will be running at `http://localhost:8080`.

### Running with Docker

1. **Build the Docker Image**
   ```bash
   docker build -t receipt-processor .
   ```

2. **Run the Docker Container**
   ```bash
   docker run -p 8080:8080 receipt-processor
   ```

3. **Access the Application**
   The application will be running at `http://localhost:8080`.

---

## Testing

### Running Unit Tests

To run the unit tests, use the following command:
```bash
  mvn test
```

### Running Integration Tests

To run the integration tests, use the following command:
```bash
  mvn verify
```

---

## API Documentation

### POST /receipts/process

**Description:**
Processes a receipt and returns a unique receipt ID.

**Request Body:**
```json
{
  "retailer": "string",
  "total": "string",
  "purchaseDate": "string",
  "purchaseTime": "string",
  "items": [
    {
      "shortDescription": "string",
      "price": "string"
    }
  ]
}
```

**Response:**
```json
{
  "id": "string"
}
```

### GET /receipts/{id}/points

**Description:**
Returns the calculated reward points for the specified receipt.

**Path Parameters:**
- `id` (UUID): The unique ID of the receipt.

**Response:**
```json
{
  "points": "integer"
}
```

---

## Contact

For any questions, feel free to open an issue or reach out via email at [mkgaikwa@uci.edu](mkgaikwa@uci.edu).

