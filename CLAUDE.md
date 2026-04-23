# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

VibeCraft is a Spring Boot 4.1.0 application (Java 25, Maven), an AI-assisted project-building platform. The JPA entity layer is implemented (`entity`/`enums` packages — see [docs/README.md](docs/schema/README.md#entities--models) for the full schema, currently **v4** — project ownership lives on `ProjectMember.projectRole == OWNER` rather than a `Project.owner` FK, and `User`'s login fields are `username`/`password`, not `email`/`passwordHash`). REST controllers, request/response DTOs, service interfaces, `@Service` implementations, a `repository` layer (3 of 10 entities so far), a MapStruct `mapper` layer (2 of 10 entities so far), and a `GlobalExceptionHandler` all exist (see [docs/README.md](docs/api/README.md#apis) for the endpoint list). `ProjectServiceImpl` (`createProject`, `getUserProjects`, `getUserProjectById`, `updateProject`, `softDelete`) and `ProjectMemberServiceImpl` (`getProjectMembers`, `inviteMember`, `updateMemberRole`, `removeProjectMember`) are fully implemented — every service method across the other 6 services is still a stub (`null`/empty/no-op). **Known gap:** none of these methods check the caller's `ProjectRole` — any project member can update/delete a project or manage other members' roles, and `updateMemberRole`/`removeProjectMember` don't even check project membership. Don't build further access-control-sensitive features on top of this without restoring an owner check first — see docs/README.md's [Project Status](docs/project-status.md#project-status) "Known gaps".

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

- Dependencies: `spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`, PostgreSQL driver (runtime), Lombok, MapStruct (`mapstruct` + `mapstruct-processor`, version pinned via the `org.mapstruct.version` property). Test scope adds `spring-boot-starter-webmvc-test` and `spring-boot-starter-data-jpa-test`.
- Controllers hardcode `Long userId = 1L` instead of reading an authenticated principal — there's no auth/security wiring yet, just the `AuthController` endpoints and DTOs.
- Every request DTO (a record actually used as `@RequestBody`) carries Bean Validation constraints, each with an explicit `message = "..."`, and the controller parameter is `@RequestBody @Valid` — follow this for any new request DTO/endpoint. Response DTOs never carry validation annotations. See [docs/README.md](docs/api/README.md#request-validation) for the full per-field constraint list.
- Entities consistently use `@Getter @Setter @FieldDefaults(level = AccessLevel.PRIVATE) @AllArgsConstructor @NoArgsConstructor @Builder`, plus `@CreationTimestamp`/`@UpdateTimestamp` (Hibernate) for `createdAt`/`updatedAt` rather than manual `@PrePersist`/`@PreUpdate`. Composite-key entities (`ProjectMember`, `ChatSession`) use `@EmbeddedId` + `@MapsId` with a small dedicated `@Embeddable Serializable` ID class (`ProjectMemberId`, `ChatSessionId`). Soft delete is a plain nullable `deletedAt` column (`User`, `Project`, `ChatSession`) with no `@SQLDelete`/`@Where` filtering wired up — exclude soft-deleted rows manually in queries.
- Repositories (`repository.XRepository extends JpaRepository<X, IdType>`) add `@Query` JPQL only as a service actually needs it (e.g. `ProjectRepository.findAllAccessibleByUser`), not upfront for every entity. Entity→DTO mapping goes through MapStruct (`mapper.XMapper`, `@Mapper(componentModel = "spring")`); add `@Mapping(target, source)` only when entity/DTO field names differ, since MapStruct auto-matches identically-named properties.
- Exception→HTTP-response translation is centralized in `error.GlobalExceptionHandler` (`@RestControllerAdvice`), returning the shared `ApiError` record — add new `@ExceptionHandler` methods there rather than handling errors per-controller.
- `application.yaml` and `data.sql` are both committed. `application.yaml` configures a PostgreSQL datasource (`ddl-auto: create`, so the schema is dropped and recreated from scratch — and `data.sql` reseeded — on every startup; convenient while entity shapes are actively changing, but it wipes local data every restart); credentials are `${DB_USERNAME:User}`/`${DB_PASSWORD:Password}` env-var placeholders with a local-dev fallback, not hardcoded — keep it that way, this repo is public. `data.sql` seeds 3 dummy `User` rows (`id=1`/`2`/`3`, `password = 'N/A'` placeholder) on every startup, each guarded with its own `ON CONFLICT (id) DO NOTHING` so it's safe to re-run.
- `VibecraftApplication.main()` forces `TimeZone.setDefault(Asia/Kolkata)` before `SpringApplication.run(...)` — needed because some Windows JVMs report the legacy alias `Asia/Calcutta`, which PostgreSQL's JDBC startup handshake rejects.
- `pom.xml` has intentionally empty `<name>`, `<description>`, `<url>`, `<license>`, `<developers>`, and `<scm>` overrides to prevent inheriting those values from the `spring-boot-starter-parent` POM — leave them empty unless populating them deliberately.
- Lombok is wired into both the `default-compile` and `default-testCompile` executions of `maven-compiler-plugin` as an annotation processor path; keep both executions in sync if Lombok config ever changes.
