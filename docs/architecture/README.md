# Architecture

- Base package: `com.java.vibecraft`
- Entry point: `VibecraftApplication` (`src/main/java/com/java/vibecraft/VibecraftApplication.java`)
- Config: `src/main/resources/application.yaml`; seed data: `src/main/resources/data.sql`
- `entity` — the 10 JPA entities described above (plus `ProjectMemberId`/`ChatSessionId` composite-key classes).
- `enums` — the 4 domain enums described above.
- `repository` — `ProjectRepository`, `UserRepository` (Spring Data JPA). Only 2 of 10 entities have one so far.
- `mapper` — `ProjectMapper` (MapStruct). Only `Project` has one so far.
- `error` — `ApiError`, `GlobalExceptionHandler`, `ResourceNotFoundException` — centralized exception→HTTP-response handling.
- `controller` — 6 REST controllers: `AuthController`, `ProjectController`, `ProjectMemberController`, `FileController`, `BillingController`, `UsageController` (see [APIs](../api/README.md#apis) above).
- `service` — 8 service interfaces, one (or two, for auth) per controller.
- `service.impl` — an `@Service` implementation of each interface above (`AuthServiceImpl`, `UserServiceImpl`, `ProjectServiceImpl`, `ProjectMemberServiceImpl`, `FileServiceImpl`, `PlanServiceImpl`, `SubscriptionServiceImpl`, `UsageServiceImpl`) — all still stubs except `ProjectServiceImpl.createProject`/`getUserProjects`.
- `dto` — request/response records, one subpackage per domain (`auth`, `project`, `member`, `subscription`). File DTOs (`FileNode`, `FileContentResponse`) live under `dto.project`, not a separate `dto.file` package.
