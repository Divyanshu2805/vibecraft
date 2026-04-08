# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

VibeCraft is a Spring Boot 4.1.0 application (Java 25, Maven), an AI-assisted project-building platform. The JPA entity layer is implemented (`entity`/`enums` packages — see [docs/README.md](docs/schema/README.md#entities--models) for the full schema, currently **v3** — a simplified redesign of the original v2 implementation). REST controllers, request/response DTOs, service interfaces, `@Service` implementations, a `repository` layer (2 of 10 entities so far), a MapStruct `mapper` (1 entity so far), and a `GlobalExceptionHandler` all exist (see [docs/README.md](docs/api/README.md#apis) for the endpoint list). Only `ProjectServiceImpl.createProject`/`getUserProjects` have real logic — every other service method across all 8 services is still a stub (`null`/empty/no-op).

- Base package: `com.java.vibecraft`
- Entry point: [src/main/java/com/java/vibecraft/VibecraftApplication.java](src/main/java/com/java/vibecraft/VibecraftApplication.java)
- Config: [src/main/resources/application.yaml](src/main/resources/application.yaml)

## Commands

Use the Maven wrapper (`mvnw`/`mvnw.cmd`), not a system-installed Maven.

```bash
./mvnw spring-boot:run          # run the app
./mvnw test                     # run all tests
./mvnw test -Dtest=ClassName    # run a single test class
./mvnw test -Dtest=ClassName#methodName  # run a single test method
./mvnw clean package            # build the jar
```

On Windows, use `mvnw.cmd` in place of `./mvnw`.

## Architecture notes

- Dependencies: `spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`, PostgreSQL driver (runtime), Lombok. Test scope adds `spring-boot-starter-webmvc-test` and `spring-boot-starter-data-jpa-test`.
- Controllers hardcode `Long userId = 1L` instead of reading an authenticated principal — there's no auth/security wiring yet, just the `AuthController` endpoints and DTOs.
- Every request DTO (a record actually used as `@RequestBody`) carries Bean Validation constraints, each with an explicit `message = "..."`, and the controller parameter is `@RequestBody @Valid` — follow this for any new request DTO/endpoint. Response DTOs never carry validation annotations. See [docs/README.md](docs/api/README.md#request-validation) for the full per-field constraint list.
- `application.yaml` is **not committed to this repo** (by explicit request — it held plaintext datasource credentials at one point, and this repo is public). It configures a PostgreSQL datasource (`ddl-auto: update`, so the schema auto-creates/updates on startup); recreate it locally before running. `data.sql` (a local seed — one dummy `User` row) is likewise uncommitted.
- `VibecraftApplication.main()` forces `TimeZone.setDefault(Asia/Kolkata)` before `SpringApplication.run(...)` — needed because some Windows JVMs report the legacy alias `Asia/Calcutta`, which PostgreSQL's JDBC startup handshake rejects.
- `pom.xml` has intentionally empty `<name>`, `<description>`, `<url>`, `<license>`, `<developers>`, and `<scm>` overrides to prevent inheriting those values from the `spring-boot-starter-parent` POM — leave them empty unless populating them deliberately.
- Lombok is wired into both the `default-compile` and `default-testCompile` executions of `maven-compiler-plugin` as an annotation processor path; keep both executions in sync if Lombok config ever changes.
