# VibeCraft

An AI-assisted project-building platform: create projects, build them via AI chat, get live previews, and collaborate with your team.

> **Status:** early stage. Signup/login, all 5 project endpoints, and all 4 project-member endpoints do real work; everything else is still a stub — see [Status](#status) below.

## Status

10 domain entities are implemented (see [docs/README.md](docs/schema/README.md#entities--models) for the full schema and ER diagram). 6 REST controllers (auth, projects, project members, project files, billing, usage) exist with their DTOs — see [docs/README.md](docs/api/README.md#apis) for the endpoint list. `AuthController`'s signup/login, `ProjectController`'s endpoints (list, get, create, update, soft-delete), and `ProjectMemberController`'s endpoints (list, invite, update role, remove) all have real logic; every other endpoint is still a stub. Authentication is real, stateless JWT (Spring Security + JJWT) — every endpoint except `/api/auth/**` requires a `Bearer` token. A PostgreSQL datasource is configured in `application.yaml`.

## Tech Stack

- **Language:** Java 25
- **Framework:** Spring Boot 4.1.0 (Spring Web MVC, Spring Data JPA, Spring Security)
- **Database:** PostgreSQL
- **Build tool:** Maven (via Maven Wrapper)
- **Other:** Lombok, MapStruct, Bean Validation, JJWT

## Data Model

Full ER diagram and entity notes: [docs/README.md](docs/schema/README.md#entities--models)

## API

Full endpoint list: [docs/README.md](docs/api/README.md#apis)

## Getting Started

Requires a local PostgreSQL instance matching `application.yaml`'s `spring.datasource` block (or override via `DB_USERNAME`/`DB_PASSWORD` env vars). Create a user via `POST /api/auth/signup`, then get a token via `POST /api/auth/login` — every other endpoint requires it as a `Bearer` token. Override the JWT signing key via the `JWT_SECRET` env var if you don't want the committed local-dev fallback.

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
src/main/java/com/java/vibecraft/security/     JWT auth (Spring Security config, filter, token utils)
src/main/java/com/java/vibecraft/controller/   REST controllers
src/main/java/com/java/vibecraft/service/      service interfaces
src/main/java/com/java/vibecraft/service/impl/ service implementations (mostly stubs)
src/main/java/com/java/vibecraft/dto/          request/response records, by domain
src/main/resources/application.yaml              configuration
```

## Documentation

- [docs/README.md](docs/README.md) — tech stack, practices, APIs, entities, and ER diagram reference.
- [CLAUDE.md](CLAUDE.md) — guidance for AI coding assistants working in this repo.
