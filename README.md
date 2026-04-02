# VibeCraft

An AI-assisted project-building platform: create projects, build them via AI chat, get live previews, and collaborate with your team.

> **Status:** early stage. Entities and a REST API surface exist, but nothing is wired up to actually run yet — see [Status](#status) below.

## Status

All 13 domain entities are implemented — see [docs/README.md](docs/schema/README.md#entities--models) for the full schema and ER diagram. 6 REST controllers (auth, projects, project members, project files, billing, usage) exist with their DTOs — see [docs/README.md](docs/api/README.md#apis) for the endpoint list — but their service interfaces have no implementations yet, there's no repository layer, no real authentication (every endpoint currently hardcodes a fake user), and no datasource is configured. Nothing is callable end-to-end yet.

## Tech Stack

- **Language:** Java 25
- **Framework:** Spring Boot 4.1.0 (Spring Web MVC, Spring Data JPA)
- **Database:** PostgreSQL
- **Build tool:** Maven (via Maven Wrapper)
- **Other:** Lombok

## Data Model

Full ER diagram and entity notes: [docs/README.md](docs/schema/README.md#entities--models)

## API

Full endpoint list: [docs/README.md](docs/api/README.md#apis)

## Getting Started

```bash
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd` instead of `./mvnw`.

### Tests

```bash
./mvnw test
```

## Project Structure

```
src/main/java/com/java/vibecraft/entity/       JPA entities
src/main/java/com/java/vibecraft/enums/        domain enums
src/main/java/com/java/vibecraft/controller/   REST controllers
src/main/java/com/java/vibecraft/service/      service interfaces (no implementations yet)
src/main/java/com/java/vibecraft/dto/          request/response records, by domain
src/main/resources/application.yaml              configuration
```

## Documentation

- [docs/README.md](docs/README.md) — tech stack, practices, APIs, entities, and ER diagram reference.
- [CLAUDE.md](CLAUDE.md) — guidance for AI coding assistants working in this repo.
