# Order Processing Platform (Java + Spring Boot)

Starter microservices project for a backend portfolio case study.

## Services

- `order-service` (`:8080`): create order, orchestrate inventory + payment, get order status
- `inventory-service` (`:8081`): reserve stock
- `payment-service` (`:8082`): capture payment

Each service has its own PostgreSQL database via Docker Compose.

## Project Structure

- `pom.xml` (parent Maven multi-module)
- `docker-compose.yml` (3 PostgreSQL containers)
- `services/order-service`
- `services/inventory-service`
- `services/payment-service`

## Prerequisites

- Java 21
- Maven 3.9+
- Docker + Docker Compose

## Run Locally

1. Start databases:

```bash
cd "/Users/alexjoy/Documents/codex projects/order-processing-platform"
docker compose up -d
```

2. Start each service in separate terminals:

```bash
cd "/Users/alexjoy/Documents/codex projects/order-processing-platform/services/inventory-service"
mvn spring-boot:run
```

```bash
cd "/Users/alexjoy/Documents/codex projects/order-processing-platform/services/payment-service"
mvn spring-boot:run
```

```bash
cd "/Users/alexjoy/Documents/codex projects/order-processing-platform/services/order-service"
mvn spring-boot:run
```

## Authentication (JWT)

All business APIs are protected. Obtain a token first from any service:

```bash
curl -X POST http://localhost:8080/api/auth/token \
  -H 'Content-Type: application/json' \
  -d '{"username":"alexjoy","password":"change-me"}'
```

Copy `data.accessToken` from the response and export it:

```bash
export TOKEN="<paste-access-token>"
```

## First Endpoints

### 1) Create an order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "productCode": "JAVA-BOOK",
    "quantity": 2,
    "amount": 499.00
  }'
```

### 2) Get order status

```bash
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/orders/1
```

### 3) Reserve inventory directly

```bash
curl -X POST http://localhost:8081/api/inventory/reserve \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"productCode":"SPRING-COURSE","quantity":1}'
```

### 4) Capture payment directly

```bash
curl -X POST http://localhost:8082/api/payments/capture \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"orderId": 101, "amount": 999.00}'
```

## Suggested Next Improvements

- Add Kafka/RabbitMQ for async order events
- Implement idempotency key support for create order
- Add Testcontainers integration tests
- Add OpenAPI docs and GitHub Actions CI
