# VibeCraft

An AI-assisted project-building platform: create projects, build them via AI chat, get live previews, and collaborate with your team.

> **Status:** early stage. The app boots and connects to Postgres, but every endpoint is still a stub — see [Status](#status) below.

## Status

All 13 domain entities are implemented — see [docs/README.md](docs/schema/README.md#entities--models) for the full schema and ER diagram. 6 REST controllers (auth, projects, project members, project files, billing, usage) exist with their DTOs and now have real `@Service` beans behind them — see [docs/README.md](docs/api/README.md#apis) for the endpoint list — but every service method is currently a stub (returns `null`/empty/no-op). There's still no repository layer and no real authentication (every endpoint hardcodes a fake user). A PostgreSQL datasource is configured and the schema auto-creates on startup. Nothing returns real data yet.

## Tech Stack

- **Language:** Java 25
- **Framework:** Spring Boot 4.1.0 (Spring Web MVC, Spring Data JPA)
- **Database:** PostgreSQL (configured; set `DB_USERNAME`/`DB_PASSWORD` env vars, or it falls back to local dev defaults — see `application.yaml`)
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
src/main/java/com/java/vibecraft/service/      service interfaces
src/main/java/com/java/vibecraft/service/impl/ service implementations (currently stubs)
src/main/java/com/java/vibecraft/dto/          request/response records, by domain
src/main/resources/application.yaml              configuration
```

## Documentation

- [docs/README.md](docs/README.md) — tech stack, practices, APIs, entities, and ER diagram reference.
- [CLAUDE.md](CLAUDE.md) — guidance for AI coding assistants working in this repo.
