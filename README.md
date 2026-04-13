# VibeCraft

An AI-assisted project-building platform: create projects, build them via AI chat, get live previews, and collaborate with your team.

> **Status:** early stage. Two endpoints (list/create projects) do real work; everything else is still a stub — see [Status](#status) below.

## Status

10 domain entities are implemented (see [docs/README.md](docs/schema/README.md#entities--models) for the full schema and ER diagram). 6 REST controllers (auth, projects, project members, project files, billing, usage) exist with their DTOs — see [docs/README.md](docs/api/README.md#apis) for the endpoint list. `ProjectController`'s list/create endpoints have real logic (repository + MapStruct mapper + a global exception handler for 404s); every other endpoint is still a stub. There's no real authentication yet (every endpoint hardcodes a fake user). A PostgreSQL datasource is configured in `application.yaml`, with a local seed (`data.sql`) inserting one dummy user on startup.

## Tech Stack

- **Language:** Java 25
- **Framework:** Spring Boot 4.1.0 (Spring Web MVC, Spring Data JPA)
- **Database:** PostgreSQL
- **Build tool:** Maven (via Maven Wrapper)
- **Other:** Lombok, MapStruct, Bean Validation

## Data Model

Full ER diagram and entity notes: [docs/README.md](docs/schema/README.md#entities--models)

## API

Full endpoint list: [docs/README.md](docs/api/README.md#apis)

## Getting Started

Requires a local PostgreSQL instance matching `application.yaml`'s `spring.datasource` block (or override via `DB_USERNAME`/`DB_PASSWORD` env vars). `data.sql` seeds one dummy user on startup.

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
src/main/java/com/java/vibecraft/repository/   Spring Data JPA repositories
src/main/java/com/java/vibecraft/mapper/       MapStruct entity<->DTO mappers
src/main/java/com/java/vibecraft/error/        global exception handling
src/main/java/com/java/vibecraft/controller/   REST controllers
src/main/java/com/java/vibecraft/service/      service interfaces
src/main/java/com/java/vibecraft/service/impl/ service implementations (mostly stubs)
src/main/java/com/java/vibecraft/dto/          request/response records, by domain
src/main/resources/application.yaml              configuration
src/main/resources/data.sql                       local seed data
```

## Documentation

- [docs/README.md](docs/README.md) — tech stack, practices, APIs, entities, and ER diagram reference.
- [CLAUDE.md](CLAUDE.md) — guidance for AI coding assistants working in this repo.
