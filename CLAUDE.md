# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

VibeCraft is a Spring Boot 4.1.0 application (Java 25, Maven), an AI-assisted project-building platform. The JPA entity layer is implemented (`entity`/`enums` packages — see [docs/README.md](docs/schema/README.md#entities--models) for the full schema). REST controllers, request/response DTOs, and service interfaces exist for auth, projects, project members, project files, billing, and usage (see [docs/README.md](docs/api/README.md#apis) for the endpoint list) — but **no service has an implementation yet**, so the app will fail to start (`No qualifying bean`) as soon as a datasource lets it get that far. There is still no `repository` package, and no datasource is configured.

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
- No datasource is configured yet in `application.yaml` — only `spring.application.name` is set. A PostgreSQL connection (URL/credentials) must be added before any JPA-backed code will start.
- `pom.xml` has intentionally empty `<name>`, `<description>`, `<url>`, `<license>`, `<developers>`, and `<scm>` overrides to prevent inheriting those values from the `spring-boot-starter-parent` POM — leave them empty unless populating them deliberately.
- Lombok is wired into both the `default-compile` and `default-testCompile` executions of `maven-compiler-plugin` as an annotation processor path; keep both executions in sync if Lombok config ever changes.
