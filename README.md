# VibeCraft

An AI-assisted project-building platform: create projects, build them via AI chat, get live previews, and collaborate with your team.

> **Status:** early stage. Domain entities are implemented; no APIs yet — see [Status](#status) below.

## Status

All 13 domain entities (users, projects, collaboration, file storage, live previews, AI chat, billing) are implemented as JPA entities — see [docs/README.md](docs/schema/README.md#entities--models) for the full schema and ER diagram. No repository, service, or REST API layer yet, and no datasource is configured.

## Tech Stack

- **Language:** Java 25
- **Framework:** Spring Boot 4.1.0 (Spring Web MVC, Spring Data JPA)
- **Database:** PostgreSQL
- **Build tool:** Maven (via Maven Wrapper)
- **Other:** Lombok

## Data Model

Full ER diagram and entity notes: [docs/README.md](docs/schema/README.md#entities--models)

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
src/main/java/com/java/vibecraft/entity/   JPA entities
src/main/java/com/java/vibecraft/enums/    domain enums
src/main/resources/application.yaml          configuration
```

## Documentation

- [docs/README.md](docs/README.md) — tech stack, practices, APIs, entities, and ER diagram reference.
- [CLAUDE.md](CLAUDE.md) — guidance for AI coding assistants working in this repo.
