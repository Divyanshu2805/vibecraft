# Architecture

- Base package: `com.java.vibecraft`
- Entry point: `VibecraftApplication` (`src/main/java/com/java/vibecraft/VibecraftApplication.java`)
- Config: `src/main/resources/application.yaml`
- `entity` — the 13 JPA entities described above.
- `enums` — the 6 domain enums described above.
- `controller` — 6 REST controllers: `AuthController`, `ProjectController`, `ProjectMemberController`, `FileController`, `BillingController`, `UsageController` (see [APIs](../api/README.md#apis) above).
- `service` — 8 service interfaces, one (or two, for auth) per controller — no implementations yet.
- `dto` — request/response records, one subpackage per domain (`auth`, `project`, `member`, `file`, `subscription`).
- No `repository` package yet.
