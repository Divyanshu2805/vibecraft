# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

VibeCraft is a Spring Boot 4.1.0 application (Java 25, Maven), an AI-assisted project-building platform. The JPA entity layer is implemented (`entity`/`enums` packages — see [docs/README.md](docs/schema/README.md#entities--models) for the full schema). REST controllers, request/response DTOs, service interfaces, and now `@Service` implementations exist for auth, projects, project members, project files, billing, and usage (see [docs/README.md](docs/api/README.md#apis) for the endpoint list). The app starts and connects to PostgreSQL, but **every service implementation is currently a stub** (returns `null`/empty/no-op) — nothing does real work yet, and there is still no `repository` package.

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
- `application.yaml` configures a PostgreSQL datasource (`ddl-auto: update`, so the schema auto-creates/updates on startup) and forces `TimeZone.setDefault(Asia/Kolkata)` in `VibecraftApplication.main()` — needed because some Windows JVMs report the legacy alias `Asia/Calcutta`, which PostgreSQL's JDBC startup handshake rejects. DB credentials are `${DB_USERNAME:User}`/`${DB_PASSWORD:Password}` placeholders (env var, with a local-dev fallback) — never hardcode real credentials here, this repo is public.
- `pom.xml` has intentionally empty `<name>`, `<description>`, `<url>`, `<license>`, `<developers>`, and `<scm>` overrides to prevent inheriting those values from the `spring-boot-starter-parent` POM — leave them empty unless populating them deliberately.
- Lombok is wired into both the `default-compile` and `default-testCompile` executions of `maven-compiler-plugin` as an annotation processor path; keep both executions in sync if Lombok config ever changes.
