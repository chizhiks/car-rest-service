# Car REST Service

A RESTful API for managing a car dealership's inventory — makes, models, categories, and individual cars — with pagination, dynamic filtering, bulk CSV import, and Keycloak-secured authentication.

## Why this project

The goal was to practice building a production-style REST API rather than a simple CRUD toy: proper layered architecture, DTO-based contracts (never leaking JPA entities to clients), dynamic query filtering, bulk data import, and OAuth2/JWT authentication via a real identity provider (Keycloak) instead of hardcoded credentials.

## Key Features

- **Full CRUD** for cars, manufacturers (makes), models, and categories
- **Dynamic filtering** — search cars by manufacturer, model, year range, and category, combined freely, built with a JPA `Specification` (`CarSpecificationBuilder`) instead of hardcoded queries
- **Pagination** on all list endpoints (`Pageable`/`Page<CarDto>`)
- **Bulk CSV import** — load car inventory in bulk from a CSV file (`CsvImportService`, Apache Commons CSV)
- **OAuth2/JWT authentication** — login and resource access secured through a Keycloak realm, not a hand-rolled auth mechanism
- **Auto-generated API docs** — every endpoint documented with Swagger annotations, browsable via Swagger UI

## Tech Stack

| Category    | Technology                                               |
|--------------|-------------------------------------------------------------|
| Language    | Java                                                     |
| Framework   | Spring Boot                                              |
| ORM         | Hibernate / Spring Data JPA                              |
| Security    | Spring Security + Keycloak (OAuth2 resource server, JWT) |
| Database    | PostgreSQL                                               |
| Migrations  | Flyway                                                    |
| Mapping     | MapStruct (Entity ↔ DTO, zero-reflection)                |
| API Docs    | springdoc-openapi / Swagger UI                           |
| Data Import | Apache Commons CSV                                       |
| Testing     | JUnit, Mockito, Testcontainers                           |
| Monitoring  | Spring Boot Actuator                                     |

## Authentication

The API delegates identity to **Keycloak**:

- `POST /auth/login` exchanges user credentials for a JWT access token, obtained from Keycloak's token endpoint (`password` grant against the `car-rest-service` realm)
- All protected endpoints validate the JWT as an OAuth2 resource server, checking the token's signature and claims against Keycloak's `issuer-uri`
- No passwords or tokens are handled or stored by the application itself — Keycloak owns identity end-to-end

## API Overview

### Cars — `/api/v1/cars`

| Method | Endpoint            | Description                                                                       |
|--------|-----------------------|--------------------------------------------------------------------------------------|
| GET    | `/api/v1/cars`      | Paginated, filterable list of cars (by manufacturer, model, year range, category) |
| POST   | `/api/v1/cars`      | Create a car                                                                      |
| GET    | `/api/v1/cars/{id}` | Get a car by ID                                                                   |
| PUT    | `/api/v1/cars/{id}` | Update a car                                                                      |
| DELETE | `/api/v1/cars/{id}` | Delete a car                                                                      |

### Manufacturers — `/api/v1/manufacturers`

| Method             | Endpoint                                                 | Description                             |
|----------------------|-------------------------------------------------------------|--------------------------------------------|
| GET                | `/api/v1/manufacturers`                                  | List manufacturers                      |
| POST               | `/api/v1/manufacturers`                                  | Create a manufacturer                   |
| GET / PUT / DELETE | `/api/v1/manufacturers/{name}`                           | Get, update, or delete a manufacturer   |
| GET / POST         | `/api/v1/manufacturers/{name}/models`                    | List or add models for a manufacturer   |
| GET / PUT / DELETE | `/api/v1/manufacturers/{name}/models/{modelName}`        | Get, update, or delete a specific model |
| POST               | `/api/v1/manufacturers/{name}/models/{modelName}/{year}` | Add a model-year variant                |

### Models — `/api/v1/models` and Categories — `/api/v1/categories`

Standard CRUD (GET list, POST, GET/PUT/DELETE by ID) for both.

## Architecture

```
HTTP Request
     │
     ▼
Spring Security (OAuth2 Resource Server)   ← validates JWT issued by Keycloak
     │
     ▼
Controller           ← CarController, ManufacturerController, ModelController,
                        CategoryController, AuthController
     │
     ▼
Service               ← business logic, including CsvImportService for bulk import
     │
     ▼
Repository + Specification   ← Spring Data JPA repositories, CarSpecificationBuilder for dynamic filtering
     │
     ▼
PostgreSQL
```

Controllers exchange only DTOs; MapStruct handles all Entity ↔ DTO conversion at compile time.

## Project Structure

```
src/main/java/ua/foxminded/chyzhov/carrestservice/
├── config/               # SecurityConfig, OpenApiConfig
├── controller/           # CarController, ManufacturerController, ModelController,
│                          # CategoryController, AuthController
├── dto/                  # CarDto, MakeDto, ModelDto, CategoryDto, CarFilterDto, AuthDto
├── entity/                # Car, Make, Model, Category
├── mapper/                # MapStruct mappers per entity
├── repository/            # Spring Data JPA repositories
├── service/                # Business logic (incl. CsvImportService)
├── specification/          # CarSpecificationBuilder — dynamic filter query building
└── util/exceptions/         # NotFoundException, RecordAlreadyExists
```

## Running Locally

```bash
git clone https://github.com/chizhiks/car-rest-service.git
cd car-rest-service

docker compose up --build
```

One command builds the app image, starts PostgreSQL, starts Keycloak, and automatically imports the `car-rest-service` realm — no manual setup in the Keycloak admin console needed.

Two demo accounts are created automatically:

| Username     | Password       | Role            |
|---------------|------------------|-------------------|
| `demo-user`  | `demo-pass123` | `USER`          |
| `demo-admin` | `demo-pass123` | `ADMIN`, `USER` |

API runs on `http://localhost:8000`; Swagger UI at `/swagger-ui-custom.html`.
Keycloak admin console: `http://localhost:8081` (`admin` / `admin`).

Tests (`mvn test`) spin up PostgreSQL automatically via Testcontainers — Docker must be running.

## Author

Andrii Chyzhov
