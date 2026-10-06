# NotifyFlow AI

**Event-driven notification backend with controlled LLM integration.**

NotifyFlow AI explores a practical backend engineering question:

> How do you integrate an LLM into a backend workflow without allowing probabilistic output to control system correctness?

The system receives application events, processes them asynchronously, uses Google Gemini to generate structured notification content, validates the model output, applies deterministic business rules, and delivers notifications through RabbitMQ.

## Why this project is interesting

The LLM is intentionally treated as **one component inside the system**, not as the source of truth.

Critical behavior remains deterministic:

- Security alerts cannot be downgraded by the model
- Invalid AI output falls back to application logic
- Delivery rules are controlled by user preferences
- Failures are retried and eventually moved to a Dead Letter Queue
- Delivery state is persisted as `PENDING`, `SENT`, or `FAILED`

## Architecture

```text
Client
  ↓
REST API
  ↓
PostgreSQL
  ↓
RabbitMQ
  ↓
Gemini
  ↓
Structured Output
  ↓
Validation
  ↓
Policy Engine
  ↓
Delivery Policy
  ↓
RabbitMQ
  ↓
PUSH / EMAIL / IN_APP
  ↓
SENT / Retry / DLQ / FAILED
```

## Core Engineering Concepts

- Event-driven architecture
- Asynchronous message processing
- Structured LLM output
- Deterministic policy enforcement
- Retry and Dead Letter Queue handling
- Persistent delivery attempts
- Pluggable delivery strategies
- User-specific notification preferences
- Structured logging
- Unit-tested business logic

## Tech Stack

**Java 17 · Spring Boot · PostgreSQL · RabbitMQ · Google Gemini · Docker · Maven · JUnit 5**

## Try It

Start the infrastructure:

```bash
docker compose up -d
```

Set the Gemini API key as an environment variable:

```text
GEMINI_API_KEY
```

Run the application:

```bash
mvn spring-boot:run
```

Create an event:

```http
POST /api/v1/events
```

List generated notifications:

```http
GET /api/v1/notifications
```

## Example

Input event:

```json
{
  "eventId": "evt-100",
  "userId": "user-123",
  "source": "security-service",
  "type": "SECURITY_ALERT",
  "message": "A login was detected from a new device"
}
```

The AI may generate notification content, but the final priority is enforced by application policy.

```text
AI output: LOW
Policy result: HIGH
```

## Reliability

Delivery failures follow:

```text
Failure
  ↓
Retry
  ↓
Retry
  ↓
DLQ
  ↓
FAILED
```

Successful delivery:

```text
PENDING → SENT
```

## Current Scope

The project currently uses simulated delivery adapters for `PUSH`, `EMAIL`, and `IN_APP`.

Potential next steps:

- End-to-end idempotency
- Real delivery providers
- Metrics and distributed tracing
- Cloud deployment
