# Tickets Event API

## Overview

Tickets Event API is a robust backend system for managing events, tickets, users, and payments. Built with Spring Boot and Java 21, it provides a secure and scalable foundation for a modern event ticketing platform.

## Features

- **User Management**: Registration, Login, Profile checks.
- **Role-Based Access Control (RBAC)**: Supports `ADMIN`, `USER`, `ORGANIZER`, and `STAFF` roles.
- **Event Management**: Create, update, and list events.
- **Ticketing System**:
  - Purchase tickets.
  - Generate QR Codes for ticket validation.
  - Validate tickets via QR scan.
- **Order Management**: Track booking history and status.
- **Payments**: Integrated with **KHQR (Bakong)** for seamless payments.
- **Security**: JWT-based authentication.

## Tech Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.5.5
- **Database**: PostgreSQL
- **Build Tool**: Maven
- **Security**: Spring Security, JWT (JJWT)
- **Utilities**: Lombok, MapStruct, ZXing (QR Code), JavaMailSender
- **API Documentation**: SpringDoc OpenAPI (Swagger UI)

## Getting Started

### Prerequisites

- Java 21
- Maven
- PostgreSQL

### Configuration

Update `src/main/resources/application-dev.yml` or set the following environment variables:

| Variable             | Description                | Example                         |
| -------------------- | -------------------------- | ------------------------------- |
| `DB_HOST`            | Database Host              | `localhost`                     |
| `DB_PORT`            | Database Port              | `5432`                          |
| `DB_NAME`            | Database Name              | `tickets_db`                    |
| `DB_USER`            | Database Username          | `postgres`                      |
| `DB_PASSWORD`        | Database Password          | `secret`                        |
| `JWT_SECRET`         | Secret key for JWT signing | `your_long_secret_key`          |
| `GMAIL_HOST`         | SMTP Host                  | `smtp.gmail.com`                |
| `GMAIL_PORT`         | SMTP Port                  | `587`                           |
| `GMAIL_USERNAME`     | SMTP Username              | `email@example.com`             |
| `GMAIL_APP_PASSWORD` | SMTP App Password          | `app_password`                  |
| `BAKONG_TOKEN`       | Bakong API Token           | `...`                           |
| `BAKONG_BASE_URI`    | Bakong Base URI            | `https://api-bakong.nbc.gov.kh` |
| `FRONTEND_URI`       | Frontend Application URL   | `http://localhost:3000`         |

### Installation

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd tickets
   ```

2. **Database Setup**
   Ensure PostgreSQL is running and create a database (e.g., `tickets_db`).
   Run the SQL script `script.sql` to initialize default roles (`ADMIN`, `USER`, `ORGANIZER`, `STAFF`).

   ```sql
   -- Example: psql -d tickets_db -f script.sql
   ```

   _Note: Application has `ddl-auto: update`, so tables will be created automatically on startup._

3. **Build the project**

   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   The server will start on port `8081`.

## API Documentation

Once the application is running, you can access the interactive API documentation at:

- **Swagger UI**: [http://localhost:8081/docs](http://localhost:8081/docs)
- **OpenAPI JSON**: [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

## Key Dependencies

- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-security`
- `jjwt-api` (JWT Auth)
- `kh.gov.nbc.bakong_khqr:sdk-java` (Payment SDK)
- `com.google.zxing` (QR Generation)
